package huongbien.util;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class ToastsMessage {
    public static void showMessage(String message, String type) {
        Notifications notifications = Notifications.create()
                .title("Thông báo")
                .text(message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_RIGHT);
        switch (type) {
            case "error" -> notifications.showError();
            case "warning" -> notifications.showWarning();
            case "success" -> notifications.showInformation();
            default -> notifications.show();
        }
    }
}
