package huongbien.dao.impl;

import huongbien.dao.remote.IAccountDAO;
import huongbien.entity.Account;
import huongbien.jpa.PersistenceUnit;
import huongbien.util.Utils;

import java.rmi.RemoteException;
import java.util.List;

public class AccountDAO extends GenericDAO<Account> implements IAccountDAO {

    public AccountDAO() throws RemoteException {
        super();
    }

    public AccountDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Account> getAll() throws RemoteException {
        return findMany("SELECT a FROM Account a", Account.class);
    }

    @Override
    public Account getByUsername(String username) throws RemoteException {
        return findOne("SELECT a FROM Account a WHERE a.username = ?1", Account.class, username);
    }

    @Override
    public Account getByEmail(String email) throws RemoteException {
        return findOne("SELECT a FROM Account a WHERE a.email = ?1", Account.class, email);
    }

    @Override
    public boolean changePassword(String email, String newPassword) throws RemoteException {
        Account account = getByEmail(email);
        if (account == null) return false;
        account.setHashcode(Utils.hashPassword(newPassword));
        return update(account);
    }

    @Override
    public boolean updateEmail(String employeeId, String email) throws RemoteException {
        Account account = getByEmployeeId(employeeId);
        if (account == null) return false;
        account.setEmail(email);
        return update(account);
    }

    private Account getByEmployeeId(String employeeId) throws RemoteException {
        return findOne("SELECT a FROM Account a WHERE a.employeeId = ?1", Account.class, employeeId);
    }

    @Override
    public boolean addAccount(Account account) throws RemoteException {
        return super.add(account);
    }

    @Override
    public boolean updateAccount(Account account) throws RemoteException {
        return super.update(account);
    }

    @Override
    public boolean softDelete(Account account) throws RemoteException {
        account.setActive(false);
        return super.update(account);
    }
}
