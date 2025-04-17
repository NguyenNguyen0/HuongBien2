package huongbien.bus;

import huongbien.dao.impl.AccountDAO;
import huongbien.entity.Account;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;

public class AccountBUS {
    private final AccountDAO accountDao = new AccountDAO(PersistenceUnit.MARIADB_JPA);

    public AccountBUS() throws RemoteException {
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
