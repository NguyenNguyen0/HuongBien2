package huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import huongbien.config.AppConfig;
import huongbien.config.Constants;
import huongbien.dao.AccountDAO;
import huongbien.entity.Account;
import huongbien.service.EmailService;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class RestaurantLoginController implements Initializable {
    private final String emailUsername = AppConfig.getEmailUsername();
    private final String emailPassword = AppConfig.getEmailPassword();
    @FXML
    private TextField employeeIdField;
    @FXML
    private PasswordField hidedPasswordField;
    @FXML
    private TextField showedPasswordField;
    @FXML
    private BorderPane borderPaneCarousel;
    @FXML
    private AnchorPane compoent_hide;
    @FXML
    private AnchorPane compoent_show;
    @FXML
    private ImageView toggleShowingPasswordButton;
    @FXML
    private Button loginButton;
    @FXML
    private boolean status = false;

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCarousel();
    }

    private void loadCarousel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/huongbien/fxml/RestaurantLoginCarousel.fxml"));
            Parent carousel = loader.load();
            borderPaneCarousel.setCenter(carousel);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @FXML
    void onForgotPasswordLabelClicked(MouseEvent event) {
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Quên mật khẩu");
        emailDialog.setHeaderText("Nhập email của bạn");
        emailDialog.setContentText("Email:");

        Optional<String> emailResult = emailDialog.showAndWait();
        if (emailResult.isPresent()) {
            String email = emailResult.get().trim();

            String otp = Utils.generateOTP();

            boolean isEmailSent = EmailService.sendEmailWithOTP(email, otp, emailUsername, emailPassword);
            if (!isEmailSent) {
                Utils.showAlert(String.valueOf(Alert.AlertType.ERROR), "Không thể gửi OTP. Vui lòng thử lại sau!");
                return;
            }

            TextInputDialog otpDialog = new TextInputDialog();
            otpDialog.setTitle("Xác thực OTP");
            otpDialog.setHeaderText("Nhập mã OTP đã gửi đến email của bạn");
            otpDialog.setContentText("OTP:");

            Optional<String> otpResult = otpDialog.showAndWait();
            if (otpResult.isPresent() && otpResult.get().equals(otp)) {

                Dialog<ButtonType> passwordDialog = new Dialog<>();
                passwordDialog.setTitle("Thay đổi mật khẩu");
                passwordDialog.setHeaderText("Nhập mật khẩu mới và xác nhận");

                PasswordField newPasswordField = new PasswordField();
                newPasswordField.setPromptText("Mật khẩu mới");
                PasswordField confirmPasswordField = new PasswordField();
                confirmPasswordField.setPromptText("Xác nhận mật khẩu");

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.add(new Label("Mật khẩu mới:"), 0, 0);
                gridPane.add(newPasswordField, 1, 0);
                gridPane.add(new Label("Xác nhận mật khẩu:"), 0, 1);
                gridPane.add(confirmPasswordField, 1, 1);

                passwordDialog.getDialogPane().setContent(gridPane);
                passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                Optional<ButtonType> result = passwordDialog.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String newPassword = newPasswordField.getText();
                    String confirmPassword = confirmPasswordField.getText();

                    if (!newPassword.equals(confirmPassword)) {
                        Utils.showAlert(String.valueOf(Alert.AlertType.ERROR), "Mật khẩu không khớp. Vui lòng thử lại!");
                        return;
                    }

                    String hashedPassword = Utils.hashPassword(newPassword);

                    boolean isPasswordUpdated = (new AccountDAO()).changePassword(email, hashedPassword);
                    if (isPasswordUpdated) {
                        Utils.showAlert(String.valueOf(Alert.AlertType.INFORMATION), "Mật khẩu đã được thay đổi thành công!");
                    } else {
                        Utils.showAlert(String.valueOf(Alert.AlertType.ERROR), "Đã xảy ra lỗi. Vui lòng thử lại sau!");
                    }
                }
            } else {
                Utils.showAlert(String.valueOf(Alert.AlertType.ERROR), "OTP không đúng hoặc đã hết hạn. Vui lòng thử lại!");
            }
        }
    }

    @FXML
    void onExitButtonClicked(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Exit");
        alert.setHeaderText("Bạn có muốn thoát khỏi?");
        ButtonType btn_ok = new ButtonType("Ok");
        ButtonType onCancelButtonClicked = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(btn_ok, onCancelButtonClicked);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn_ok) {
            JsonArray jsonArray = new JsonArray();
            Utils.writeJsonToFile(jsonArray, Constants.LOGIN_SESSION_PATH);
            System.exit(0);
        } else if (result.isPresent() && result.get() == onCancelButtonClicked) {
            employeeIdField.requestFocus();
        }
    }

    @FXML
    void onToggleShowingPasswordButtonClicked(MouseEvent event) {
        if (!status) {
            Image imgShow = new Image(getClass().getResourceAsStream("/huongbien/icon/mg_login/eye-16px.png"));
            toggleShowingPasswordButton.setImage(imgShow);
            compoent_show.setVisible(true);
            showedPasswordField.setVisible(true);
            compoent_hide.setVisible(false);
            hidedPasswordField.setVisible(false);
            showedPasswordField.setText(hidedPasswordField.getText());
            status = true;
        } else {
            Image imgHide = new Image(getClass().getResourceAsStream("/huongbien/icon/mg_login/hiddenEye-16px.png"));
            toggleShowingPasswordButton.setImage(imgHide);
            compoent_show.setVisible(false);
            showedPasswordField.setVisible(false);
            compoent_hide.setVisible(true);
            hidedPasswordField.setVisible(true);
            hidedPasswordField.setText(showedPasswordField.getText());
            status = false;
        }
    }

    private String getCurrentPassword() {
        if (hidedPasswordField.isVisible()) {
            return hidedPasswordField.getText();
        }
        if (showedPasswordField.isVisible()) {
            return showedPasswordField.getText();
        }
        return "";
    }

    @FXML
    void onLoginButtonClicked(ActionEvent event) throws SQLException, IOException {
        AccountDAO accountDAO = new AccountDAO();

        String username = employeeIdField.getText();
        String password = getCurrentPassword();
        String passwordHash = Utils.hashPassword(password);

        Account account = accountDAO.getByUsername(username);

        if (account == null) {
            ToastsMessage.showMessage("Tài khoản không tồn tại.", "warning");
            return;
        }

        if (!account.isActive()) {
            ToastsMessage.showMessage("Tài khoản của bạn đã bị khoá, vui lòng liên hệ quản lý để mở lại!", "warning");
            return;
        }

        if (username.equals(account.getUsername().trim()) && passwordHash.equals(account.getHashcode().trim())) {
            ToastsMessage.showMessage("Đăng nhập thành công", "success");
            //Role check
            String path;
            if (account.getRole().equals("Quản lý")) {
                path = "/huongbien/fxml/RestaurantMainManager.fxml";
                ToastsMessage.showMessage("Bạn đang đăng nhập với quyền quản lý", "success");
            } else {
                path = "/huongbien/fxml/RestaurantMainStaff.fxml";
                ToastsMessage.showMessage("Bạn đang đăng nhập với quyền nhân viên", "success");
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();
            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.setMaximized(true);
            mainStage.setTitle("Dashboard - Huong Bien Restaurant");
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.getIcons().add(new Image("/huongbien/icon/favicon/favicon-logo-restaurant-128px.png"));
            mainStage.show();

            //write JSON user current
            JsonArray jsonArray;
            try {
                jsonArray = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
            } catch (FileNotFoundException e) {
                jsonArray = new JsonArray();
            }
            if (!jsonArray.isEmpty()) {
                jsonArray.remove(0);
            }
            //WRITE JSON
            String id = employeeIdField.getText();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Employee ID", id);
            jsonArray.add(jsonObject);
            Utils.writeJsonToFile(jsonArray, Constants.LOGIN_SESSION_PATH);

            if (account.getRole().equals("Quản lý")) {
                RestaurantMainManagerController restaurantMainManagerController = loader.getController();
                restaurantMainManagerController.loadUserInfoFromJSON();
            } else {
                RestaurantMainStaffController restaurantMainStaffController = loader.getController();
                restaurantMainStaffController.loadUserInfoFromJSON();
            }
        } else {
            ToastsMessage.showMessage("Sai mã nhân viên và mật khẩu đăng nhập", "warning");
        }
    }

    @FXML
    void onEmployeeIdFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            if (hidedPasswordField.isVisible()) {
                hidedPasswordField.requestFocus();
            }
            if (showedPasswordField.isVisible()) {
                showedPasswordField.requestFocus();
            }
        }
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }

    @FXML
    void onHidedPasswordFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            loginButton.requestFocus();
        }
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }

    @FXML
    void onShowedPasswordFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            loginButton.requestFocus();
        }
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }
}
