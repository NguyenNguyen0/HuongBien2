package huongbien.service;

import huongbien.entity.Customer;
import huongbien.entity.FoodOrder;
import huongbien.entity.Order;
import huongbien.entity.OrderDetail;
import huongbien.entity.Payment;
import huongbien.entity.PaymentMethod;
import huongbien.entity.Promotion;
import huongbien.entity.Table;
import huongbien.rmi.RmiClient;
import huongbien.util.ExceptionHandler;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for OrderService remote interface.
 * Provides a simplified API for UI components and handles remote exceptions.
 */
public class OrderServiceAdapter {

    /**
     * Creates a new order.
     * 
     * @param tables The tables for the order
     * @param customer The customer (can be null for walk-in customers)
     * @param foodOrders The food items ordered
     * @param promotion The applied promotion (can be null if no promotion)
     * @return The created order or null if an error occurs
     */
    public static Order createOrder(List<Table> tables, Customer customer, 
                                   List<FoodOrder> foodOrders, Promotion promotion) {
        try {
            return RmiClient.getOrderService().createOrder(tables, customer, foodOrders, promotion);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to create order", 
                e
            );
            return null;
        }
    }
    
    /**
     * Gets an order by ID.
     * 
     * @param id The order ID
     * @return The order with the specified ID or null if not found or an error occurs
     */
    public static Order getOrderById(String id) {
        try {
            return RmiClient.getOrderService().getOrderById(id);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to retrieve order with ID: " + id, 
                e
            );
            return null;
        }
    }
    
    /**
     * Gets active orders (those that haven't been paid).
     * 
     * @return List of active orders or empty list if an error occurs
     */
    public static List<Order> getActiveOrders() {
        try {
            return RmiClient.getOrderService().getActiveOrders();
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to retrieve active orders", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets orders by table.
     * 
     * @param table The table to filter by
     * @return List of orders for the specified table or empty list if an error occurs
     */
    public static List<Order> getOrdersByTable(Table table) {
        try {
            return RmiClient.getOrderService().getOrdersByTable(table);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to retrieve orders for table: " + table.getName(), 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets orders by customer.
     * 
     * @param customer The customer to filter by
     * @return List of orders for the specified customer or empty list if an error occurs
     */
    public static List<Order> getOrdersByCustomer(Customer customer) {
        try {
            return RmiClient.getOrderService().getOrdersByCustomer(customer);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to retrieve orders for customer: " + customer.getName(), 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets orders by date range.
     * 
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of orders in the specified date range or empty list if an error occurs
     */
    public static List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            return RmiClient.getOrderService().getOrdersByDateRange(startDate, endDate);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to retrieve orders for date range", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Adds food items to an existing order.
     * 
     * @param orderId The order ID
     * @param foodOrders The food items to add
     * @return true if successful, false otherwise
     */
    public static boolean addFoodToOrder(String orderId, List<FoodOrder> foodOrders) {
        try {
            return RmiClient.getOrderService().addFoodToOrder(orderId, foodOrders);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to add food items to order", 
                e
            );
            return false;
        }
    }
    
    /**
     * Updates food quantities in an order.
     * 
     * @param orderId The order ID
     * @param foodOrderId The food order ID
     * @param newQuantity The new quantity
     * @return true if successful, false otherwise
     */
    public static boolean updateFoodQuantity(String orderId, String foodOrderId, int newQuantity) {
        try {
            return RmiClient.getOrderService().updateFoodQuantity(orderId, foodOrderId, newQuantity);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to update food quantity", 
                e
            );
            return false;
        }
    }
    
    /**
     * Applies a promotion to an order.
     * 
     * @param orderId The order ID
     * @param promotionId The promotion ID
     * @return true if successful, false otherwise
     */
    public static boolean applyPromotion(String orderId, String promotionId) {
        try {
            return RmiClient.getOrderService().applyPromotion(orderId, promotionId);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to apply promotion to order", 
                e
            );
            return false;
        }
    }
    
    /**
     * Processes payment for an order.
     * 
     * @param orderId The order ID
     * @param amount The payment amount
     * @param method The payment method
     * @param transactionDetails Any additional transaction details
     * @return The created payment or null if an error occurs
     */
    public static Payment processPayment(String orderId, double amount, 
                                        PaymentMethod method, String transactionDetails) {
        try {
            return RmiClient.getOrderService().processPayment(orderId, amount, method, transactionDetails);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to process payment for order", 
                e
            );
            return null;
        }
    }
    
    /**
     * Gets order statistics for a specific period.
     * 
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     * @return List of order statistics or empty list if an error occurs
     */
    public static List<OrderDetail> getOrderStatistics(LocalDate startDate, LocalDate endDate) {
        try {
            return RmiClient.getOrderService().getOrderStatistics(startDate, endDate);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Order Service Error", 
                "Failed to retrieve order statistics", 
                e
            );
            return new ArrayList<>();
        }
    }
}