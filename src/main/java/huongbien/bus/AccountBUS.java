package huongbien.bus;

import huongbien.dao.AccountDAO;
import huongbien.entity.Account;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

public class AccountBUS {
    private final AccountDAO accountDao = new AccountDAO(PersistenceUnit.MARIADB_JPA);

    public List<Account> getAllAccount() {
        return accountDao.getAll();
    }

    public Account getAccount(String username) {
        if (username.isBlank() || username.isEmpty()) return null;
        return accountDao.getByUsername(username);
    }

    public boolean changePassword(String email, String newPassword) {
        if (email.isBlank() || email.isEmpty() || newPassword.isBlank() || newPassword.isEmpty()) return false;
        return accountDao.changePassword(email, newPassword);
    }
}
