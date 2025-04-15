package huongbien.ui;

import huongbien.config.ClientConfig;
import huongbien.rmi.RmiClient;
import huongbien.service.CustomerServiceAdapter;
import huongbien.util.ExceptionHandler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Main client application launcher for HuongBien Restaurant Management System.
 */
public class HuongBienClientApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Starting HuongBien client application...");
            
            // Connect to the RMI server
            connectToServer();
            
            // Main application UI would be initialized here in the actual implementation
            // For now, we'll just show a simple test window
            showConnectionTestWindow(primaryStage);
            
        } catch (Exception e) {
            System.err.println("Error starting client application: " + e.getMessage());
            e.printStackTrace();
            
            ExceptionHandler.handleException(
                "Application Error",
                "Failed to start application",
                "An unexpected error occurred while starting the application.",
                e
            );
            
            Platform.exit();
        }
    }
    
    /**
     * Connect to the RMI server using settings from configuration.
     */
    private void connectToServer() {
        try {
            System.out.println("Connecting to RMI server...");
            RmiClient.connect();
            System.out.println("Connected to RMI server successfully");
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Failed to connect to RMI server: " + e.getMessage());
            e.printStackTrace();
            
            ExceptionHandler.handleException(
                "Connection Error",
                "Failed to connect to the server",
                "Please ensure the server is running at " + 
                ClientConfig.getRmiHost() + ":" + ClientConfig.getRmiPort() + 
                " and try again.",
                e
            );
            
            // Exit the application
            Platform.exit();
        }
    }
    
    /**
     * Show a test window with connection information.
     * This is temporary and would be replaced with the actual application UI.
     */
    private void showConnectionTestWindow(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label titleLabel = new Label("HuongBien Restaurant Management System");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label connectionLabel = new Label("Connected to server: " + 
                                        ClientConfig.getRmiHost() + ":" + 
                                        ClientConfig.getRmiPort());
        
        // Test retrieving data from the server
        Label customerCountLabel = new Label();
        try {
            int customerCount = CustomerServiceAdapter.getTotalCustomerCount();
            customerCountLabel.setText("Customers in database: " + customerCount);
        } catch (Exception e) {
            customerCountLabel.setText("Could not retrieve customer count");
            System.err.println("Error retrieving customer count: " + e.getMessage());
        }
        
        Button logoutButton = new Button("Disconnect and Exit");
        logoutButton.setOnAction(event -> {
            RmiClient.disconnect();
            Platform.exit();
        });
        
        root.getChildren().addAll(
            titleLabel,
            new Label("Connection test successful"),
            connectionLabel,
            customerCountLabel,
            logoutButton
        );
        
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("HuongBien Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    public void stop() {
        // Disconnect from the RMI server when the application closes
        if (RmiClient.isConnected()) {
            System.out.println("Disconnecting from RMI server...");
            RmiClient.disconnect();
        }
    }
    
    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        // Set system properties for RMI if needed
        // System.setProperty("java.security.policy", "client.policy");
        
        launch(args);
    }
}