package huongbien.dao.remote;

import huongbien.entity.OrderDetail;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IOrderDetailDAO extends Remote {
    List<OrderDetail> getAllByOrderId(String orderId) throws RemoteException;

    OrderDetail getById(String id) throws RemoteException;

    int getCountOfUnitsSold(String cuisineId) throws RemoteException;

    boolean add(OrderDetail orderDetail) throws RemoteException;

    boolean update(OrderDetail orderDetail) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}