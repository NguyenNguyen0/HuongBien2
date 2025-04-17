package dao;

import huongbien.dao.impl.AccountDAO;
import huongbien.entity.Account;
import huongbien.entity.Employee;
import huongbien.entity.Gender;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import huongbien.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOTest {

    private AccountDAO accountDAO;
    private PersistenceUnit testPersistenceUnit;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private String testUsername;
    private String testEmail;

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with test persistence unit
        testPersistenceUnit = PersistenceUnit.MARIADB_JPA;
        accountDAO = new AccountDAO(testPersistenceUnit);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test credentials for each test
        testUsername = "testuser-" + UUID.randomUUID().toString().substring(0, 8);
        testEmail = "test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        // Setup and persist test account
        setupTestAccount();
    }

    private void setupTestAccount() {
        Account testAccount = new Account();
        testAccount.setUsername(testUsername);
        testAccount.setEmail(testEmail);
        testAccount.setHashcode(Utils.hashPassword("password"));
        testAccount.setRole("USER");
        testAccount.setActive(true);

        // Create test employee
        Employee employee = new Employee();
        employee.setId("EMP-" + UUID.randomUUID().toString().substring(0, 8));
        employee.setName("Test");
        employee.setEmail("User");
        employee.setPosition("Tester");
        employee.setPhoneNumber("123456789");
        employee.setAddress("Test Address");
        employee.setGender(Gender.MALE);

        testAccount.setEmployee(employee);

        // Persist in database
        transaction.begin();
        entityManager.persist(employee);
        entityManager.persist(testAccount);
        transaction.commit();
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Account> accounts = accountDAO.getAll();

        // Assert
        assertNotNull(accounts);
        assertTrue(accounts.size() > 0);
    }

    @Test
    void testGetByUsername() throws RemoteException {
        // Act
        Account account = accountDAO.getByUsername(testUsername);

        // Assert
        assertNotNull(account);
        assertEquals(testUsername, account.getUsername());
        assertEquals(testEmail, account.getEmail());
    }

    @Test
    void testGetByEmail() throws RemoteException {
        // Act
        Account account = accountDAO.getByEmail(testEmail);

        // Assert
        assertNotNull(account);
        assertEquals(testUsername, account.getUsername());
        assertEquals(testEmail, account.getEmail());
    }

    @Test
    void testChangePassword_Success() throws RemoteException {
        // Arrange
        String newPassword = "newPassword123";

        // Act
        boolean result = accountDAO.changePassword(testEmail, newPassword);

        // Assert
        assertTrue(result);

        // Verify password was updated
        Account updatedAccount = accountDAO.getByEmail(testEmail);
        assertTrue(Utils.hashPassword(newPassword).equals(updatedAccount.getHashcode()));
    }

    @Test
    void testChangePassword_AccountNotFound() throws RemoteException {
        // Act
        boolean result = accountDAO.changePassword("nonexistent@example.com", "newpassword");

        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateEmail_Success() throws RemoteException {
        // Arrange
        String employeeId = accountDAO.getByUsername(testUsername).getEmployee().getId();
        String newEmail = "newemail-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        // Act
        boolean result = accountDAO.updateEmail(employeeId, newEmail);

        // Assert
        assertTrue(result);

        // Verify email was updated
        Account updatedAccount = accountDAO.getByUsername(testUsername);
        assertEquals(newEmail, updatedAccount.getEmail());
    }

    @Test
    void testUpdateEmail_AccountNotFound() throws RemoteException {
        // Act
        boolean result = accountDAO.updateEmail("NONEXISTENT", "newemail@example.com");

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetByEmployeeId() throws RemoteException {
        // Arrange
        String employeeId = accountDAO.getByUsername(testUsername).getEmployee().getId();

        // Act
        Account account = accountDAO.getByUsername(employeeId);

        // Assert
        assertNotNull(account);
        assertEquals(testUsername, account.getUsername());
        assertEquals(testEmail, account.getEmail());
    }

    @Test
    void testAddAccount() throws RemoteException {
        // Arrange
        Account newAccount = new Account();
        String newUsername = "newuser-" + UUID.randomUUID().toString().substring(0, 8);
        String newEmail = "newemail-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        newAccount.setUsername(newUsername);
        newAccount.setEmail(newEmail);
        newAccount.setHashcode(Utils.hashPassword("password123"));
        newAccount.setRole("USER");
        newAccount.setActive(true);

        // Act
        boolean result = accountDAO.addAccount(newAccount);

        // Assert
        assertTrue(result);

        // Verify account was added
        Account addedAccount = accountDAO.getByUsername(newUsername);
        assertNotNull(addedAccount);
        assertEquals(newEmail, addedAccount.getEmail());
    }

    @Test
    void testUpdateAccount() throws RemoteException {
        // Arrange
        Account account = accountDAO.getByUsername(testUsername);
        account.setRole("ADMIN");

        // Act
        boolean result = accountDAO.updateAccount(account);

        // Assert
        assertTrue(result);

        // Verify account was updated
        Account updatedAccount = accountDAO.getByUsername(testUsername);
        assertEquals("ADMIN", updatedAccount.getRole());
    }

    @Test
    void testSoftDelete() throws RemoteException {
        // Arrange
        Account account = accountDAO.getByUsername(testUsername);

        // Act
        boolean result = accountDAO.softDelete(account);

        // Assert
        assertTrue(result);

        // Verify account was soft deleted
        Account deletedAccount = accountDAO.getByUsername(testUsername);
        assertFalse(deletedAccount.isActive());
    }

    @Test
    void testGetByEmployeeId_notFound() throws RemoteException {
        // Act
        Account account = accountDAO.getByUsername("NONEXISTENT");

        // Assert
        assertNull(account);
    }

    @Test
    void testGetByUsername_notFound() throws RemoteException {
        // Act
        Account account = accountDAO.getByUsername("nonexistentuser");

        // Assert
        assertNull(account);
    }

    @Test
    void testGetByEmail_notFound() throws RemoteException {
        // Act
        Account account = accountDAO.getByEmail("nonexistent@example.com");

        // Assert
        assertNull(account);
    }
}