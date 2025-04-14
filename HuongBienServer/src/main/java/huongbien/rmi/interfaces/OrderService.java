package huongbien.rmi.interfaces;

import huongbien.entity.Order;
import huongbien.entity.OrderDetail;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface for order services
 */
public interface OrderService extends Remote {
    
    // Order retrieval
    List<Order> getAllOrders() throws RemoteException;
    List<Order> getAllWithPagination(int offset, int limit) throws RemoteException;
    List<Order> getOrdersByIdWithPagination(int offset, int limit, String orderId) throws RemoteException;
    List<Order> getOrdersByEmployeeIdWithPagination(int offset, int limit, String employeeId) throws RemoteException;
    List<Order> getOrdersByCustomerPhoneNumberWithPagination(int offset, int limit, String customerPhoneNumber) throws RemoteException;
    
    // Order counts
    int countTotalOrders() throws RemoteException;
    int countTotalOrdersByEmployeeId(String employeeId) throws RemoteException;
    int countTotalOrdersByCustomerPhoneNumber(String customerPhoneNumber) throws RemoteException;
    int countTotalOrdersByOrderId(String orderId) throws RemoteException;
    
    // Order details
    List<OrderDetail> getAllOrderDetailByOrderId(String orderId) throws RemoteException;
    
    // Order management
    boolean addOrder(Order order) throws RemoteException;
    boolean addOrderDetail(OrderDetail orderDetail) throws RemoteException;
}