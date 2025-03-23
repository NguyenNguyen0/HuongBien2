package huongbien.ui.controller;

import huongbien.bus.PromotionBUS;
import huongbien.entity.MembershipLevel;
import huongbien.entity.Promotion;
import huongbien.util.Pagination;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PromotionManagementController implements Initializable {
    @FXML
    private Label pageIndexLabel;
    @FXML
    private ImageView clearSearchButton;
    @FXML
    private TextField promotionSearchField;
    @FXML
    private TextField promotionNameField;
    @FXML
    private DatePicker startedDateDatePicker;
    @FXML
    private DatePicker endedDateDatePicker;
    @FXML
    private TextField discountField;
    @FXML
    private ComboBox<String> promotionStatusComboBox;
    @FXML
    private TextField minimumOrderField;
    @FXML
    private ComboBox<String> memberShipLevelComboBox;
    @FXML
    private TextArea promotionDescriptionTextArea;
    @FXML
    private Button handleActionPromotionButton;
    @FXML
    private Button swapModePromotionButton;
    @FXML
    private ComboBox<String> filterPromotionStatusComboBox;
    @FXML
    private TableView<Promotion> promotionTable;
    @FXML
    private TableColumn<Promotion, String> promotionIdColumn;
    @FXML
    private TableColumn<Promotion, Date> promotionStartedDateColumn;
    @FXML
    private TableColumn<Promotion, Date> promotionEndedDateColumn;
    @FXML
    private TableColumn<Promotion, Double> promotionDiscountColumn;
    @FXML
    private TableColumn<Promotion, String> promotionMembershipLevelColumn;

    PromotionBUS promotionBUS = new PromotionBUS();
    Pagination<Promotion> promotionPagination;

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearSearchButton.setVisible(false);
        changeHandleButtonModeToAddPromotion();
        disableInput();
        setComboBoxValue();
        setPromotionTableColumn();
        setPaginationGetByStatus();
        setPromotionTableValue();
        setPageIndexLabel();
    }

    public void changeHandleButtonModeToEditPromotion() {
        swapModePromotionButton.setText("Thêm");
        handleActionPromotionButton.setText("Sửa");
        swapModePromotionButton.setStyle("-fx-background-color:   #1D557E");
        handleActionPromotionButton.setStyle("-fx-background-color:  #761D7E");
    }

    public void changeHandleButtonModeToAddPromotion() {
        swapModePromotionButton.setText("Sửa");
        handleActionPromotionButton.setText("Thêm");
        swapModePromotionButton.setStyle("-fx-background-color:   #761D7E");
        handleActionPromotionButton.setStyle("-fx-background-color:  #1D557E");
    }

    private void setComboBoxValue() {
        // Membership level
        ObservableList<String> memberShipLevelList = FXCollections.observableArrayList("Đồng", "Bạc", "Vàng", "Kim cương");
        memberShipLevelComboBox.setItems(memberShipLevelList);
        memberShipLevelComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return memberShipLevelComboBox.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        // Promotion status
        ObservableList<String> statusList = FXCollections.observableArrayList("Còn hiệu lực", "Hết hiệu lực", "Tất cả");
        filterPromotionStatusComboBox.setItems(statusList);
        filterPromotionStatusComboBox.setValue(statusList.getFirst());
        filterPromotionStatusComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return filterPromotionStatusComboBox.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        ObservableList<String> statusListShow = FXCollections.observableArrayList("Còn hiệu lực", "Hết hiệu lực");
        promotionStatusComboBox.setItems(statusListShow);
        promotionStatusComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return promotionStatusComboBox.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public void setPromotionTableColumn() {
        promotionIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        promotionEndedDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        promotionStartedDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        promotionMembershipLevelColumn.setCellValueFactory(cellData -> {
            int memberShip = cellData.getValue().getMembershipLevel().ordinal();
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            return new SimpleStringProperty(memberShipLevel);
        });
        promotionDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
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
    }

    public void setPromotionTableValue() {
        promotionTable.getItems().clear();
        List<Promotion> promotions = promotionPagination.getCurrentPage();
        if (promotions.isEmpty()) {
            Label placeholder = new Label("Không có khuyến mãi");
            placeholder.setStyle("-fx-text-fill: #ccc; -fx-font-size: 24px; -fx-font-weight: bold;");
            promotionTable.setPlaceholder(placeholder);
        } else {
            promotionTable.setItems(FXCollections.observableArrayList(promotions));
        }
        setPageIndexLabel();
    }

    public void setPaginationGetByStatus() {
        String status = filterPromotionStatusComboBox.getValue();
        int itemPerPage = 7;
        int totalPromotions = promotionBUS.countTotalPromotionByStatus(status);
        boolean isRollBack = false;
        promotionPagination = new Pagination<>(
                (offset, limit) -> promotionBUS.getPromotionByStatusWithPagination(offset, limit, status),
                itemPerPage,
                totalPromotions,
                isRollBack
        );
    }

    public void setPaginationSearchById() {
        String id = promotionSearchField.getText();
        int itemPerPage = 7;
        boolean isRollBack = false;
        int totalPromotions = promotionBUS.countTotalPromotionById(id);
        promotionPagination = new Pagination<>(
                (offset, limit) -> promotionBUS.getPromotionByIdWithPagination(offset, limit, id),
                itemPerPage,
                totalPromotions,
                isRollBack
        );
    }

    public void setPageIndexLabel() {
        int currentPageIndex = promotionPagination.getCurrentPageIndex();
        int totalPage = promotionPagination.getTotalPages() == 0 ? 1 : promotionPagination.getTotalPages();
        pageIndexLabel.setText(currentPageIndex + "/" + totalPage);
    }

    public void setPromotionDataToForm(Promotion promotion) {
        promotionNameField.setText(promotion.getName());
//        promotionStatusComboBox.getSelectionModel().select(promotion.getStatus());
        double dis = promotion.getDiscount();
        int discount = (int) (dis * 100);

        DecimalFormat priceFormat = new DecimalFormat("#,###");
        String formattedPrice = priceFormat.format(promotion.getMinimumOrderAmount());
        minimumOrderField.setText(formattedPrice);

        promotionDescriptionTextArea.setText(promotion.getDescription());
        discountField.setText(Double.toString(discount));
        endedDateDatePicker.setValue(promotion.getEndDate());
        startedDateDatePicker.setValue(promotion.getStartDate());
        int memberShip = promotion.getMembershipLevel().ordinal();
        String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
        memberShipLevelComboBox.getSelectionModel().select(memberShipLevel);
    }

    public void enableInput() {
        promotionStatusComboBox.setDisable(false);
        minimumOrderField.setDisable(false);
        promotionNameField.setDisable(false);
        discountField.setDisable(false);
        memberShipLevelComboBox.setDisable(false);
        promotionDescriptionTextArea.setDisable(false);
        endedDateDatePicker.setDisable(false);
        startedDateDatePicker.setDisable(false);
    }

    public void disableInput() {
        promotionStatusComboBox.setDisable(true);
        minimumOrderField.setDisable(true);
        promotionNameField.setDisable(true);
        discountField.setDisable(true);
        memberShipLevelComboBox.setDisable(true);
        promotionDescriptionTextArea.setDisable(true);
        endedDateDatePicker.setDisable(true);
        startedDateDatePicker.setDisable(true);
    }

    public void clearPromotionForm() {
        promotionStatusComboBox.getSelectionModel().clearSelection();
        minimumOrderField.clear();
        promotionNameField.clear();
        discountField.clear();
        promotionDescriptionTextArea.clear();
        startedDateDatePicker.setValue(null);
        endedDateDatePicker.setValue(null);
        memberShipLevelComboBox.getSelectionModel().clearSelection();
        promotionTable.getSelectionModel().clearSelection();
    }

    public boolean validatePromotionData() {
        if (promotionNameField.getText().trim().isEmpty()) {
            ToastsMessage.showMessage("Tên khuyến mãi không được để trống", "error");
            return false;
        }
        if (discountField.getText().trim().isEmpty()) {
            ToastsMessage.showMessage("Giảm giá không được để trống", "error");
            return false;
        }
        if (endedDateDatePicker.getValue() == null) {
            ToastsMessage.showMessage("Ngày kết thúc không được để trống", "error");
            return false;
        }
        if (startedDateDatePicker.getValue() == null) {
            ToastsMessage.showMessage("Ngày bắt đầu không được để trống", "error");
            return false;
        }

        if (endedDateDatePicker.getValue().isBefore(startedDateDatePicker.getValue())) {
            ToastsMessage.showMessage("Ngày kết thúc không được trước ngày bắt đầu", "error");
            return false;
        }

        return true;
    }

    public Promotion getPromotionFromForm() {
        System.out.println(memberShipLevelComboBox.getValue());
        String name = promotionNameField.getText();
        LocalDate startDate = startedDateDatePicker.getValue();
        LocalDate endDate = endedDateDatePicker.getValue();
//        String status = promotionStatusComboBox.getSelectionModel().getSelectedItem();
        String description = promotionDescriptionTextArea.getText();
        double minimumOrder = Double.parseDouble(minimumOrderField.getText().replace(",", ""));
        double discount = Double.parseDouble(discountField.getText().replace("%", "")) / 100;
//        int membershipLevel = Utils.toIntMembershipLevel(memberShipLevelComboBox.getValue());
        MembershipLevel membershipLevel = MembershipLevel.valueOf(memberShipLevelComboBox.getValue());


//        return new Promotion(name, startDate, endDate, discount, description, minimumOrder, membershipLevel, status);
        return new Promotion(name, startDate, endDate, discount, description, minimumOrder, membershipLevel, true);
    }

    public void handleEditPromotion() {
        if (!validatePromotionData()) return;
        if (promotionTable.getSelectionModel().getSelectedItem() == null) return;

        Promotion editedPromotion = getPromotionFromForm();
        Promotion selectedPromotion = promotionTable.getSelectionModel().getSelectedItem();
        editedPromotion.setId(selectedPromotion.getId());
        if (promotionBUS.updatePromotion(editedPromotion)) {
            setPromotionTableValue();
            setPageIndexLabel();
            clearPromotionForm();
            ToastsMessage.showMessage("Sửa khuyến mãi thành công", "success");
        } else {
            ToastsMessage.showMessage("Sửa khuyến mãi thất bại", "error");
        }
    }

    public void handleAddPromotion() {
        if (!validatePromotionData()) return;

        Promotion newPromotion = getPromotionFromForm();
        if (newPromotion != null && promotionBUS.addPromotion(newPromotion)) {
            setPromotionTableValue();
            setPageIndexLabel();
            clearPromotionForm();
            ToastsMessage.showMessage("Thêm khuyến mãi thành công", "success");
        } else {
            ToastsMessage.showMessage("Thêm khuyến mãi thất bại", "error");
        }
    }

    @FXML
    void onPromotionTableClicked(MouseEvent mouseEvent) {
        changeHandleButtonModeToEditPromotion();
        Promotion selectedItem = promotionTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            enableInput();
            setPromotionDataToForm(selectedItem);
        }
    }

    @FXML
    void onClearSearchButtonClicked(MouseEvent mouseEvent) {
        promotionSearchField.setText("");
        clearSearchButton.setVisible(false);
        setPaginationGetByStatus();
        setPromotionTableValue();
    }

    @FXML
    void onPromotionSearchFieldKeyReleased(KeyEvent keyEvent) {
        String id = promotionSearchField.getText();
        if (id.isBlank()) {
            clearSearchButton.setVisible(false);
            setPaginationGetByStatus();
            setPromotionTableValue();
        } else {
            clearSearchButton.setVisible(true);
            setPaginationSearchById();
            setPromotionTableValue();
        }
    }

    @FXML
    void onSwapModePromotionButtonClicked(ActionEvent actionEvent) {
        switch (swapModePromotionButton.getText()) {
            case "Thêm" -> {
                promotionNameField.requestFocus();
                changeHandleButtonModeToAddPromotion();
                clearPromotionForm();
                enableInput();
                promotionStatusComboBox.getSelectionModel().select(0);
                promotionStatusComboBox.setDisable(true);
                memberShipLevelComboBox.getSelectionModel().selectFirst();
            }
            case "Sửa" -> {
                disableInput();
                changeHandleButtonModeToEditPromotion();
            }
        }
    }

    @FXML
    void onHandleActionPromotionButtonClicked(ActionEvent actionEvent) {
        if (handleActionPromotionButton.getText().equals("Sửa")) {
            handleEditPromotion();
        } else if (handleActionPromotionButton.getText().equals("Thêm")) {
            handleAddPromotion();
        }
    }

    @FXML
    void onClearPromotionFormButtonClicked(ActionEvent actionEvent) {
        clearPromotionForm();
    }

    @FXML
    public void onFilterPromotionStatusComboBoxSelected(ActionEvent actionEvent) {
        String statusFilter = filterPromotionStatusComboBox.getValue();
        if (statusFilter == null) return;
        promotionPagination.setCurrentPage(
                (offset, limit) -> promotionBUS.getPromotionByStatusWithPagination(offset, limit, statusFilter),
                promotionBUS.countTotalPromotionByStatus(statusFilter)
        );
        setPromotionTableValue();
    }

    @FXML
    void onMinimumOrderFieldKeyReleased(KeyEvent keyEvent) {
        String input = minimumOrderField.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            minimumOrderField.setText(formattedText);
            minimumOrderField.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            minimumOrderField.setText(validInput.toString());
            minimumOrderField.positionCaret(validInput.length());
        }
    }

    @FXML
    void onFirstPageButtonClicked(MouseEvent event) {
        promotionPagination.goToFirstPage();
        setPromotionTableValue();
    }

    @FXML
    void onPrevPageButtonClicked(MouseEvent event) {
        promotionPagination.goToPreviousPage();
        setPromotionTableValue();
    }

    @FXML
    void onNextPageButtonClicked(MouseEvent event) {
        promotionPagination.goToNextPage();
        setPromotionTableValue();
    }

    @FXML
    void onLastPageButtonClicked(MouseEvent event) {
        promotionPagination.goToLastPage();
        setPromotionTableValue();
    }
}
