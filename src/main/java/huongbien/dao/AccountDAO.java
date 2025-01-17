package huongbien.dao;

import huongbien.entity.Account;
import huongbien.jpa.PersistenceUnit;
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
        return findOne("SELECT a FROM Account a WHERE a.username LIKE ?1", Account.class, "%" + username + "%");
    }

    public Account getByEmail(String email) {
        return findOne("SELECT a FROM Account a WHERE a.email = ?1", Account.class, email);
    }
}

