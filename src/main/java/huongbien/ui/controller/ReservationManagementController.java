package huongbien.ui.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.bus.CustomerBUS;
import huongbien.bus.TableBUS;
import huongbien.config.Constants;
import huongbien.config.Variable;
import huongbien.dao.CustomerDAO;
import huongbien.dao.PromotionDAO;
import huongbien.dao.ReservationDAO;
import huongbien.dao.TableDAO;
import huongbien.entity.*;
import huongbien.jpa.PersistenceUnit;
import huongbien.util.ClearJSON;
import huongbien.util.Converter;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReservationManagementController implements Initializable {
    //Payment-Queue
    @FXML
    private TableView<Map<String, Object>> paymentQueueTableView;
    @FXML
    private TableColumn<Map<String, Object>, Integer> numericalPaymentQueueOrderColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> customerPaymentQueueColumn;
    @FXML
    private TableColumn<Map<String, Object>, Integer> quantityCuisinePaymentQueueColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> promotionPaymentQueueColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> totalAmountPaymentQueueColumn;
    @FXML
    private Button toOrderPaymentButton;
    @FXML
    private Button deletePaymentQueueButton;
    @FXML
    private Label countPaymentQueueLabel;
    @FXML
    private Label customerNamePaymentQueueLabel;
    @FXML
    private Label tableAreaPaymentQueueLabel;
    @FXML
    private Label promotionNamePaymentQueueLabel;
    @FXML
    private Label cuisineQuantityPaymentQueueLabel;
    @FXML
    private Label totalAmountPaymentQueueLabel;
    //Pre-Order
    @FXML
    private TableView<Reservation> preOrderTableView;
    @FXML
    private TableColumn<Reservation, String> idPreOrderColumn;
    @FXML
    private TableColumn<Reservation, String> customerPreOrderColumn;
    @FXML
    private TableColumn<Reservation, Integer> partySizePreOrderColumn;
    @FXML
    private TableColumn<Reservation, LocalTime> receiveTimePreOrderColumn;
    @FXML
    private TableColumn<Reservation, String> partyTypePreOrderColumn;
    @FXML
    private ComboBox<String> statusPreOrderComboBox;
    @FXML
    private DatePicker receivePreOrderDatePicker;
    @FXML
    private Button editPreOrderButton;
    @FXML
    private Button confirmTablePreOrderButton;
    @FXML
    private Button cancelPreOrderButton;
    @FXML
    private Label countPreOrderLabel;
    @FXML
    private Label customerPreOrderLabel;
    @FXML
    private Label tablePreOrderLabel;
    @FXML
    private Label cuisinePreOrderLabel;
    @FXML
    private Label depositPreOrderLabel;
    @FXML
    private Label refundDepositPreOrderLabel;
    @FXML
    private Label notePreOrderLabel;
    @FXML
    private TextField searchReservation;

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
        //Payment Queue
        try {
            setUIDefault();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadPaymentQueueDataFromJSON();

        //Pre-Order
        setPreOrderTableViewColumn();
        searchReservation.clear();
    }

    private void disablePayQueueButton() {
        deletePaymentQueueButton.setVisible(false);
        toOrderPaymentButton.setVisible(false);
    }

    private void enablePayQueueButton() {
        deletePaymentQueueButton.setVisible(true);
        toOrderPaymentButton.setVisible(true);
    }

    private void disablePreOrderButton() {
        editPreOrderButton.setVisible(false);
        cancelPreOrderButton.setVisible(false);
        confirmTablePreOrderButton.setVisible(false);
    }

    private void enablePreOrderButton() {
        editPreOrderButton.setVisible(true);
        cancelPreOrderButton.setVisible(true);
        confirmTablePreOrderButton.setVisible(true);
    }

    private void setUIDefault() throws FileNotFoundException {
        //Payment Queue
        customerNamePaymentQueueLabel.setText("");
        tableAreaPaymentQueueLabel.setText("");
        cuisineQuantityPaymentQueueLabel.setText("");
        promotionNamePaymentQueueLabel.setText("");
        totalAmountPaymentQueueLabel.setText("");
        countPaymentQueueLabel.setText("( " + Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH).size() + " )");
        disablePayQueueButton();
        //Pre-Order
        statusPreOrderComboBox.getItems().addAll(ReservationStatus.PENDING.getStatus(), ReservationStatus.COMPLETED.getStatus(), ReservationStatus.CANCELLED.getStatus());
        statusPreOrderComboBox.getSelectionModel().selectFirst();
        receivePreOrderDatePicker.setValue(LocalDate.now());
        customerPreOrderLabel.setText("");
        tablePreOrderLabel.setText("");
        cuisinePreOrderLabel.setText("");
        depositPreOrderLabel.setText("");
        refundDepositPreOrderLabel.setText("");
        notePreOrderLabel.setText("");
        disablePreOrderButton();
    }

    //TODO: Payment Queue
    //function area
    private void loadPaymentQueueDataFromJSON() {
        CustomerDAO customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);
        paymentQueueTableView.setPlaceholder(new Label("Không có dữ liệu"));
        numericalPaymentQueueOrderColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>((Integer) cellData.getValue().get("Numerical Order"))
        );

        customerPaymentQueueColumn.setCellValueFactory(cellData -> {
            String customerId = (String) cellData.getValue().get("Customer ID");
            if (customerId == null || customerId.isEmpty()) {
                return new SimpleObjectProperty<>("Khách vãng lai");
            }
            Customer customer = customerDAO.getById(customerId);
            String customerName = (customer != null) ? customer.getName() : "Không xác định";
            return new SimpleObjectProperty<>(customerName);
        });

        PromotionDAO promotionDAO = new PromotionDAO(PersistenceUnit.MARIADB_JPA);
        promotionPaymentQueueColumn.setCellValueFactory(cellData -> {
            String promotionId = (String) cellData.getValue().get("Promotion ID");
            if (promotionId == null || promotionId.isEmpty()) {
                return new SimpleObjectProperty<>("Không áp dụng");
            }
            Promotion promotion = promotionDAO.getById(promotionId);
            String promotionName = (promotion != null) ? promotion.getName() : "Không xác định";
            return new SimpleObjectProperty<>(promotionName);
        });

        quantityCuisinePaymentQueueColumn.setCellValueFactory(cellData -> {
            int cuisineCount = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).size();
            return new SimpleObjectProperty<>(cuisineCount);
        });

        totalAmountPaymentQueueColumn.setCellValueFactory(cellData -> {
            double totalAmount = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).stream()
                    .mapToDouble(cuisine -> (double) cuisine.get("Cuisine Money"))
                    .sum();
            return new SimpleObjectProperty<>(String.format("%,.0f VNĐ", totalAmount));
        });

        ObservableList<Map<String, Object>> paymentQueueObservableList = FXCollections.observableArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(Constants.PAYMENT_QUEUE_PATH));
            for (JsonNode orderNode : rootNode) {
                Map<String, Object> orderMap = objectMapper.convertValue(orderNode, Map.class);
                paymentQueueObservableList.add(orderMap);
            }
            paymentQueueTableView.setItems(paymentQueueObservableList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInfoJsonHandlers() throws IOException {
        int selectedIndex = paymentQueueTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Integer numericalOrder = numericalPaymentQueueOrderColumn.getCellData(selectedIndex);
            JsonArray paymentQueueArray = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);
            JsonObject selectedPaymentQueue = null;
            int selectedPaymentQueueIndex = -1;
            for (int i = 0; i < paymentQueueArray.size(); i++) {
                JsonObject paymentQueue = paymentQueueArray.get(i).getAsJsonObject();
                if (paymentQueue.get("Numerical Order").getAsInt() == numericalOrder) {
                    selectedPaymentQueue = paymentQueue;
                    selectedPaymentQueueIndex = i;
                    break;
                }
            }

            //cuisine JSON
            assert selectedPaymentQueue != null;
            JsonArray cuisineOrderArray = selectedPaymentQueue.getAsJsonArray("Cuisine Order");
            Utils.writeJsonToFile(cuisineOrderArray, Constants.CUISINE_PATH);
            //Table JSON
            JsonArray tableIDArray = selectedPaymentQueue.getAsJsonArray("Table ID");
            JsonArray tableArray = new JsonArray();
            for (int i = 0; i < tableIDArray.size(); i++) {
                JsonObject tableObject = new JsonObject();
                tableObject.addProperty("Table ID", tableIDArray.get(i).getAsString());
                tableArray.add(tableObject);
            }
            Utils.writeJsonToFile(tableArray, Constants.TABLE_PATH);
            // Customer JSON
            JsonArray customerArray = new JsonArray();
            JsonObject customerObject = new JsonObject();
            customerObject.addProperty("Customer ID", selectedPaymentQueue.get("Customer ID").getAsString());
            customerObject.addProperty("Promotion ID", selectedPaymentQueue.get("Promotion ID").getAsString());
            customerArray.add(customerObject);
            Utils.writeJsonToFile(customerArray, Constants.CUSTOMER_PATH);
            //
            paymentQueueArray.remove(selectedPaymentQueueIndex);
            Utils.writeJsonToFile(paymentQueueArray, Constants.PAYMENT_QUEUE_PATH);
            if(restaurantMainManagerController != null) {
                restaurantMainManagerController.openOrderPayment();
            } else {
                restaurantMainStaffController.openOrderPayment();
            }
        }
    }

    private void deleteInfoFromPaymentQueueJsonHandlers() throws FileNotFoundException {
        int selectedIndex = paymentQueueTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Integer numericalOrder = numericalPaymentQueueOrderColumn.getCellData(selectedIndex);
            JsonArray paymentQueueArray = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);
            int selectedPaymentQueueIndex = -1;

            for (int i = 0; i < paymentQueueArray.size(); i++) {
                JsonObject paymentQueue = paymentQueueArray.get(i).getAsJsonObject();
                if (paymentQueue.get("Numerical Order").getAsInt() == numericalOrder) {
                    selectedPaymentQueueIndex = i;
                    break;
                }
            }

            if (selectedPaymentQueueIndex != -1) {
                paymentQueueArray.remove(selectedPaymentQueueIndex);
                Utils.writeJsonToFile(paymentQueueArray, Constants.PAYMENT_QUEUE_PATH);
                ToastsMessage.showMessage("Đã xóa mục thanh toán với số thứ tự: " + numericalOrder, "success");
            } else {
                ToastsMessage.showMessage("Không tìm thấy mục thanh toán với số thứ tự: " + numericalOrder, "warning");
            }
        } else {
            ToastsMessage.showMessage("Vui lòng chọn một thanh toán để xóa", "warning");
        }
    }

    @FXML
    void onPaymentQueueTableViewClicked(MouseEvent event) throws FileNotFoundException {
        int selectedIndex = paymentQueueTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Integer numericalOrder = numericalPaymentQueueOrderColumn.getCellData(selectedIndex);
            JsonArray jsonArray = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);
            for (JsonElement element : jsonArray) {
                JsonObject order = element.getAsJsonObject();
                if (order.get("Numerical Order").getAsInt() == numericalOrder) {
                    // Lấy thông tin khách hàng
                    String customerID = order.has("Customer ID") ? order.get("Customer ID").getAsString() : "";
                    Customer customer = customerID.isEmpty() ? null : new CustomerDAO(PersistenceUnit.MARIADB_JPA).getById(customerID);
                    String customerName = (customer != null) ? customer.getName() : "Khách vãng lai";
                    // Lấy thông tin khu vực bàn
                    TableDAO tableDAO = new TableDAO(PersistenceUnit.MARIADB_JPA);
                    JsonArray tableIDArray = order.getAsJsonArray("Table ID");
                    StringBuilder tableArea = new StringBuilder();
                    for (int j = 0; j < tableIDArray.size(); j++) {
                        String tableId = tableIDArray.get(j).getAsString();
                        Table table = tableDAO.getById(tableId);
                        if (table != null) {
                            String tableName = table.getName();
                            int tableFloor = table.getFloor();
                            String tableFloorStr = (tableFloor == 0) ? "Tầng trệt" : "Tầng " + tableFloor;
                            tableArea.append(tableName).append(" (").append(tableFloorStr).append(")");
                        } else {
                            tableArea.append("Tên bàn không xác định");
                        }
                        if (j < tableIDArray.size() - 1) {
                            tableArea.append(", ");
                        }
                    }
                    // Lấy thông tin khuyến mãi
                    String promotionID = order.has("Promotion ID") ? order.get("Promotion ID").getAsString() : "";
                    Promotion promotion = promotionID.isEmpty() ? null : new PromotionDAO(PersistenceUnit.MARIADB_JPA).getById(promotionID);
                    String promotionName = (promotion != null) ? promotion.getName() : "Không áp dụng";

                    JsonArray cuisineOrderArray = order.getAsJsonArray("Cuisine Order");
                    int cuisineQuantity = 0;
                    double totalAmount = 0;
                    for (JsonElement cuisineElement : cuisineOrderArray) {
                        JsonObject cuisine = cuisineElement.getAsJsonObject();
                        double money = cuisine.get("Cuisine Money").getAsDouble();
                        int quantity = cuisine.get("Cuisine Quantity").getAsInt();
                        totalAmount += money;
                        cuisineQuantity += quantity;
                    }
                    //setLabel
                    customerNamePaymentQueueLabel.setText(customerName);
                    tableAreaPaymentQueueLabel.setText(tableArea.toString());
                    cuisineQuantityPaymentQueueLabel.setText(cuisineQuantity + " món");
                    promotionNamePaymentQueueLabel.setText(promotionName);
                    totalAmountPaymentQueueLabel.setText(String.format("%,.0f VNĐ", totalAmount));
                    break;
                }
            }
            enablePayQueueButton();
        }
    }

    @FXML
    void onOrderPaymentButtonAction(ActionEvent event) throws IOException {
        writeInfoJsonHandlers();
    }

    @FXML
    void onDeletePaymentQueueButtonAction(ActionEvent event) throws FileNotFoundException {
        deleteInfoFromPaymentQueueJsonHandlers();
        paymentQueueTableView.getItems().clear();
        loadPaymentQueueDataFromJSON();
        setUIDefault();
    }

    //Pre-Order here
    private void setPreOrderTableViewColumn() {
        ReservationDAO reservationDAO = new ReservationDAO();
        CustomerBUS customerBUS = new CustomerBUS();
        String id = "";
        if(!searchReservation.getText().isEmpty()) {
            String search = searchReservation.getText();
            if(customerBUS.getCustomerSearchReservation(search) != null){
                id = customerBUS.getCustomerSearchReservation(search).getId();
            }
        }
        //set Quantity Pre Order
        int quantity = reservationDAO.getCountStatusReservationByDate(receivePreOrderDatePicker.getValue(), statusPreOrderComboBox.getValue(), id);
        countPreOrderLabel.setText("( " + quantity + " )");
        //table view
        preOrderTableView.getItems().clear();
        preOrderTableView.setPlaceholder(new Label("Không có dữ liệu"));
        List<Reservation> reservationList = reservationDAO.getStatusReservationByDate(receivePreOrderDatePicker.getValue(), statusPreOrderComboBox.getValue(), id);
        idPreOrderColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        customerPreOrderColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue().getCustomer();
            return new SimpleStringProperty(customer.getName());
        });
        partySizePreOrderColumn.setCellValueFactory(new PropertyValueFactory<>("partySize"));
        receiveTimePreOrderColumn.setCellValueFactory(new PropertyValueFactory<>("receiveTime"));
        partyTypePreOrderColumn.setCellValueFactory(new PropertyValueFactory<>("partyType"));
        ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList(reservationList);
        preOrderTableView.setItems(reservationObservableList);
    }

    @FXML
    void receivePreOrderDatePickerAction(ActionEvent event) {
        setPreOrderTableViewColumn();
    }

    @FXML
    void statusPreOrderComboBoxAction(ActionEvent event) {
        setPreOrderTableViewColumn();
    }

    @FXML
    void onPreOrderTableViewClicked(MouseEvent event) throws FileNotFoundException {
        int selectedIndex = preOrderTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Reservation reservation = preOrderTableView.getSelectionModel().getSelectedItem();
            customerPreOrderLabel.setText(reservation.getCustomer().getName());
            //Format Table label
            List<Table> tables = reservation.getTables();
            StringBuilder tableInfo = new StringBuilder();
            for (Table table : tables) {
                String tableFloorStr = (table.getFloor() == 0) ? "Tầng trệt" : "Tầng " + table.getFloor();
                tableInfo.append(table.getName())
                        .append(" (")
                        .append(tableFloorStr)
                        .append(" - ")
                        .append(table.getTableType().getName())
                        .append("), ");
            }
            if (!tableInfo.isEmpty()) {
                tableInfo.setLength(tableInfo.length() - 2);
            }
            tablePreOrderLabel.setText(tableInfo.toString());
            int cuisineQuantity = 0;
            for (FoodOrder foodOrder : reservation.getFoodOrders()) {
                cuisineQuantity += foodOrder.getQuantity();
            }
            cuisinePreOrderLabel.setText(cuisineQuantity + " món");
            depositPreOrderLabel.setText(String.format("%,.0f VNĐ", reservation.getDeposit()));
            refundDepositPreOrderLabel.setText(String.format("%,.0f VNĐ", reservation.getRefundDeposit()));
            notePreOrderLabel.setText(reservation.getNote() != null ? reservation.getNote() : "");
            enablePreOrderButton();
        }
    }

    @FXML
    void onPreOrderButtonAction(ActionEvent event) throws IOException {
        ClearJSON.clearAllJsonWithoutLoginSession();
        if(restaurantMainManagerController != null) {
            restaurantMainManagerController.openPreOrder();
        } else {
            restaurantMainStaffController.openPreOrder();
        }
    }

    @FXML
    void onEditPreOrderButtonAction(ActionEvent event) throws IOException {
        int selectedIndex = preOrderTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Reservation reservation = preOrderTableView.getSelectionModel().getSelectedItem();
            String id = reservation.getId();
            ToastsMessage.showMessage("Đang cập nhật bàn cho đơn hàng đặt trước: " + id, "success");
            //reservation
            JsonArray jsonArrayReservation = new JsonArray();
            JsonObject jsonObjectReservation = new JsonObject();
            jsonObjectReservation.addProperty("Reservation ID", id);
            jsonArrayReservation.add(jsonObjectReservation);
            Utils.writeJsonToFile(jsonArrayReservation, Constants.RESERVATION_PATH);

            //Convert Reservation Database to JSON
            //cuisine
            List<FoodOrder> foodOrders = reservation.getFoodOrders();
            JsonArray jsonArrayCuisine = new JsonArray();
            for (FoodOrder foodOrder : foodOrders) {
                JsonObject jsonObjectCuisine = new JsonObject();
                jsonObjectCuisine.addProperty("Cuisine ID", foodOrder.getCuisine().getId());
                jsonObjectCuisine.addProperty("Cuisine Name", foodOrder.getCuisine().getName());
                jsonObjectCuisine.addProperty("Cuisine Price", foodOrder.getCuisine().getPrice());
                jsonObjectCuisine.addProperty("Cuisine Note", foodOrder.getNote());
                jsonObjectCuisine.addProperty("Cuisine Quantity", foodOrder.getQuantity());
                jsonObjectCuisine.addProperty("Cuisine Money", foodOrder.getQuantity() * foodOrder.getCuisine().getPrice());
                jsonArrayCuisine.add(jsonObjectCuisine);
            }
            Utils.writeJsonToFile(jsonArrayCuisine, Constants.CUISINE_PATH);

            //table
            List<Table> tables = reservation.getTables();
            JsonArray jsonArrayTable = new JsonArray();
            for (Table table : tables) {
                JsonObject jsonObjectTable = new JsonObject();
                jsonObjectTable.addProperty("Table ID", table.getId());
                jsonArrayTable.add(jsonObjectTable);
            }
            Utils.writeJsonToFile(jsonArrayTable, Constants.TABLE_PATH);

            //customer
            JsonArray jsonArrayCustomer = new JsonArray();
            JsonObject jsonObjectCustomer = new JsonObject();
            jsonObjectCustomer.addProperty("Customer ID", reservation.getCustomer().getId());
            jsonArrayCustomer.add(jsonObjectCustomer);
            Utils.writeJsonToFile(jsonArrayCustomer, Constants.CUSTOMER_PATH);
        }
        if(restaurantMainManagerController != null) {
            restaurantMainManagerController.openPreOrder();
        } else {
            restaurantMainStaffController.openPreOrder();
        }
    }

    @FXML
    void onConfirmTablePreOrderButtonAction(ActionEvent event) throws IOException {
        ReservationDAO reservationDAO = new ReservationDAO();
        int selectedIndex = preOrderTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Reservation reservation = preOrderTableView.getSelectionModel().getSelectedItem();
            //check status before write JSON
            if(!reservation.getStatus().equals(Variable.statusReservation[0])){
                ToastsMessage.showMessage("Đơn đặt đang ở trạng thái: "+ reservation.getStatus() +", nên không thể nhận bàn", "warning");
                return;
            }
            //check receice date valid
            if(!reservation.getReceiveDate().equals(LocalDate.now())){
                ToastsMessage.showMessage("Vui lòng đợi đến ngày: " + reservation.getReceiveDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " để nhận bàn.", "success");
                ToastsMessage.showMessage("Chưa đến ngày nhận bàn này!", "warning");
                return;
            }
            ////----Table
            JsonArray jsonArrayTable = new JsonArray();
            for (Table table : reservation.getTables()) {
                JsonObject tableObject = new JsonObject();
                tableObject.addProperty("Table ID", table.getId());
                jsonArrayTable.add(tableObject);
            }
            ////----Customer
            JsonArray jsonArrayCustomer = new JsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Customer ID", reservation.getCustomer().getId());
            jsonArrayCustomer.add(jsonObject);
            ////----Cuisine
            JsonArray jsonArrayCuisine = new JsonArray();
            for (FoodOrder foodOrder : reservation.getFoodOrders()) {
                JsonObject foodOrderObject = new JsonObject();
                foodOrderObject.addProperty("Cuisine ID", foodOrder.getCuisine().getId());
                foodOrderObject.addProperty("Cuisine Name", foodOrder.getCuisine().getName());
                foodOrderObject.addProperty("Cuisine Price", foodOrder.getCuisine().getPrice());
                foodOrderObject.addProperty("Cuisine Note", foodOrder.getNote());
                foodOrderObject.addProperty("Cuisine Quantity", foodOrder.getQuantity());
                foodOrderObject.addProperty("Cuisine Money", foodOrder.getQuantity() * foodOrder.getCuisine().getPrice());
                jsonArrayCuisine.add(foodOrderObject);
            }
            Utils.writeJsonToFile(jsonArrayTable, Constants.TABLE_PATH);
            Utils.writeJsonToFile(jsonArrayCustomer, Constants.CUSTOMER_PATH);
            Utils.writeJsonToFile(jsonArrayCuisine, Constants.CUISINE_PATH);
            reservationDAO.updateStatus(reservation.getId(), ReservationStatus.COMPLETED);
            if(restaurantMainManagerController != null) {
                restaurantMainManagerController.openOrderPayment();
            } else {
                restaurantMainStaffController.openOrderPayment();
            }
        } else {
            ToastsMessage.showMessage("Vui lòng chọn một đơn đặt để xác nhận", "warning");
        }
    }

    @FXML
    void onCancelPreOrderButtonAction(ActionEvent event) {
        ReservationDAO reservationDAO = new ReservationDAO();
        TableBUS tableBUS = new TableBUS();
        int selectedIndex = preOrderTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Reservation reservation = preOrderTableView.getSelectionModel().getSelectedItem();
            String id = reservation.getId();
            String status = reservation.getStatus().getStatus();
            double deposit = reservation.getDeposit();
            double refundDeposit = 0;
            if (status.equals(Variable.statusReservation[0])) {
                //deposit refund processing
                ////---Check deposit refund by datetime (ke tu luc dat den truoc luc huy la 12h 50%, con lai 0%)
                LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getReservationDate(), reservation.getReservationTime());
                LocalDateTime deadline = reservationDateTime.plusHours(12);
                LocalDateTime now = LocalDateTime.now();
                String notify;

                if (now.isBefore(deadline)) {
                    refundDeposit = deposit / 2;
                    notify = "Đơn đặt huỷ trước 12h, bạn sẽ nhận lại 50% số tiền đặt cọc.";
                } else {
                    notify = "Đơn đặt huỷ sau 12h, bạn sẽ không nhận lại số tiền đặt cọc.";
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText("Thông báo");
                alert.setContentText("Đơn hàng với ID: " + id + " có yêu cầu huỷ đặt trước" + "\n"
                        + notify + "\n"
                        + "Số tiền đặt cọc là " + Converter.formatMoney(deposit) + " VNĐ" + "\n"
                        + "Bạn sẽ được hoàn tiền là: " + Converter.formatMoney(refundDeposit) + " VNĐ" + "\n\n"
                        + "Bạn có chắc chắn muốn huỷ đơn hàng này không?"
                );
                ButtonType btn_ok = new ButtonType("Đồng ý");
                ButtonType btn_cancel = new ButtonType("Không");
                alert.getButtonTypes().setAll(btn_ok, btn_cancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == btn_ok) {
                    reservationDAO.updateRefundDeposit(id, refundDeposit);
                    reservationDAO.updateStatus(id, ReservationStatus.CANCELLED);
                    setPreOrderTableViewColumn();
                    ToastsMessage.showMessage("Đã huỷ đơn đặt ID: " + id + ", thành công", "success");
                    ToastsMessage.showMessage("Đã hoàn số tiền: " + Converter.formatMoney(refundDeposit) + " VNĐ", "success");
                }
                for (Table table : reservation.getTables()) {
                    tableBUS.updateStatusTable(table.getId(), TableStatus.AVAILABLE);
                }
            } else {
                ToastsMessage.showMessage("Đơn hàng đang trạng thái " + status + ", nên không thể huỷ", "warning");
            }
        } else {
            ToastsMessage.showMessage("Vui lòng chọn một đơn đặt để huỷ", "warning");
        }
    }
    @FXML
    void onSearchReservationKeyTyped(KeyEvent keyEvent) {
        setPreOrderTableViewColumn();
    }
}