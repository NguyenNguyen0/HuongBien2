package huongbien.dao.remote;

import huongbien.entity.Order;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IOrderDAO extends Remote {
    List<Order> getAllByCustomerId(String customerId) throws RemoteException;

    List<Order> getAllByEmployeeId(String employeeId) throws RemoteException;

    List<Order> getAllWithPagination(int offset, int limit) throws RemoteException;

    List<Order> getAllByCustomerPhoneNumberWithPagination(int offset, int limit, String customerPhoneNumber) throws RemoteException;

    List<Order> getAllByEmployeeIdWithPagination(int offset, int limit, String employeeId) throws RemoteException;

    List<Order> getAllByIdWithPagination(int offset, int limit, String orderId) throws RemoteException;

    List<Order> getAll() throws RemoteException;

    Order getById(String id) throws RemoteException;

    int countTotal() throws RemoteException;

    int countTotalByOrderId(String orderId) throws RemoteException;

    int countTotalByCustomerPhoneNumber(String customerPhoneNumber) throws RemoteException;

    int countTotalByEmployeeId(String employeeId) throws RemoteException;

    boolean addOrder(Order order) throws RemoteException;

    boolean add(Order order) throws RemoteException;

    boolean update(Order order) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}