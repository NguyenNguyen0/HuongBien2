package huongbien.dao;

import huongbien.entity.Account;
import huongbien.entity.Employee;
import huongbien.entity.Gender;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOTest {
    private static final AccountDAO accountDAO = new AccountDAO(PersistenceUnit.MARIADB_JPA);

    @BeforeEach
    void setUp() {
        Employee employee = new Employee(
                "NV001122009",
                "Nguyen Van A",
                "1234567890",
                "123456789012",
                "0123456789",
                LocalDate.of(1990, 1, 1),
                Gender.MALE,
                "Active",
                "NV001122009@example.com",
                LocalDate.now(),
                "Manager",
                40,
                50.0,
                2000.0,
                null,
                null
        );

        Account account = new Account(
                "NV001122009",
                "4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b1", // password hash
                "manager",
                "NV001122009@example.com",
                true
        );

        account.setEmployee(employee);

        accountDAO.addAccount(account);
    }

    @AfterEach
    void tearDown() {
        Account account = accountDAO.getByUsername("NV001122009");
        if (account != null) {
            accountDAO.softDelete(account);
        }
    }

    @Test
    void add() {
        Account addedAccount = accountDAO.getByUsername("NV001122009");
        assertNotNull(addedAccount);
        assertEquals("NV001122009@example.com", addedAccount.getEmail());
        assertNotNull(addedAccount.getEmployee());
    }

    @Test
    void update() {
        Account account = accountDAO.getByUsername("NV001122009");
        assertNotNull(account);

        account.setRole("admin");
        boolean isUpdated = accountDAO.updateAccount(account);
        assertTrue(isUpdated);

        Account updatedAccount = accountDAO.getByUsername("NV001122009");
        assertNotNull(updatedAccount);
        assertEquals("admin", updatedAccount.getRole());
    }

    @Test
    void getAll() {
        List<Account> accounts = accountDAO.getAll();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    void getByUsername() {
        Account account = accountDAO.getByUsername("NV001122009");
        assertNotNull(account);
        assertEquals("NV001122009", account.getUsername());
    }

    @Test
    void getByEmail() {
        Account account = accountDAO.getByEmail("NV001122009@example.com");
        assertNotNull(account);
        assertEquals("NV001122009@example.com", account.getEmail());
    }

    @Test
    void softDelete() {
        Account account = accountDAO.getByUsername("NV001122009");
        assertNotNull(account);

        boolean isDeleted = accountDAO.softDelete(account);
        assertTrue(isDeleted);

        Account deletedAccount = accountDAO.getByUsername("NV001122009");
        assertNotNull(deletedAccount);
        assertFalse(deletedAccount.isActive());
    }
}
