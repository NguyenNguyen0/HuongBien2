package huongbien.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.config.Constants;
import huongbien.config.Variable;
import huongbien.dao.remote.IPromotionDAO;
import huongbien.dao.remote.ITableDAO;
import huongbien.entity.*;
import huongbien.rmi.RMIClient;
import huongbien.service.*;
import huongbien.util.ClearJSON;
import huongbien.util.Converter;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderPaymentFinalController implements Initializable {
    @FXML
    public GridPane suggestMoneyButtonContainer;
    @FXML
    public GridPane cuisineGridPane;
    //Controller area
    public RestaurantMainManagerController restaurantMainManagerController;
    public RestaurantMainStaffController restaurantMainStaffController;
    @FXML
    private VBox screenCashMethodVBox;
    @FXML
    private VBox screenBankingMethodVBox;
    @FXML
    private VBox screenEWalletMethodVBox;
    @FXML
    private ScrollPane cuisineScrollPane;
    @FXML
    private TextField paymentAmountField;
    @FXML
    private Label paymentAmountLabel;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label dispensedAmountLabel;
    @FXML
    private Label statusLabel;
    private boolean statusFlags = false;

    public void setRestaurantMainManagerController(RestaurantMainManagerController restaurantMainManagerController) {
        this.restaurantMainManagerController = restaurantMainManagerController;
    }

    public void setRestaurantMainStaffController(RestaurantMainStaffController restaurantMainStaffController) {
        this.restaurantMainStaffController = restaurantMainStaffController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefaultInfo();
        try {
            setFinalAmountInfoFromJSON();
            loadCuisine();
        } catch (FileNotFoundException | SQLException | RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDefaultInfo() {
        paymentAmountField.setText("0");
        paymentAmountLabel.setText("0 VNĐ");
        totalAmountLabel.setText("0 VNĐ");
        dispensedAmountLabel.setText("0 VNĐ");
        statusLabel.setText("Chưa thu tiền khách");
        statusLabel.setStyle("-fx-text-fill: red");
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
                OrderPaymentBillItemController orderPaymentBillItemController = fxmlLoader.getController();
                orderPaymentBillItemController.setDataBill(orderDetails.get(i));
                orderPaymentBillItemController.setOrderPaymentFinalController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                cuisineGridPane.add(paymentBillBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cuisineScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cuisineGridPane.prefWidthProperty().bind(cuisineScrollPane.widthProperty());
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

    public void setFinalAmountInfoFromJSON() throws FileNotFoundException, SQLException, RemoteException, NotBoundException {
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);
        JsonArray jsonArrayCustomer = Utils.readJsonFromFile(Constants.CUSTOMER_PATH);
        //Set screen method default
        screenCashMethodVBox.setVisible(true);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(false);
        //calc table amount
        double tableAmount = 0.0;
        TableBUS tableBUS = new TableBUS();
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            tableBUS.updateStatusTable(id, TableStatus.AVAILABLE);
            ITableDAO dao_table = RMIClient.getInstance().getTableDAO();
            Table table = dao_table.getById(id);
            tableAmount += (table.getTableType().getId().equals(Variable.tableVipID)) ? Variable.tableVipPrice : 0;
        }
        //calc cuisine amount
        double cuisineAmount = 0.0;
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            cuisineAmount += cuisineMoney;
        }
        //calc discount
        double discount = 0.0;
        for (JsonElement element : jsonArrayCustomer) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Promotion ID").getAsString();
            IPromotionDAO promotionDAO = RMIClient.getInstance().getPromotionDAO();
            Promotion promotion = promotionDAO.getById(id);
            if (promotion != null) {
                discount = promotion.getDiscount();
            }
        }
        double discountMoney = cuisineAmount * discount;
        double vat = cuisineAmount * 0.1;
        double finalAmount = tableAmount + cuisineAmount + vat - discountMoney;
        totalAmountLabel.setText(Converter.formatMoney(finalAmount) + " VNĐ");

        renderSuggestMoneyButtons(finalAmount);
    }

    public Button createSuggestMoneyButton(String label) {
        Button button = new Button(label);
        button.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        button.setFont(javafx.scene.text.Font.font("System", FontWeight.BOLD, 30));
        button.setStyle("-fx-background-color: #fff; -fx-text-fill: #000;");
        button.setMinSize(200, 60);
        return button;
    }

    public void renderSuggestMoneyButtons(double finalAmount) {
        suggestMoneyButtonContainer.getChildren().clear();
        int buttonsPerRow = 3;
        int[] suggestedMoneys = Utils.suggestMoneyReceived((int) finalAmount);
        int columns = 0;
        int rows = 0;
        for (int label : suggestedMoneys) {
            Button button = createSuggestMoneyButton(Converter.formatMoney(label));
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, s -> {
                paymentAmountField.setText(Converter.formatMoney(label));
                paymentAmountLabel.setText(Converter.formatMoney(label) + " VNĐ");
                setFinalAmountInfo();
            });
            suggestMoneyButtonContainer.add(button, columns, rows);
            columns++;
            if (columns == buttonsPerRow) {
                columns = 0;
                rows++;
            }
        }
    }

    public void setFinalAmountInfo() {
        double moneyFromCustomer = Converter.parseMoney(paymentAmountField.getText());
        double finalAmount = Converter.parseMoney(totalAmountLabel.getText());
        if (moneyFromCustomer >= finalAmount) {
            statusLabel.setText("Khách đưa đủ tiền");
            statusLabel.setStyle("-fx-text-fill: white");
            double refund = moneyFromCustomer - finalAmount;
            dispensedAmountLabel.setText(Converter.formatMoney((int) refund) + " VNĐ");
            statusFlags = true;
        } else {
            statusLabel.setText("Khách đưa thiếu tiền");
            statusLabel.setStyle("-fx-text-fill: red");
            dispensedAmountLabel.setText("0 VNĐ");
            statusFlags = false;
        }
    }

    public void openCashMethod() {
        screenCashMethodVBox.setVisible(true);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(false);
    }

    public void openBankingMethod() {
        screenCashMethodVBox.setVisible(false);
        screenBankingMethodVBox.setVisible(true);
        screenEWalletMethodVBox.setVisible(false);
    }

    public void openEWalletMethod() {
        screenCashMethodVBox.setVisible(false);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(true);
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        if (restaurantMainManagerController != null) {
            restaurantMainManagerController.openOrderPayment();
        } else {
            restaurantMainStaffController.openOrderPayment();

        }
    }

    @FXML
    void onCashButtonAction(ActionEvent event) {
        openCashMethod();
    }

    @FXML
    void onBankingButtonAction(ActionEvent event) {
        openBankingMethod();
    }

    @FXML
    void onEWalletButtonAction(ActionEvent event) {
        openEWalletMethod();
    }

    //mini calculator
    @FXML
    void onNumberClicked(MouseEvent event) {
        int value = Integer.parseInt(((Pane) event.getSource()).getId().replace("keyFlowPane", ""));
        double currentResult = Converter.parseMoney(paymentAmountField.getText());
        double newResult = currentResult == 0 ? (double) value : currentResult * 10 + value;
        String result = Converter.formatMoney((int) newResult);
        paymentAmountField.setText(result);
        paymentAmountLabel.setText(result + " VNĐ");
        setFinalAmountInfo();
    }

    @FXML
    void onSymbolClicked(MouseEvent event) {
        String symbol = ((Pane) event.getSource()).getId().replace("keyFlowPane", "");
        switch (symbol) {
            case "Clear":
                paymentAmountField.setText("0");
                paymentAmountLabel.setText("0" + " VNĐ");
                break;
            case "Delete":
                String currentText = paymentAmountField.getText().replace(".", "");
                if (!currentText.isEmpty()) {
                    currentText = currentText.substring(0, currentText.length() - 1);
                    String result = currentText.isEmpty() ? "0" : Converter.formatMoney(Converter.parseMoney(currentText));
                    paymentAmountField.setText(result);
                    paymentAmountLabel.setText(result + " VNĐ");
                }
                break;
        }
        setFinalAmountInfo();
    }

    private void openPrinter() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/InvoicePrinterDialog.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 1200, 700);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        InvoicePrinterDialogController invoicePrinterDialogController = loader.getController();
        invoicePrinterDialogController.setOrderPaymentFinalController(this);
        primaryStage.show();
    }

    @FXML
    void onInvoicePrinterDialogAction(ActionEvent event) throws IOException {
        openPrinter();
    }

    @FXML
    void onCompleteOrderPaymentFinalAction(ActionEvent event) throws IOException {
        if (!statusFlags) {
            ToastsMessage.showMessage("Vui lòng thu tiền thanh toán", "warning");
            return;
        }
        try {
            String orderId = Order.generateId(LocalDate.now(), LocalTime.now());

            JsonArray jsonArraySession = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
            Employee employee = null;
            for (JsonElement element : jsonArraySession) {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = jsonObject.get("Employee ID").getAsString();
                EmployeeBUS employeeBUS = new EmployeeBUS();
                employee = employeeBUS.getEmployeeById(id).get(0);
            }

            JsonArray jsonArrayCustomer = Utils.readJsonFromFile(Constants.CUSTOMER_PATH);
            Customer customer = null;
            Promotion promotion = null;
            for (JsonElement element : jsonArrayCustomer) {
                JsonObject jsonObject = element.getAsJsonObject();
                String customerId = jsonObject.get("Customer ID").getAsString();
                String promotionId = jsonObject.get("Promotion ID").getAsString();
                CustomerBUS customerBUS = new CustomerBUS();
                customer = customerBUS.getCustomerById(customerId);
                promotion = new PromotionBUS().getPromotion(promotionId);
            }

            JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);
            ArrayList<Table> tables = new ArrayList<>();
            for (JsonElement element : jsonArrayTable) {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = jsonObject.get("Table ID").getAsString();
                TableBUS tableBUS = new TableBUS();
                Table table = tableBUS.getTable(id);
                tables.add(table);
            }

            JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
            ArrayList<OrderDetail> orderDetails = new ArrayList<>();
            for (JsonElement element : jsonArrayCuisine) {
                JsonObject jsonObject = element.getAsJsonObject();
                String cuisineId = jsonObject.get("Cuisine ID").getAsString();
                String note = jsonObject.get("Cuisine Note").getAsString();
                int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
                double salePrice = jsonObject.get("Cuisine Price").getAsDouble();
                CuisineBUS cuisineBUS = new CuisineBUS();
                OrderDetail orderDetail = new OrderDetail(orderId, quantity, note, salePrice, cuisineBUS.getCuisineById(cuisineId));
                orderDetails.add(orderDetail);
            }

            double moneyFromCustomer = Converter.parseMoney(paymentAmountField.getText());
            double finalAmount = Converter.parseMoney(totalAmountLabel.getText());
            Payment payment = new Payment(moneyFromCustomer, PaymentMethod.CASH);

            Order order = new Order("", employee, customer, payment, promotion, orderDetails, tables);
            order.setId(orderId);
            order.setTotalAmount(finalAmount);
            order.setDispensedAmount(moneyFromCustomer - finalAmount);

            OrderBUS orderBUS = new OrderBUS();
            if (orderBUS.addOrder(order)) {
                ClearJSON.clearAllJsonWithoutLoginSession_PaymentQueue();
                ToastsMessage.showMessage("Thanh toán thành công", "success");
                //Alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText("Thanh toán thành công");
                alert.setContentText("Bạn có muốn tiếp tục bán hàng không?");
                ButtonType btn_ok = new ButtonType("Đồng ý");
                ButtonType onCancelButtonClicked = new ButtonType("Không");
                alert.getButtonTypes().setAll(btn_ok, onCancelButtonClicked);
                Optional<ButtonType> result = alert.showAndWait();
                ClearJSON.clearAllJsonWithoutLoginSession_PaymentQueue();
                if (result.isPresent() && result.get() == btn_ok) {
                    if (restaurantMainManagerController != null) {
                        restaurantMainManagerController.openOrderTable();
                    } else {
                        restaurantMainStaffController.openOrderTable();
                    }
                } else {
                    if (restaurantMainManagerController != null) {
                        restaurantMainManagerController.openHome();
                    } else {
                        restaurantMainStaffController.openHome();
                    }
                }
            } else {
                System.err.println("order: " + order);
                ToastsMessage.showMessage("Thanh toán thất bại", "error");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClearOrderPaymentFinalAction(ActionEvent event) throws IOException {
        try {
            ClearJSON.clearAllJsonWithoutLoginSession_PaymentQueue();
            if (restaurantMainManagerController != null) {
                restaurantMainManagerController.openOrderTable();
            } else {
                restaurantMainStaffController.openOrderTable();
            }
            ToastsMessage.showMessage("Hủy đơn hàng thành công", "success");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}