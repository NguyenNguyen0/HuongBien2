package huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.bus.ReservationBUS;
import huongbien.bus.TableBUS;
import huongbien.config.Constants;
import huongbien.config.Variable;
import huongbien.dao.TableDAO;
import huongbien.dao.TableTypeDAO;
import huongbien.entity.Reservation;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.entity.TableType;
import huongbien.jpa.PersistenceUnit;
import huongbien.util.Converter;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderTableController implements Initializable {
    @FXML
    private Label tableEmptyCountLabel;
    @FXML
    private Label tablePreOrderCountLabel;
    @FXML
    private Label tableOpenCountLabel;
    @FXML
    public Label statisticalOverviewLabel;
    @FXML
    public Label currentFloorLabel;
    @FXML
    public Label statisticalFloorLabel;
    @FXML
    private ScrollPane orderTableScrollPane;
    @FXML
    private GridPane orderTableGridPane;
    @FXML
    public ComboBox<String> tableFloorComboBox;
    @FXML
    public ComboBox<String> tableStatusComboBox;
    @FXML
    public ComboBox<String> tableTypeComboBox;
    @FXML
    private ComboBox<String> tableSeatsComboBox;
    @FXML
    public TabPane tableInfoTabPane;
    @FXML
    public Label tableQuantityLabel;
    @FXML
    public Label seatTotalLabel;
    @FXML
    public Label tableAmountLabel;
    @FXML
    public Label tableFeeLabel;

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
        setDefaultStatusTable();
        try {
            setTablesStatusWaitedToday();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            setTablesStatusServingToday();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        tableFeeLabel.setText(tableFeeLabel.getText() + Converter.formatMoney(Variable.tableVipPrice) + " VNĐ");
        loadTablesToGridPane(Variable.floor, Variable.status, Variable.tableTypeName, Variable.seats); //value mặc định
        setComboBoxValue();
        statisticalRestaurant(Variable.floor);
        try {
            readTableDataFromJSON();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void statisticalRestaurant(int floor) {
        int statisticalOverviewTableEmpty = new TableDAO(PersistenceUnit.MARIADB_JPA).getCountStatisticalOverviewTableEmpty();
        int statisticalOverview = new TableDAO(PersistenceUnit.MARIADB_JPA).getCountStatisticalOverviewTable();
        int statisticalFloorTableEmpty = new TableDAO(PersistenceUnit.MARIADB_JPA).getCountStatisticalFloorTableEmpty(floor);
        int statisticalFloorTablePreOrder = new TableDAO(PersistenceUnit.MARIADB_JPA).getCountStatisticalFloorTablePreOrder(floor);
        int statisticalFloorTableOpen = new TableDAO(PersistenceUnit.MARIADB_JPA).getCountStatisticalFloorTableOpen(floor);
        int statisticalFloor = new TableDAO(PersistenceUnit.MARIADB_JPA).getCountStatisticalFloorTable(floor);
        tableEmptyCountLabel.setText("( " + statisticalFloorTableEmpty + " )");
        tablePreOrderCountLabel.setText("( " + statisticalFloorTablePreOrder + ")");
        tableOpenCountLabel.setText("( " + statisticalFloorTableOpen + " )");
        currentFloorLabel.setText(floor == 0 ? "Tầng trệt:" : "Tầng " + floor + ":");
        statisticalOverviewLabel.setText("(" + statisticalOverviewTableEmpty + " / " + statisticalOverview + ")");
        statisticalFloorLabel.setText("(" + statisticalFloorTableEmpty + " / " + statisticalFloor + ")");
    }

    //---
    private void loadTablesToGridPane(int floor, String status, String type, String seat) {
        List<Table> tables = new ArrayList<>(getTableDataByCriteria(floor, status, type, seat));
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < tables.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/huongbien/fxml/OrderTableItem.fxml"));
                VBox tableBox = fxmlLoader.load();
                OrderTableItemController orderTableItemController = fxmlLoader.getController();
                orderTableItemController.setTableItemData(tables.get(i));
                orderTableItemController.setOrderTableController(this);
                if (columns == 4) {
                    columns = 0;
                    ++rows;
                }
                orderTableGridPane.add(tableBox, columns++, rows);
                GridPane.setMargin(tableBox, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        orderTableScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        orderTableGridPane.prefWidthProperty().bind(orderTableScrollPane.widthProperty());
    }

    private List<Table> getTableDataByCriteria(int floor, String status, String type, String seat) {
        TableDAO tableDAO = new TableDAO(PersistenceUnit.MARIADB_JPA);
        if (status.equals("Trạng thái")) {
            status = null;
        }
        if (type.equals("Loại bàn")) {
            type = null;
        }
        if (seat.equals("Số chỗ")) {
            seat = null;
        }
        if (seat != null) {
            seat = seat.replace(" chỗ", "");
        }
        return tableDAO.getByCriteria(String.valueOf(floor), status, type, seat);
    }

    private void setTablesStatusWaitedToday() throws SQLException {
        ReservationBUS reservationBUS = new ReservationBUS();
        TableBUS tableBUS = new TableBUS();
        List<Reservation> reservationList = reservationBUS.getListWaitedToday();
        if(reservationList !=null ){
            List<Table> tableList = reservationBUS.getListTableStatusToday(reservationList);
            if(tableList != null) {
                for (Table table : tableList) {
                    tableBUS.updateStatusTable(table.getId(), TableStatus.OCCUPIED);
                }
            }
        }
    }

    private void setDefaultStatusTable(){
        TableBUS tableBUS = new TableBUS();
        List<Table> tableList = tableBUS.getAllTable();
        for (Table table : tableList) {
            tableBUS.updateStatusTable(table.getId(), TableStatus.AVAILABLE);
        }
    }

    private void setTablesStatusServingToday() throws FileNotFoundException {
        TableBUS tableBUS = new TableBUS();
        JsonArray jsonArrayPaymentQueue = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);
        for (JsonElement element : jsonArrayPaymentQueue) {
            JsonObject table = element.getAsJsonObject();
            JsonArray tableIdArray = table.getAsJsonArray("Table ID");
            for (JsonElement tableIdElement : tableIdArray) {
                String tableId = tableIdElement.getAsString();
                tableBUS.updateStatusTable(tableId, TableStatus.RESERVED);
            }

        }
    }

    private void setComboBoxValue() {
        TableDAO tableDAO = new TableDAO(PersistenceUnit.MARIADB_JPA);
        TableTypeDAO tableTypeDAO = new TableTypeDAO();
        //floor
        List<Integer> floors = tableDAO.getDistinctFloor();
        ObservableList<String> floorOptions = FXCollections.observableArrayList();
        for (Integer floor : floors) {
            floorOptions.add(floor.toString());
        }
        tableFloorComboBox.setItems(floorOptions);
        tableFloorComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String floor) {
                return floor.equals("0") ? "Tầng trệt" : "Tầng " + floor;
            }

            @Override
            public String fromString(String string) {
                return string.replace("Tầng ", "").trim() + string.replace("Tầng trệt", "0").trim();
            }
        });
        tableFloorComboBox.getSelectionModel().selectFirst();
        //Status
        List<String> statuses = tableDAO.getDistinctStatuses();
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Trạng thái");
        statusOptions.addAll(statuses);
        tableStatusComboBox.setItems(statusOptions);
        tableStatusComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        tableStatusComboBox.getSelectionModel().selectFirst();
        //Type
        List<String> tableTypes = tableTypeDAO.getDistinctTableType();
        ObservableList<String> typeOptions = FXCollections.observableArrayList("Loại bàn");
        typeOptions.addAll(tableTypes);
        tableTypeComboBox.setItems(typeOptions);
        tableTypeComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String tableType) {
                return tableType != null ? tableType : "";
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        tableTypeComboBox.getSelectionModel().selectFirst();
        //Seats
        List<String> seats = tableDAO.getDistinctSeat();
        ObservableList<String> seatOptions = FXCollections.observableArrayList("Số chỗ");
        seatOptions.addAll(seats);
        tableSeatsComboBox.setItems(seatOptions);
        tableSeatsComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String seat) {
                return seat.equals("Số chỗ") ? seat : seat + " chỗ";
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        tableSeatsComboBox.getSelectionModel().selectFirst();
    }

    public void setTableTabPane(String name, int floor, int seats, String typeName) {
        String floorString;
        if (floor == 0) {
            floorString = "Tầng trệt";
        } else {
            floorString = "Tầng " + floor;
        }

        Tab newTab = new Tab();
        newTab.setText(floorString);

        HBox tabContentHBox = new HBox(10);
        tabContentHBox.setPadding(new Insets(10, 0, 0, 10));
        tabContentHBox.setAlignment(Pos.CENTER_LEFT);
        tabContentHBox.setStyle("-fx-background-color: white");

        HBox tableNameHBox = new HBox(10);
        Label tableNameValue = new Label(name);
        tableNameValue.setFont(new Font("System Bold", 15));
        tableNameHBox.getChildren().addAll(tableNameValue);

        HBox tableseparator1 = new HBox(10);
        Label separator1 = new Label("-");
        separator1.setFont(new Font("System Bold", 15));
        tableseparator1.getChildren().addAll(separator1);

        HBox seatCountHBox = new HBox(10);
        Label seatCountLabel = new Label("Số chỗ:");
        seatCountLabel.setFont(new Font("System Bold", 15));
        Label seatCountValue = new Label(String.valueOf(seats));
        seatCountValue.setFont(new Font("System Bold", 15));
        seatCountHBox.getChildren().addAll(seatCountLabel, seatCountValue);

        HBox tableseparator2 = new HBox(10);
        Label separator2 = new Label("-");
        separator2.setFont(new Font("System Bold", 15));
        tableseparator2.getChildren().addAll(separator2);

        HBox tableTypeHBox = new HBox(10);
        Label tableTypeLabel = new Label("Loại bàn:");
        tableTypeLabel.setFont(new Font("System Bold", 15));
        Label tableTypeValue = new Label(typeName);
        tableTypeValue.setFont(new Font("System Bold", 15));
        tableTypeHBox.getChildren().addAll(tableTypeLabel, tableTypeValue);

        tabContentHBox.getChildren().addAll(tableNameHBox, tableseparator1, seatCountHBox, tableseparator2, tableTypeHBox);
        newTab.setContent(tabContentHBox);
        tableInfoTabPane.getTabs().add(newTab);
        tableInfoTabPane.getSelectionModel().select(newTab);
    }

    public void readTableDataFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.TABLE_PATH);
        int seatTotal = 0;
        double tableAmount = 0;
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO tableDAO = new TableDAO(PersistenceUnit.MARIADB_JPA);
            Table table = tableDAO.getById(id);
            setTableTabPane(table.getName(), table.getFloor(), table.getSeats(), table.getTableType().getName());
            //calculate seat total
            seatTotal += table.getSeats();
            //calculate table amount
            tableAmount += table.getTableType().getId().equals(Variable.tableVipID) ? Variable.tableVipPrice : 0;
        }
        tableQuantityLabel.setText(String.valueOf(jsonArray.size()));
        seatTotalLabel.setText(seatTotal + " chỗ");
        tableAmountLabel.setText(String.format("%,.0f VNĐ", tableAmount));
    }

    public void handleLoadTableFromComboBoxSelection() throws SQLException {
        int floor = Integer.parseInt(tableFloorComboBox.getValue());
        String status = tableStatusComboBox.getValue();
        String tableTypeName = tableTypeComboBox.getValue();
        String seat = tableSeatsComboBox.getValue(); //Để kiểu string hiên thị trên comboBox
        TableTypeDAO tableTypeDAO = new TableTypeDAO();
        TableType tableType = tableTypeDAO.getByName(tableTypeName);
        String tableTypeId = (tableType != null) ? tableType.getId() : "";
        orderTableGridPane.getChildren().clear();
        loadTablesToGridPane(floor, status, tableTypeId, seat);
    }

    //comboBox
    @FXML
    void onTableFloorComboBoxSelected(ActionEvent event) throws SQLException {
        statisticalRestaurant(Integer.parseInt(tableFloorComboBox.getValue()));
        handleLoadTableFromComboBoxSelection();
    }

    @FXML
    void onTableStatusComboBoxSelected(ActionEvent event) throws SQLException {
        handleLoadTableFromComboBoxSelection();
    }

    @FXML
    void onTableTypeComboBoxSelected(ActionEvent event) throws SQLException {
        handleLoadTableFromComboBoxSelection();
    }

    @FXML
    void onTableSeatsComboBoxSelected(ActionEvent event) throws SQLException {
        handleLoadTableFromComboBoxSelection();
    }

    @FXML
    void onOrderCuisineButtonAction(ActionEvent event) throws IOException {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TABLE_PATH);
        } catch (FileNotFoundException e) {
            return;
        }
        if (jsonArray.isEmpty()) {
            ToastsMessage.showMessage("Vui lòng chọn bàn", "warning");
            return;
        }
        if(restaurantMainManagerController != null) {
            restaurantMainManagerController.openOrderCuisine();
        }else {
            restaurantMainStaffController.openOrderCuisine();
        }
    }

    @FXML
    void onPreOrderButtonAction(ActionEvent event) throws IOException {
        if(restaurantMainManagerController != null) {
            restaurantMainManagerController.openPreOrder();
        }else {
            restaurantMainStaffController.openPreOrder();
        }
    }

    @FXML
    void onClearTableButtonAction(ActionEvent event) throws IOException {
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.TABLE_PATH);
        if (jsonArray.isEmpty()) {
            ToastsMessage.showMessage("Vui lòng chọn bàn", "warning");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Thông báo");
        alert.setContentText("Bạn có chắc chắn muốn xóa tất cả bàn đã chọn?");
        ButtonType btn_ok = new ButtonType("Xoá tất cả");
        ButtonType btn_cancel = new ButtonType("Không xoá");
        alert.getButtonTypes().setAll(btn_ok, btn_cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn_ok) {
            Utils.writeJsonToFile(new JsonArray(), Constants.TABLE_PATH);
            if(restaurantMainManagerController != null) {
                restaurantMainManagerController.openOrderTable();
            }else {
                restaurantMainStaffController.openOrderTable();
            }
        }
    }
}
