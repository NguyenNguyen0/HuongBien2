package huongbien.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.config.AppConfig;
import huongbien.config.Constants;
import huongbien.dao.remote.IAccountDAO;
import huongbien.dao.remote.IEmployeeDAO;
import huongbien.entity.*;
import huongbien.rmi.RMIClient;
import huongbien.service.CustomerBUS;
import huongbien.service.EmailService;
import huongbien.service.ReservationBUS;
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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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

public class RestaurantMainStaffController implements Initializable {
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
    private IEmployeeDAO employeeDAO;
    private IAccountDAO accountDAO;

    public RestaurantMainStaffController() throws RemoteException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            employeeDAO = RMIClient.getInstance().getEmployeeDAO();
            accountDAO = RMIClient.getInstance().getAccountDAO();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
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
            List<Reservation> reservationList = null;
            try {
                reservationList = reservationBUS.getListWaitedToday();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            CustomerBUS customerBUS = null;
            try {
                customerBUS = new CustomerBUS();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (reservationList != null) {
                for (Reservation reservation : reservationList) {
                    LocalTime receiveTime = reservation.getReceiveTime();
                    LocalTime now = LocalTime.now();
                    int hoursDifference = now.getHour() - receiveTime.getHour();
                    int minutesDifference = now.getMinute() - receiveTime.getMinute();
                    int totalMinutesDifference = hoursDifference * 60 + minutesDifference;
                    System.out.println(totalMinutesDifference);
                    //Thời gian đơn sau thời gian hiện tại 20p
                    if (totalMinutesDifference >= 20) {
                        try {
                            reservationBUS.updateStatus(reservation.getId(), ReservationStatus.CANCELLED);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //Gửi email khi đơn đặt còn cách 1 tiếng
                    if (totalMinutesDifference >= -60) {
                        int check = 0;
                        if (listSentEmailAlready != null) {
                            for (String s : listSentEmailAlready) {
                                if (s.equals(reservation.getId())) {
                                    check = 1;
                                }
                            }
                        }
                        if (check == 0) {
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
                                                        padding: 25px 0;
                                                        background: linear-gradient(135deg, var(--primary-dark) 0%%, var(--primary) 100%%);
                                                        color: white;
                                                    }
                                            
                                                    .email-header h1 {
                                                        font-size: 28px;
                                                        font-weight: 700;
                                                        margin: 0;
                                                        letter-spacing: -0.5px;
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
                                                        font-size: 24px;
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
                                            
                                                    .urgent {
                                                        color: #e53935;
                                                        font-weight: 600;
                                                    }
                                            
                                                    .reservation-container {
                                                        margin: 35px auto;
                                                        max-width: 400px;
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
                                            
                                                    .reservation-info {
                                                        margin-bottom: 20px;
                                                        text-align: left;
                                                    }
                                            
                                                    .info-row {
                                                        display: flex;
                                                        margin-bottom: 12px;
                                                        align-items: center;
                                                    }
                                            
                                                    .info-label {
                                                        width: 120px;
                                                        color: var(--text-on-light);
                                                        font-weight: 500;
                                                        font-size: 14px;
                                                    }
                                            
                                                    .info-value {
                                                        flex: 1;
                                                        color: var(--primary);
                                                        font-weight: 600;
                                                        font-size: 15px;
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
                                            
                                                    .warning-box {
                                                        background-color: rgba(229, 57, 53, 0.1);
                                                        border-left: 3px solid #e53935;
                                                        padding: 15px;
                                                        margin: 25px 0 15px;
                                                        text-align: left;
                                                        border-radius: 4px;
                                                    }
                                            
                                                    .warning-title {
                                                        color: #e53935;
                                                        font-weight: 600;
                                                        font-size: 15px;
                                                        margin-bottom: 5px;
                                                        display: flex;
                                                        align-items: center;
                                                        gap: 6px;
                                                    }
                                            
                                                    .warning-content {
                                                        font-size: 14px;
                                                        color: var(--text-on-light);
                                                        margin-top: 8px;
                                                    }
                                            
                                                    .divider {
                                                        height: 1px;
                                                        background: linear-gradient(90deg, rgba(0,0,0,0) 0%%, rgba(0,0,0,0.1) 50%%, rgba(0,0,0,0) 100%%);
                                                        margin: 30px 0;
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
                                                            font-size: 22px;
                                                        }
                                            
                                                        .reservation-box {
                                                            padding: 20px;
                                                        }
                                            
                                                        .info-row {
                                                            flex-direction: column;
                                                            align-items: flex-start;
                                                        }
                                            
                                                        .info-label {
                                                            width: 100%%;
                                                            margin-bottom: 5px;
                                                        }
                                                    }
                                                </style>
                                            </head>
                                            <body>
                                                <div class="email-wrapper">
                                                    <div class="email-header">
                                                        <h1 style="color: black;">NHÀ HÀNG HƯƠNG BIỂN</h1>
                                                        <div class="wave-divider">
                                                            <svg data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 120" preserveAspectRatio="none">
                                                                <path d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z" opacity=".25" class="shape-fill"></path>
                                                                <path d="M0,0V15.81C13,36.92,27.64,56.86,47.69,72.05,99.41,111.27,165,111,224.58,91.58c31.15-10.15,60.09-26.07,89.67-39.8,40.92-19,84.73-46,130.83-49.67,36.26-2.85,70.9,9.42,98.6,31.56,31.77,25.39,62.32,62,103.63,73,40.44,10.79,81.35-6.69,119.13-24.28s75.16-39,116.92-43.05c59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z" opacity=".5" class="shape-fill"></path>
                                                                <path d="M0,0V5.63C149.93,59,314.09,71.32,475.83,42.57c43-7.64,84.23-20.12,127.61-26.46,59-8.63,112.48,12.24,165.56,35.4C827.93,77.22,886,95.24,951.2,90c86.53-7,172.46-45.71,248.8-84.81V0Z" class="shape-fill"></path>
                                                            </svg>
                                                        </div>
                                                    </div>
                                            
                                                    <div class="email-body">
                                                        <h2 class="greeting">Quý khách có đơn đặt bàn sắp đến giờ!</h2>
                                                        <p class="message">Cảm ơn quý khách đã sử dụng dịch vụ của <span class="highlight">Nhà Hàng Hương Biển</span>.</p>
                                            
                                                        <div class="reservation-container">
                                                            <div class="reservation-box">
                                                                <div class="reservation-label">Thông tin đặt bàn</div>
                                                                <div class="reservation-info">
                                                                    <div class="info-row">
                                                                        <div class="info-label">Mã đơn đặt:</div>
                                                                        <div class="info-value">%s</div>
                                                                    </div>
                                                                    <div class="info-row">
                                                                        <div class="info-label">Thời gian nhận:</div>
                                                                        <div class="info-value"><span class="time-highlight" style="color: black;">%s</span></div>
                                                                    </div>
                                                                </div>
                                            
                                                                <div class="warning-box">
                                                                    <div class="warning-title">
                                                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                                            <path d="M12 9V14" stroke="#e53935" stroke-width="2" stroke-linecap="round"/>
                                                                            <path d="M12 17.5V18" stroke="#e53935" stroke-width="2" stroke-linecap="round"/>
                                                                            <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" stroke="#e53935" stroke-width="2"/>
                                                                        </svg>
                                                                        Lưu ý quan trọng
                                                                    </div>
                                                                    <div class="warning-content">
                                                                        Đơn đặt bàn sẽ <span class="urgent">tự động hủy sau 20 phút</span> nếu quý khách không đến nhận bàn. Vui lòng đến đúng giờ để đảm bảo quyền lợi của quý khách.
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                            
                                                        <div class="divider"></div>
                                            
                                                        <p class="message">Nếu quý khách có bất kỳ câu hỏi nào hoặc cần thay đổi thông tin đặt bàn, vui lòng liên hệ với chúng tôi qua số điện thoại <span class="highlight">0123 456 789</span>.</p>
                                                    </div>
                                                </div>
                                            </body>
                                            </html>
                                            """,
                                    reservation.getId(),
                                    receiveTime);
                            String emailContent = htmlContent;
                            Customer customer = null;
                            try {
                                customer = customerBUS.getCustomerById(reservation.getCustomer().getId());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            EmailService.sendEmailWithReservation(customer.getEmail(), "Thông tin đặt trước", emailContent, AppConfig.getEmailUsername(), AppConfig.getEmailPassword());
                            listSentEmailAlready.add(reservation.getId());
                        }
                    }
                }
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
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
            try {
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
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
        restaurantLookupController.setRestaurantMainStaffController(this);
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
        orderTableController.setRestaurantMainStaffController(this);
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
        preOrderController.setRestaurantMainStaffController(this);
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
        orderCuisineController.setRestaurantMainStaffController(this);
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
        preOrderCuisineController.setRestaurantMainStaffController(this);
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
        orderPaymentController.setRestaurantMainStaffController(this);
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
        orderPaymentFinalController.setRestaurantMainStaffController(this);
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
        reservationManagementController.setRestaurantMainStaffController(this);
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
            try {
                List<Employee> employees = employeeDAO.getManyById(id);
                Employee employee = (employees.isEmpty() ? null : employees.get(0));
                assert employee != null;
                employeeNameLabel.setText(employee.getName());
                setAvatar(employee.getProfileImage());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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

        try {
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
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        String hashedNewPwd = Utils.hashPassword(newPwd);

        try {
            boolean isUpdated = accountDAO.changePassword(email, hashedNewPwd);
            if (isUpdated) {
                ToastsMessage.showMessage("Đổi mật khẩu thành công", "success");
                currentPwdField.clear();
                newPwdField.clear();
                new2PwdField.clear();
            } else {
                ToastsMessage.showMessage("Đổi mật khẩu thất bại, vui lòng thử lại", "error");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
            JsonArray jsonArray = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = jsonObject.get("Employee ID").getAsString();
                try {
                    Employee employee = employeeDAO.getOneById(id);
                    employeeDAO.updateWorkHour(id, employee.getWorkHours() + hours);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
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
