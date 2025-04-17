package huongbien.dao.remote;

import huongbien.entity.FoodOrder;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IFoodOrderDAO extends Remote {
    List<FoodOrder> getAll() throws RemoteException;

    List<FoodOrder> getAllByReservationId(String reservationId) throws RemoteException;

    FoodOrder getById(String foodOrderId) throws RemoteException;

    boolean add(FoodOrder foodOrder) throws RemoteException;

    boolean update(FoodOrder foodOrder) throws RemoteException;

    boolean delete(FoodOrder foodOrder) throws RemoteException;
}