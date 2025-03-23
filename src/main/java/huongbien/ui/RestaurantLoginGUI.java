package huongbien.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class RestaurantLoginGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/huongbien/fxml/RestaurantLogin.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.getIcons().add(new Image("/huongbien/icon/favicon/favicon-logo-restaurant-128px.png"));
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.setTitle("Login - Huong Bien Restaurant");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
