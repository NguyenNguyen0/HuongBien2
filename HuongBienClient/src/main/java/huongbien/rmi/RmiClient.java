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
    private static CuisineService cuisineService;
    private static EmployeeService employeeService;
    private static OrderService orderService;
    private static PromotionService promotionService;
    private static ReservationService reservationService;
    private static StatisticsService statisticsService;
    private static TableService tableService;
    
    // Connection management
    private static boolean isConnected = false;
    
    /**
     * Connect to the RMI server with settings from client.properties.
     */
    public static void connect() throws RemoteException, NotBoundException {
        String host = ClientConfig.getRmiHost();
        int port = ClientConfig.getRmiPort();
        connect(host, port);
    }
    
    /**
     * Connect to a specific RMI server.
     * 
     * @param host The host name or IP address of the RMI server.
     * @param port The port of the RMI registry.
     */
    public static void connect(String host, int port) throws RemoteException, NotBoundException {
        if (isConnected) {
            return;
        }
        
        System.out.println("Connecting to RMI server at " + host + ":" + port);
        registry = LocateRegistry.getRegistry(host, port);
        
        // Lookup all remote services
        customerService = (CustomerService) registry.lookup("CustomerService");
        cuisineService = (CuisineService) registry.lookup("CuisineService");
        employeeService = (EmployeeService) registry.lookup("EmployeeService");
        orderService = (OrderService) registry.lookup("OrderService");
        promotionService = (PromotionService) registry.lookup("PromotionService");
        reservationService = (ReservationService) registry.lookup("ReservationService");
        statisticsService = (StatisticsService) registry.lookup("StatisticsService");
        tableService = (TableService) registry.lookup("TableService");
        
        isConnected = true;
        System.out.println("Successfully connected to RMI server at " + host + ":" + port);
    }
    
    /**
     * Disconnect from the RMI server.
     */
    public static void disconnect() {
        customerService = null;
        cuisineService = null;
        employeeService = null;
        orderService = null;
        promotionService = null;
        reservationService = null;
        statisticsService = null;
        tableService = null;
        
        registry = null;
        isConnected = false;
        System.out.println("Disconnected from RMI server");
    }
    
    /**
     * @return true if connected to the RMI server, false otherwise.
     */
    public static boolean isConnected() {
        return isConnected;
    }
    
    // Getters for remote services
    
    public static CustomerService getCustomerService() throws NotConnectedException {
        checkConnection();
        return customerService;
    }
    
    public static CuisineService getCuisineService() throws NotConnectedException {
        checkConnection();
        return cuisineService;
    }
    
    public static EmployeeService getEmployeeService() throws NotConnectedException {
        checkConnection();
        return employeeService;
    }
    
    public static OrderService getOrderService() throws NotConnectedException {
        checkConnection();
        return orderService;
    }
    
    public static PromotionService getPromotionService() throws NotConnectedException {
        checkConnection();
        return promotionService;
    }
    
    public static ReservationService getReservationService() throws NotConnectedException {
        checkConnection();
        return reservationService;
    }
    
    public static StatisticsService getStatisticsService() throws NotConnectedException {
        checkConnection();
        return statisticsService;
    }
    
    public static TableService getTableService() throws NotConnectedException {
        checkConnection();
        return tableService;
    }
    
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