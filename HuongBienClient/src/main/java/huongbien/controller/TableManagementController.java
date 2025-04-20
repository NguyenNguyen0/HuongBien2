package huongbien.controller;

import huongbien.dao.remote.ITableDAO;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.entity.TableType;
import huongbien.rmi.RMIClient;
import huongbien.service.TableBUS;
import huongbien.service.TableTypeBUS;
import huongbien.util.Pagination;
import huongbien.util.ToastsMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

public class TableManagementController implements Initializable {
    private final Image DEFAULT_IMAGE = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/huongbien/icon/all/gallery-512px.png")));
    private final TableBUS tableBUS = new TableBUS();
    @FXML
    public Button searchTableButton;
    @FXML
    public ComboBox<String> searchMethodComboBox;
    @FXML
    public Label pageIndexLabel;
    @FXML
    public ImageView clearSearchFieldButton;
    @FXML
    private Button clearTableFormButton;
    @FXML
    private Button handleActionTableButton;
    @FXML
    private Button swapModeTableButton;
    @FXML
    private ComboBox<Integer> tableFloorComboBox;
    @FXML
    private ComboBox<String> tableStatusComboBox;
    @FXML
    private ComboBox<TableType> tableTypeComboBox;
    @FXML
    private ImageView tableImageView;
    @FXML
    private TableColumn<Table, String> tableFloorColumn;
    @FXML
    private TableColumn<Table, String> tableIdColumn;
    @FXML
    private TableColumn<Table, String> tableNameColumn;
    @FXML
    private TableColumn<Table, Integer> tableSeatsColumn;
    @FXML
    private TableColumn<Table, String> tableStatusColumn;
    @FXML
    private TableColumn<Table, String> tableTypeColumn;
    @FXML
    private TableView<Table> tableTableView;
    @FXML
    private TextField tableNameField;
    @FXML
    private TextField tableSearchField;
    @FXML
    private TextField tableSeatsField;
    private Pagination<Table> tablePagination;
    private final ITableDAO tableDao;

    {
        try {
            tableDao = RMIClient.getInstance().getTableDAO();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public TableManagementController() throws RemoteException {
    }

    public void setTableTableViewColumn() {
        tableTableView.getItems().clear();
        tableTableView.setPlaceholder(new Label("Không có dữ liệu"));

        tableIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
        tableStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableTypeColumn.setCellValueFactory(cellData -> {
            TableType tableType = cellData.getValue().getTableType();
            return new SimpleStringProperty(tableType.getName());
        });
        tableFloorColumn.setCellValueFactory(cellData -> {
            int floor = cellData.getValue().getFloor();
            String formattedFloor = "Tầng " + (floor == 0 ? "trệt" : floor);
            return new SimpleStringProperty(formattedFloor);
        });
    }

    private void setTableImage(TableStatus tableStatus) {
        switch (tableStatus) {
            case TableStatus.AVAILABLE ->
                    tableImageView.setImage(new Image("/huongbien/icon/order/tableEmpty-512px.png"));
            case TableStatus.OCCUPIED ->
                    tableImageView.setImage(new Image("/huongbien/icon/order/tableReserved-512px.png"));
            case TableStatus.RESERVED ->
                    tableImageView.setImage(new Image("/huongbien/icon/order/tableOpen-512px.png"));
            case TableStatus.UNAVAILABLE ->
                    tableImageView.setImage(new Image("/huongbien/icon/order/tableClosed-512px.png"));
            default -> tableImageView.setImage(DEFAULT_IMAGE);
        }
    }

    private void setTableTableValue() {
        setPageIndexLabel();
        tableTableView.getItems().clear();
        tableTableView.setItems(FXCollections.observableArrayList(tablePagination.getCurrentPage()));
    }

    public void setTableTypeComboBox() {
        TableTypeBUS tableTypeBUS = new TableTypeBUS();
        try {
            List<TableType> tableTypeList = tableTypeBUS.getAllTableType();
            ObservableList<TableType> tableTypes = FXCollections.observableArrayList(tableTypeList);
            tableTypeComboBox.setItems(tableTypes);
            tableTypeComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(TableType tableType) {
                    return tableType != null ? tableType.getName() : "";
                }

                @Override
                public TableType fromString(String string) {
                    return tableTypeComboBox.getItems().stream()
                            .filter(item -> item.getName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTableStatusComboBox() {
        try {
            List<String> statusList = tableBUS.getDistinctStatuses();
            ObservableList<String> statuses = FXCollections.observableArrayList(statusList);
            tableStatusComboBox.setItems(statuses);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTableFloorComboBox() {
        try {
            List<Integer> floors = tableBUS.getDistinctFloors();
            ObservableList<Integer> floorList = FXCollections.observableArrayList(floors);
            tableFloorComboBox.setItems(floorList);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSearchMethodComboBox() {
        ObservableList<String> searchMethods = FXCollections.observableArrayList("Tên Bàn", "Tầng", "Tất cả");
        searchMethodComboBox.setItems(searchMethods);
        searchMethodComboBox.setValue("Tất cả");
    }

    public void setPageIndexLabel() {
        int currentPageIndex = tablePagination.getCurrentPageIndex();
        int totalPage = tablePagination.getTotalPages() == 0 ? 1 : tablePagination.getTotalPages();
        pageIndexLabel.setText(currentPageIndex + "/" + totalPage);
    }

    public void setTablePaginationDataGetter(BiFunction<Integer, Integer, List<Table>> dataGetter, int totalItems) {
        int itemPerPage = 10;
        boolean isRollBack = false;
        tablePagination = new Pagination<>(
                dataGetter,
                itemPerPage,
                totalItems,
                isRollBack
        );
    }

    public void setTablePaginationGetAll() {
        try {
            int totalTable = tableBUS.countTotalTables();
            setTablePaginationDataGetter((offset, limit) -> {
                try {
                    return tableBUS.getTablesWithPagination(offset, limit);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }, totalTable);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTablePaginationGetByFloor() {
        int floor;
        try {
            floor = Integer.parseInt(tableSearchField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá trị tầng không hợp lệ.");
            return;
        }
        try {
            int totalTable = tableBUS.countTotalTablesByFloor(floor);
            setTablePaginationDataGetter((offset, limit) -> {
                try {
                    return tableBUS.getTablesByFloorWithPagination(offset, limit, floor);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }, totalTable);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTablePaginationGetByName() {
        String name = tableSearchField.getText();
        try {
            int totalTable = tableBUS.countTotalTablesByName(name);
            setTablePaginationDataGetter((offset, limit) -> {
                try {
                    return tableBUS.getTablesByNameWithPagination(offset, limit, name);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }, totalTable);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Table getTableFromForm() {
        String name = tableNameField.getText();
        int seats;
        int floor;
        String statusDisplayValue = tableStatusComboBox.getValue();
        TableType selectedTableType = tableTypeComboBox.getValue();

        try {
            seats = Integer.parseInt(tableSeatsField.getText());
            floor = tableFloorComboBox.getValue();
        } catch (NumberFormatException e) {
            ToastsMessage.showMessage("Giá trị số ghế hoặc tầng không hợp lệ.", "error");
            return null;
        }

        if (selectedTableType == null) {
            System.out.println("Lỗi: Chưa chọn loại bàn.");
            ToastsMessage.showMessage("Chưa chọn loại bàn.", "error");
            return null;
        }
        
        if (statusDisplayValue == null) {
            ToastsMessage.showMessage("Vui lòng chọn trạng thái bàn", "error");
            return null;
        }
        
        // Chỉ cần truyền giá trị String vào constructor, constructor sẽ tự chuyển đổi sang enum
        return new Table(name, seats, floor, statusDisplayValue, selectedTableType);
    }

    public void addNewTable() {
        Table table = getTableFromForm();
        try {
            if (tableBUS.addTable(table)) {
                ToastsMessage.showMessage("Thêm bàn thành công", "success");
            } else {
                ToastsMessage.showMessage("Thêm bàn không thành công", "error");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTable() {
        Table selectedTable = tableTableView.getSelectionModel().getSelectedItem();
        if (selectedTable == null) {
            ToastsMessage.showMessage("Chưa chọn bàn để sửa.", "error");
            return;
        }

        String existingTableId = selectedTable.getId();
        Table table = getTableFromForm();
        table.setId(existingTableId);

        try {
            if (tableBUS.updateTableInfo(table)) {
                ToastsMessage.showMessage("Sửa bàn thành công", "success");
            } else {
                ToastsMessage.showMessage("Sửa bàn không thành công", "error");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearTableForm() {
        tableImageView.setImage(DEFAULT_IMAGE);
        tableNameField.setText("");
        tableSeatsField.setText("");
        tableTypeComboBox.getSelectionModel().clearSelection();
        tableFloorComboBox.getSelectionModel().clearSelection();
        tableStatusComboBox.getSelectionModel().clearSelection();
        tableTableView.getSelectionModel().clearSelection();
    }

    public void clearSearchField() {
        tableSearchField.setText("");
    }

    public void changeHandleButtonModeToEditTable() {
        swapModeTableButton.setText("Thêm bàn");
        handleActionTableButton.setText("Sửa bàn");
        swapModeTableButton.setStyle("-fx-background-color:   #1D557E");
        handleActionTableButton.setStyle("-fx-background-color:  #761D7E");
    }

    public void changeHandleButtonModeToAddTable() {
        swapModeTableButton.setText("Sửa bàn");
        handleActionTableButton.setText("Thêm bàn");
        swapModeTableButton.setStyle("-fx-background-color:   #761D7E");
        handleActionTableButton.setStyle("-fx-background-color:  #1D557E");
    }

    void disableInput() {
        tableNameField.setDisable(true);
        tableSeatsField.setDisable(true);
        tableTypeComboBox.setDisable(true);
        tableStatusComboBox.setDisable(true);
        tableFloorComboBox.setDisable(true);
    }

    void enableInput() {
        tableNameField.setDisable(false);
        tableSeatsField.setDisable(false);
        tableFloorComboBox.setDisable(false);
        tableStatusComboBox.setDisable(false);
        tableTypeComboBox.setDisable(false);
    }

    void setSearchTablePagination() {
        String searchMethod = searchMethodComboBox.getValue();
        switch (searchMethod) {
            case "Tầng" -> setTablePaginationGetByFloor();
            case "Tên Bàn" -> setTablePaginationGetByName();
            case "Tất cả" -> setTablePaginationGetAll();
        }

        setTableTableValue();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableSearchField.setDisable(true);
        clearSearchFieldButton.setVisible(false);
        changeHandleButtonModeToAddTable();
        setTableTypeComboBox();
        setTableStatusComboBox();
        setTableFloorComboBox();
        setSearchMethodComboBox();

        setTableTableViewColumn();
        setTablePaginationGetAll();
        setTableTableValue();
    }

    @FXML
    public void onClearSearchButtonClicked(MouseEvent mouseEvent) {
        tableSearchField.setText("");
        clearSearchFieldButton.setVisible(false);
    }

    @FXML
    void onClearTableFormButtonClicked(ActionEvent event) {
        clearTableForm();
    }

    @FXML
    void onHandleActionTableButtonClicked(MouseEvent event) {
        if (handleActionTableButton.getText().equals("Thêm bàn")) {
            addNewTable();
        } else {
            updateTable();
        }
        clearTableForm();
        disableInput();
        tableTableView.getItems().clear();
        setTableTableValue();
        changeHandleButtonModeToEditTable();
    }

    @FXML
    void onSwapModeTableButtonClicked(MouseEvent event) {
        if (swapModeTableButton.getText().equals("Thêm bàn")) {
            clearTableForm();
            enableInput();
            changeHandleButtonModeToAddTable();
        } else {
            clearTableForm();
            disableInput();
            changeHandleButtonModeToEditTable();
        }
    }

    @FXML
    void onTableTableViewClicked(MouseEvent event) {
        Table selectedItem = tableTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        String id = selectedItem.getId();
        try {
            Table table = tableDao.getById(id);
            tableNameField.setText(table.getName());
            tableSeatsField.setText(String.valueOf(table.getSeats()));

            tableTypeComboBox.getItems().stream()
                    .filter(type -> type.getName().equals(table.getTableType().getName()))
                    .findFirst()
                    .ifPresent(tableTypeComboBox.getSelectionModel()::select);

            tableStatusComboBox.getItems().stream()
                    .filter(status -> status.equals(table.getStatus()))
                    .findFirst()
                    .ifPresent(tableStatusComboBox.getSelectionModel()::select);

            tableFloorComboBox.getItems().stream()
                    .filter(floor -> floor.equals(table.getFloor()))
                    .findFirst()
                    .ifPresent(tableFloorComboBox.getSelectionModel()::select);

            setTableImage(table.getStatus());

            handleActionTableButton.setVisible(true);
            swapModeTableButton.setVisible(true);
            clearTableFormButton.setVisible(true);
            enableInput();
            changeHandleButtonModeToEditTable();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onSearchTableFieldKeyReleased(KeyEvent keyEvent) {
        String searchValue = tableSearchField.getText();
        clearSearchFieldButton.setVisible(!searchValue.isEmpty());
        setSearchTablePagination();
    }

    @FXML
    void onSearchTableButtonClicked(MouseEvent mouseEvent) {
        setSearchTablePagination();
    }

    @FXML
    void onSearchMethodComboBoxSelected(ActionEvent actionEvent) {
        String selectedMethod = searchMethodComboBox.getValue();
        if (selectedMethod == null) return;

        clearSearchField();
        tableSearchField.setDisable(false);
        clearSearchFieldButton.setVisible(false);
        switch (selectedMethod) {
            case "Tầng" -> tableSearchField.setPromptText("Nhập tầng");
            case "Tên Bàn" -> tableSearchField.setPromptText("Nhập tên bàn");
            case "Tất cả" -> {
                tableSearchField.setDisable(true);
                tableSearchField.setPromptText("Tìm kiếm");
                setSearchTablePagination();
            }
        }
    }

    @FXML
    public void onFirstPageButtonClicked(MouseEvent mouseEvent) {
        tablePagination.goToFirstPage();
        setTableTableValue();
    }

    @FXML
    public void onPrevPageButtonClicked(MouseEvent mouseEvent) {
        tablePagination.goToPreviousPage();
        setTableTableValue();
    }

    @FXML
    public void onNextPageButtonClicked(MouseEvent mouseEvent) {
        tablePagination.goToNextPage();
        setTableTableValue();
    }

    @FXML
    public void onLastPageButtonClicked(MouseEvent mouseEvent) {
        tablePagination.goToLastPage();
        setTableTableValue();
    }
}