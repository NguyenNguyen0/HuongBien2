package huongbien.ui;

import huongbien.rmi.RmiClient;
import huongbien.service.CustomerServiceAdapter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Main client application launcher for HuongBien Restaurant Management System.
 * Connects to the RMI server and launches the client UI.
 */
public class HuongBienClientApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Try to connect to the RMI server
        try {
            System.out.println("Connecting to RMI server...");
            RmiClient.connect();
            System.out.println("Connected successfully!");
            
            // Display a connection success message in a simple UI
            // This would be replaced by your actual UI in the full implementation
            Label connectionLabel = new Label("Connected to HuongBien Restaurant Server!");
            Label customerCountLabel = new Label("Total customers: " + CustomerServiceAdapter.getTotalCustomerCount());
            
            VBox root = new VBox(10);
            root.getChildren().addAll(connectionLabel, customerCountLabel);
            
            Scene scene = new Scene(root, 400, 200);
            primaryStage.setTitle("HuongBien Client");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Failed to connect to RMI server: " + e.getMessage());
            e.printStackTrace();
            
            // Show error dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Failed to connect to the server");
            alert.setContentText("Please ensure the server is running and try again.\n\n" + e.getMessage());
            alert.showAndWait();
            
            // Exit the application
            System.exit(1);
        }
    }
    
    @Override
    public void stop() {
        // Disconnect from the RMI server when the application closes
        if (RmiClient.isConnected()) {
            RmiClient.disconnect();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}