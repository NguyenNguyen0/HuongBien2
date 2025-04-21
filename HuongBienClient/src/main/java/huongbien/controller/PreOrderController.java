package huongbien.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.config.AppConfig;
import huongbien.config.Constants;
import huongbien.config.Variable;
import huongbien.dao.remote.ICustomerDAO;
import huongbien.dao.remote.IEmployeeDAO;
import huongbien.dao.remote.IReservationDAO;
import huongbien.dao.remote.ITableDAO;
import huongbien.entity.*;
import huongbien.rmi.RMIClient;
import huongbien.service.EmailService;
import huongbien.service.QRCodeHandler;
import huongbien.service.ReservationBUS;
import huongbien.service.TableBUS;
import huongbien.util.ClearJSON;
import huongbien.util.Converter;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PreOrderController implements Initializable {
    static {
        try {
            System.load(System.getProperty("user.dir") + "/libs/native/opencv_java451.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    @FXML
    public Label tableFeeLabel;
    //Controller area
    public RestaurantMainManagerController restaurantMainManagerController;
    public RestaurantMainStaffController restaurantMainStaffController;
    @FXML
    private Label reservationIDLabel;
    @FXML
    private TextField numOfAttendeesField;
    @FXML
    private ComboBox<String> hourComboBox;
    @FXML
    private ComboBox<String> minuteComboBox;
    @FXML
    private TextField tableInfoField;
    @FXML
    private Label tableAmountLabel;
    @FXML
    private Label cuisineAmountLabel;
    @FXML
    private Label totalAmoutLabel;
    @FXML
    private Label preOrderCuisineLabel;
    @FXML
    private DatePicker receiveDatePicker;
    @FXML
    private ComboBox<String> partyTypeComboBox;
    @FXML
    private TextField phoneNumField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField customerIDField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField noteField;
    private VideoCapture capture;
    private Timer timer;
    private QRCodeHandler qrCodeHandler;
    private ITableDAO tableDAO;
    private ICustomerDAO customerDAO;
    private IReservationDAO reservationDAO;
    private IEmployeeDAO employeeDAO;

    public void setRestaurantMainManagerController(RestaurantMainManagerController restaurantMainManagerController) {
        this.restaurantMainManagerController = restaurantMainManagerController;
    }

    public void setRestaurantMainStaffController(RestaurantMainStaffController restaurantMainStaffController) {
        this.restaurantMainStaffController = restaurantMainStaffController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tableDAO = RMIClient.getInstance().getTableDAO();
            customerDAO = RMIClient.getInstance().getCustomerDAO();
            reservationDAO = RMIClient.getInstance().getReservationDAO();
            employeeDAO = RMIClient.getInstance().getEmployeeDAO();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        try {
            this.qrCodeHandler = new QRCodeHandler();
            setInfoPreOrder();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //function area
    private void setTimeComboBox() {
        hourComboBox.getItems().clear();
        for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(String.format("%02d", i));
        }
        minuteComboBox.getItems().clear();
        for (int i = 0; i < 60; i++) {
            minuteComboBox.getItems().add(String.format("%02d", i));
        }
        hourComboBox.setValue(String.format("%02d", LocalTime.now().getHour()));
        minuteComboBox.setValue(String.format("%02d", LocalTime.now().getMinute()));
    }

    private void setInfoPreOrder() throws FileNotFoundException {
        tableFeeLabel.setText(tableFeeLabel.getText() + Converter.formatMoney(Variable.tableVipPrice) + " VNĐ");
        receiveDatePicker.setValue(LocalDate.now());
        setTimeComboBox();
        ObservableList<String> partyTypes = FXCollections.observableArrayList(Variable.partyTypesArray);
        partyTypeComboBox.setItems(partyTypes);
        partyTypeComboBox.getSelectionModel().selectLast();
        //get table info from json file
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayCustomer = Utils.readJsonFromFile(Constants.CUSTOMER_PATH);
        JsonArray jsonArrayReservation = Utils.readJsonFromFile(Constants.RESERVATION_PATH);
        //table
        StringBuilder tableInfoBuilder = new StringBuilder();
        double tableAmount = 0;
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            try {
                Table table = tableDAO.getById(id);
                if (table != null) {
                    //set table text
                    String floorStr = (table.getFloor() == 0) ? "Tầng trệt" : "Tầng " + table.getFloor();
                    tableInfoBuilder.append(table.getName()).append(" (").append(floorStr).append("), ");
                } else {
                    tableInfoBuilder.append("Thông tin bàn không xác định, ");
                }
                //calculate table fee
                assert table != null;
                tableAmount += (table.getTableType().getId().equals(Variable.tableVipID)) ? Variable.tableVipPrice : 0;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        if (!tableInfoBuilder.isEmpty()) {
            tableInfoBuilder.setLength(tableInfoBuilder.length() - 2);
        }
        //Cuisine
        int cuisineQuantity = 0;
        double cuisineAmount = 0;
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();
            cuisineQuantity += quantity;
            cuisineAmount += money;
        }
        //customer
        for (JsonElement element : jsonArrayCustomer) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Customer ID").getAsString();
            try {
                Customer customer = customerDAO.getById(id);
                if (customer != null) {
                    customerIDField.setText(customer.getId());
                    nameField.setText(customer.getName());
                    phoneNumField.setText(customer.getPhoneNumber());
                    emailField.setText(customer.getEmail() == null ? "" : customer.getEmail());
                } else {
                    customerIDField.setText("");
                    nameField.setText("");
                    phoneNumField.setText("");
                    emailField.setText("");
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        for (JsonElement element : jsonArrayReservation) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.has("Reservation ID") ? jsonObject.get("Reservation ID").getAsString() : "";
            try {
                Reservation reservation = reservationDAO.getById(id);
                if (reservation != null) {
                    reservationIDLabel.setText("Mã đặt bàn: " + reservation.getId());
                    hourComboBox.setValue((reservation.getReceiveTime() != null) ? String.format("%02d", reservation.getReceiveTime().getHour()) : "23");
                    minuteComboBox.setValue((reservation.getReceiveTime() != null) ? String.format("%02d", reservation.getReceiveTime().getMinute()) : "55");
                    numOfAttendeesField.setText((reservation.getPartySize() != 0) ? String.valueOf(reservation.getPartySize()) : "1");
                    receiveDatePicker.setValue((reservation.getReceiveDate() != null) ? reservation.getReceiveDate() : LocalDate.now());
                    noteField.setText((reservation.getNote() != null) ? reservation.getNote() : "");
                    partyTypeComboBox.getSelectionModel().select(reservation.getPartyType() != null ? reservation.getPartyType() : "Khác...");
                }
                break;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        //set info to reservation
        tableInfoField.setText(tableInfoBuilder.toString());
        preOrderCuisineLabel.setText(cuisineQuantity + " món");
        tableAmountLabel.setText(String.format("%,.0f VNĐ", tableAmount));
        cuisineAmountLabel.setText(String.format("%,.0f VNĐ", cuisineAmount));
        totalAmoutLabel.setText(String.format("%,.0f VNĐ", tableAmount + cuisineAmount));
    }

    private void openSwingWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cửa sổ quét mã QR");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(640, 480);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);
            JLabel cameraLabel = new JLabel();
            frame.add(cameraLabel, BorderLayout.CENTER);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    stopCamera();
                }
            });

            frame.setVisible(true);
            readQRCode(cameraLabel, frame);
        });
    }

    private void readQRCode(JLabel cameraLabel, JFrame frame) {
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            ToastsMessage.showMessage("Không thể mở camera!", "error");
            return;
        }

        timer = new Timer(100, e -> {
            Mat frameMat = new Mat();
            if (capture != null && capture.read(frameMat)) {
                if (!frameMat.empty()) {
                    BufferedImage bufferedImage = qrCodeHandler.matToBufferedImage(frameMat);
                    String qrCodeContent = qrCodeHandler.decodeQRCode(bufferedImage);
                    if (qrCodeContent != null) {
                        try {
                            updateCustomerFields(qrCodeContent);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        stopCamera();
                        capture.release();
                        cameraLabel.setIcon(null);
                        frame.dispose();
                    } else {
                        ImageIcon icon = new ImageIcon(bufferedImage);
                        cameraLabel.setIcon(icon);
                    }
                }
            } else {
                ToastsMessage.showMessage("Không thể đọc frame từ camera.", "error");
            }
        });

        timer.start();
    }

    private void stopCamera() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (capture != null && capture.isOpened()) {
            capture.release();
        }
    }

    private void updateCustomerFields(String qrCodeContent) throws SQLException, FileNotFoundException {
        String[] parts = qrCodeContent.split(",");
        if (parts.length >= 4) {
            Platform.runLater(() -> {
                try {
                    Customer customer = customerDAO.getById(parts[0]);
                    customerIDField.setText(customer.getId() == null ? "" : customer.getId());
                    nameField.setText(customer.getName() == null ? "" : customer.getName());
                    phoneNumField.setText(customer.getPhoneNumber() == null ? "" : customer.getPhoneNumber());
                    emailField.setText(customer.getEmail() == null ? "" : customer.getEmail());

                    JsonArray jsonArray = new JsonArray();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Customer ID", parts[0]);
                    jsonArray.add(jsonObject);
                    Utils.writeJsonToFile(jsonArray, Constants.CUSTOMER_PATH);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @FXML
    void onBackButtonAction(ActionEvent event) throws IOException {
        if (restaurantMainManagerController != null) {
            restaurantMainManagerController.openOrderTable();
        } else {
            restaurantMainStaffController.openOrderTable();
        }
    }

    @FXML
    void onQRButtonAction(ActionEvent event) throws IOException {
        openSwingWindow();
    }

    @FXML
    void onReservationManagementButtonAction(ActionEvent event) throws IOException {
        if (restaurantMainManagerController != null) {
            restaurantMainManagerController.openReservationManagement();
        } else {
            restaurantMainStaffController.openReservationManagement();
        }
    }

    @FXML
    void onDecreaseButtonAction(ActionEvent event) {
        int currentValue = Integer.parseInt(numOfAttendeesField.getText());
        if (currentValue > 1) {
            numOfAttendeesField.setText(String.valueOf(currentValue - 1));
        }
    }

    @FXML
    void onIncreaseButtonAction(ActionEvent event) {
        int currentValue = Integer.parseInt(numOfAttendeesField.getText());
        numOfAttendeesField.setText(String.valueOf(currentValue + 1));
    }

    @FXML
    void onEditTableButtonAction(ActionEvent event) throws IOException {
        if (restaurantMainManagerController != null) {
            restaurantMainManagerController.openOrderTable();
        } else {
            restaurantMainStaffController.openOrderTable();
        }
    }

    @FXML
    void onPreOrderCuisineButtonAction(ActionEvent event) throws IOException {
        if (restaurantMainManagerController != null) {
            restaurantMainManagerController.openPreOrderCuisine();
        } else {
            restaurantMainStaffController.openPreOrderCuisine();
        }
    }

    @FXML
    void onSearchCustomerExisKeyType(KeyEvent event) {
        String phone = phoneNumField.getText();
        if (!phone.matches("\\d*")) {
            phoneNumField.setText(phone.replaceAll("[^\\d]", ""));
            phoneNumField.positionCaret(phoneNumField.getText().length());
            ToastsMessage.showMessage("Chỉ nhập số, vui lòng không nhập ký tự khác", "warning");
            return;
        }

        if (phone.length() > 10) {
            ToastsMessage.showMessage("Số điện thoại không hợp lệ, phải bao gồm 10 số", "warning");
            phoneNumField.setText(phone.substring(0, 10));
            phoneNumField.positionCaret(10);
            return;
        }

        if (phone.length() < 10) {
            customerIDField.setText("");
            nameField.setText("");
            emailField.setText("");
        } else {
            try {
                Customer customer = customerDAO.getByPhoneNumber(phone);
                if (customer != null) {
                    customerIDField.setText(customer.getId());
                    nameField.setText(customer.getName());
                    emailField.setText(customer.getEmail() == null ? "" : customer.getEmail());
                    //Write JSON
                    JsonArray jsonArray = new JsonArray();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Customer ID", customer.getId());
                    jsonArray.add(jsonObject);
                    Utils.writeJsonToFile(jsonArray, Constants.CUSTOMER_PATH);
                } else {
                    ToastsMessage.showMessage("Không tìm thấy khách hàng, nhập thông tin khách hàng để đăng ký mới", "warning");
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void onSavePreOrderTableButtonAction(ActionEvent event) throws IOException {
        JsonArray jsonArrayReservation = Utils.readJsonFromFile(Constants.RESERVATION_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayEmployee = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);

        if (phoneNumField.getText().isEmpty()) {
            ToastsMessage.showMessage("Vui lòng nhập số điện thoại để kiểm tra khách hàng", "warning");
            return;
        }

        if (phoneNumField.getText().length() != 10) {
            ToastsMessage.showMessage("Số điện thoại không hợp lệ, phải bao gồm 10 số", "warning");
            return;
        }

        if (nameField.getText().isEmpty() || phoneNumField.getText().isEmpty()) {
            ToastsMessage.showMessage("Vui lòng nhập đầy đủ thông tin khách hàng để thực hiện đăng ký", "warning");
            return;
        }

        if (customerIDField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Thông báo");
            alert.setContentText("Không tìm thấy khách hàng trong hệ thống, bạn có muốn đăng ký khách hàng mới không?");
            ButtonType btn_ok = new ButtonType("Đăng ký");
            ButtonType btn_cancel = new ButtonType("Không");
            alert.getButtonTypes().setAll(btn_ok, btn_cancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == btn_ok) {
                String phone = phoneNumField.getText();
                String name = nameField.getText();
                String email = emailField.getText().isEmpty() ? null : emailField.getText();
                customerDAO.add(new Customer(name, null, Gender.OTHER, phone, email, null));
                Customer customer = customerDAO.getByPhoneNumber(phone);
                customerIDField.setText(customer.getId());
                ToastsMessage.showMessage("Đăng ký khách hàng mới thành công, nhấn LƯU để tạo đơn đặt mới", "success");
                return;
            }
        }

        if (jsonArrayTable.isEmpty()) {
            ToastsMessage.showMessage("Vui lòng chọn ít nhất 1 bàn", "warning");
            return;
        }

        Reservation reservation = new Reservation();

        String reservationID = null;
        for (JsonElement element : jsonArrayReservation) {
            JsonObject jsonObject = element.getAsJsonObject();
            reservationID = jsonObject.has("Reservation ID") ? jsonObject.get("Reservation ID").getAsString() : null;
            break;
        }

        String employeeID = null;
        for (JsonElement element : jsonArrayEmployee) {
            JsonObject jsonObject = element.getAsJsonObject();
            employeeID = jsonObject.get("Employee ID").getAsString();
            break;
        }

        String customerID = customerIDField.getText();
        LocalDate receiveDate = receiveDatePicker.getValue();
        LocalTime receiveTime = LocalTime.of(Integer.parseInt(hourComboBox.getValue()), Integer.parseInt(minuteComboBox.getValue()));
        int partySize = Integer.parseInt(numOfAttendeesField.getText());
        String note = noteField.getText();
        String partyType = partyTypeComboBox.getValue();
        Customer customer = customerDAO.getById(customerID);
        Employee employee = employeeDAO.getOneById(employeeID);
        double deposit = Double.parseDouble(
                totalAmoutLabel.getText()
                        .replaceAll(",", "")
                        .replaceAll(" VNĐ", "")
        );

        // Tạo ID mới cho reservation nếu là đơn đặt mới
        if (reservationID == null) {
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            reservationID = Reservation.generateId(currentDate, currentTime);
        }

        reservation.setId(reservationID);
        reservation.setPartyType(partyType);
        reservation.setPartySize(partySize);
        reservation.setReservationDate(LocalDate.now());
        reservation.setReservationTime(LocalTime.now());
        reservation.setReceiveDate(receiveDate);
        reservation.setReceiveTime(receiveTime);
        reservation.setDeposit(deposit);
        reservation.setNote(note);
        reservation.setEmployee(employee);
        reservation.setCustomer(customer);

        // Sửa đổi: Load các đối tượng Table từ database thay vì tạo mới
        ArrayList<Table> tables = new ArrayList<>();
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String tableID = jsonObject.get("Table ID").getAsString();
            // Load đối tượng Table từ database
            Table table = tableDAO.getById(tableID);
            if (table != null) {
                tables.add(table);
            } else {
                ToastsMessage.showMessage("Không tìm thấy bàn với ID: " + tableID + ", vui lòng thử lại", "error");
                return;
            }
        }

        // Sửa đổi: Tạo FoodOrder với đúng entitys từ database và gán ID
        ArrayList<FoodOrder> foodOrders = new ArrayList<>();
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();

            String cuisineID = jsonObject.get("Cuisine ID").getAsString();
            double cuisinePrice = jsonObject.get("Cuisine Price").getAsDouble();
            String cuisineNote = jsonObject.get("Cuisine Note").getAsString();
            int cuisineQuantity = jsonObject.get("Cuisine Quantity").getAsInt();

            FoodOrder foodOrder = new FoodOrder();
            // Tạo ID cho FoodOrder ngay tại đây
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            String foodOrderId = String.format("FD%02d%02d%02d%02d%02d%02d%03d",
                    currentDate.getYear() % 100,
                    currentDate.getMonthValue(),
                    currentDate.getDayOfMonth(),
                    currentTime.getHour(),
                    currentTime.getMinute(),
                    currentTime.getSecond(),
                    (int) (Math.random() * 999));
            foodOrder.setId(foodOrderId);
            foodOrder.setQuantity(cuisineQuantity);
            foodOrder.setNote(cuisineNote);
            foodOrder.setSalePrice(cuisinePrice);

            // Load đối tượng Cuisine từ database
            try {
                Cuisine cuisine = RMIClient.getInstance().getCuisineDAO().getById(cuisineID);
                if (cuisine != null) {
                    foodOrder.setCuisine(cuisine);
                } else {
                    ToastsMessage.showMessage("Không tìm thấy món ăn với ID: " + cuisineID + ", vui lòng thử lại", "error");
                    return;
                }
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }

            foodOrders.add(foodOrder);
        }

        reservation.setTables(tables);
        reservation.setFoodOrders(foodOrders);

        ReservationBUS reservationBUS = new ReservationBUS();
        // Thay đổi kiểm tra để tránh dựa vào reservationDAO.getById()
        boolean isNewReservation = reservationID == null ||
                (reservationID != null && !isReservationExist(reservationID));

        if (isNewReservation) {
            //Check receive time is valid
            LocalDateTime receiveDateTime = LocalDateTime.of(receiveDatePicker.getValue(), LocalTime.of(Integer.parseInt(hourComboBox.getValue()), Integer.parseInt(minuteComboBox.getValue())));
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (receiveDateTime.isBefore(currentDateTime)) {
                ToastsMessage.showMessage("Thời gian nhận phải lớn hơn thời gian hiện tại", "warning");
                return;
            }

            reservation.setStatus(ReservationStatus.PENDING);
            Payment payment = new Payment(deposit, PaymentMethod.CASH); //Default payment method is cash
            reservation.setPayment(payment);

            if (reservationBUS.addReservation(reservation)) {
                TableBUS tableBUS = new TableBUS();
                //Update status table
                for (Table table : tables) {
                    tableBUS.updateStatusTable(table.getId(), TableStatus.RESERVED);
                }
                String tableInfo = "Không xác định";

                if (!jsonArrayTable.isEmpty()) {
                    JsonObject tableObject = jsonArrayTable.get(0).getAsJsonObject();
                    tableInfo = tableObject.get("Table ID").getAsString();
                }

                String pickupTime = receiveDate.toString() + " " + receiveTime.toString();

                String htmlContent = String.format("""
                                    <html>
                                    <head>
                                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                        <style>
                                            @import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;600;700&display=swap');
                                
                                            :root {
                                                --primary: #0277bd;
                                                --primary-light: #58a5f0;
                                                --primary-dark: #004c8c;
                                                --accent: #26a69a;
                                                --text-on-light: #37474f;
                                                --text-on-dark: #ffffff;
                                                --background-light: #ffffff;
                                                --background-gray: #f5f7fa;
                                                --warning: #e53935;
                                            }
                                
                                            * {
                                                margin: 0;
                                                padding: 0;
                                                box-sizing: border-box;
                                            }
                                
                                            body {
                                                font-family: 'Montserrat', Arial, sans-serif;
                                                line-height: 1.6;
                                                color: var(--text-on-light);
                                                background-color: #e1f5fe;
                                                margin: 0;
                                                padding: 0;
                                            }
                                
                                            .email-wrapper {
                                                max-width: 650px;
                                                margin: 0 auto;
                                                background: linear-gradient(180deg, #e3f2fd 0%%, #ffffff 100%%);
                                                border-radius: 16px;
                                                overflow: hidden;
                                                box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
                                                border: 1px solid #e0e0e0;
                                            }
                                
                                            .email-header {
                                                position: relative;
                                                text-align: center;
                                                padding: 0;
                                                background: linear-gradient(135deg, var(--primary-dark) 0%%, var(--primary) 100%%);
                                            }
                                
                                            .email-header img {
                                                width: 100%%;
                                                max-width: 100%%;
                                                display: block;
                                            }
                                
                                            .wave-divider {
                                                position: absolute;
                                                bottom: -2px;
                                                left: 0;
                                                width: 100%%;
                                                overflow: hidden;
                                                line-height: 0;
                                            }
                                
                                            .wave-divider svg {
                                                display: block;
                                                width: calc(100%% + 1.3px);
                                                height: 46px;
                                            }
                                
                                            .wave-divider .shape-fill {
                                                fill: var(--background-light);
                                            }
                                
                                            .email-body {
                                                padding: 40px 50px;
                                                text-align: center;
                                                background-color: var(--background-light);
                                            }
                                
                                            .greeting {
                                                color: var(--primary);
                                                font-size: 28px;
                                                font-weight: 700;
                                                margin-bottom: 25px;
                                                letter-spacing: -0.5px;
                                            }
                                
                                            .message {
                                                font-size: 16px;
                                                line-height: 1.7;
                                                margin-bottom: 25px;
                                                color: var(--text-on-light);
                                            }
                                
                                            .highlight {
                                                color: var(--primary);
                                                font-weight: 600;
                                            }
                                
                                            .accent {
                                                color: var(--accent);
                                                font-weight: 600;
                                            }
                                
                                            .reservation-container {
                                                margin: 35px auto;
                                                max-width: 500px;
                                                position: relative;
                                            }
                                
                                            .reservation-box {
                                                background: var(--background-gray);
                                                border-radius: 12px;
                                                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
                                                padding: 30px;
                                                position: relative;
                                                border-left: 4px solid var(--accent);
                                            }
                                
                                            .reservation-label {
                                                font-size: 14px;
                                                font-weight: 600;
                                                text-transform: uppercase;
                                                color: var(--accent);
                                                letter-spacing: 1.5px;
                                                margin-bottom: 20px;
                                                text-align: center;
                                            }
                                
                                            .section-title {
                                                font-size: 16px;
                                                font-weight: 600;
                                                color: var(--primary);
                                                margin: 20px 0 10px;
                                                text-align: left;
                                                border-bottom: 1px solid rgba(0,0,0,0.07);
                                                padding-bottom: 5px;
                                            }
                                
                                            .info-table {
                                                width: 100%%;
                                                border-collapse: collapse;
                                                margin: 15px 0;
                                            }
                                
                                            .info-table td {
                                                padding: 8px 5px;
                                                text-align: left;
                                                border-bottom: 1px solid rgba(0,0,0,0.03);
                                            }
                                
                                            .info-table td:first-child {
                                                font-weight: 500;
                                                width: 40%%;
                                                color: var(--text-on-light);
                                                opacity: 0.85;
                                            }
                                
                                            .info-table td:last-child {
                                                font-weight: 600;
                                                color: var(--primary);
                                            }
                                
                                            .time-highlight {
                                                background: linear-gradient(135deg, var(--primary) 0%%, var(--accent) 100%%);
                                                color: white;
                                                padding: 5px 15px;
                                                border-radius: 30px;
                                                font-size: 16px;
                                                font-weight: 600;
                                                display: inline-block;
                                            }
                                
                                            .cancellation-box {
                                                background-color: rgba(2, 119, 189, 0.05);
                                                border-left: 3px solid var(--primary);
                                                padding: 15px;
                                                margin: 25px 0;
                                                text-align: left;
                                                border-radius: 4px;
                                            }
                                
                                            .cancellation-title {
                                                color: var(--primary);
                                                font-weight: 600;
                                                font-size: 15px;
                                                margin-bottom: 8px;
                                                display: flex;
                                                align-items: center;
                                                gap: 6px;
                                            }
                                
                                            .cancellation-content {
                                                font-size: 14px;
                                                color: var(--text-on-light);
                                                margin: 12px 0 0 0;
                                                padding-left: 0;
                                            }
                                
                                            .cancellation-content li {
                                                margin-bottom: 6px;
                                                list-style-type: none;
                                                position: relative;
                                                padding-left: 20px;
                                            }
                                
                                            .cancellation-content li:before {
                                                content: "•";
                                                color: var(--primary);
                                                font-weight: bold;
                                                display: inline-block;
                                                position: absolute;
                                                left: 0;
                                            }
                                
                                            .divider {
                                                height: 1px;
                                                background: linear-gradient(90deg, rgba(0,0,0,0) 0%%, rgba(0,0,0,0.1) 50%%, rgba(0,0,0,0) 100%%);
                                                margin: 30px 0;
                                            }
                                
                                            .confirmation-badge {
                                                display: inline-block;
                                                background-color: #4caf50;
                                                color: white;
                                                font-weight: 600;
                                                padding: 8px 20px;
                                                border-radius: 30px;
                                                margin: 10px 0;
                                                font-size: 15px;
                                            }
                                
                                            .deposit-info {
                                                margin: 20px 0;
                                                padding: 15px;
                                                background: linear-gradient(135deg, rgba(38, 166, 154, 0.08) 0%%, rgba(2, 119, 189, 0.08) 100%%);
                                                border-radius: 8px;
                                                text-align: center;
                                            }
                                
                                            .deposit-amount {
                                                font-size: 24px;
                                                font-weight: 700;
                                                color: var(--primary);
                                                margin: 10px 0;
                                            }
                                
                                            .email-footer {
                                                background-color: var(--primary-dark);
                                                color: var(--text-on-dark);
                                                text-align: center;
                                                padding: 30px;
                                                border-bottom-left-radius: 16px;
                                                border-bottom-right-radius: 16px;
                                            }
                                
                                            .footer-logo {
                                                font-size: 20px;
                                                font-weight: 700;
                                                letter-spacing: 1px;
                                                margin-bottom: 15px;
                                                color: white;
                                            }
                                
                                            .footer-info {
                                                font-size: 13px;
                                                color: rgba(255, 255, 255, 0.8);
                                                margin-bottom: 8px;
                                            }
                                
                                            .footer-contact {
                                                margin-top: 15px;
                                            }
                                
                                            .contact-link {
                                                display: inline-block;
                                                color: white;
                                                text-decoration: none;
                                                margin: 0 10px;
                                                font-size: 12px;
                                                transition: all 0.2s;
                                            }
                                
                                            .contact-link:hover {
                                                color: var(--accent);
                                            }
                                
                                            @media only screen and (max-width: 600px) {
                                                .email-body {
                                                    padding: 30px 20px;
                                                }
                                
                                                .greeting {
                                                    font-size: 24px;
                                                }
                                
                                                .reservation-box {
                                                    padding: 20px;
                                                }
                                
                                                .info-table td {
                                                    display: block;
                                                    width: 100%%;
                                                }
                                
                                                .info-table td:first-child {
                                                    margin-top: 10px;
                                                    padding-bottom: 0;
                                                }
                                            }
                                        </style>
                                    </head>
                                    <body>
                                        <div class="email-wrapper">
                                            <div class="email-header">
                                                <h1>NHÀ HÀNG HƯƠNG BIỂN</h1>
                                                <div class="wave-divider">
                                                    <svg data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 120" preserveAspectRatio="none">
                                                        <path d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z" opacity=".25" class="shape-fill"></path>
                                                        <path d="M0,0V15.81C13,36.92,27.64,56.86,47.69,72.05,99.41,111.27,165,111,224.58,91.58c31.15-10.15,60.09-26.07,89.67-39.8,40.92-19,84.73-46,130.83-49.67,36.26-2.85,70.9,9.42,98.6,31.56,31.77,25.39,62.32,62,103.63,73,40.44,10.79,81.35-6.69,119.13-24.28s75.16-39,116.92-43.05c59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z" opacity=".5" class="shape-fill"></path>
                                                        <path d="M0,0V5.63C149.93,59,314.09,71.32,475.83,42.57c43-7.64,84.23-20.12,127.61-26.46,59-8.63,112.48,12.24,165.56,35.4C827.93,77.22,886,95.24,951.2,90c86.53-7,172.46-45.71,248.8-84.81V0Z" class="shape-fill"></path>
                                                    </svg>
                                                </div>
                                            </div>
                                
                                            <div class="email-body">
                                                <h1 class="greeting">Xác nhận đặt bàn thành công!</h1>
                                                <p class="message">Cảm ơn quý khách <span class="highlight">%s</span> đã sử dụng dịch vụ đặt bàn tại <span class="highlight">Nhà Hàng Hương Biển</span>.</p>
                                
                                                <div class="confirmation-badge">✓ Đơn đặt bàn đã được xác nhận</div>
                                
                                                <div class="reservation-container">
                                                    <div class="reservation-box">
                                                        <div class="reservation-label">Thông tin đặt bàn</div>
                                
                                                        <div class="section-title">Chi tiết bàn</div>
                                                        <table class="info-table">
                                                            <tr>
                                                                <td>Mã đặt bàn:</td>
                                                                <td>#%s</td>
                                                            </tr>
                                                            <tr>
                                                                <td>Bàn đã đặt:</td>
                                                                <td>%s</td>
                                                            </tr>
                                                            <tr>
                                                                <td>Thời gian nhận:</td>
                                                                <td><span class="time-highlight" style="color: black;">%s</span></td>
                                                            </tr>
                                                        </table>
                                
                                                        <div class="section-title">Thông tin khách hàng</div>
                                                        <table class="info-table">
                                                            <tr>
                                                                <td>Tên khách hàng:</td>
                                                                <td>%s</td>
                                                            </tr>
                                                            <tr>
                                                                <td>Loại khách:</td>
                                                                <td>%s</td>
                                                            </tr>
                                                            <tr>
                                                                <td>Số khách:</td>
                                                                <td>%s người</td>
                                                            </tr>
                                                            <tr>
                                                                <td>Ghi chú:</td>
                                                                <td>%s</td>
                                                            </tr>
                                                        </table>
                                
                                                        <div class="deposit-info">
                                                            <div class="section-title" style="text-align: center; border-bottom: none;">Thông tin đặt cọc</div>
                                                            <div class="deposit-amount">%,.0f VNĐ</div>
                                                        </div>
                                                    </div>
                                                </div>
                                
                                                <div class="cancellation-box">
                                                    <div class="cancellation-title">
                                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                            <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" stroke="#0277bd" stroke-width="2"/>
                                                            <path d="M12 8V12" stroke="#0277bd" stroke-width="2" stroke-linecap="round"/>
                                                            <path d="M12 16H12.01" stroke="#0277bd" stroke-width="2" stroke-linecap="round"/>
                                                        </svg>
                                                        Chính sách phí hủy đặt bàn
                                                    </div>
                                                    <ul class="cancellation-content">
                                                        <li>Hủy trước 24 giờ: <span class="accent">Miễn phí</span>.</li>
                                                        <li>Hủy trong vòng 24 giờ đến 1 giờ trước giờ đặt: <span class="accent">Phí hủy bằng 50 phần trăm số tiền cọc</span>.</li>
                                                        <li>Hủy trong vòng 1 giờ hoặc không đến: <span class="accent">Phí hủy bằng toàn bộ số tiền cọc</span>.</li>
                                                        <li>Đơn đặt bàn sẽ tự động hủy sau 20 phút nếu quý khách không đến nhận bàn đúng giờ.</li>
                                                    </ul>
                                                </div>
                                
                                                <div class="divider"></div>
                                
                                                <p class="message">Nếu quý khách có bất kỳ thắc mắc hoặc cần thay đổi đơn đặt bàn, vui lòng liên hệ với chúng tôi qua số điện thoại <span class="highlight">0123 456 789</span> hoặc phản hồi email này.</p>
                                            </div>
                                        </div>
                                    </body>
                                    </html>
                                """,
                        customer.getName(),
                        reservation.getId() != null ? reservation.getId() : "Chưa có mã",
                        tableInfo,
                        pickupTime,
                        customer.getName(),
                        partyType,
                        partySize,
                        note.isEmpty() ? "Không có" : note,
                        deposit);

                EmailService.sendEmailWithReservation(customer.getEmail(), "Thông tin đặt trước", htmlContent, AppConfig.getEmailUsername(), AppConfig.getEmailPassword());
                ToastsMessage.showMessage("Tạo đơn đặt trước thành công", "success");
            } else {
                ToastsMessage.showMessage("Tạo đơn đặt trước thất bại, vui lòng thử lại", "error");
            }
        } else {
            //Temporary solution for updating reservation
            Reservation reservationUpdate = reservationDAO.getById(reservationID);
            reservation.setStatus(reservationUpdate != null ? reservationUpdate.getStatus() : ReservationStatus.PENDING);
            reservation.setPayment(reservationUpdate != null ? reservationUpdate.getPayment() : null);

            if (reservationBUS.updateReservation(reservation)) {
                ToastsMessage.showMessage("Cập nhật đơn đặt trước: " + reservationID + " thành công", "success");
            } else {
                ToastsMessage.showMessage("Cập nhật đơn đặt trước: " + reservationID + " thất bại", "error");
            }
        }

        ClearJSON.clearAllJsonWithoutLoginSession_PaymentQueue();
        if (restaurantMainManagerController != null) {
            restaurantMainManagerController.openReservationManagement();
        } else {
            restaurantMainStaffController.openReservationManagement();
        }
    }

    // Thêm phương thức kiểm tra tồn tại của Reservation
    private boolean isReservationExist(String reservationId) {
        try {
            List<Reservation> allReservations = reservationDAO.getAll();
            for (Reservation r : allReservations) {
                if (r.getId().equals(reservationId)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @FXML
    void onChangeValueQuantityKeyTyped(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c) || c == '0') {
            numOfAttendeesField.setText("1");
            ToastsMessage.showMessage("Vui lòng chỉ nhập số, từ 1 người trở lên", "warning");
        }
    }

    @FXML
    void onClearPhoneNumFieldButton(ActionEvent event) {
        phoneNumField.setText("");
        emailField.setText("");
        customerIDField.setText("");
        nameField.setText("");
        noteField.setText("");
        ClearJSON.clearCustomerJson();
    }
}