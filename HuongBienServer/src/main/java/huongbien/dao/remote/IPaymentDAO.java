package huongbien.dao.remote;

import huongbien.entity.Payment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IPaymentDAO extends Remote {
    List<Payment> getAll() throws RemoteException;

    Payment getById(String id) throws RemoteException;

    boolean add(Payment payment) throws RemoteException;

    boolean update(Payment payment) throws RemoteException;

    boolean delete(Payment payment) throws RemoteException;
}