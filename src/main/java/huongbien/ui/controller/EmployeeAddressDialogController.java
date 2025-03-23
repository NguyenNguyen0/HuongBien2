package com.huongbien.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeAddressDialogController implements Initializable {
    @FXML private TextField addressDetailField;
    @FXML private ComboBox<String> cityComboBox;
    @FXML private ComboBox<String> districtComboBox;
    @FXML private ComboBox<String> wardComboBox;

    //Initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWardComboBoxValue();
        setDistrictComboBoxValue();
        setCityComboBoxValue();
    }

    public void setWardComboBoxValue() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Phường 1",
                "Phường 2", "Phường 3", "Phường 4", "Phường 5", "Phường 6",
                "Phường 7", "Phường 8", "Phường 9", "Phường 10", "Phường 11",
                "Phường 12", "Phường 13", "Phường 14", "Phường 15");
        wardComboBox.setItems(statusList);
        wardComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String s) {
                return "";
            }
        });
//        wardComboBox.getSelectionModel().selectFirst();
    }

    public void setDistrictComboBoxValue() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Quận 1",
                "Quận 3", "Quận 4", "Quận 5", "Quận 6",
                "Quận 7", "Quận 8", "Quận 10", "Quận 11",
                "Quận 12", "Quận Bình Tân", "Quận Bình Thạnh",
                "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình",
                "Quận Tân Phú", "Huyện Bình Chánh", "Huyện Cần Giờ",
                "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Nhà Bè");
        districtComboBox.setItems(statusList);
        districtComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String s) {
                return "";
            }
        });
//        districtComboBox.getSelectionModel().selectFirst();
    }

    public void setCityComboBoxValue() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Thành phố Hồ Chí Minh");
        cityComboBox.setItems(statusList);
        cityComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String s) {
                return "";
            }
        });
        cityComboBox.getSelectionModel().selectFirst();
    }

    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void onExitButtonClicked(MouseEvent event) {
        closeWindow(event);
    }

    @FXML
    void onOverlayClicked(MouseEvent event) {
        closeWindow(event);
    }

    @FXML
    void onClearFormButtonClicked(ActionEvent event) {
        addressDetailField.setText("");
        addressDetailField.requestFocus();
        wardComboBox.getSelectionModel().clearSelection();
        districtComboBox.getSelectionModel().clearSelection();
    }

    private EmployeeManagementController employeeManagementController;
    public void setEmployeeManagementController(EmployeeManagementController employeeManagementController) {
        this.employeeManagementController = employeeManagementController;
    }

    @FXML
    void saveAddressButton(ActionEvent event) {
        String address = addressDetailField.getText() + ", "
                + wardComboBox.getValue() + ", "
                + districtComboBox.getValue() + ", "
                + cityComboBox.getValue();
        System.out.println(address);
        employeeManagementController.employeeAddressField.setText(address);
        //close()
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
