package huongbien.controller;

import huongbien.config.Variable;
import huongbien.dao.remote.IEmployeeDAO;
import huongbien.entity.Employee;
import huongbien.rmi.RMIClient;
import huongbien.service.EmployeeBUS;
import huongbien.util.Converter;
import huongbien.util.Pagination;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeManagementController implements Initializable {
    private final EmployeeBUS employeeBUS;
    private final Image DEFAULT_AVATAR = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/huongbien/icon/mg_employee/user-256px.png")));
    @FXML
    public TextField employeeAddressField;
    @FXML
    public Label pageIndexLabel;
    public TextField employeeWorkingHourField;
    public RestaurantMainManagerController restaurantMainManagerController;
    public RestaurantMainStaffController restaurantMainStaffController;
    @FXML
    private TableColumn<Employee, String> employeeGenderColumn;
    @FXML
    private TableColumn<Employee, String> employeeIdColumn;
    @FXML
    private TableColumn<Employee, String> employeeNameColumn;
    @FXML
    private TableColumn<Employee, String> employeePhoneColumn;
    @FXML
    private TableColumn<Employee, String> employeePositionColumn;
    @FXML
    private TableColumn<Employee, String> employeeStatusColumn;
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private ComboBox<String> employeeStatusComboBox;
    @FXML
    private ComboBox<Employee> employeePositionComboBox;
    @FXML
    private DatePicker employeeBirthdayDatePicker;
    @FXML
    private DatePicker employeeHiredateDatePicker;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private TextField employeeCitizenIdField;
    @FXML
    private ComboBox<Employee> employeeManagerIdComboBox;
    @FXML
    private TextField employeeEmailField;
    @FXML
    private TextField employeeNameField;
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField employeePhoneField;
    @FXML
    private TextField employeeSalaryField;
    @FXML
    private TextField employeeHourlyPayField;
    @FXML
    private ImageView clearSearchButton;
    @FXML
    private ComboBox<String> searchMethodComboBox;
    @FXML
    private TextField employeeSearchField;
    @FXML
    private Button searchEmployeeButton;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private Button employeeClearFormButton;
    @FXML
    private Button fireEmployeeButton;
    @FXML
    private Button handleActionEmployeeButton;
    @FXML
    private Button swapModeEmployeeButton;
    @FXML
    private Button chooseImageButton;
    @FXML
    private Circle employeeAvatarCircle;
    private byte[] employeeImageBytes = null;
    private Pagination<Employee> employeePagination;

    {
        try {
            employeeBUS = new EmployeeBUS();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRestaurantMainManagerController(RestaurantMainManagerController restaurantMainManagerController) {
        this.restaurantMainManagerController = restaurantMainManagerController;
    }

    public void setRestaurantMainStaffController(RestaurantMainStaffController restaurantMainStaffController) {
        this.restaurantMainStaffController = restaurantMainStaffController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearSearchButton.setVisible(false);
        employeeSearchField.setDisable(true);
        setComboBoxValue();
        changeHandleActionButtonToAddEmployee();
        setEmployeePaginationGetStillWorking();
        setEmployeeTable();
        setEmployeeTableValue();
        employeeAvatarCircle.setFill(new ImagePattern(DEFAULT_AVATAR));
    }

    public void setEmployeePaginationGetAll() {
        try {
            int itemPerPage = 10;
            int totalItem = employeeBUS.countAllEmployees();
            boolean isRollBack = false;
            employeePagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return employeeBUS.getAllEmployeesWithPagination(offset, limit);
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

    public void setEmployeePaginationGetStillWorking() {
        try {
            int itemPerPage = 10;
            int totalItem = employeeBUS.countStillWorkingEmployees();
            boolean isRollBack = false;
            employeePagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return employeeBUS.getStillWorkingEmployeesWithPagination(offset, limit);
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

    public void setEmployeePaginationGetByPhone(String phoneNumber) {
        try {
            int itemPerPage = 10;
            int totalItem = employeeBUS.countEmployeesByPhoneNumber(phoneNumber);
            boolean isRollBack = false;
            employeePagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return employeeBUS.getEmployeesByPhoneNumberWithPagination(phoneNumber, offset, limit);
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

    public void setEmployeePaginationGetByName(String name) {
        try {
            int itemPerPage = 10;
            int totalItem = employeeBUS.countEmployeesByName(name);
            boolean isRollBack = false;
            employeePagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return employeeBUS.getEmployeesByNameWithPagination(name, offset, limit);
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

    public void setEmployeePaginationGetByPosition(String name) {
        try {
            int itemPerPage = 10;
            int totalItem = employeeBUS.countEmployeesByPosition(name);
            boolean isRollBack = false;
            employeePagination = new Pagination<>(
                    (offset, limit) -> {
                        try {
                            return employeeBUS.getEmployeesByPositionWithPagination(name, offset, limit);
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

    public void setEmployeeTable() {
        employeeTable.setPlaceholder(new Label("Không có dữ liệu"));

        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeGenderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Utils.toStringGender(cellData.getValue().getGender().ordinal())));
        employeePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        employeePositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        employeeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setEmployeeTableValue() {
        employeeTable.getItems().clear();
        setPageIndexLabel();
        ObservableList<Employee> listEmployee = FXCollections.observableArrayList(employeePagination.getCurrentPage());
        employeeTable.setItems(listEmployee);
    }

    public void setPageIndexLabel() {
        int currentPageIndex = employeePagination.getCurrentPageIndex();
        int totalPage = employeePagination.getTotalPages() == 0 ? 1 : employeePagination.getTotalPages();
        pageIndexLabel.setText(currentPageIndex + "/" + totalPage);
    }

    public void setComboBoxValue() {
        setStatusComboBox();
        setPositionComboBox();
        setSearchMethodComboBox();
        setEmployeeManagerIdComboBox();
    }

    public void setEmployeeManagerIdComboBox() {
        try {
            List<Employee> managers = employeeBUS.getEmployeeByPosition("Quản lý");
            employeeManagerIdComboBox.getItems().add(null);
            employeeManagerIdComboBox.getItems().addAll(managers);
            employeeManagerIdComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Employee employee) {
                    return employee != null ? employee.getId() : "Không có";
                }

                @Override
                public Employee fromString(String string) {
                    return employeeManagerIdComboBox.getItems().stream()
                            .filter(item -> item.getId().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSearchMethodComboBox() {
        ObservableList<String> searchMethodList = FXCollections.observableArrayList(Variable.searchMethods);
        searchMethodComboBox.setItems(searchMethodList);
        searchMethodComboBox.setValue(Variable.searchMethods[0]);
    }

    public void setStatusComboBox() {
        ObservableList<String> statusList = FXCollections.observableArrayList(Variable.statusOptions);
        employeeStatusComboBox.setItems(statusList);
        employeeStatusComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return employeeStatusComboBox.getItems().stream().filter(item -> item.equals(string)).findFirst().orElse(null);
            }
        });
        employeeStatusComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setDisable(item != null && item.equals("Nghỉ việc"));
            }
        });
    }

    public void setPositionComboBox() {
        try {
            IEmployeeDAO employeeDao = RMIClient.getInstance().getEmployeeDAO();
            List<Employee> employeeList = employeeDao.getAll();
            List<Employee> distinctEmployees = new ArrayList<>(employeeList.stream()
                    .filter(e -> e.getPosition() != null && !"Quản lý".equals(e.getPosition()))
                    .collect(Collectors.toMap(Employee::getPosition, e -> e, (e1, e2) -> e1))
                    .values());
            ObservableList<Employee> employees = FXCollections.observableArrayList(distinctEmployees);
            employeePositionComboBox.setItems(employees);
            employeePositionComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Employee employee) {
                    return employee != null ? employee.getPosition() : "";
                }

                @Override
                public Employee fromString(String string) {
                    return employeePositionComboBox.getItems().stream().filter(item -> item.getPosition().equals(string)).findFirst().orElse(null);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearChooserImage() {
        employeeImageBytes = null;
        employeeAvatarCircle.setFill(new ImagePattern(DEFAULT_AVATAR));
    }

    public void clearEmployeeForm() {
        employeeIdField.clear();
        employeeNameField.clear();
        employeeCitizenIdField.clear();
        employeePhoneField.clear();
        employeeEmailField.clear();
        genderGroup.selectToggle(null);
        employeeManagerIdComboBox.getSelectionModel().clearSelection();
        employeeBirthdayDatePicker.setValue(null);
        employeeHourlyPayField.clear();
        employeeSalaryField.clear();
        employeeAddressField.clear();
        employeeHiredateDatePicker.setValue(null);
        employeeWorkingHourField.clear();
        employeeStatusComboBox.getSelectionModel().clearSelection();
        employeePositionComboBox.getSelectionModel().clearSelection();
        employeeTable.getSelectionModel().clearSelection();
        fireEmployeeButton.setVisible(false);
        clearChooserImage();
    }

    public void enableInput() {
        setInputFieldsDisable(false);
    }

    public void disableInput() {
        setInputFieldsDisable(true);
    }

    public void setInputFieldsDisable(boolean disable) {
        employeeNameField.setDisable(disable);
        employeeCitizenIdField.setDisable(disable);
        employeePhoneField.setDisable(disable);
        employeeEmailField.setDisable(disable);
        maleRadioButton.setDisable(disable);
        femaleRadioButton.setDisable(disable);
        employeeWorkingHourField.setDisable(disable);
        employeeEmailField.setDisable(disable);
        employeeBirthdayDatePicker.setDisable(disable);
        employeeHourlyPayField.setDisable(disable);
        employeeHiredateDatePicker.setDisable(disable);
        employeeSalaryField.setDisable(disable);
        employeeAddressField.setDisable(disable);
        employeeManagerIdComboBox.setDisable(disable);
        employeeStatusComboBox.setDisable(disable);
        employeePositionComboBox.setDisable(disable);
        chooseImageButton.setDisable(disable);
    }

    public void changeHandleActionButtonToUpdateEmployee() {
        fireEmployeeButton.setVisible(true);
        setButtonMode(Variable.addMode, Variable.editMode, "#1D557E", "#761D7E");
    }

    public void changeHandleActionButtonToAddEmployee() {
        fireEmployeeButton.setVisible(false);
        employeeHourlyPayField.setText("20000");
        employeeSalaryField.setText("1000000");
        employeeHiredateDatePicker.setValue(LocalDate.now());
        employeeStatusComboBox.setValue("Đang làm");
        setButtonMode(Variable.editMode, Variable.addMode, "#761D7E", "#1D557E");
    }

    public void setButtonMode(String swapModeText, String handleActionText, String swapModeColor, String handleActionColor) {
        swapModeEmployeeButton.setText(swapModeText);
        handleActionEmployeeButton.setText(handleActionText);
        swapModeEmployeeButton.setStyle("-fx-background-color: " + swapModeColor);
        handleActionEmployeeButton.setStyle("-fx-background-color: " + handleActionColor);
    }

    public void formatMoneyField(TextField moneyField) {
        String input = moneyField.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            String formattedText = NumberFormat.getInstance().format(Long.parseLong(input));
            moneyField.setText(formattedText);
            moneyField.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            moneyField.setText(validInput.toString());
            moneyField.positionCaret(validInput.length());
        }
    }

    public void populateEmployeeForm(Employee employee) {
        if (employee.getProfileImage() == null || employee.getProfileImage().length == 0) {
            employeeAvatarCircle.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/huongbien/icon/mg_employee/user-256px.png")))));
        } else {
            employeeAvatarCircle.setFill(new ImagePattern(new Image(Converter.bytesToInputStream(employee.getProfileImage()))));
        }
        employeeIdField.setText(employee.getId());
        employeeNameField.setText(employee.getName());
        employeeCitizenIdField.setText(employee.getCitizenId());
        employeePhoneField.setText(employee.getPhoneNumber());
        employeeEmailField.setText(employee.getEmail());
        employeeManagerIdComboBox.setValue(employee.getManager());
        employeeHourlyPayField.setText(formatMoney(employee.getHourlyPay()));
        employeeSalaryField.setText(formatMoney(employee.getSalary()));
        employeeAddressField.setText(employee.getAddress());
        employeeWorkingHourField.setText(formatMoney(employee.getWorkHours()));
        employeeHiredateDatePicker.setValue(employee.getHireDate());
        employeeStatusComboBox.getSelectionModel().select(employee.getStatus());
        employeePositionComboBox.getSelectionModel().select(employee);
        employeeBirthdayDatePicker.setValue(employee.getBirthday());
        switch (employee.getGender().ordinal()) {
            case 1 -> genderGroup.selectToggle(maleRadioButton);
            case 2 -> genderGroup.selectToggle(femaleRadioButton);
        }
    }

    public boolean validateEmployeeInfo() {
        if (employeeNameField.getText().isEmpty()) {
            employeeNameField.setStyle("-fx-border-color: red");
            ToastsMessage.showMessage("Tên nhân viên không được để trống", "error");
            return false;
        } else {
            employeeNameField.setStyle(null);
        }
        if (employeePhoneField.getText().isEmpty()) {
            employeePhoneField.setStyle("-fx-border-color: red");
            ToastsMessage.showMessage("Số điện thoại không được để trống", "error");
            return false;
        } else {
            employeePhoneField.setStyle(null);
        }
        if (employeePositionComboBox.getValue() == null) {
            employeePositionComboBox.setStyle("-fx-border-color: red");
            ToastsMessage.showMessage("Chức vụ không được để trống", "error");
            return false;
        } else {
            employeePositionComboBox.setStyle(null);
        }
        if (employeeStatusComboBox.getValue() == null) {
            employeeStatusComboBox.setStyle("-fx-border-color: red");
            ToastsMessage.showMessage("Trạng thái không được để trống", "error");
            return false;
        } else {
            employeeStatusComboBox.setStyle(null);
        }
//
        return true;
    }

    public void updateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) return;
        if (!validateEmployeeInfo()) return;

        if (employeeImageBytes == null) {
            employeeImageBytes = selectedEmployee.getProfileImage();
        }
        Employee updatedEmployee = createEmployeeFromForm(selectedEmployee.getId(), selectedEmployee.getWorkHours(), selectedEmployee.getHireDate(), employeeImageBytes);
        try {
            if (employeeBUS.updateEmployeeInfo(updatedEmployee)) {
                setEmployeeTableValue();
                ToastsMessage.showMessage("Cập nhật nhân viên thành công", "success");
            } else {
                ToastsMessage.showMessage("Cập nhật nhân viên thất bại", "error");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEmployee() {
        if (!validateEmployeeInfo()) return;

        Employee newEmployee = createEmployeeFromForm(null, 0, LocalDate.now(), employeeImageBytes);
        try {
            if (employeeBUS.addEmployee(newEmployee)) {
                clearEmployeeForm();
                setEmployeeTableValue();
                ToastsMessage.showMessage("Thêm nhân viên thành công", "success");
            } else {
                ToastsMessage.showMessage("Thêm nhân viên thất bại", "error");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee createEmployeeFromForm(String employeeId, double workHours, LocalDate hireDate, byte[] profileImage) {
        String name = employeeNameField.getText();
        String citizenId = employeeCitizenIdField.getText();
        String phone = employeePhoneField.getText();
        String email = employeeEmailField.getText();
        LocalDate birthDate = employeeBirthdayDatePicker.getValue();
        double hourPay = Converter.parseMoney(employeeHourlyPayField.getText());
        double salary = Converter.parseMoney(employeeSalaryField.getText());
        Employee managerId = employeeManagerIdComboBox.getValue();
        String address = employeeAddressField.getText();
        String status = employeeStatusComboBox.getValue();
        String position = employeePositionComboBox.getValue() != null ? employeePositionComboBox.getValue().getPosition() : employeePositionComboBox.getEditor().getText();
        int gender = 0;
        if (maleRadioButton.isSelected()) {
            gender = 1;
        } else if (femaleRadioButton.isSelected()) {
            gender = 2;
        }
        return new Employee(employeeId, name, phone, citizenId, gender, address, birthDate, email, status, hireDate, position, workHours, hourPay, salary, managerId, profileImage);
    }

    public void clearSearchField(TextField searchField) {
        searchField.clear();
        searchField.requestFocus();
    }

    public String formatMoney(double amount) {
        return new DecimalFormat("#,###").format(amount);
    }

    public void setEmployeeSearchPagination() {
        String selectedMethod = searchMethodComboBox.getValue();
        String searchValue = employeeSearchField.getText();
        switch (selectedMethod) {
            case "Tất cả" -> setEmployeePaginationGetAll();
            case "Còn làm việc" -> setEmployeePaginationGetStillWorking();
            case "Tìm theo tên" -> setEmployeePaginationGetByName(searchValue);
            case "Tìm theo số điện thoại" -> setEmployeePaginationGetByPhone(searchValue);
            case "Tìm theo chức vụ" -> setEmployeePaginationGetByPosition(searchValue);
        }

        setEmployeeTableValue();
    }

    @FXML
    public void onEmployeeTableClicked(MouseEvent event) {
        Employee selectedItem = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            enableInput();
            populateEmployeeForm(selectedItem);
            changeHandleActionButtonToUpdateEmployee();
        }
    }

    @FXML
    public void onClearSearchButtonClicked(MouseEvent event) {
        clearSearchField(employeeSearchField);
        clearSearchButton.setVisible(false);
    }


    @FXML
    public void onSwapModeEmployeeButtonClicked(MouseEvent event) {
        if (swapModeEmployeeButton.getText().equals(Variable.editMode)) {
            disableInput();
            clearChooserImage();
            changeHandleActionButtonToUpdateEmployee();
        } else if (swapModeEmployeeButton.getText().equals(Variable.addMode)) {
            enableInput();
            clearChooserImage();
            clearEmployeeForm();
            changeHandleActionButtonToAddEmployee();
        }
    }

    @FXML
    public void onHandleActionEmployeeButtonClicked(ActionEvent event) throws FileNotFoundException {
        if (handleActionEmployeeButton.getText().equals(Variable.editMode)) {
            updateEmployee();
        } else if (handleActionEmployeeButton.getText().equals(Variable.addMode)) {
            addEmployee();
        }
        clearEmployeeForm();

        if (restaurantMainManagerController != null) {
            restaurantMainManagerController.setDetailUserInfo();
        } else {
            restaurantMainStaffController.setDetailUserInfo();
        }
    }

    @FXML
    public void onEmployeeClearFormButtonClicked(ActionEvent event) {
        clearEmployeeForm();
    }

    @FXML
    public void onFireEmployeeButtonClicked(ActionEvent event) {
        Employee selectedItem = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        try {
            if (employeeBUS.updateEmployeeStatus(selectedItem.getId(), "Nghỉ việc")) {
                ToastsMessage.showMessage("Sa thải nhân viên thành công", "success");
            } else {
                ToastsMessage.showMessage("Sa thải nhân viên thất bại", "error");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        setEmployeeTableValue();
        clearEmployeeForm();
    }

    @FXML
    public void onSearchEmployeeButtonClicked(ActionEvent event) {
        setEmployeeSearchPagination();
    }

    @FXML
    public void onOpenAddressEditingDialogButtonClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/EmployeeAddressDialog.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 1200, 700);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        EmployeeAddressDialogController employeeAddressDialogController = loader.getController();
        employeeAddressDialogController.setEmployeeManagementController(this);
        primaryStage.show();
    }

    @FXML
    public void onEmployeeHourlyPayFieldKeyReleased(KeyEvent event) {
        formatMoneyField(employeeHourlyPayField);
    }

    @FXML
    public void onEmployeeSalaryFieldKeyReleased(KeyEvent event) {
        formatMoneyField(employeeSalaryField);
    }

    @FXML
    public void onImageChooserButtonClicked(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                employeeImageBytes = Converter.fileToBytes(selectedFile);
                Image image = new Image(Converter.bytesToInputStream(employeeImageBytes));
                employeeAvatarCircle.setFill(new ImagePattern(image));
            } catch (IOException e) {
                ToastsMessage.showMessage("Không thể mở ảnh", "error");
            }
        }
    }

    @FXML
    public void onEmployeeSearchFieldKeyReleased(KeyEvent keyEvent) {
        String searchValue = employeeSearchField.getText();
        clearSearchButton.setVisible(!searchValue.isEmpty());
        setEmployeeSearchPagination();
    }

    @FXML
    public void onSearchMethodComboBoxSelected(ActionEvent actionEvent) {
        String selectedMethod = searchMethodComboBox.getValue();
        switch (selectedMethod) {
            case "Còn làm việc", "Tất cả":
                employeeSearchField.setDisable(true);
                setEmployeeSearchPagination();
                break;
            case "Tìm theo tên", "Tìm theo số điện thoại", "Tìm theo chức vụ":
                employeeSearchField.setDisable(false);
                break;
        }
    }

    @FXML
    public void onFirstPageButtonClicked(MouseEvent mouseEvent) {
        employeePagination.goToFirstPage();
        setEmployeeTableValue();
    }

    @FXML
    public void onPrevPageButtonClicked(MouseEvent mouseEvent) {
        employeePagination.goToPreviousPage();
        setEmployeeTableValue();
    }

    @FXML
    public void onNextPageButtonClicked(MouseEvent mouseEvent) {
        employeePagination.goToNextPage();
        setEmployeeTableValue();
    }

    @FXML
    public void onLastPageButtonClicked(MouseEvent mouseEvent) {
        employeePagination.goToLastPage();
        setEmployeeTableValue();
    }
}