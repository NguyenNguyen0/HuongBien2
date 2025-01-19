package huongbien.dao;

import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountDAOTest {
    private static final AccountDAO accountDAO = new AccountDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void getAll() {
        assertNotNull(accountDAO.getAll());
    }

    @Test
    void getById() {
    }

    @Test
    void getByUsername() {
    }

    @Test
    void getByEmail() {
    }
}