package huongbien.controller;

import huongbien.entity.Order;
import huongbien.entity.OrderDetail;
import huongbien.service.OrderBUS;
import huongbien.util.Pagination;
import huongbien.util.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderManagementController implements Initializable {
    private static final OrderBUS orderBUS;
    private static Pagination<Order> orderPagination;

    static {
        try {
            orderBUS = new OrderBUS();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public TextField orderIdField;
    @FXML
    public TextField orderDiscountField;
    @FXML
    public TextField orderTotalOrderDetailAmount;
    @FXML
    public TextField orderVATField;
    @FXML
    public TextField orderTotalAmountField;
    @FXML
    public TextField orderCustomerField;
    @FXML
    public TextField orderEmployeeIdField;
    @FXML
    public TextField orderPromotionIdField;
    @FXML
    public TextField orderPaymentIdField;
    @FXML
    public TextArea orderNoteTextArea;
    @FXML
    public TextField orderSearchField;
    @FXML
    public Button searchOrderButton;
    @FXML
    public ImageView clearSearchButton;
    @FXML
    public TableView<Order> orderTable;
    @FXML
    public TableColumn<Order, String> orderIdColumn;
    @FXML
    public TableColumn<Order, Date> orderCreatedDateColumn;
    @FXML
    public TableColumn<Order, Double> orderTotalAmountColumn;
    @FXML
    public TableColumn<Order, String> orderEmployeeIdColumn;
    @FXML
    public TableColumn<Order, String> customerPhoneNumberColumn;
    @FXML
    public DatePicker orderDateDatePicker;
    @FXML
    private TextField orderTablesField;
    @FXML
    private ComboBox<String> searchMethodComboBox;
    @FXML
    private Label pageIndexLabel;
    @FXML
    private TableColumn<OrderDetail, String> orderDetailCuisineColumn;
    @FXML
    private TableColumn<OrderDetail, String> orderDetailNoteColumn;
    @FXML
    private TableColumn<OrderDetail, Integer> orderDetailQuantityColumn;
    @FXML
    private TableColumn<OrderDetail, Double> orderDetailSalePriceColumn;
    @FXML
    private TableView<OrderDetail> orderDetailTable;

    // initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearSearchButton.setVisible(false);
        setSearchMethodComboBoxValue();
        setOrderTableColumn();
        setOrderDetailTableColumn();
        setOrderPaginationGetAllOrder();
        setOrderTableValue();
    }

    public void setSearchMethodComboBoxValue() {
        searchMethodComboBox.getItems().addAll("Mã nhân viên", "Số điện thoại khách hàng", "Mã hóa đơn", "Tất cả");
        searchMethodComboBox.setValue("Tất cả");
        orderSearchField.setDisable(true);
    }

    public void setOrderTableColumn() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        orderCreatedDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        DecimalFormat priceFormat = new DecimalFormat("#,###");
        orderTotalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        orderTotalAmountColumn.setCellFactory(cellData -> new TextFieldTableCell<>(new StringConverter<>() {
            @Override
            public String toString(Double price) {
                return price != null ? priceFormat.format(price) : "";
            }

            @Override
            public Double fromString(String string) {
                try {
                    return priceFormat.parse(string).doubleValue();
                } catch (Exception e) {
                    return 0.0;
                }
            }
        }));
        customerPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCustomer() != null
                                ? cellData.getValue().getCustomer().getPhoneNumber()
                                : ""
                )
        );
        orderEmployeeIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getEmployee() != null
                                ? cellData.getValue().getEmployee().getId()
                                : ""
                )
        );
    }

    public void setOrderTableValue() {
        orderTable.getItems().clear();
        setPageIndexLabel();
        ObservableList<Order> listOrder = FXCollections.observableArrayList(orderPagination.getCurrentPage());
        orderTable.setItems(listOrder);
    }

    public void setOrderDetailTableColumn() {
        orderDetailCuisineColumn.setCellValueFactory(new PropertyValueFactory<>("cuisine"));
        orderDetailCuisineColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCuisine().getName())
        );
        orderDetailQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDetailSalePriceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        orderDetailNoteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    public void setPageIndexLabel() {
        int currentPageIndex = orderPagination.getCurrentPageIndex();
        int totalPage = orderPagination.getTotalPages() == 0 ? 1 : orderPagination.getTotalPages();
        pageIndexLabel.setText(currentPageIndex + "/" + totalPage);
    }

    public void setOrderDetailTableValue(List<OrderDetail> orderDetails) {
        orderDetailTable.getItems().clear();
        orderDetailTable.setItems(FXCollections.observableArrayList(orderDetails));
    }

    public void setOrderPaginationGetAllOrder() {
        try {
            boolean isRollback = false;
            int itemsPerPage = 10;
            int totalItems = orderBUS.countTotalOrders();
            orderPagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return orderBUS.getAllWithPagination(offset, limit);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    itemsPerPage,
                    totalItems,
                    isRollback
            );
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOrderPaginationGetByOrderId(String orderId) {
        try {
            boolean isRollback = false;
            int itemsPerPage = 10;
            int totalItems = orderBUS.countTotalOrdersByOrderId(orderId);
            orderPagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return orderBUS.getOrdersByIdWithPagination(offset, limit, orderId);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    itemsPerPage,
                    totalItems,
                    isRollback
            );
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOrderPaginationGetByCustomerPhoneNumber(String phoneNumber) {
        try {
            boolean isRollback = false;
            int itemsPerPage = 10;
            int totalItems = 0;
            totalItems = orderBUS.countTotalOrdersByCustomerPhoneNumber(phoneNumber);
            orderPagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return orderBUS.getOrdersByCustomerPhoneNumberWithPagination(offset, limit, phoneNumber);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    itemsPerPage,
                    totalItems,
                    isRollback
            );
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOrderPaginationGetByEmployeeId(String employeeId) {
        try {
            boolean isRollback = false;
            int itemsPerPage = 10;
            int totalItems = 0;
            totalItems = orderBUS.countTotalOrdersByEmployeeId(employeeId);
            orderPagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return orderBUS.getOrdersByEmployeeIdWithPagination(offset, limit, employeeId);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    itemsPerPage,
                    totalItems,
                    isRollback
            );
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchOrder() {
        String searchInfo = orderSearchField.getText();

        switch (searchMethodComboBox.getValue()) {
            case "Mã nhân viên" -> setOrderPaginationGetByEmployeeId(searchInfo);
            case "Số điện thoại khách hàng" -> setOrderPaginationGetByCustomerPhoneNumber(searchInfo);
            case "Mã hóa đơn" -> setOrderPaginationGetByOrderId(searchInfo);
            case "Tất cả" -> setOrderPaginationGetAllOrder();
        }

        setOrderTableValue();
    }

    @FXML
    void onSearchOrderButtonClicked(MouseEvent event) {
        searchOrder();
    }

    @FXML
    void onClearSearchButtonClicked(MouseEvent mouseEvent) {
        orderSearchField.clear();
        clearSearchButton.setVisible(false);
        setOrderTableValue();
    }

    @FXML
    void onSearchFieldKeyReleased(KeyEvent keyEvent) {
        boolean isSearchFieldEmpty = orderSearchField.getText().isEmpty();
        clearSearchButton.setVisible(isSearchFieldEmpty);
        searchOrder();
    }

    @FXML
    void onSearchMethodComboBoxSelected(ActionEvent actionEvent) {
        String searchMethod = searchMethodComboBox.getValue();
        orderSearchField.setDisable(searchMethod.equals("Tất cả"));
        switch (searchMethod) {
            case "Mã nhân viên" -> orderSearchField.setPromptText("Nhập mã nhân viên");
            case "Số điện thoại khách hàng" -> orderSearchField.setPromptText("Nhập số điện thoại khách hàng");
            case "Mã hóa đơn" -> orderSearchField.setPromptText("Nhập mã hóa đơn");
            case "Tất cả" -> {
                orderSearchField.setPromptText("Tìm kiếm");
                searchOrder();
            }
        }

        clearSearchButton.setVisible(false);
        orderSearchField.clear();
        setOrderTableValue();
    }

    @FXML
    void onOrderTableClicked(MouseEvent mouseEvent) {
        Order selectedItem = orderTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        orderIdField.setText(selectedItem.getId());
        
        try {
            orderTablesField.setText(Utils.toStringTables(selectedItem.getTables()));
        } catch (Exception e) {
            // Handle LazyInitializationException
            orderTablesField.setText("Không thể hiển thị");
        }

        if (selectedItem.getCustomer() == null) {
            orderCustomerField.setText("Vãng lai");
        } else {
            orderCustomerField.setText(selectedItem.getCustomer().getId());
        }

        orderDateDatePicker.setValue(selectedItem.getOrderDate());
        orderEmployeeIdField.setText(selectedItem.getEmployee().getId());

        // Clear order details table first, then try to populate it
        orderDetailTable.getItems().clear();
        try {
            setOrderDetailTableValue(selectedItem.getOrderDetails());
        } catch (Exception e) {
            // Already cleared the table above
        }

        DecimalFormat priceFormat = new DecimalFormat("#,###");

        // Use the stored totalAmount instead of calculating it
        double totalOrderDetailAmount;
        try {
            totalOrderDetailAmount = selectedItem.calculateTotalAmount();
        } catch (Exception e) {
            // If we can't calculate, use the stored value
            totalOrderDetailAmount = selectedItem.getTotalAmount();
        }
        String formattedTotalOrderDetailAmount = priceFormat.format(totalOrderDetailAmount);
        orderTotalOrderDetailAmount.setText(formattedTotalOrderDetailAmount);

        // Try to calculate discount or use 0 as fallback
        double discountAmount;
        try {
            discountAmount = selectedItem.calculateReducedAmount();
        } catch (Exception e) {
            discountAmount = 0;
        }
        String formattedDiscount = priceFormat.format(discountAmount);
        orderDiscountField.setText(formattedDiscount);

        String formattedTotalAmount = priceFormat.format(selectedItem.getTotalAmount());
        orderTotalAmountField.setText(formattedTotalAmount);

        // Try to calculate VAT or use a percentage of totalAmount as fallback
        double vatAmount;
        try {
            vatAmount = selectedItem.calculateVatTaxAmount();
        } catch (Exception e) {
            // Estimate VAT as 10% of totalAmount
            vatAmount = selectedItem.getTotalAmount() * 0.1;
        }
        String formattedVatTax = priceFormat.format(vatAmount);
        orderVATField.setText(formattedVatTax);

        if (selectedItem.getPromotion() == null) {
            orderPromotionIdField.setText("Không");
        } else {
            orderPromotionIdField.setText(selectedItem.getPromotion().getId());
        }

        orderPaymentIdField.setText(selectedItem.getPayment().getPaymentId());
        orderNoteTextArea.setText(selectedItem.getNotes());
    }

    @FXML
    void onLastPageButtonClicked(MouseEvent event) {
        orderPagination.goToLastPage();
        setOrderTableValue();
    }

    @FXML
    void onNextPageButtonClicked(MouseEvent event) {
        orderPagination.goToNextPage();
        setOrderTableValue();
    }

    @FXML
    void onPrevPageButtonClicked(MouseEvent event) {
        orderPagination.goToPreviousPage();
        setOrderTableValue();
    }

    @FXML
    void onFirstPageButtonClicked(MouseEvent event) {
        orderPagination.goToFirstPage();
        setOrderTableValue();
    }
}
