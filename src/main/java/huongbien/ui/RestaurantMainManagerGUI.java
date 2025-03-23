package huongbien.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RestaurantMainManagerGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/huongbien/fxml/RestaurantMainManager.fxml")));
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.getIcons().add(new Image("/huongbien/icon/favicon/favicon-logo-restaurant-128px.png"));
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.setTitle("Dashboard - Huong Bien Restaurant");
//        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
