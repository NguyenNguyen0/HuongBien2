package huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.bus.PromotionBUS;
import huongbien.bus.TableBUS;
import huongbien.config.Constants;
import huongbien.config.Variable;
import huongbien.dao.CustomerDAO;
import huongbien.dao.EmployeeDAO;
import huongbien.dao.PromotionDAO;
import huongbien.dao.TableDAO;
import huongbien.entity.*;
import huongbien.jpa.PersistenceUnit;
import huongbien.service.QRCodeHandler;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class OrderPaymentController implements Initializable {
    @FXML public TableView<Promotion> promotionTableView;
    @FXML private TableColumn<Promotion, Double> promotionDiscountColumn;
    @FXML private TableColumn<Promotion, String> promotionIdColumn;
    @FXML private TableColumn<Promotion, String> promotionNameColumn;
    @FXML private Label employeeLabel;
    @FXML private Label tableInfoLabel;
    @FXML private Label cuisineQuantityLabel;
    @FXML private Label tableAmountLabel;
    @FXML private Label cuisineAmountLabel;
    @FXML private Label vATLabel;
    @FXML private Label vatNameLabel;
    @FXML private Label promotionDiscountLabel;
    @FXML private Label finalAmountLabel;
    @FXML private ScrollPane billScrollPane;
    @FXML public GridPane billGridPane;
    @FXML private TextField searchCustomerField;
    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField customerRankField;
    @FXML private Button searchCustomerButton;

    private VideoCapture capture;
    private Timer timer;
    private QRCodeHandler qrCodeHandler;

    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            // Use the absolute path to the native library
            System.load(System.getProperty("user.dir") + "/libs/native/opencv_java451.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    //Controller area
    public RestaurantMainManagerController restaurantMainManagerController;
    public void setRestaurantMainManagerController(RestaurantMainManagerController restaurantMainManagerController) {
        this.restaurantMainManagerController = restaurantMainManagerController;
    }

    public RestaurantMainStaffController restaurantMainStaffController;
    public void setRestaurantMainStaffController(RestaurantMainStaffController restaurantMainStaffController) {
        this.restaurantMainStaffController = restaurantMainStaffController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.qrCodeHandler = new QRCodeHandler();
            loadCuisine();
            setPaymentInfo();
            searchCustomerButton.fire();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        promotionTableView.setMouseTransparent(true);
        vatNameLabel.setText(vatNameLabel.getText()+"("+(int)(Constants.VAT * 100)+"%):");
    }

    public void loadCuisine() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(getCuisineData());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/huongbien/fxml/OrderPaymentBillItem.fxml"));
                HBox paymentBillBox = fxmlLoader.load();
                OrderPaymentBillItemController _OrderPaymentBillItemController = fxmlLoader.getController();
                _OrderPaymentBillItemController.setDataBill(orderDetails.get(i));
                _OrderPaymentBillItemController.setOrderPaymentController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                billGridPane.add(paymentBillBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        billScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        billGridPane.prefWidthProperty().bind(billScrollPane.widthProperty());
    }

    public List<OrderDetail> readFromCuisineJSON() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH);

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            String id = jsonObject.get("Cuisine ID").getAsString();
            String name = jsonObject.get("Cuisine Name").getAsString();
            double price = jsonObject.get("Cuisine Price").getAsDouble();
            String note = jsonObject.get("Cuisine Note").getAsString();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();

            Cuisine cuisine = new Cuisine();
            cuisine.setId(id);
            cuisine.setName(name);
            cuisine.setPrice(price);
            OrderDetail orderDetail = new OrderDetail(null, quantity, note, money, cuisine);
            orderDetailsList.add(orderDetail);
        }
        return orderDetailsList;
    }

    private List<OrderDetail> getCuisineData() throws FileNotFoundException {
        return readFromCuisineJSON();
    }

    public void setPaymentInfo() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);
        JsonArray jsonArraySession = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        //Emp Session
        String currentUser = "";
        for (JsonElement element : jsonArraySession) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO dao_employee = new EmployeeDAO(PersistenceUnit.MARIADB_JPA);
            Employee employee = dao_employee.getManyById(id).get(0);
            currentUser  = (employee.getName() != null ? employee.getName() : "Không xác định");
        }
        //get info table and calc table amout
        StringBuilder tabInfoBuilder = new StringBuilder();
        double tableAmount = 0.0;
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO dao_table = new TableDAO(PersistenceUnit.MARIADB_JPA);
            Table table = dao_table.getById(id);
            //append table info
            String floorStr = (table.getFloor() == 0 ? "Tầng trệt" : "Tầng " + table.getFloor());
            tabInfoBuilder.append(table.getName()).append(" (").append(floorStr).append(") ").append(", ");
            //calc table amount
            tableAmount += (table.getTableType().getId().equals(Variable.tableVipID)) ? Variable.tableVipPrice : 0;
        }
        if (!tabInfoBuilder.isEmpty()) {
            tabInfoBuilder.setLength(tabInfoBuilder.length() - 2);
        }
        //calc quantity and amount
        int totalQuantityCuisine = 0;
        double cuisineAmount = 0.0;
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            int cuisineQuantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalQuantityCuisine += cuisineQuantity;
            cuisineAmount += cuisineMoney;
        }
        //calc discount
        double discount = 0.0;
        double discountMoney = cuisineAmount * discount;
        double vat = cuisineAmount * 0.1;
        double finalAmount = tableAmount + cuisineAmount + vat - discountMoney;
        //set Info
        employeeLabel.setText(currentUser);
        tableInfoLabel.setText(tabInfoBuilder.toString());
        cuisineQuantityLabel.setText(totalQuantityCuisine + " món");
        tableAmountLabel.setText(String.format("%,.0f VNĐ", tableAmount));
        cuisineAmountLabel.setText(String.format("%,.0f VNĐ", cuisineAmount));
        vATLabel.setText(String.format("%,.0f VNĐ", vat));
        promotionDiscountLabel.setText(String.format("%,.0f VNĐ", discountMoney));
        finalAmountLabel.setText(String.format("%,.0f VNĐ", finalAmount));
        readCumtomerExistsFromJSON();
    }

    public void setPromotionTableValue() throws FileNotFoundException {
        double totalAmount = 0;
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalAmount += cuisineMoney;
        }
        promotionTableView.getItems().clear();
        PromotionBUS promotionBUS = new PromotionBUS();
        if (!customerRankField.getText().isEmpty()) {
            System.out.println("Rank: " + customerRankField.getText() + " Rank level: " + Utils.toIntMembershipLevel(customerRankField.getText()) + "Total: " + totalAmount);
            int memberShipLevel = Utils.toIntMembershipLevel(customerRankField.getText());
            List<Promotion> promotionList = promotionBUS.getPaymentPromotion(memberShipLevel, totalAmount);
            promotionList.sort(Comparator.comparing(Promotion::getDiscount).reversed());

            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
            promotionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            promotionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            promotionDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
            promotionDiscountColumn.setCellFactory(col -> new TableCell<Promotion, Double>() {
                @Override
                public void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.0f%%", item * 100));
                    }
                }
            });
            promotionTableView.setItems(listPromotion);
        }
    }

    private void readCumtomerExistsFromJSON() throws FileNotFoundException {
        JsonArray customerArray = Utils.readJsonFromFile(Constants.CUSTOMER_PATH);
        if (!customerArray.isEmpty()) {
            JsonObject customerObject = customerArray.get(0).getAsJsonObject();
            String idCustomer = customerObject.get("Customer ID").getAsString();
            CustomerDAO customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);
            Customer customer = customerDAO.getById(idCustomer);
            searchCustomerField.setText((customer != null ? customer.getPhoneNumber() : ""));
        }
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        if(restaurantMainManagerController != null) {
            restaurantMainManagerController.openOrderCuisine();
        } else {
            restaurantMainStaffController.openOrderCuisine();
        }
    }

    @FXML
    void addCustomerButton(ActionEvent event) throws IOException {
        if(restaurantMainManagerController != null) {
            restaurantMainManagerController.openCustomerManagement();
        } else {
            restaurantMainStaffController.openCustomerManagement();
        }
    }

    private void setDiscountFromPromotionSearch() throws FileNotFoundException {
        double totalAmount = 0;
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalAmount += cuisineMoney;
        }
        double discount = 0.0;
        if(!customerRankField.getText().isEmpty()){
            PromotionDAO promotionDAO = new PromotionDAO(PersistenceUnit.MARIADB_JPA);
            List<Promotion> promotionList = promotionDAO.getPaymentPromotion(Utils.toIntMembershipLevel(customerRankField.getText()), totalAmount);
            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
            if (!listPromotion.isEmpty()) {
                Promotion maxDiscountPromotion = listPromotion.stream()
                        .max(Comparator.comparingDouble(Promotion::getDiscount))
                        .orElse(null);

                promotionTableView.getSelectionModel().select(maxDiscountPromotion);
                discount = maxDiscountPromotion.getDiscount();
            } else {
                //Not promotion set default 0
                discount = 0.0;
            }
        }
        //calc table amount
        double tableAmount = 0.0;
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO dao_table = new TableDAO(PersistenceUnit.MARIADB_JPA);
            Table table = dao_table.getById(id);
            tableAmount += (table.getTableType().getId().equals(Variable.tableVipID)) ? Variable.tableVipPrice : 0;
        }
        double discountMoney = totalAmount * discount;
        double vat = totalAmount * 0.1;
        double finalAmount = tableAmount + totalAmount  + vat - discountMoney;
        //set Label
        tableAmountLabel.setText(String.format("%,.0f VNĐ", tableAmount));
        cuisineAmountLabel.setText(String.format("%,.0f VNĐ", totalAmount));
        vATLabel.setText(String.format("%,.0f VNĐ", vat));
        promotionDiscountLabel.setText(String.format("- %,.0f VNĐ", discountMoney));
        finalAmountLabel.setText(String.format("%,.0f VNĐ", finalAmount));
    }

    @FXML
    void onSearchCustomerButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String inputPhone = searchCustomerField.getText().trim();
        CustomerDAO customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);
        Customer customer;
        try {
            customer = customerDAO.getByPhoneNumber(inputPhone);
        } catch (Exception e) {
            customer = null;
        }
        if (customer != null) {
            //Write Down JSON
            String customerID = customer.getId();
            String promotionID = "";
            //display label
            customerIdField.setText(customerID);
            customerNameField.setText(customer.getName());
            customerRankField.setText(Utils.toStringMembershipLevel(customer.getMembershipLevel().getLevel()));
            //enable table
            promotionTableView.setDisable(false);
            // Set discount
            setDiscountFromPromotionSearch();
            //load list promotion
            setPromotionTableValue();
            if(!promotionTableView.getSelectionModel().isEmpty()){
                promotionID = promotionTableView.getSelectionModel().getSelectedItem().getId();
            }
            JsonArray customerArray = new JsonArray();
            JsonObject customerObject = new JsonObject();
            customerObject.addProperty("Customer ID", customerID);
            customerObject.addProperty("Promotion ID", promotionID);
            customerArray.add(customerObject);
            Utils.writeJsonToFile(customerArray, Constants.CUSTOMER_PATH);
        } else {
            customerIdField.setText("");
            customerNameField.setText("");
            customerRankField.setText("");
            promotionTableView.setDisable(true);
            promotionTableView.getSelectionModel().clearSelection();
            //clear JSON if exists
            Utils.writeJsonToFile(new JsonArray(), Constants.CUSTOMER_PATH);
            setPaymentInfo();
        }
    }

    @FXML
    void onCreateCustomerQRButtonClickedClicked(ActionEvent event) {
        openSwingWindow();
    }

    private void openSwingWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cửa sổ quét mã QR");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(640, 480);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);
            JLabel cameraLabel = new JLabel();
            frame.add(cameraLabel, BorderLayout.CENTER);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    stopCamera();
                }
            });

            frame.setVisible(true);
            readQRCode(cameraLabel, frame);
        });
    }

    private void readQRCode(JLabel cameraLabel, JFrame frame) {
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            ToastsMessage.showMessage("Không thể mở camera!", "error");
            return;
        }

        timer = new Timer(100, e -> {
            Mat frameMat = new Mat();
            if (capture != null && capture.read(frameMat)) {
                if (!frameMat.empty()) {
                    BufferedImage bufferedImage = qrCodeHandler.matToBufferedImage(frameMat);
                    String qrCodeContent = qrCodeHandler.decodeQRCode(bufferedImage);
                    if (qrCodeContent != null) {
                        try {
                            updateCustomerFields(qrCodeContent);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        stopCamera();
                        capture.release();
                        cameraLabel.setIcon(null);
                        frame.dispose();
                    } else {
                        ImageIcon icon = new ImageIcon(bufferedImage);
                        cameraLabel.setIcon(icon);
                    }
                }
            } else {
                ToastsMessage.showMessage("Không thể đọc frame từ camera.", "error");
            }
        });

        timer.start();
    }

    private void stopCamera() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (capture != null && capture.isOpened()) {
            capture.release();
        }
    }

    private void updateCustomerFields(String qrCodeContent) throws SQLException, FileNotFoundException {
        String[] parts = qrCodeContent.split(",");
        if (parts.length >= 4) {
            Platform.runLater(() -> {
//              No Delete that code, it's for testing
//                customerIdField.setText(parts[0]);
//                customerNameField.setText(parts[1]);
//                customerRankField.setText(Utils.toStringMembershipLevel(Integer.parseInt(parts[2])));
                String inputPhone = parts[3];
                searchCustomerField.setText(inputPhone);
                searchCustomerButton.fire();
            });
        }
    }

    public void addNewPaymentQueue() throws FileNotFoundException {
        JsonArray paymentQueueArray;
        JsonArray tableArray;
        JsonArray cuisineArray;

        try {
            paymentQueueArray = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);
            tableArray = Utils.readJsonFromFile(Constants.TABLE_PATH);
            cuisineArray = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        } catch (FileNotFoundException e) {
            paymentQueueArray = new JsonArray();
            tableArray = new JsonArray();
            cuisineArray = new JsonArray();
        }

        int newNumericalOrder = !paymentQueueArray.isEmpty()
                ? paymentQueueArray.get(paymentQueueArray.size() - 1).getAsJsonObject().get("Numerical Order").getAsInt() + 1
                : 1;

        JsonObject newPaymentQueue = new JsonObject();
        newPaymentQueue.addProperty("Numerical Order", newNumericalOrder);
        //
        JsonArray customerArray = Utils.readJsonFromFile(Constants.CUSTOMER_PATH);
        if (!customerArray.isEmpty()) {
            JsonObject customerObject = customerArray.get(0).getAsJsonObject();
            newPaymentQueue.addProperty("Customer ID", customerObject.get("Customer ID").getAsString());
            newPaymentQueue.addProperty("Promotion ID", customerObject.get("Promotion ID").getAsString());
        } else {
            newPaymentQueue.addProperty("Customer ID", "");
            newPaymentQueue.addProperty("Promotion ID", "");
        }
        //
        JsonArray tableIDs = new JsonArray();
        for (int i = 0; i < tableArray.size(); i++) {
            JsonObject tableObject = tableArray.get(i).getAsJsonObject();
            tableIDs.add(tableObject.get("Table ID").getAsString());
        }
        newPaymentQueue.add("Table ID", tableIDs);
        //Update status table
        TableBUS tableBUS = new TableBUS();
        System.out.println(tableArray.size());
        for (int i = 0; i < tableArray.size(); i++) {
            System.out.println(tableIDs.get(i).toString());
            tableBUS.updateStatusTable(tableIDs.get(i).toString().replace("\"",""), TableStatus.RESERVED);
        }

        JsonArray cuisineOrderArray = new JsonArray();
        for (int i = 0; i < cuisineArray.size(); i++) {
            JsonObject cuisineItem = cuisineArray.get(i).getAsJsonObject();
            JsonObject cuisineOrderItem = new JsonObject();
            //
            cuisineOrderItem.addProperty("Cuisine ID", cuisineItem.get("Cuisine ID").getAsString());
            cuisineOrderItem.addProperty("Cuisine Name", cuisineItem.get("Cuisine Name").getAsString());
            cuisineOrderItem.addProperty("Cuisine Price", cuisineItem.get("Cuisine Price").getAsDouble());
            cuisineOrderItem.addProperty("Cuisine Note", cuisineItem.get("Cuisine Note").getAsString());
            cuisineOrderItem.addProperty("Cuisine Quantity", cuisineItem.get("Cuisine Quantity").getAsInt());
            cuisineOrderItem.addProperty("Cuisine Money", cuisineItem.get("Cuisine Money").getAsDouble());
            cuisineOrderArray.add(cuisineOrderItem);
        }
        newPaymentQueue.add("Cuisine Order", cuisineOrderArray);

        paymentQueueArray.add(newPaymentQueue);

        Utils.writeJsonToFile(paymentQueueArray, Constants.PAYMENT_QUEUE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TABLE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.CUSTOMER_PATH);
    }

    //save payment queue
    @FXML
    void onSavePaymentQueueButtonClicked(ActionEvent event) throws IOException {
        addNewPaymentQueue();
        if(restaurantMainManagerController != null) {
            restaurantMainManagerController.openReservationManagement();
        } else {
            restaurantMainStaffController.openReservationManagement();
        }
    }

    @FXML
    void onSearchCustomerFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchCustomerButton.fire();
        }
    }

    @FXML
    void onPaymentButtonClicked(ActionEvent event) throws IOException {
        if(restaurantMainManagerController != null) {
            restaurantMainManagerController.openOrderPaymentFinal();
        } else {
            restaurantMainStaffController.openOrderPaymentFinal();

        }
    }
}