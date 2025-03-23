package huongbien.ui.controller;

import huongbien.bus.*;
import huongbien.entity.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class RestaurantLookupController implements Initializable {
    //Vbox1
    @FXML
    private ComboBox<String> tableFloorComboBox;
    @FXML
    private TextField tableNameTextField;
    @FXML
    private ComboBox<String> tableSeatsComboBox;
    @FXML
    private ComboBox<String> tableTypesComboBox;
    @FXML
    private ComboBox<String> tableStatusComboBox;
    @FXML
    private TableView<Table> tablesTableView;
    @FXML
    private TableColumn<Table, String> tableFloorColumn;
    @FXML
    private TableColumn<Table, String> tableNameColumn;
    @FXML
    private TableColumn<Table, Integer> tableSeatColumn;
    @FXML
    private TableColumn<Table, String> tableTypeColumn;
    @FXML
    private TableColumn<Table, String> tableStatusColumn;
    @FXML
    private Pagination tablePagination;
    //Vbox2
    @FXML
    private ComboBox<String> cuisineCategoryComboBox;
    @FXML
    private TextField cuisineNameTextField;
    @FXML
    private TableView<Cuisine> cuisinesTableView;
    @FXML
    private TableColumn<Cuisine, String> cuisineCategoryColumn;
    @FXML
    private TableColumn<Cuisine, String> cuisineNameColumn;
    @FXML
    private TableColumn<Cuisine, Double> cuisinePriceColumn;
    @FXML
    private TableColumn<Cuisine, Integer> cuisineCountSaleColumn;
    @FXML
    private Pagination cuisinePagination;
    //Vbox3
    @FXML
    private TextField promotionNameTextField;
    @FXML
    private DatePicker promotionStartDate;
    @FXML
    private DatePicker promotionEndDate;
    @FXML
    private ComboBox<String> promotionDiscountComboBox;
    @FXML
    private ComboBox<String> promotionMinimumOrderAmountComboBox;
    @FXML
    private ComboBox<String> promotionStatusComboBox;
    @FXML
    private TableView<Promotion> promotionsTableView;
    @FXML
    private TableColumn<Promotion, String> promotionNameColumn;
    @FXML
    private TableColumn<Promotion, LocalDate> promotionStartDateColumn;
    @FXML
    private TableColumn<Promotion, LocalDate> promotionEndDateColumn;
    @FXML
    private TableColumn<Promotion, Double> promotionDiscountColumn;
    @FXML
    private TableColumn<Promotion, Double> promotionMinimumOrderAmountColumn;
    @FXML
    private TableColumn<Promotion, String> promotionStatusColumn;
    @FXML
    private Pagination promotionPagination;
    //Vbox4
    @FXML
    private TextField reservationCustomerPhoneTextField;
    @FXML
    private TextField reservationIdTextField;
    @FXML
    private DatePicker reservationDate;
    @FXML
    private DatePicker reservationReceiveDate;
    @FXML
    private TableView<Reservation> reservationsTableView;
    @FXML
    private TableColumn<Reservation, String> reservationIdColumn;
    @FXML
    private TableColumn<Reservation, String> reservationCustomerPhoneColumn;
    @FXML
    private TableColumn<Reservation, LocalDate> reservationDateColumn;
    @FXML
    private TableColumn<Reservation, LocalTime> reservationTimeColumn;
    @FXML
    private TableColumn<Reservation, LocalDate> reservationReceiveDateColumn;
    @FXML
    private TableColumn<Reservation, LocalTime> reservationReceiveTimeColumn;
    @FXML
    private TableColumn<Reservation, Double> reservationDepositColumn;
    @FXML
    private Pagination reservationPagination;
    //All
    @FXML
    private ComboBox<String> restaurantLookupComboBox;
    @FXML
    private VBox restaurantLookupCuisineVBox;
    @FXML
    private VBox restaurantLookupPreOrderTableVBox;
    @FXML
    private VBox restaurantLookupPromotionVBox;
    @FXML
    private VBox restaurantLookupTableVBox;

    //BUS area
    private final CuisineBUS cuisineBUS = new CuisineBUS();
    private final CategoryBUS categoryBUS = new CategoryBUS();
    private final OrderDetailBUS orderDetailBUS = new OrderDetailBUS();
    private final PromotionBUS promotionBUS = new PromotionBUS();
    private final TableBUS tableBUS = new TableBUS();
    private final TableTypeBUS tableTypeBUS = new TableTypeBUS();
    private final ReservationBUS reservationBUS = new ReservationBUS();
    private final CustomerBUS customerBUS = new CustomerBUS();

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
        setDefaultRestaurantLookupComboBox();
        setDefaultTableView(); //set default table view display table first
        setEventForPagination();
        setValueTableComboBox();
        setValueCuisineComboBox();
        setValuePromotionComboBox();
    }

    //function area
    private <T> void applyDateFormat(TableColumn<T, LocalDate> column) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
    }

    private <T> void applyCurrencyFormat(TableColumn<T, Double> column) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(decimalFormat.format(item));
                }
            }
        });
    }

    private void setValueCuisineComboBox() {
        // Lấy danh sách tên loại món
        List<String> cuisinCategoryIdList = cuisineBUS.getCuisineCategory();
        ObservableList<String> cuisineCategoryList = FXCollections.observableArrayList();
        cuisineCategoryList.add("Tất cả");
        for (String s : cuisinCategoryIdList) {
            cuisineCategoryList.add(categoryBUS.getCategoryById(s).getName());
        }
        cuisineCategoryComboBox.setItems(cuisineCategoryList);
        selectFirstWithoutAction(cuisineCategoryComboBox);
    }

    private void setValueTableComboBox() {
        // Lấy danh sách tầng
        ObservableList<String> tableFloorList = FXCollections.observableArrayList();
        tableFloorList.add("Tất cả");
        for (int i = 0; i < tableBUS.getDistinctFloors().size(); i++) {
            if (tableBUS.getDistinctFloors().get(i).equals(0)) {
                tableFloorList.add("Tầng trệt");
            } else {
                tableFloorList.add("Tầng " + tableBUS.getDistinctFloors().get(i));
            }
        }
        tableFloorComboBox.setItems(tableFloorList);

        // Lấy danh sách chỗ ngồi
        ObservableList<String> tableSeatList = FXCollections.observableArrayList();
        tableSeatList.add("Tất cả");
        for (int i = 0; i < tableBUS.getDistinctSeats().size(); i++) {
            tableSeatList.add(tableBUS.getDistinctSeats().get(i) + "");
        }
        tableSeatsComboBox.setItems(tableSeatList);

        // Lấy danh sách loại bàn
        ObservableList<String> tableTypeList = FXCollections.observableArrayList();
        tableTypeList.add("Tất cả");
        List<String> getTypeListFromTable = tableBUS.getDistinctTableTypes();
        for (String s : getTypeListFromTable) {
            tableTypeList.add(tableTypeBUS.getTableTypeName(s).getName());
        }
        tableTypesComboBox.setItems(tableTypeList);

        // Lấy danh sách trạng thái bàn
        ObservableList<String> tableStatusesList = FXCollections.observableArrayList();
        tableStatusesList.add("Tất cả");
        tableStatusesList.addAll(tableBUS.getDistinctStatuses());
        tableStatusComboBox.setItems(tableStatusesList);

        // Cài đặt mặc định cho các combobox
        selectFirstWithoutAction(tableFloorComboBox);
        selectFirstWithoutAction(tableStatusComboBox);
        selectFirstWithoutAction(tableSeatsComboBox);
        selectFirstWithoutAction(tableTypesComboBox);
    }

    private void setValuePromotionComboBox() {
        // Lấy danh sách giảm giá %
        ObservableList<String> promotionDiscountList = FXCollections.observableArrayList();
        promotionDiscountList.add("Tất cả");
        List<String> discountList = promotionBUS.getDistinctPromotionDiscount();
        promotionDiscountList.addAll(discountList);
        promotionDiscountComboBox.setItems(promotionDiscountList);

        // Lấy danh sách trạng thái
        ObservableList<String> promotionStatusList = FXCollections.observableArrayList();
        promotionStatusList.add("Tất cả");
        List<String> statusList = promotionBUS.getDistinctPromotionStatus();
        promotionStatusList.addAll(statusList);
        promotionStatusComboBox.setItems(promotionStatusList);

        // Lấy danh sách minimumOrderAmount
        ObservableList<String> promotionMinimumOrderAmountList = FXCollections.observableArrayList();
        promotionMinimumOrderAmountList.add("Tất cả");
        List<String> minimumOrderAmountList = promotionBUS.getDistinctPromotionMinimumOrderAmount();
        promotionMinimumOrderAmountList.addAll(minimumOrderAmountList);
        promotionMinimumOrderAmountComboBox.setItems(promotionMinimumOrderAmountList);

        //Đặt giá trị mặc đinh
        selectFirstWithoutAction(promotionMinimumOrderAmountComboBox);
        selectFirstWithoutAction(promotionStatusComboBox);
        selectFirstWithoutAction(promotionDiscountComboBox);
    }

    private void setPaginationPageCount(Pagination pagination, int pageCount) {
        pagination.setPageCount(pageCount / 7 + 1);
    }

    private void setEventForPagination() {
        //TablePagination
        tablePagination.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            setTableViewColumn();
            event.consume();  // Ngừng sự kiện để không tiếp tục được xử lý
        });
        //CuisinePagination
        cuisinePagination.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            setTableViewColumn();
            event.consume();  // Ngừng sự kiện để không tiếp tục được xử lý
        });
        //PromotionPagination
        promotionPagination.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            setTableViewColumn();
            event.consume();  // Ngừng sự kiện để không tiếp tục được xử lý
        });
        //ReservationPagination
        reservationPagination.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            setTableViewColumn();
            event.consume();  // Ngừng sự kiện để không tiếp tục được xử lý
        });
    }

    private void setTableViewColumn() {
        String selectedItem = restaurantLookupComboBox.getValue();
        int pageIndex = 0;
        int pageCount = 0;
        switch (selectedItem) {
            // LOAD DANH SÁCH BÀN
            case "Bàn":
                tablesTableView.getItems().clear();
                //Lấy dữ liệu để tra cứu
                String tableName = tableNameTextField.getText();
                String tableTypeId = "";
                String tableTypeName = tableTypesComboBox.getValue();
                if (!tableTypesComboBox.getSelectionModel().isSelected(0)) {
                    tableTypeId = tableTypeBUS.getTableTypeId(tableTypeName);
                }
                int tableFloor = switch (tableFloorComboBox.getValue()) {
                    case "Tầng trệt" -> 0;
                    case "Tầng 1" -> 1;
                    case "Tầng 2" -> 2;
                    default -> -1;
                };
                int tableSeat = -1;
                if (!tableSeatsComboBox.getSelectionModel().isSelected(0)) {
                    tableSeat = Integer.parseInt(tableSeatsComboBox.getValue());
                }
                String tableStatus = "";
                if (!tableStatusComboBox.getSelectionModel().isSelected(0)) {
                    tableStatus = tableStatusComboBox.getValue();
                }
                //Cài đặt Pagination
                pageCount = tableBUS.getCountLookUpTable(tableFloor, tableName, tableSeat, tableTypeId, tableStatus);
                setPaginationPageCount(tablePagination, pageCount);
                pageIndex = tablePagination.getCurrentPageIndex() * 7;
                //Đổ dữ liệu vào bảng
                List<Table> tableList = tableBUS.getLookUpTable(tableFloor, tableName, tableSeat, tableTypeId, tableStatus, pageIndex);
                ObservableList<Table> tableObservableList = FXCollections.observableArrayList(tableList);
                if (tableList.isEmpty()) {
                    Label placeholder = new Label("Không có bàn");
                    placeholder.setStyle("-fx-text-fill: #ccc; -fx-font-size: 24px; -fx-font-weight: bold;");
                    tablesTableView.setPlaceholder(placeholder);
                } else {
                    tableFloorColumn.setCellValueFactory(cellData -> {
                        String floor = switch (cellData.getValue().getFloor() + "") {
                            case "1" -> "Tầng 1";
                            case "2" -> "Tầng 2";
                            default -> "Tầng trệt";
                        };
                        return new SimpleStringProperty(floor);
                    });
                    tableNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    tableSeatColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
                    tableTypeColumn.setCellValueFactory(cellData -> {
                        String type = tableTypeBUS.getTableTypeName(cellData.getValue().getTableType().getId()).getName();
                        return new SimpleStringProperty(type);
                    });
                    tableStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
                }
                tablesTableView.setItems(tableObservableList);
                break;
            // LOAD DANH SÁCH MÓN ĂN
            case "Món ăn":
                cuisinesTableView.getItems().clear();
                //Lấy dữ liệu để tra cứu
                String cuisineName = cuisineNameTextField.getText();
                String categoryName = categoryBUS.getCategoryId(cuisineCategoryComboBox.getValue());
                //Cài đặt Pagination
                pageCount = cuisineBUS.getCountLookUpCuisine(cuisineName, categoryName);
                setPaginationPageCount(cuisinePagination, pageCount);
                pageIndex = cuisinePagination.getCurrentPageIndex() * 7;
                //Đổ dữ liệu vào bảng
                List<Cuisine> cuisineList = cuisineBUS.getLookUpCuisine(cuisineName, categoryName, pageIndex);
                ObservableList<Cuisine> listCuisine = FXCollections.observableArrayList(cuisineList);
                if (cuisineList.isEmpty()) {
                    Label placeholder = new Label("Không có món ăn");
                    placeholder.setStyle("-fx-text-fill: #ccc; -fx-font-size: 24px; -fx-font-weight: bold;");
                    cuisinesTableView.setPlaceholder(placeholder);
                } else {
                    cuisineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    cuisineCategoryColumn.setCellValueFactory(cellData -> {
                        String category = categoryBUS.getCategoryById(cellData.getValue().getCategory().getId()).getName();
                        return new SimpleStringProperty(category);
                    });
                    cuisinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                    applyCurrencyFormat(cuisinePriceColumn);
                    cuisineCountSaleColumn.setCellValueFactory(cellData -> {
                        int countOfUnitsSold = orderDetailBUS.getCountOfUnitsSold(cellData.getValue().getId());
                        return new ReadOnlyObjectWrapper<>(countOfUnitsSold);
                    });
                    cuisinesTableView.setItems(listCuisine);
                }
                break;
            // LOAD DANH SÁCH KHUYẾN MÃI
            case "Khuyến mãi":
                promotionsTableView.getItems().clear();
                //Lấy dữ liệu để tra cứu
                String promotionName = promotionNameTextField.getText();
                LocalDate promotionStart = promotionStartDate.getValue();
                LocalDate promotionEnd = promotionEndDate.getValue();
                double promotionDiscount = 0;

                if (!promotionDiscountComboBox.getSelectionModel().isSelected(0)) {
                    String discount = promotionDiscountComboBox.getValue().replace("%", "");
                    promotionDiscount = Double.parseDouble(discount) / 100;
                }
                double promotionMinimumOrderAmount = 0;
                if (!promotionMinimumOrderAmountComboBox.getSelectionModel().isSelected(0)) {
                    String minimumOrderAmount = promotionMinimumOrderAmountComboBox.getValue().replace(",", "");
                    promotionMinimumOrderAmount = Double.parseDouble(minimumOrderAmount);
                }
                String promotionStatus = "";
                if (!promotionStatusComboBox.getSelectionModel().isSelected(0)) {
                    promotionStatus = promotionStatusComboBox.getValue();
                }
                //Cài đặt Pagination
                pageCount = promotionBUS.getCountLookUpPromotion(promotionName, promotionStart, promotionEnd, promotionDiscount, promotionMinimumOrderAmount, promotionStatus);
                setPaginationPageCount(promotionPagination, pageCount);
                pageIndex = promotionPagination.getCurrentPageIndex() * 7;
                //Đổ dữ liệu vào bảng
                List<Promotion> promotionList = promotionBUS.getLookUpPromotion(promotionName, promotionStart, promotionEnd, promotionDiscount, promotionMinimumOrderAmount, promotionStatus, pageIndex);
                ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
                if (promotionList.isEmpty()) {
                    Label placeholder = new Label("Không có món ăn");
                    placeholder.setStyle("-fx-text-fill: #ccc; -fx-font-size: 24px; -fx-font-weight: bold;");
                    promotionsTableView.setPlaceholder(placeholder);
                } else {
                    promotionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    promotionStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
                    applyDateFormat(promotionStartDateColumn);
                    promotionEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
                    applyDateFormat(promotionEndDateColumn);
                    promotionDiscountColumn.setCellFactory(cellData -> new TableCell<>() {
                        @Override
                        public void updateItem(Double item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                // Chuyển đổi và định dạng giá trị thành phần trăm
                                setText(String.format("%.0f%%", item * 100));
                            }
                        }
                    });
                    promotionDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
                    promotionMinimumOrderAmountColumn.setCellValueFactory(new PropertyValueFactory<>("minimumOrderAmount"));
                    applyCurrencyFormat(promotionMinimumOrderAmountColumn);
                    promotionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
                    promotionsTableView.setItems(listPromotion);
                }
                break;
            // LOAD DANH SÁCH ĐƠN ĐẶT TRƯỚC
            case "Đơn đặt trước":
                reservationsTableView.getItems().clear();
                //Lấy dữ diệu tra cứu
                String reservationId = reservationIdTextField.getText();
                String reservationCustomerPhone = reservationCustomerPhoneTextField.getText();
                LocalDate getReservationDate = reservationDate.getValue();
                LocalDate getReservationReceiveDate = reservationReceiveDate.getValue();
                //Cài đặt Pagination
                pageCount = reservationBUS.getCountLookUpReservation(reservationId, reservationCustomerPhone, getReservationDate, getReservationReceiveDate);
                setPaginationPageCount(reservationPagination, pageCount);
                pageIndex = (reservationPagination.getCurrentPageIndex()) * 7;
                List<Reservation> reservationList = reservationBUS.getLookUpReservation(reservationId, reservationCustomerPhone, getReservationDate, getReservationReceiveDate, pageIndex);
                //Đổ dữ liệu vào bảng
                ObservableList<Reservation> listReservation = FXCollections.observableArrayList(reservationList);
                if (reservationList.isEmpty()) {
                    Label placeholder = new Label("Không có đơn đặt");
                    placeholder.setStyle("-fx-text-fill: #ccc; -fx-font-size: 24px; -fx-font-weight: bold;");
                    reservationsTableView.setPlaceholder(placeholder);
                } else {
                    reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
                    reservationCustomerPhoneColumn.setCellValueFactory(cellData -> {
                        String phone = customerBUS.getCustomer(cellData.getValue().getCustomer().getId()).getId();
                        return new SimpleStringProperty(phone);
                    });
                    reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
                    applyDateFormat(reservationDateColumn);

                    reservationReceiveDateColumn.setCellValueFactory(new PropertyValueFactory<>("receiveDate"));
                    applyDateFormat(reservationReceiveDateColumn);

                    reservationDepositColumn.setCellValueFactory(new PropertyValueFactory<>("deposit"));
                    applyCurrencyFormat(reservationDepositColumn);

                    reservationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("reservationTime"));
                    reservationReceiveTimeColumn.setCellValueFactory(new PropertyValueFactory<>("receiveTime"));
                    reservationsTableView.setItems(listReservation);
                }
                break;
        }
    }

    private void selectFirstWithoutAction(ComboBox<String> comboBox) {
        EventHandler<ActionEvent> currentHandler = comboBox.getOnAction();
        comboBox.setOnAction(null);
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(currentHandler);
    }

    private void setDefaultTableView() {
        restaurantLookupTableVBox.setVisible(true);
        restaurantLookupCuisineVBox.setVisible(false);
        restaurantLookupPromotionVBox.setVisible(false);
        restaurantLookupPreOrderTableVBox.setVisible(false);
        setValueTableComboBox();
        setTableViewColumn();
    }

    private void setDefaultRestaurantLookupComboBox() {
        ObservableList<String> restaurantLookupList = FXCollections.observableArrayList();
        restaurantLookupList.add("Bàn");
        restaurantLookupList.add("Món ăn");
        restaurantLookupList.add("Khuyến mãi");
        restaurantLookupList.add("Đơn đặt trước");
        restaurantLookupComboBox.setItems(restaurantLookupList);
        restaurantLookupComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        restaurantLookupComboBox.getSelectionModel().selectFirst();
    }
    //---TODO: add more function

    //event area--------------------------------------------------------------------------
    //---
    @FXML
    void onRestaurantLookupComboBoxAction(ActionEvent event) {
        String selectedItem = restaurantLookupComboBox.getValue();
        restaurantLookupCuisineVBox.setVisible(false);
        restaurantLookupPreOrderTableVBox.setVisible(false);
        restaurantLookupPromotionVBox.setVisible(false);
        restaurantLookupTableVBox.setVisible(false);
        switch (selectedItem) {
            case "Bàn":
                if(restaurantMainManagerController != null) {
                    restaurantMainManagerController.featureTitleLabel.setText("Tra cứu bàn");
                } else {
                    restaurantMainStaffController.featureTitleLabel.setText("Tra cứu bàn");
                }
                restaurantLookupTableVBox.setVisible(true);
                tableNameTextField.clear();
                selectFirstWithoutAction(tableTypesComboBox);
                selectFirstWithoutAction(tableStatusComboBox);
                selectFirstWithoutAction(tableFloorComboBox);
                selectFirstWithoutAction(tableSeatsComboBox);
                setTableViewColumn();
                break;
            case "Món ăn":
                if(restaurantMainManagerController != null) {
                    restaurantMainManagerController.featureTitleLabel.setText("Tra cứu món ăn");
                } else {
                    restaurantMainStaffController.featureTitleLabel.setText("Tra cứu món ăn");
                }
                restaurantLookupCuisineVBox.setVisible(true);
                cuisineNameTextField.clear();
                selectFirstWithoutAction(cuisineCategoryComboBox);
                setTableViewColumn();
                break;
            case "Khuyến mãi":
                if(restaurantMainManagerController != null) {
                    restaurantMainManagerController.featureTitleLabel.setText("Tra cứu khuyến mãi");
                } else {
                    restaurantMainStaffController.featureTitleLabel.setText("Tra cứu khuyến mãi");
                }
                restaurantLookupPromotionVBox.setVisible(true);
                promotionNameTextField.clear();
                promotionEndDate.setValue(null);
                promotionStartDate.setValue(null);
                selectFirstWithoutAction(promotionMinimumOrderAmountComboBox);
                selectFirstWithoutAction(promotionStatusComboBox);
                selectFirstWithoutAction(promotionDiscountComboBox);
                setTableViewColumn();
                break;
            case "Đơn đặt trước":
                if(restaurantMainManagerController != null) {
                    restaurantMainManagerController.featureTitleLabel.setText("Tra cứu đơn đặt trước");
                } else {
                    restaurantMainStaffController.featureTitleLabel.setText("Tra cứu đơn đặt trước");
                }
                restaurantLookupPreOrderTableVBox.setVisible(true);
                reservationIdTextField.clear();
                reservationCustomerPhoneTextField.clear();
                setTableViewColumn();
                break;
        }
    }

    @FXML
    void onTableNameTextFieldKeyReleased(KeyEvent keyEvent) {
        setTableViewColumn();
    }

    @FXML
    void onClearTableNameMouseClicked(MouseEvent mouseEvent) {
        tableNameTextField.clear();
        tableNameTextField.requestFocus();
        setTableViewColumn();
    }

    @FXML
    void onTableFloorComboBoxAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onTableSeatsComboBoxAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onTableTypesComboBoxAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onTableStatusComboBoxAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onCuisineCategoryComboBoxAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onCuisineNameTextFieldKeyReleased(KeyEvent keyEvent) {
        setTableViewColumn();
    }

    @FXML
    void onClearCuisineNameMouseClicked(MouseEvent mouseEvent) {
        cuisineNameTextField.setText("");
        cuisineNameTextField.requestFocus();
        setTableViewColumn();
    }

    @FXML
    void onPromotionNameTextFieldKeyReleased(KeyEvent keyEvent) {
        setTableViewColumn();
    }

    @FXML
    void onClearPromotionNameMouseClicked(MouseEvent mouseEvent) {
        promotionNameTextField.clear();
        promotionNameTextField.requestFocus();
        setTableViewColumn();
    }

    @FXML
    void onPromotionStartDateAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onPromotionEndDateAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onCLearPromotionStartDateMouseClicked(MouseEvent mouseEvent) {
        promotionStartDate.setValue(null);
        setTableViewColumn();
    }

    @FXML
    void onCLearPromotionEndDateMouseClicked(MouseEvent mouseEvent) {
        promotionEndDate.setValue(null);
        setTableViewColumn();
    }

    @FXML
    void onPromotionDiscountComboBoxAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onPromotionMinimumOrderAmountComboBoxAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onPromotionStatusComboBoxAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onReservationIdTextFieldKeyReleased(KeyEvent keyEvent) {
        setTableViewColumn();
    }

    @FXML
    void onClearReservationIdButtonMouseClicked(MouseEvent mouseEvent) {
        reservationIdTextField.clear();
        reservationIdTextField.requestFocus();
        setTableViewColumn();
    }

    @FXML
    void onReservationCustomerPhoneTextFieldKeyReleased(KeyEvent keyEvent) {
        setTableViewColumn();
    }

    @FXML
    void onCLearReservationCustomerPhoneMouseClicked(MouseEvent mouseEvent) {
        reservationCustomerPhoneTextField.clear();
        reservationCustomerPhoneTextField.requestFocus();
        setTableViewColumn();
    }

    @FXML
    void onReservationDateAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onReservationReceiveDateAction(ActionEvent actionEvent) {
        setTableViewColumn();
    }

    @FXML
    void onReservationDateMouseClicked(MouseEvent mouseEvent) {
        reservationDate.setValue(null);
        setTableViewColumn();
    }

    @FXML
    void onClearReceiveDateMouseClicked(MouseEvent mouseEvent) {
        reservationReceiveDate.setValue(null);
        setTableViewColumn();
    }
}
