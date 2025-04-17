package dao;

import huongbien.dao.impl.CustomerDAO;
import huongbien.entity.Customer;
import huongbien.entity.Gender;
import huongbien.entity.MembershipLevel;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDAOTest {

    private CustomerDAO customerDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private String testCustomerId;
    private String testPhoneNumber;
    private String testCustomerName;
    private LocalDate testRegistrationDate;

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test customer data
        testCustomerId = "CUS-" + UUID.randomUUID().toString().substring(0, 8);
        testPhoneNumber = "0" + (900000000 + (int)(Math.random() * 99999999));
        testCustomerName = "Test Customer " + UUID.randomUUID().toString().substring(0, 8);
        testRegistrationDate = LocalDate.now();

        // Setup test customer
        setupTestCustomer();
    }

    private void setupTestCustomer() {
        Customer testCustomer = new Customer();
        testCustomer.setId(testCustomerId);
        testCustomer.setName(testCustomerName);
        testCustomer.setPhoneNumber(testPhoneNumber);
        testCustomer.setRegistrationDate(testRegistrationDate);
        testCustomer.setMembershipLevel(MembershipLevel.BRONZE);
        testCustomer.setAccumulatedPoints(0);
        testCustomer.setEmail("test@example.com");
        testCustomer.setAddress("Test Address");
        testCustomer.setBirthday(LocalDate.of(1990, 1, 1));
        testCustomer.setGender(Gender.MALE);

        // Persist in database
        transaction.begin();
        entityManager.persist(testCustomer);
        transaction.commit();
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getAll();

        // Assert
        assertNotNull(customers);
        assertTrue(customers.size() > 0);
        assertTrue(customers.stream()
                .anyMatch(c -> c.getId().equals(testCustomerId) && c.getName().equals(testCustomerName)));
    }

    @Test
    void testGetByPhoneNumber() throws RemoteException {
        // Act
        Customer customer = customerDAO.getByPhoneNumber(testPhoneNumber);

        // Assert
        assertNotNull(customer);
        assertEquals(testCustomerId, customer.getId());
        assertEquals(testCustomerName, customer.getName());
    }

    @Test
    void testGetByPhoneNumberPattern() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getByPhoneNumberPattern(testPhoneNumber.substring(0, 5));

        // Assert
        assertNotNull(customers);
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals(testCustomerId)));
    }

    @Test
    void testGetByName() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getByName(testCustomerName.substring(5, 10));

        // Assert
        assertNotNull(customers);
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals(testCustomerId)));
    }

    @Test
    void testGetAllWithPagination() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getAllWithPagination(0, 10);

        // Assert
        assertNotNull(customers);
        assertTrue(customers.size() > 0);
    }

    @Test
    void testGetAllWithPaginationByPhoneNumber() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getAllWithPaginationByPhoneNumber(testPhoneNumber.substring(0, 5), 0, 10);

        // Assert
        assertNotNull(customers);
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals(testCustomerId)));
    }

    @Test
    void testGetAllWithPaginationByName() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getAllWithPaginationByName(testCustomerName.substring(5, 10), 0, 10);

        // Assert
        assertNotNull(customers);
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals(testCustomerId)));
    }

    @Test
    void testGetAllWithPaginationById() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getAllWithPaginationById(testCustomerId.substring(0, 6), 0, 10);

        // Assert
        assertNotNull(customers);
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals(testCustomerId)));
    }

    @Test
    void testGetCustomerInDay() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getCustomerInDay(testRegistrationDate);

        // Assert
        assertNotNull(customers);
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals(testCustomerId)));
    }

    @Test
    void testGetNewCustomersInYear() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getNewCustomersInYear(LocalDate.now().getYear());

        // Assert
        assertNotNull(customers);
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals(testCustomerId)));
    }

    @Test
    void testGetTopMembershipCustomers() throws RemoteException {
        // Act
        List<Customer> customers = customerDAO.getTopMembershipCustomers(LocalDate.now().getYear(), 10);

        // Assert
        assertNotNull(customers);
        // Note: We don't assert the test customer is in this list as it depends on sorting by membership level
    }

    @Test
    void testCountNewCustomerQuantityByYear() throws RemoteException {
        // Act
        int count = customerDAO.countNewCustomerQuantityByYear(LocalDate.now().getYear());

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalById() throws RemoteException {
        // Act
        int count = customerDAO.countTotalById(testCustomerId);

        // Assert
        assertEquals(1, count);
    }

    @Test
    void testCountTotal() throws RemoteException {
        // Act
        int count = customerDAO.countTotal();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalByPhoneNumber() throws RemoteException {
        // Act
        int count = customerDAO.countTotalByPhoneNumber(testPhoneNumber.substring(0, 5));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalByName() throws RemoteException {
        // Act
        int count = customerDAO.countTotalByName(testCustomerName.substring(5, 10));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testGetPhoneNumber() throws RemoteException {
        // Act
        List<String> phoneNumbers = customerDAO.getPhoneNumber();

        // Assert
        assertNotNull(phoneNumbers);
        assertTrue(phoneNumbers.contains(testPhoneNumber));
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        Customer customer = customerDAO.getById(testCustomerId);

        // Assert
        assertNotNull(customer);
        assertEquals(testCustomerId, customer.getId());
        assertEquals(testCustomerName, customer.getName());
    }

    @Test
    void testGetCustomerSearchReservation() throws RemoteException {
        // Act
        Customer customer = customerDAO.getCustomerSearchReservation(testPhoneNumber.substring(1, 6));

        // Assert
        assertNotNull(customer);
        assertEquals(testCustomerId, customer.getId());
    }

    @Test
    void testIncreasePoint() throws RemoteException {
        // Arrange
        int initialPoints = customerDAO.getById(testCustomerId).getAccumulatedPoints();
        int pointsToAdd = 50;

        // Act
        boolean result = customerDAO.increasePoint(testCustomerId, pointsToAdd);

        // Assert
        assertTrue(result);

        // Verify points were increased
        Customer updatedCustomer = customerDAO.getById(testCustomerId);
        assertEquals(initialPoints + pointsToAdd, updatedCustomer.getAccumulatedPoints());
    }

    @Test
    void testAddCustomer() throws RemoteException {
        // Arrange
        String newCustomerId = "CUS-" + UUID.randomUUID().toString().substring(0, 8);
        String newPhoneNumber = "0" + (900000000 + (int)(Math.random() * 99999999));
        String newCustomerName = "New Customer " + UUID.randomUUID().toString().substring(0, 8);

        Customer newCustomer = new Customer();
        newCustomer.setId(newCustomerId);
        newCustomer.setName(newCustomerName);
        newCustomer.setPhoneNumber(newPhoneNumber);
        newCustomer.setRegistrationDate(LocalDate.now());
        newCustomer.setMembershipLevel(MembershipLevel.BRONZE);
        newCustomer.setAccumulatedPoints(0);
        newCustomer.setEmail("new@example.com");
        newCustomer.setAddress("New Address");
        newCustomer.setBirthday(LocalDate.of(1995, 5, 5));
        newCustomer.setGender(Gender.FEMALE);

        // Act
        boolean result = customerDAO.add(newCustomer);

        // Assert
        assertTrue(result);

        // Verify customer was added
        Customer addedCustomer = customerDAO.getById(newCustomerId);
        assertNotNull(addedCustomer);
        assertEquals(newCustomerName, addedCustomer.getName());
    }

    @Test
    void testUpdateCustomer() throws RemoteException {
        // Arrange
        Customer customer = customerDAO.getById(testCustomerId);
        String updatedAddress = "Updated address " + UUID.randomUUID().toString();
        customer.setAddress(updatedAddress);

        // Act
        boolean result = customerDAO.update(customer);

        // Assert
        assertTrue(result);

        // Verify customer was updated
        Customer updatedCustomer = customerDAO.getById(testCustomerId);
        assertEquals(updatedAddress, updatedCustomer.getAddress());
    }

    @Test
    void testDeleteCustomer() throws RemoteException {
        // Arrange
        // Create a temporary customer to delete
        String tempCustomerId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
        String tempPhoneNumber = "0" + (900000000 + (int)(Math.random() * 99999999));
        
        Customer tempCustomer = new Customer();
        tempCustomer.setId(tempCustomerId);
        tempCustomer.setName("Temporary Customer");
        tempCustomer.setPhoneNumber(tempPhoneNumber);
        tempCustomer.setRegistrationDate(LocalDate.now());
        tempCustomer.setMembershipLevel(MembershipLevel.BRONZE);
        tempCustomer.setAccumulatedPoints(0);
        tempCustomer.setEmail("temp@example.com");

        // Add the customer first
        transaction.begin();
        entityManager.persist(tempCustomer);
        transaction.commit();

        // Verify it exists
        Customer addedCustomer = customerDAO.getById(tempCustomerId);
        assertNotNull(addedCustomer);

        // Act
        boolean result = customerDAO.delete(addedCustomer);

        // Assert
        assertTrue(result);

        // Verify customer was deleted
        Customer deletedCustomer = customerDAO.getById(tempCustomerId);
        assertNull(deletedCustomer);
    }
}