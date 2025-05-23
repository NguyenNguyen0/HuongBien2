package huongbien.controller;

import huongbien.dao.remote.ICategoryDAO;
import huongbien.entity.Category;
import huongbien.entity.Cuisine;
import huongbien.rmi.RMIClient;
import huongbien.service.CuisineBUS;
import huongbien.util.Converter;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CuisineManagementController implements Initializable {
    private final CuisineBUS cuisineBUS = new CuisineBUS();
    private final Image DEFAULT_IMAGE = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/huongbien/icon/all/gallery-512px.png")));
    @FXML
    public Label pageIndexLabel;
    @FXML
    public ComboBox<String> searchMethodComboBox;
    @FXML
    public Button searchCuisineButton;
    @FXML
    public ImageView clearCuisineSearchButton;
    @FXML
    public ComboBox<String> cuisineStatusComboBox;
    @FXML
    private TableColumn<Cuisine, String> cuisineCategoryColumn;
    @FXML
    private TableColumn<Cuisine, String> cuisineIdColumn;
    @FXML
    private TableColumn<Cuisine, String> cuisineNameColumn;
    @FXML
    private TableColumn<Cuisine, String> cuisineStatusColumn;
    @FXML
    private TableColumn<Cuisine, Double> cuisinePriceColumn;
    @FXML
    private TableView<Cuisine> cuisineTable;
    @FXML
    private TextField cuisineNameField;
    @FXML
    private TextField cuisinePriceField;
    @FXML
    private ComboBox<Category> cuisineCategoryComboBox;
    @FXML
    private TextArea cuisineDescriptionTextArea;
    @FXML
    private TextField cuisineSearchField;
    @FXML
    private Button swapModeCuisineButton;
    @FXML
    private Button handleActionCuisineButton;
    @FXML
    private Button deleteCuisineButton;
    @FXML
    private Button clearCuisineButton;
    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView cuisineImageView;
    private byte[] imageCuisineByte = null;
    private Pagination<Cuisine> cuisinePagination;

    public CuisineManagementController() throws RemoteException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearCuisineSearchButton.setVisible(false);
        setHandleActionButtonToAddCuisine();
        setSearchMethodComboBoxValue();
        setCuisinePaginationGetAll();
        setCuisineTableColumns();
        setCuisineTableValues();
        setCategoryComboBoxValue();
        setCuisineStatusComboBoxValue();
    }

    public void setCuisineTableColumns() {
        cuisineTable.setPlaceholder(new Label("Không có món ăn nào"));
        cuisineIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        cuisineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cuisinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        cuisineStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        cuisinePriceColumn.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Double>() {
            private final DecimalFormat priceFormat = new DecimalFormat("#,###");

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
        cuisineCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));
    }

    private void setCuisineTableValues() {
        List<Cuisine> cuisineList = cuisinePagination.getCurrentPage();
        ObservableList<Cuisine> listCuisine = FXCollections.observableArrayList(cuisineList);
        cuisineTable.setItems(listCuisine);
        setPageIndexLabel();
    }

    private void setCuisineStatusComboBoxValue() {
        ObservableList<String> status = FXCollections.observableArrayList("Còn bán", "Ngừng bán");
        cuisineStatusComboBox.setItems(status);
        cuisineStatusComboBox.getSelectionModel().selectFirst();
    }

    private void setCategoryComboBoxValue() {
        try {
            ICategoryDAO categoryDAO = RMIClient.getInstance().getCategoryDAO();
            List<Category> categoryList = categoryDAO.getAll();
            ObservableList<Category> categories = FXCollections.observableArrayList(categoryList);
            cuisineCategoryComboBox.setItems(categories);
            cuisineCategoryComboBox.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category != null ? category.getName() : "";
                }

                @Override
                public Category fromString(String string) {
                    return cuisineCategoryComboBox.getItems().stream()
                            .filter(item -> item.getName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSearchMethodComboBoxValue() {
        ObservableList<String> searchMethods = FXCollections.observableArrayList("Tất cả", "Tìm theo tên");
        searchMethodComboBox.setItems(searchMethods);
        searchMethodComboBox.getSelectionModel().selectFirst();
    }

    public void setCuisinePaginationGetAll() {
        try {
            int itemPerPage = 10;
            int totalItem = cuisineBUS.countTotalCuisine();
            boolean isRollBack = false;
            cuisinePagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return cuisineBUS.getAllCuisineWithPagination(offset, limit);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    itemPerPage,
                    totalItem,
                    isRollBack
            );
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCuisinePaginationGetByName(String name) throws RemoteException {
        int itemPerPage = 10;
        int totalItem = cuisineBUS.countCuisinesByName(name);
        boolean isRollBack = false;
        cuisinePagination = new Pagination<>(
                (offset, limit) -> {
                    try {
                        return cuisineBUS.getCuisinesByNameWithPagination(offset, limit, name);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                },
                itemPerPage,
                totalItem,
                isRollBack
        );
    }

    public void setCuisinePaginationGetByCategory(String categoryName) throws RemoteException {
        int itemPerPage = 10;
        int totalItem = cuisineBUS.countCuisinesByCategory(categoryName);
        boolean isRollBack = false;
        cuisinePagination = new Pagination<>(
                (offset, limit) -> {
                    try {
                        return cuisineBUS.getCuisinesByCategoryWithPagination(offset, limit, categoryName);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                },
                itemPerPage,
                totalItem,
                isRollBack
        );
    }

    public void setPageIndexLabel() {
        int currentPageIndex = cuisinePagination.getCurrentPageIndex();
        int totalPage = cuisinePagination.getTotalPages() == 0 ? 1 : cuisinePagination.getTotalPages();
        pageIndexLabel.setText(currentPageIndex + "/" + totalPage);
    }

    private void clearChooserImage() {
        imageCuisineByte = null;
        cuisineImageView.setImage(DEFAULT_IMAGE);
    }

    private void clearCuisineForm() {
        cuisineNameField.clear();
        cuisinePriceField.clear();
        cuisineDescriptionTextArea.clear();
        cuisineCategoryComboBox.getSelectionModel().clearSelection();
        cuisineStatusComboBox.getSelectionModel().clearSelection();
        cuisineTable.getSelectionModel().clearSelection();
        deleteCuisineButton.setVisible(false);
        clearChooserImage();
    }

    private void setHandleActionButtonToEditCuisine() {
        swapModeCuisineButton.setText("Thêm món");
        handleActionCuisineButton.setText("Sửa món");
        swapModeCuisineButton.setStyle("-fx-background-color: #1D557E");
        handleActionCuisineButton.setStyle("-fx-background-color: #761D7E");
        deleteCuisineButton.setVisible(true);
    }

    private void setHandleActionButtonToAddCuisine() {
        swapModeCuisineButton.setText("Sửa món");
        handleActionCuisineButton.setText("Thêm món");
        swapModeCuisineButton.setStyle("-fx-background-color: #761D7E");
        handleActionCuisineButton.setStyle("-fx-background-color: #1D557E");
        deleteCuisineButton.setVisible(false);
    }

    private void disableInput() {
        cuisineNameField.setDisable(true);
        cuisinePriceField.setDisable(true);
        cuisineCategoryComboBox.setDisable(true);
        cuisineDescriptionTextArea.setDisable(true);
        cuisineStatusComboBox.setDisable(true);
        chooseImageButton.setDisable(true);
    }

    private void enableInput() {
        cuisineNameField.setDisable(false);
        cuisinePriceField.setDisable(false);
        cuisineStatusComboBox.setDisable(false);
        cuisineCategoryComboBox.setDisable(false);
        cuisineDescriptionTextArea.setDisable(false);
        chooseImageButton.setDisable(false);
    }

    @FXML
    private void onCuisineTableViewClicked(MouseEvent event) {
        Cuisine selectedItem = cuisineTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            loadCuisineDetails(selectedItem);
            enableInput();
            setHandleActionButtonToEditCuisine();
        }
    }

    private void loadCuisineDetails(Cuisine cuisine) {
        loadImage(cuisine.getImage());
        cuisineNameField.setText(cuisine.getName());
        cuisinePriceField.setText(new DecimalFormat("#,###").format(cuisine.getPrice()));
        cuisineCategoryComboBox.getSelectionModel().select(cuisine.getCategory());
        cuisineDescriptionTextArea.setText(cuisine.getDescription());
        cuisineStatusComboBox.getSelectionModel().select(cuisine.getStatus().getStatus());
        deleteCuisineButton.setVisible(true);
        clearCuisineButton.setVisible(true);
        swapModeCuisineButton.setVisible(true);
        handleActionCuisineButton.setVisible(true);
    }

    private void loadImage(byte[] imageBytes) {
        imageCuisineByte = imageBytes;
        Image image = (imageBytes != null) ? Converter.bytesToImage(imageBytes) : DEFAULT_IMAGE;
        cuisineImageView.setImage(image);
    }

    @FXML
    private void onClearCuisineClicked(ActionEvent event) {
        clearCuisineForm();
        disableInput();
        setHandleActionButtonToEditCuisine();
    }

    @FXML
    private void onClearSearchClicked(MouseEvent event) {
        cuisineSearchField.clear();
        cuisineSearchField.requestFocus();
        setCuisineTableValues();
    }

    @FXML
    private void onSwapModeButtonClicked(ActionEvent event) {
        if (swapModeCuisineButton.getText().equals("Sửa món")) {
            clearChooserImage();
            setHandleActionButtonToEditCuisine();
        } else {
            clearCuisineForm();
            enableInput();
            setHandleActionButtonToAddCuisine();
        }
    }

    @FXML
    private void onHandleActionButtonClicked(ActionEvent event) {
        if (handleActionCuisineButton.getText().equals("Sửa món")) {
            updateCuisine();
        } else {
            try {
                addCuisine();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        disableInput();
        refreshCuisineTable();
        setHandleActionButtonToEditCuisine();
    }

    private void updateCuisine() {
        Cuisine selectedItem = cuisineTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Cuisine cuisine = createCuisineFromForm(selectedItem.getId());
            try {
                if (cuisineBUS.updateCuisineInfo(cuisine)) {
                    ToastsMessage.showMessage("Sửa món thành công", "success");
                } else {
                    ToastsMessage.showMessage("Sửa món không thành công, vui lòng kiểm tra lại", "error");
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        clearCuisineForm();
    }

    private void addCuisine() throws RemoteException {
        Cuisine cuisine = createCuisineFromForm(null);
        
        // Thêm kiểm tra và log để xác định vấn đề
        if (cuisine.getName() == null || cuisine.getName().trim().isEmpty()) {
            ToastsMessage.showMessage("Tên món ăn không được để trống", "error");
            return;
        }
        
        if (cuisine.getCategory() == null) {
            ToastsMessage.showMessage("Vui lòng chọn loại món ăn", "error");
            return;
        }
        
        if (cuisine.getPrice() <= 0) {
            ToastsMessage.showMessage("Giá món ăn phải lớn hơn 0", "error");
            return;
        }
        
        try {
            // Sinh ID tự động nếu cần
            if (cuisine.getId() == null || cuisine.getId().trim().isEmpty()) {
                String randomId = "M" + System.currentTimeMillis() % 100000;
                cuisine = new Cuisine(randomId, cuisine.getName(), cuisine.getPrice(), 
                    cuisine.getDescription(), cuisine.getImage(), "AVAILABLE", cuisine.getCategory());
            }
            
            if (cuisineBUS.addCuisine(cuisine)) {
                ToastsMessage.showMessage("Thêm món thành công", "success");
            } else {
                ToastsMessage.showMessage("Thêm món không thành công", "error");
            }
        } catch (Exception e) {
            ToastsMessage.showMessage("Lỗi: " + e.getMessage(), "error");
            e.printStackTrace();
        }
        clearCuisineForm();
    }

    private Cuisine createCuisineFromForm(String cuisineId) {
        String name = cuisineNameField.getText();
        double price = cuisinePriceField.getText().isEmpty() ? 0.0 : Converter.parseMoney(cuisinePriceField.getText());
        String description = cuisineDescriptionTextArea.getText();
        Category category = cuisineCategoryComboBox.getValue();
        
        // Fix for the encoding issue - map UI display values to proper enum constants
        String status = "AVAILABLE"; // Default value
        if (cuisineStatusComboBox.getValue() != null) {
            String selectedStatus = cuisineStatusComboBox.getValue();
            if (selectedStatus.equals("Còn bán") || selectedStatus.equals("Con ban")) {
                status = "AVAILABLE";
            } else if (selectedStatus.equals("Ngừng bán") || selectedStatus.equals("Ngung ban")) {
                status = "UNAVAILABLE";
            }
        }
        
        return new Cuisine(cuisineId, name, price, description, imageCuisineByte, status, category);
    }

    private void refreshCuisineTable() {
        cuisineTable.getItems().clear();
        setCuisineTableValues();
    }

    @FXML
    private void onDeleteCuisineButtonClicked(ActionEvent event) throws RemoteException {
        Cuisine selectedItem = cuisineTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (cuisineBUS.stopSellCuisine(selectedItem.getId())) {
                ToastsMessage.showMessage("Ngừng bán món thành công", "success");
            } else {
                ToastsMessage.showMessage("Ngừng bán món thất bại", "error");
            }
        }
        refreshCuisineTable();
        clearCuisineForm();
        disableInput();
    }

    @FXML
    private void onCuisinePriceTextFieldKeyReleased(KeyEvent event) {
        formatPriceField();
    }

    private void formatPriceField() {
        String input = cuisinePriceField.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            cuisinePriceField.setText(formattedText);
            cuisinePriceField.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            cuisinePriceField.setText(validInput.toString());
            cuisinePriceField.positionCaret(validInput.length());
        }
    }

    @FXML
    public void onSearchMethodComboBoxSelected(ActionEvent actionEvent) {
        String selectedMethod = searchMethodComboBox.getValue();
        if (selectedMethod == null) return;
        cuisineSearchField.setDisable(false);
        switch (selectedMethod) {
            case "Tìm theo tên":
                cuisineSearchField.setPromptText("Nhập tên món ăn");
                cuisineSearchField.clear();
                cuisineSearchField.requestFocus();
                break;
            case "Tìm theo loại món":
                cuisineSearchField.setPromptText("Nhập loại món ăn");
                cuisineSearchField.clear();
                cuisineSearchField.requestFocus();
                break;
            default:
                cuisineSearchField.setPromptText("Thông tin món ăn");
                cuisineSearchField.clear();
                cuisineSearchField.setDisable(true);
                try {
                    searchCuisine();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    public void searchCuisine() throws RemoteException {
        String selectedMethod = searchMethodComboBox.getValue();
        String searchValue = cuisineSearchField.getText();
        if (selectedMethod == null) return;
        switch (selectedMethod) {
            case "Tìm theo tên" -> setCuisinePaginationGetByName(searchValue);
            case "Tìm theo loại món" -> setCuisinePaginationGetByCategory(searchValue);
            default -> setCuisinePaginationGetAll();
        }
        setCuisineTableValues();
    }

    @FXML
    public void onSearchCuisineButtonClicked(MouseEvent mouseEvent) {
        try {
            searchCuisine();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onCuisineSearchTextFieldClicked(MouseEvent event) {
    }

    @FXML
    private void onCuisineSearchTextFieldKeyReleased(KeyEvent event) {
        clearCuisineSearchButton.setVisible(!cuisineSearchField.getText().isEmpty());
        try {
            searchCuisine();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onImageChooserButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            cuisineImageView.setImage(image);
            try {
                imageCuisineByte = Converter.fileToBytes(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onFirstPageButtonClicked(MouseEvent mouseEvent) {
        cuisinePagination.goToFirstPage();
        setCuisineTableValues();
    }

    @FXML
    public void onPrevPageButtonClicked(MouseEvent mouseEvent) {
        cuisinePagination.goToPreviousPage();
        setCuisineTableValues();
    }

    @FXML
    public void onNextPageButtonClicked(MouseEvent mouseEvent) {
        cuisinePagination.goToNextPage();
        setCuisineTableValues();
    }

    @FXML
    public void onLastPageButtonClicked(MouseEvent mouseEvent) {
        cuisinePagination.goToLastPage();
        setCuisineTableValues();
    }
}