package huongbien.dao.remote;

import huongbien.entity.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAccountDAO extends Remote {
    List<Account> getAll() throws RemoteException;

    Account getByUsername(String username) throws RemoteException;

    Account getByEmail(String email) throws RemoteException;

    boolean changePassword(String email, String newPassword) throws RemoteException;

    boolean updateEmail(String employeeId, String email) throws RemoteException;

    boolean addAccount(Account account) throws RemoteException;

    boolean updateAccount(Account account) throws RemoteException;

    boolean softDelete(Account account) throws RemoteException;
}