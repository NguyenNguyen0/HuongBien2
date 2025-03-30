package huongbien.dao;

import huongbien.entity.Account;
import huongbien.jpa.PersistenceUnit;
import huongbien.util.Utils;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class AccountDAO extends GenericDAO<Account> {

    public AccountDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Account> getAll() {
        return findMany("SELECT a FROM Account a", Account.class);
    }

    public Account getByUsername(String username) {
        return findOne("SELECT a FROM Account a WHERE a.username = ?1", Account.class, username);
    }

    public Account getByEmail(String email) {
        return findOne("SELECT a FROM Account a WHERE a.email = ?1", Account.class, email);
    }

    public boolean changePassword(String email, String newPassword) {
        Account account = getByEmail(email);
        if (account == null) return false;
        account.setHashcode(Utils.hashPassword(newPassword));
        return update(account);
    }

    public boolean updateEmail(String employeeId, String email) {
        Account account = getByEmployeeId(employeeId);
        if (account == null) return false;
        account.setEmail(email);
        return update(account);
    }

    private Account getByEmployeeId(String employeeId) {
        return findOne("SELECT a FROM Account a WHERE a.employeeId = ?1", Account.class, employeeId);
    }

    public boolean addAccount(Account account) {
        return add(account);
    }

    public boolean updateAccount(Account account) {
        return update(account);
    }

    public boolean softDelete(Account account) {
        account.setActive(false);
        return update(account);
    }

}
