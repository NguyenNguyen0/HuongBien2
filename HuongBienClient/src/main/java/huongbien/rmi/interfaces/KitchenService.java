package huongbien.rmi.interfaces;

import huongbien.entity.Order;
import huongbien.entity.OrderItem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface for managing kitchen operations.
 */
public interface KitchenService extends Remote {
    /**
     * Retrieves all pending orders for the kitchen.
     * 
     * @return List of orders that need to be prepared
     * @throws RemoteException If a remote communication error occurs
     */
    List<Order> getPendingOrders() throws RemoteException;
    
    /**
     * Updates the status of an order item (e.g., in preparation, ready, etc.)
     * 
     * @param orderItemId The ID of the order item
     * @param status The new status
     * @return true if updated successfully, false otherwise
     * @throws RemoteException If a remote communication error occurs
     */
    boolean updateOrderItemStatus(int orderItemId, String status) throws RemoteException;
    
    /**
     * Marks an entire order as ready.
     * 
     * @param orderId The ID of the order
     * @return true if updated successfully, false otherwise
     * @throws RemoteException If a remote communication error occurs
     */
    boolean markOrderReady(int orderId) throws RemoteException;
    
    /**
     * Retrieves all order items for a specific order.
     * 
     * @param orderId The ID of the order
     * @return List of order items
     * @throws RemoteException If a remote communication error occurs
     */
    List<OrderItem> getOrderItems(int orderId) throws RemoteException;
}