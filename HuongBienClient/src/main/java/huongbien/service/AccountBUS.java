package huongbien.service;

import huongbien.dao.remote.IAccountDAO;
import huongbien.entity.Account;
import huongbien.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class AccountBUS {
    private final IAccountDAO accountDao = RMIClient.getInstance().getAccountDAO();

    public AccountBUS() throws RemoteException, NotBoundException {
    }

    public List<Account> getAllAccount() throws RemoteException {
        return accountDao.getAll();
    }

    public Account getAccount(String username) throws RemoteException {
        if (username.isBlank() || username.isEmpty()) return null;
        return accountDao.getByUsername(username);
    }

    public boolean changePassword(String email, String newPassword) throws RemoteException {
        if (email.isBlank() || email.isEmpty() || newPassword.isBlank() || newPassword.isEmpty()) return false;
        return accountDao.changePassword(email, newPassword);
    }
}
