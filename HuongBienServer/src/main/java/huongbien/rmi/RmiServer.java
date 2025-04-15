package huongbien.rmi;

import huongbien.config.ServerConfig;
import huongbien.service.impl.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Main RMI server class for HuongBien Restaurant Management System.
 * Starts the RMI registry and registers all remote services.
 */
public class RmiServer {
    private Registry registry;
    
    /**
     * Starts the RMI server using the default port from configuration.
     */
    public void start() {
        int port = ServerConfig.getRmiPort();
        start(port);
    }
    
    /**
     * Starts the RMI server using the specified port.
     * 
     * @param port The port to use for the RMI registry
     */
    public void start(int port) {
        try {
            System.out.println("Starting HuongBien RMI server on port " + port + "...");
            
            // Create and export the registry on the specified port
            registry = LocateRegistry.createRegistry(port);
            
            // Register all services
            registerServices();
            
            System.out.println("HuongBien RMI server started successfully on port " + port);
            System.out.println("Server is running. Press Ctrl+C to stop.");
        } catch (Exception e) {
            System.err.println("HuongBien RMI server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Registers all remote service implementations with the RMI registry.
     */
    private void registerServices() {
        try {
            // Create service instances
            CustomerServiceImpl customerService = new CustomerServiceImpl();
            TableServiceImpl tableService = new TableServiceImpl();
            ReservationServiceImpl reservationService = new ReservationServiceImpl();
            // Add more service implementations as needed
            
            // Register services with the registry
            System.out.println("Registering CustomerService...");
            registry.rebind("CustomerService", customerService);
            
            System.out.println("Registering TableService...");
            registry.rebind("TableService", tableService);
            
            System.out.println("Registering ReservationService...");
            registry.rebind("ReservationService", reservationService);
            
            // Register additional services here
            
            System.out.println("All services registered successfully");
        } catch (Exception e) {
            System.err.println("Error registering services: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stops the RMI server and unbinds all services.
     */
    public void stop() {
        try {
            System.out.println("Stopping HuongBien RMI server...");
            
            // Unbind all services
            for (String name : registry.list()) {
                System.out.println("Unbinding service: " + name);
                registry.unbind(name);
            }
            
            // Unexport the registry
            UnicastRemoteObject.unexportObject(registry, true);
            
            System.out.println("HuongBien RMI server stopped successfully");
        } catch (Exception e) {
            System.err.println("Error stopping RMI server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Main method to start the server.
     */
    public static void main(String[] args) {
        RmiServer server = new RmiServer();
        
        // Add a shutdown hook to stop the server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown signal received. Stopping server...");
            server.stop();
        }));
        
        // Start the server
        server.start();
    }
}