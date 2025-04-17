package dao;

import huongbien.dao.impl.OrderDAO;
import huongbien.dao.impl.CustomerDAO;
import huongbien.dao.impl.EmployeeDAO;
import huongbien.entity.*;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTest {

    private OrderDAO orderDAO;
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    
    private String testOrderId;
    private String testCustomerId;
    private String testEmployeeId;
    private String testTableId;

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        orderDAO = new OrderDAO(PersistenceUnit.MARIADB_JPA);
        customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);
        employeeDAO = new EmployeeDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test data
        testOrderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        testCustomerId = "CUS-" + UUID.randomUUID().toString().substring(0, 8);
        testEmployeeId = "EMP-" + UUID.randomUUID().toString().substring(0, 8);
        testTableId = "TBL-" + UUID.randomUUID().toString().substring(0, 8);

        // Setup test data
        setupTestCustomer();
        setupTestEmployee();
        setupTestTable();
        setupTestOrder();
    }

    private void setupTestCustomer() {
        Customer customer = new Customer();
        customer.setId(testCustomerId);
        customer.setName("Test Customer");
        customer.setPhoneNumber("0987654321");
        customer.setEmail("customer@example.com");
        customer.setRegistrationDate(LocalDate.now());

        // Persist in database
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();
    }

    private void setupTestEmployee() {
        Employee employee = new Employee();
        employee.setId(testEmployeeId);
        employee.setName("Test Employee");
        employee.setPhoneNumber("0123456789");
        employee.setEmail("employee@example.com");
        employee.setPosition("Waiter");
        employee.setStatus("Đang làm");

        // Persist in database
        transaction.begin();
        entityManager.persist(employee);
        transaction.commit();
    }

    private void setupTestTable() {
        Table table = new Table();
        table.setId(testTableId);
        table.setName("Test Table");
        table.setFloor(1);
        table.setSeats(4);
        table.setStatus(TableStatus.AVAILABLE);

        // Persist in database
        transaction.begin();
        entityManager.persist(table);
        transaction.commit();
    }

    private void setupTestOrder() {
        Customer customer = entityManager.find(Customer.class, testCustomerId);
        Employee employee = entityManager.find(Employee.class, testEmployeeId);
        Table table = entityManager.find(Table.class, testTableId);
        
        Order order = new Order();
        order.setId(testOrderId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(100000);
        order.setCustomer(customer);
        order.setEmployee(employee);
        
        List<Table> tables = new ArrayList<>();
        tables.add(table);
        order.setTables(tables);

        // Persist in database
        transaction.begin();
        entityManager.persist(order);
        
        // Create order-table relationship
        entityManager.createNativeQuery("INSERT INTO orders_tables (order_id, table_id) VALUES (?, ?)")
                .setParameter(1, testOrderId)
                .setParameter(2, testTableId)
                .executeUpdate();
                
        transaction.commit();
    }

    @Test
    void testGetAllByCustomerId() throws RemoteException {
        // Act
        List<Order> orders = orderDAO.getAllByCustomerId(testCustomerId);

        // Assert
        assertNotNull(orders);
        assertTrue(orders.size() > 0);
        assertTrue(orders.stream().anyMatch(o -> o.getId().equals(testOrderId)));
    }

    @Test
    void testGetAllByEmployeeId() throws RemoteException {
        // Act
        List<Order> orders = orderDAO.getAllByEmployeeId(testEmployeeId);

        // Assert
        assertNotNull(orders);
        assertTrue(orders.size() > 0);
        assertTrue(orders.stream().anyMatch(o -> o.getId().equals(testOrderId)));
    }

    @Test
    void testGetAllWithPagination() throws RemoteException {
        // Act
        List<Order> orders = orderDAO.getAllWithPagination(0, 10);

        // Assert
        assertNotNull(orders);
        assertTrue(orders.size() > 0);
    }

    @Test
    void testGetAllByCustomerPhoneNumberWithPagination() throws RemoteException {
        // Act
        List<Order> orders = orderDAO.getAllByCustomerPhoneNumberWithPagination(0, 10, "0987");

        // Assert
        assertNotNull(orders);
        assertTrue(orders.stream().anyMatch(o -> o.getId().equals(testOrderId)));
    }

    @Test
    void testGetAllByEmployeeIdWithPagination() throws RemoteException {
        // Act
        List<Order> orders = orderDAO.getAllByEmployeeIdWithPagination(0, 10, testEmployeeId.substring(0, 5));

        // Assert
        assertNotNull(orders);
        assertTrue(orders.stream().anyMatch(o -> o.getId().equals(testOrderId)));
    }

    @Test
    void testGetAllByIdWithPagination() throws RemoteException {
        // Act
        List<Order> orders = orderDAO.getAllByIdWithPagination(0, 10, testOrderId.substring(0, 5));

        // Assert
        assertNotNull(orders);
        assertTrue(orders.stream().anyMatch(o -> o.getId().equals(testOrderId)));
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        Order order = orderDAO.getById(testOrderId);

        // Assert
        assertNotNull(order);
        assertEquals(testOrderId, order.getId());
        assertEquals(testCustomerId, order.getCustomer().getId());
        assertEquals(testEmployeeId, order.getEmployee().getId());
    }

    @Test
    void testCountTotal() throws RemoteException {
        // Act
        int count = orderDAO.countTotal();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalByOrderId() throws RemoteException {
        // Act
        int count = orderDAO.countTotalByOrderId(testOrderId.substring(0, 5));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalByCustomerPhoneNumber() throws RemoteException {
        // Act
        int count = orderDAO.countTotalByCustomerPhoneNumber("0987");

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalByEmployeeId() throws RemoteException {
        // Act
        int count = orderDAO.countTotalByEmployeeId(testEmployeeId.substring(0, 5));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testAddOrder() throws RemoteException {
        // Arrange
        String newOrderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        
        Customer customer = entityManager.find(Customer.class, testCustomerId);
        Employee employee = entityManager.find(Employee.class, testEmployeeId);
        Table table = entityManager.find(Table.class, testTableId);
        
        Order newOrder = new Order();
        newOrder.setId(newOrderId);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setTotalAmount(150000);
        newOrder.setCustomer(customer);
        newOrder.setEmployee(employee);
        
        List<Table> tables = new ArrayList<>();
        tables.add(table);
        newOrder.setTables(tables);

        // Act
        boolean result = orderDAO.addOrder(newOrder);

        // Assert
        assertTrue(result);

        // Verify order was added
        Order addedOrder = orderDAO.getById(newOrderId);
        assertNotNull(addedOrder);
        assertEquals(newOrderId, addedOrder.getId());
        assertEquals(150000, addedOrder.getTotalAmount());
    }
}