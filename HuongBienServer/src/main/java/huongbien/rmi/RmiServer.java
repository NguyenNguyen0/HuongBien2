package huongbien.rmi;

import huongbien.config.ServerConfig;
import huongbien.rmi.impl.*;
import huongbien.rmi.interfaces.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI Server for HuongBien Restaurant Management System.
 * Starts and registers all remote services.
 */
public class RmiServer {
    
    public static void main(String[] args) {
        try {
            System.out.println("Starting HuongBien RMI Server...");
            
            // Load server configuration
            String host = ServerConfig.getRmiHost();
            int port = ServerConfig.getRmiPort();
            
            System.out.println("Using RMI host: " + host);
            System.out.println("Using RMI port: " + port);
            
            // Set the java.rmi.server.hostname property if host is not localhost
            if (!host.equals("localhost") && !host.equals("127.0.0.1")) {
                System.setProperty("java.rmi.server.hostname", host);
            }
            
            // Create and export remote service implementations
            CustomerServiceImpl customerService = new CustomerServiceImpl();
            CuisineServiceImpl cuisineService = new CuisineServiceImpl();
            EmployeeServiceImpl employeeService = new EmployeeServiceImpl();
            OrderServiceImpl orderService = new OrderServiceImpl();
            PromotionServiceImpl promotionService = new PromotionServiceImpl();
            ReservationServiceImpl reservationService = new ReservationServiceImpl();
            StatisticsServiceImpl statisticsService = new StatisticsServiceImpl();
            TableServiceImpl tableService = new TableServiceImpl();
            
            // Create remote stubs for the implementations
            CustomerService customerStub = (CustomerService) 
                UnicastRemoteObject.exportObject(customerService, 0);
            CuisineService cuisineStub = (CuisineService) 
                UnicastRemoteObject.exportObject(cuisineService, 0);
            EmployeeService employeeStub = (EmployeeService) 
                UnicastRemoteObject.exportObject(employeeService, 0);
            OrderService orderStub = (OrderService) 
                UnicastRemoteObject.exportObject(orderService, 0);
            PromotionService promotionStub = (PromotionService) 
                UnicastRemoteObject.exportObject(promotionService, 0);
            ReservationService reservationStub = (ReservationService) 
                UnicastRemoteObject.exportObject(reservationService, 0);
            StatisticsService statisticsStub = (StatisticsService) 
                UnicastRemoteObject.exportObject(statisticsService, 0);
            TableService tableStub = (TableService) 
                UnicastRemoteObject.exportObject(tableService, 0);
            
            // Create or get the RMI registry
            System.out.println("Starting RMI registry on port " + port);
            Registry registry = LocateRegistry.createRegistry(port);
            
            // Register remote objects with the registry
            registry.rebind("CustomerService", customerStub);
            registry.rebind("CuisineService", cuisineStub);
            registry.rebind("EmployeeService", employeeStub);
            registry.rebind("OrderService", orderStub);
            registry.rebind("PromotionService", promotionStub);
            registry.rebind("ReservationService", reservationStub);
            registry.rebind("StatisticsService", statisticsStub);
            registry.rebind("TableService", tableStub);
            
            System.out.println("HuongBien RMI Server is running on port " + port);
            System.out.println("Press Ctrl+C to stop the server");
            
        } catch (Exception e) {
            System.err.println("RMI Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}