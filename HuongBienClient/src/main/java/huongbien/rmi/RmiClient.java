package huongbien.rmi;

import huongbien.config.ClientConfig;
import huongbien.rmi.interfaces.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI Client for HuongBien Restaurant Management System.
 * Handles connections to remote services.
 */
public class RmiClient {
    private static Registry registry;
    
    // Remote service instances
    private static CustomerService customerService;
    private static TableService tableService;
    private static ReservationService reservationService;
    // TODO: Add more service references as they are implemented
    
    // Connection management
    private static boolean isConnected = false;
    
    /**
     * Connect to the RMI server using settings from configuration.
     * 
     * @throws RemoteException if a connection error occurs
     * @throws NotBoundException if a service is not bound in the registry
     */
    public static void connect() throws RemoteException, NotBoundException {
        String host = ClientConfig.getRmiHost();
        int port = ClientConfig.getRmiPort();
        connect(host, port);
    }
    
    /**
     * Connect to a specific RMI server.
     * 
     * @param host The host name or IP address of the RMI server
     * @param port The port of the RMI registry
     * @throws RemoteException if a connection error occurs
     * @throws NotBoundException if a service is not bound in the registry
     */
    public static void connect(String host, int port) throws RemoteException, NotBoundException {
        if (isConnected) {
            return;
        }
        
        System.out.println("Connecting to RMI server at " + host + ":" + port);
        registry = LocateRegistry.getRegistry(host, port);
        
        // Lookup all remote services
        customerService = (CustomerService) registry.lookup("CustomerService");
        tableService = (TableService) registry.lookup("TableService");
        reservationService = (ReservationService) registry.lookup("ReservationService");
        // TODO: Add more service lookups as they are implemented
        
        isConnected = true;
        System.out.println("Successfully connected to RMI server at " + host + ":" + port);
    }
    
    /**
     * Disconnect from the RMI server.
     */
    public static void disconnect() {
        if (!isConnected) {
            return;
        }
        
        customerService = null;
        tableService = null;
        reservationService = null;
        // TODO: Clear other service references
        
        registry = null;
        isConnected = false;
        System.out.println("Disconnected from RMI server");
    }
    
    /**
     * Check if connected to the RMI server.
     * 
     * @return true if connected to the RMI server, false otherwise
     */
    public static boolean isConnected() {
        return isConnected;
    }
    
    /**
     * Get the CustomerService remote interface.
     * 
     * @return the CustomerService interface
     * @throws NotConnectedException if not connected to the RMI server
     */
    public static CustomerService getCustomerService() throws NotConnectedException {
        checkConnection();
        return customerService;
    }
    
    /**
     * Get the TableService remote interface.
     * 
     * @return the TableService interface
     * @throws NotConnectedException if not connected to the RMI server
     */
    public static TableService getTableService() throws NotConnectedException {
        checkConnection();
        return tableService;
    }
    
    /**
     * Get the ReservationService remote interface.
     * 
     * @return the ReservationService interface
     * @throws NotConnectedException if not connected to the RMI server
     */
    public static ReservationService getReservationService() throws NotConnectedException {
        checkConnection();
        return reservationService;
    }
    
    // TODO: Add more getters for other services
    
    /**
     * Check if connected to the RMI server.
     * 
     * @throws NotConnectedException if not connected to the RMI server
     */
    private static void checkConnection() throws NotConnectedException {
        if (!isConnected) {
            throw new NotConnectedException("Not connected to RMI server. Call connect() first.");
        }
    }
    
    /**
     * Exception thrown when trying to access a service without being connected.
     */
    public static class NotConnectedException extends RuntimeException {
        public NotConnectedException(String message) {
            super(message);
        }
    }
}