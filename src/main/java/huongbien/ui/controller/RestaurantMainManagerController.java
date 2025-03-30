package huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.bus.CustomerBUS;
import huongbien.bus.ReservationBUS;
import huongbien.config.AppConfig;
import huongbien.config.Constants;
import huongbien.dao.AccountDAO;
import huongbien.dao.EmployeeDAO;
import huongbien.entity.*;
import huongbien.service.EmailService;
import huongbien.util.ClearJSON;
import huongbien.util.Converter;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RestaurantMainManagerController implements Initializable {
    private static ScheduledExecutorService scheduler;
    private static final List<String> listSentEmailAlready = null;
    //timer
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final ReservationBUS reservationBUS = new ReservationBUS();
    @FXML
    public Label employeeNameLabel;
    @FXML
    public Label featureTitleLabel;
    @FXML
    private Label userNameDetailUserLabel;
    @FXML
    private Circle avatarDetailUserCircle;
    @FXML
    private Circle avatarMainCircle;
    @FXML
    private Button hideMenuButton;
    @FXML
    private Button showMenuButton;
    @FXML
    private HBox hidedMenuBarHBox;
    @FXML
    private HBox hideDetailUserHBox;
    @FXML
    private HBox menuOverlayHBox;
    @FXML
    private HBox detailUserOverlayHBox;
    @FXML
    private VBox menuBarOverlayHBox;
    @FXML
    private VBox detailUserBarOverlayHBox;
    @FXML
    private Pane linePane;
    @FXML
    private Label currentDateLabel;
    @FXML
    private Label currentDayLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private BorderPane menuBorderPane;
    @FXML
    private BorderPane detailUserBorderPane;
    @FXML
    private Label nameDetailUserLabel;
    @FXML
    private TextField idDetailUserField;
    @FXML
    private TextField nameDetailUserField;
    @FXML
    private DatePicker birthDateDetailUserDatePicker;
    @FXML
    private TextField phoneDetailUserField;
    @FXML
    private TextField emailDetailUserField;
    @FXML
    private PasswordField currentPwdField;
    @FXML
    private PasswordField newPwdField;
    @FXML
    private PasswordField new2PwdField;
    @FXML
    private Label roleDetailUserLabel;
    @FXML
    private Label hourPayDetailUserLabel;
    @FXML
    private Label salaryDetailUserLabel;
    @FXML
    private Label countHourWorkDetailUserLabel;
    @FXML
    private Label worksHourDetailUserLabel;
    private byte[] employeeImageBytes = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefault();
        try {
            setDetailUserInfo();
            loadUserInfoFromJSON();
            openHome();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        setTime();
        menuBorderPane.setTranslateX(-250);
        detailUserBorderPane.setTranslateX(250);

        scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            List<Reservation> reservationList = reservationBUS.getListWaitedToday();
            CustomerBUS customerBUS = new CustomerBUS();
            if (reservationList != null) {
                for (Reservation reservation : reservationList) {
                    LocalTime receiveTime = reservation.getReceiveTime();
                    LocalTime now = LocalTime.now();
                    int hoursDifference = now.getHour() - receiveTime.getHour();
                    int minutesDifference = now.getMinute() - receiveTime.getMinute();
                    int totalMinutesDifference = hoursDifference * 60 + minutesDifference;
                    //Thời gian đơn sau thời gian hiện tại 20p
                    if (totalMinutesDifference >= 20) {
                        reservationBUS.updateStatus(reservation.getId(), ReservationStatus.CANCELLED);
                    }
                    //Gửi email khi đơn đặt còn cách 1 tiếng
                    if (totalMinutesDifference >= -60) {
                        int check = 0;
                        if (listSentEmailAlready != null) {
                            for (String s : listSentEmailAlready) {
                                if (s.equals(reservation.getId())) {
                                    check = 1;
                                    break;
                                }
                            }
                        }
                        if (check == 0) {
                            String htmlContent = "<html>" +
                                    "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; text-align: left;\">" +
                                    "<h2 style=\"color: #2c3e50;\">Quý khách có đơn đặt bàn sắp đến giờ!</h2>" +
                                    "<p>Mã đơn đặt: <b>" + reservation.getId() + "<b> </p>" +
                                    "<p>Thời gian đến nhận: " + receiveTime + "</p>" +
                                    "<p style=\"color: #34495e;\">Đơn đặt sẽ bị hủy sau 20 phút nếu quý khách không đến nhận bàn, vui lòng chú ý thời gian nhận bàn!.</p>" +
                                    "<p style=\"margin-top: 20px;\">Trân trọng,<br><b>Nhà Hàng Hương Biển</b></p>" +
                                    "</body>" +
                                    "</html>";
                            String emailContent = htmlContent;
                            Customer customer = customerBUS.getCustomerById(reservation.getCustomer().getId());
                            EmailService.sendEmailWithReservation(customer.getEmail(), "Thông tin đặt trước", emailContent, AppConfig.getEmailUsername(), AppConfig.getEmailPassword());
                            listSentEmailAlready.add(reservation.getId());
                        }
                    }
                }
            }
        };

        // Lên lịch để chạy mỗi 5 giây
        scheduler.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
        // Đăng ký shutdown hook để đảm bảo scheduler dừng khi chương trình kết thúc
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdown();  // Dừng scheduler
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();  // Buộc dừng nếu không hoàn thành trong 5 giây
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }));
    }

    public void setDefault() {
        mainBorderPane.setVisible(true);
        menuBorderPane.setVisible(false);
        detailUserBorderPane.setVisible(false);
    }

    public void setDetailUserInfo() throws FileNotFoundException {
        employeeImageBytes = null; //reset byte image
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO employeeDAO = new EmployeeDAO();
            Employee employee = employeeDAO.getOneById(id);
            nameDetailUserLabel.setText(employee.getName());
            userNameDetailUserLabel.setText("@" + employee.getId());
            setAvatar(employee.getProfileImage());
            //table Pane
            idDetailUserField.setText(employee.getId());
            nameDetailUserField.setText(employee.getName());
            birthDateDetailUserDatePicker.setValue(employee.getBirthday());
            phoneDetailUserField.setText(employee.getPhoneNumber());
            emailDetailUserField.setText(employee.getEmail());
            roleDetailUserLabel.setText(employee.getPosition());
            hourPayDetailUserLabel.setText(Converter.formatMoney(employee.getHourlyPay()) + " VNĐ/h");
            salaryDetailUserLabel.setText(Converter.formatMoney(employee.getSalary()) + " VNĐ");
            worksHourDetailUserLabel.setText((int) (employee.getWorkHours()) + " Giờ");
        }
    }

    public void setAvatar(byte[] profileImage) {
        try {
            if (profileImage == null || profileImage.length == 0) {
                throw new IllegalArgumentException("Profile image is null or empty");
            }
            Image image = new Image(Converter.bytesToInputStream(profileImage)); //fix here
            avatarMainCircle.setFill(new ImagePattern(image));
            avatarDetailUserCircle.setFill(new ImagePattern(image));
        } catch (Exception e) {
            avatarMainCircle.setFill(new ImagePattern(new Image("/huongbien/img/avatar/avatar-default-128px.png")));
            avatarDetailUserCircle.setFill(new ImagePattern(new Image("/huongbien/img/avatar/avatar-default-128px.png")));
        }
    }

    private void setTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String time = timeFormat.format(Calendar.getInstance().getTime());
            currentTimeLabel.setText(time);
            String day = dayFormat.format(Calendar.getInstance().getTime());
            currentDayLabel.setText(day);
            String date = dateFormat.format(Calendar.getInstance().getTime());
            currentDateLabel.setText(date);
            //count hour work
            try {
                String[] timeParts = countHourWorkDetailUserLabel.getText().split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);

                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                    if (minutes == 60) {
                        minutes = 0;
                        hours++;
                    }
                }
                countHourWorkDetailUserLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            } catch (Exception e) {
                // Khởi tạo lại khi nhãn không hợp lệ
                countHourWorkDetailUserLabel.setText("00:00:00");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void openHome() throws IOException {
        featureTitleLabel.setText("Trang chủ");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/RestaurantHome.fxml"));
        BorderPane home = loader.load();
        mainBorderPane.setCenter(home);
        home.prefWidthProperty().bind(mainBorderPane.widthProperty());
        home.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openLookup() throws IOException {
        featureTitleLabel.setText("Tra cứu bàn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/RestaurantLookup.fxml"));
        BorderPane lookup = loader.load();
        mainBorderPane.setCenter(lookup);
        lookup.prefWidthProperty().bind(mainBorderPane.widthProperty());
        lookup.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        RestaurantLookupController restaurantLookupController = loader.getController();
        restaurantLookupController.setRestaurantMainManagerController(this);
    }

    public void openOrderTable() throws IOException {
        featureTitleLabel.setText("Chọn bàn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/OrderTable.fxml"));
        BorderPane orderTable = loader.load();
        mainBorderPane.setCenter(orderTable);
        orderTable.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderTable.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderTableController orderTableController = loader.getController();
        orderTableController.setRestaurantMainManagerController(this);
    }

    public void openPreOrder() throws IOException {
        featureTitleLabel.setText("Chọn bàn  -  Thông tin đặt trước");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/PreOrder.fxml"));
        BorderPane preOrderTable = loader.load();
        mainBorderPane.setCenter(preOrderTable);
        preOrderTable.prefWidthProperty().bind(mainBorderPane.widthProperty());
        preOrderTable.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        PreOrderController preOrderController = loader.getController();
        preOrderController.setRestaurantMainManagerController(this);
    }

    public void openOrderCuisine() throws IOException {
        featureTitleLabel.setText("Chọn bàn  -  Đặt món");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/OrderCuisine.fxml"));
        BorderPane orderCuisine = loader.load();
        mainBorderPane.setCenter(orderCuisine);
        orderCuisine.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderCuisine.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderCuisineController orderCuisineController = loader.getController();
        orderCuisineController.setRestaurantMainManagerController(this);
    }

    public void openPreOrderCuisine() throws IOException {
        featureTitleLabel.setText("Chọn bàn  - Thông tin đặt trước -  Đặt món");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/PreOrderCuisine.fxml"));
        BorderPane orderCuisine = loader.load();
        mainBorderPane.setCenter(orderCuisine);
        orderCuisine.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderCuisine.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        PreOrderCuisineController preOrderCuisineController = loader.getController();
        preOrderCuisineController.setRestaurantMainManagerController(this);
    }

    public void openOrderPayment() throws IOException {
        featureTitleLabel.setText("Chọn bàn  -  Đặt món  -  Tính tiền");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/OrderPayment.fxml"));
        BorderPane orderPayment = loader.load();
        mainBorderPane.setCenter(orderPayment);
        orderPayment.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderPayment.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderPaymentController orderPaymentController = loader.getController();
        orderPaymentController.setRestaurantMainManagerController(this);
    }

    public void openOrderPaymentFinal() throws IOException {
        featureTitleLabel.setText("Chọn bàn  -  Đặt món  -  Tính tiền  -  Thanh toán");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/OrderPaymentFinal.fxml"));
        BorderPane orderPaymentFinal = loader.load();
        mainBorderPane.setCenter(orderPaymentFinal);
        orderPaymentFinal.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderPaymentFinal.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderPaymentFinalController orderPaymentFinalController = loader.getController();
        orderPaymentFinalController.setRestaurantMainManagerController(this);
    }

    public void openStatistics() throws IOException {
        featureTitleLabel.setText("Thống kê");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/RestaurantStatistics.fxml"));
        BorderPane statistics = loader.load();
        mainBorderPane.setCenter(statistics);
        statistics.prefWidthProperty().bind(mainBorderPane.widthProperty());
        statistics.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openReservationManagement() throws IOException {
        featureTitleLabel.setText("Quản lý đơn đặt");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/ReservationManagement.fxml"));
        BorderPane listOrder = loader.load();
        mainBorderPane.setCenter(listOrder);
        listOrder.prefWidthProperty().bind(mainBorderPane.widthProperty());
        listOrder.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        ReservationManagementController reservationManagementController = loader.getController();
        reservationManagementController.setRestaurantMainManagerController(this);
    }

    public void openOrderManagement() throws IOException {
        featureTitleLabel.setText("Quản lý hoá đơn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/OrderManagement.fxml"));
        BorderPane manageBill = loader.load();
        mainBorderPane.setCenter(manageBill);
        manageBill.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageBill.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openCuisineManagement() throws IOException {
        featureTitleLabel.setText("Quản lý món ăn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/CuisineManagement.fxml"));
        BorderPane manageCuisine = loader.load();
        mainBorderPane.setCenter(manageCuisine);
        manageCuisine.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageCuisine.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openTableManagement() throws IOException {
        featureTitleLabel.setText("Quản lý bàn ăn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/TableManagement.fxml"));
        BorderPane manageTable = loader.load();
        mainBorderPane.setCenter(manageTable);
        manageTable.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageTable.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openCustomerManagement() throws IOException {
        featureTitleLabel.setText("Quản lý khách hàng");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/CustomerManagement.fxml"));
        BorderPane manageCustomer = loader.load();
        mainBorderPane.setCenter(manageCustomer);
        manageCustomer.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageCustomer.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openEmployeeManagement() throws IOException {
        featureTitleLabel.setText("Quản lý nhân viên");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/EmployeeManagement.fxml"));
        BorderPane manageEmployee = loader.load();
        mainBorderPane.setCenter(manageEmployee);
        manageEmployee.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageEmployee.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        EmployeeManagementController employeeManagementController = loader.getController();
        employeeManagementController.setRestaurantMainManagerController(this);
    }

    public void openPromotionManagement() throws IOException {
        featureTitleLabel.setText("Quản lý khuyến mãi");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/PromotionManagement.fxml"));
        BorderPane managePromotion = loader.load();
        mainBorderPane.setCenter(managePromotion);
        managePromotion.prefWidthProperty().bind(mainBorderPane.widthProperty());
        managePromotion.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openRestaurantHelp() throws IOException {
        featureTitleLabel.setText("Help");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/RestaurantHelp.fxml"));
        BorderPane help = loader.load();
        mainBorderPane.setCenter(help);
        help.prefWidthProperty().bind(mainBorderPane.widthProperty());
        help.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openRestaurantAbout() throws IOException {
        featureTitleLabel.setText("About");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/RestaurantAbout.fxml"));
        BorderPane help = loader.load();
        mainBorderPane.setCenter(help);
        help.prefWidthProperty().bind(mainBorderPane.widthProperty());
        help.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    //navbar-hide
    @FXML
    void onHideMenuButtonClicked(ActionEvent event) {
        openMenu();
    }

    @FXML
    void onOpenDetailUserHBoxClicked(MouseEvent event) throws FileNotFoundException {
        openDetailUser();
        setDetailUserInfo();
    }

    @FXML
    void onHideHomeButtonClicked(ActionEvent event) throws IOException {
        openHome();
    }

    @FXML
    void onHideLookupButtonClicked(ActionEvent event) throws IOException {
        openLookup();
    }

    @FXML
    void onHideOrderButtonClicked(ActionEvent event) throws IOException {
        openOrderTable();
    }

    @FXML
    void onHideReservationButtonClicked(ActionEvent event) throws IOException {
        openReservationManagement();
    }

    @FXML
    void onHideStatisticButtonClicked(ActionEvent event) throws IOException {
        openStatistics();
    }

    @FXML
    void onHideBillButtonClicked(ActionEvent event) throws IOException {
        openOrderManagement();
    }

    @FXML
    void onHideCuisineButtonClicked(ActionEvent event) throws IOException {
        openCuisineManagement();
    }

    @FXML
    void onHideTableButtonClicked(ActionEvent event) throws IOException {
        openTableManagement();
    }

    @FXML
    void onHideCustomerButtonClicked(ActionEvent event) throws IOException {
        openCustomerManagement();
    }

    @FXML
    void onHideEmployeeButtonClicked(ActionEvent event) throws IOException {
        openEmployeeManagement();
    }

    @FXML
    void onHidePromotionButtonClicked(ActionEvent event) throws IOException {
        openPromotionManagement();
    }

    //navbar-show
    @FXML
    void onShowMenuButtonClicked(ActionEvent event) {
        hideMenu();
    }

    @FXML
    void onShowHomeButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openHome();
    }

    @FXML
    void onShowLookupButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openLookup();
    }

    @FXML
    void onShowOrderButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openOrderTable();
    }

    @FXML
    void onShowReservationButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openReservationManagement();
    }

    @FXML
    void onShowStatisticButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openStatistics();
    }

    @FXML
    void onShowBillButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openOrderManagement();
    }

    @FXML
    void onShowCuisineButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openCuisineManagement();
    }

    @FXML
    void onShowTableButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openTableManagement();
    }

    @FXML
    void onShowCustomerButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openCustomerManagement();
    }

    @FXML
    void onShowEmployeeButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openEmployeeManagement();
    }

    @FXML
    void onShowPromotionButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openPromotionManagement();
    }

    //service button
    @FXML
    void onShowRestaurantHelpButtonAction(ActionEvent event) throws IOException {
        hideMenu();
        openRestaurantHelp();
    }

    @FXML
    void onShowRestaurantAboutButtonAction(ActionEvent event) throws IOException {
        hideMenu();
        openRestaurantAbout();
    }

    public void translateAnimationMenu(double duration, Node node, double toX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(toX);
        translateTransition.setOnFinished(event -> enableMenuButtons());
        translateTransition.play();
    }

    public void translateAnimationDetailUser(double duration, Node node, double toX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(toX);
        translateTransition.setOnFinished(event -> enableDetailUserButtons());
        translateTransition.play();
    }

    //fix bug khi translate khong dung vi tri =)))
    private void disableMenuButtons() {
        hideMenuButton.setDisable(true);
        showMenuButton.setDisable(true);
    }

    private void disableDetailUserButtons() {
        hideDetailUserHBox.setDisable(true);
    }

    private void enableMenuButtons() {
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> {
            hideMenuButton.setDisable(false);
            showMenuButton.setDisable(false);
        });
        pause.play();
    }

    private void enableDetailUserButtons() {
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            hideDetailUserHBox.setDisable(false);
        });
        pause.play();
    }

    public void loadUserInfoFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO employeeDAO = new EmployeeDAO();
            List<Employee> employees = employeeDAO.getManyById(id);
            Employee employee = (employees.isEmpty() ? null : employees.get(0));
            assert employee != null;
            employeeNameLabel.setText(employee.getName());
            setAvatar(employee.getProfileImage());
        }
    }

    private void openMenu() {
        disableMenuButtons();
        menuBorderPane.setVisible(true);
        translateAnimationMenu(0.3, menuBorderPane, 250);
        //menuOverlayHBox
        FadeTransition fadeInOverlay_menuOverlayHBox = new FadeTransition(Duration.seconds(0.5), menuOverlayHBox);
        fadeInOverlay_menuOverlayHBox.setFromValue(0);
        fadeInOverlay_menuOverlayHBox.setToValue(0.6);
        fadeInOverlay_menuOverlayHBox.play();
        //menuBarOverlayHBox
        FadeTransition fadeInOverlay_menuBarOverlayHBox = new FadeTransition(Duration.seconds(0.5), menuBarOverlayHBox);
        fadeInOverlay_menuBarOverlayHBox.setFromValue(0);
        fadeInOverlay_menuBarOverlayHBox.setToValue(0.6);
        fadeInOverlay_menuBarOverlayHBox.play();
        //
        hidedMenuBarHBox.setVisible(false);
        linePane.setOpacity(0);
    }

    private void openDetailUser() {
        detailUserBorderPane.setVisible(true);
        translateAnimationDetailUser(0.2, detailUserBorderPane, -250);
        //detailUserOverlayHBox
        FadeTransition fadeInOverlay_detailUserOverlayHBox = new FadeTransition(Duration.seconds(0.5), detailUserOverlayHBox);
        fadeInOverlay_detailUserOverlayHBox.setFromValue(0);
        fadeInOverlay_detailUserOverlayHBox.setToValue(0.6);
        fadeInOverlay_detailUserOverlayHBox.play();
        //detailUserBarOverlayHBox
        FadeTransition fadeInOverlay_detailUserBarOverlayHBox = new FadeTransition(Duration.seconds(0.5), detailUserBarOverlayHBox);
        fadeInOverlay_detailUserBarOverlayHBox.setFromValue(0);
        fadeInOverlay_detailUserBarOverlayHBox.setToValue(0.6);
        fadeInOverlay_detailUserBarOverlayHBox.play();
    }

    @FXML
    void onMenuOverlayHBoxClicked(MouseEvent event) {
        disableMenuButtons();
        menuBorderPane.setVisible(false);
        translateAnimationMenu(0.5, menuBorderPane, -250);
        hidedMenuBarHBox.setVisible(true);
        //design
        linePane.setOpacity(1);
    }

    private void hideMenu() {
        disableMenuButtons();
        menuBorderPane.setVisible(false);
        translateAnimationMenu(0.3, menuBorderPane, -250);
        hidedMenuBarHBox.setVisible(true);
        //design
        linePane.setOpacity(1);
    }

    @FXML
    void onDetailUserOverlayHBoxClicked(MouseEvent event) throws FileNotFoundException {
        disableDetailUserButtons();
        detailUserBorderPane.setVisible(false);
        translateAnimationDetailUser(0.5, detailUserBorderPane, 250);
        //reset detail User Info
        setDetailUserInfo();
    }

    @FXML
    public void onImageChooserButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                employeeImageBytes = Converter.fileToBytes(selectedFile);
                Image image = new Image(Converter.bytesToInputStream(employeeImageBytes));
                avatarMainCircle.setFill(new ImagePattern(image));
                avatarDetailUserCircle.setFill(new ImagePattern(image));
            } catch (IOException e) {
                ToastsMessage.showMessage("Lỗi: " + e.getMessage(), "error");
            }
        }
    }

    @FXML
    void saveInfoDetailUserButtonAction(ActionEvent event) throws FileNotFoundException {
        try {
            String employeeId = idDetailUserField.getText();
            EmployeeDAO employeeDAO = new EmployeeDAO();
            AccountDAO accountDAO = new AccountDAO();

            Employee existingEmployee = employeeDAO.getOneById(employeeId);

            byte[] profileImage = (employeeImageBytes != null)
                    ? employeeImageBytes
                    : existingEmployee.getProfileImage();

            String name = nameDetailUserField.getText();
            LocalDate birthDate = birthDateDetailUserDatePicker.getValue();
            String phone = phoneDetailUserField.getText();
            String email = emailDetailUserField.getText();

            Employee employee = new Employee();
            employee.setId(employeeId);
            employee.setName(name);
            employee.setBirthday(birthDate);
            employee.setPhoneNumber(phone);
            employee.setEmail(email);
            employee.setProfileImage(profileImage);

            boolean isUpdated = employeeDAO.updateEmployeeInfoProfile(employee);
            if (isUpdated) {
                boolean isEmailUpdated = accountDAO.updateEmail(employeeId, email);
                if (isEmailUpdated) {
                    ToastsMessage.showMessage("Cập nhật thông tin thành công", "success");
                } else {
                    ToastsMessage.showMessage("Cập nhật thông tin thành công nhưng không thể cập nhật email", "warning");
                }
            } else {
                ToastsMessage.showMessage("Cập nhật thông tin thất bại, vui lòng thử lại", "error");
            }

            setDetailUserInfo();
        } catch (Exception e) {
            ToastsMessage.showMessage("Lỗi cơ sở dữ liệu: " + e.getMessage(), "error");
        }
    }

    @FXML
    void updatePwdDetailUserButtonAction(ActionEvent event) {
        String currentPwd = currentPwdField.getText();
        String newPwd = newPwdField.getText();
        String new2Pwd = new2PwdField.getText();

        if (currentPwd.isEmpty() || newPwd.isEmpty() || new2Pwd.isEmpty()) {
            ToastsMessage.showMessage("Vui lòng nhập đầy đủ thông tin", "warning");
            return;
        }

        if (!newPwd.equals(new2Pwd)) {
            ToastsMessage.showMessage("Mật khẩu mới không khớp", "error");
            return;
        }

        String email = emailDetailUserField.getText();

        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.getByEmail(email);

        if (account == null) {
            ToastsMessage.showMessage("Không tìm thấy tài khoản", "error");
            return;
        }

        String hashedCurrentPwd = Utils.hashPassword(currentPwd);
        if (!account.getHashcode().equals(hashedCurrentPwd)) {
            ToastsMessage.showMessage("Mật khẩu hiện tại không đúng", "error");
            return;
        }

        String hashedNewPwd = Utils.hashPassword(newPwd);

        boolean isUpdated = accountDAO.changePassword(email, hashedNewPwd);
        if (isUpdated) {
            ToastsMessage.showMessage("Đổi mật khẩu thành công", "success");
            currentPwdField.clear();
            newPwdField.clear();
            new2PwdField.clear();
        } else {
            ToastsMessage.showMessage("Đổi mật khẩu thất bại, vui lòng thử lại", "error");
        }
    }

    @FXML
    void onExitButtonClicked(MouseEvent event) throws FileNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Exit");
        double hours = Double.parseDouble(countHourWorkDetailUserLabel.getText().split(":")[0]);
        alert.setHeaderText("Bạn có muốn kết thúc phiên làm việc, với số giờ đã ghi nhận là " + (int) hours + " giờ?");
        ButtonType btn_ok = new ButtonType("Ok");
        ButtonType onCancelButtonClicked = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(btn_ok, onCancelButtonClicked);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn_ok) {
            //update work hours
            EmployeeDAO employeeDAO = new EmployeeDAO();
            JsonArray jsonArray = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = jsonObject.get("Employee ID").getAsString();
                Employee employee = employeeDAO.getOneById(id);
                employeeDAO.updateWorkHour(id, employee.getWorkHours() + hours);
                break;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/RestaurantLogin.fxml"));
                Parent root = loader.load();
                Scene mainScene = new Scene(root);
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();
                Stage mainStage = new Stage();
                mainStage.setScene(mainScene);
                mainStage.setMaximized(true);
                mainStage.getIcons().add(new Image("/huongbien/icon/favicon/favicon-logo-restaurant-128px.png"));
                mainStage.initStyle(StageStyle.UNDECORATED);
                mainStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ClearJSON.clearAllJson();
        }
    }
}
