package dao;

import huongbien.dao.impl.*;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TableDAOTest {

    private TableDAO tableDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    
    private String testTableId;
    private String testTableName;
    private int testFloor;
    private String testReservationId;
    private String testOrderId;

    private TableType getTableTypeById(String typeId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM TableType t WHERE t.id = :id", TableType.class)
                    .setParameter("id", typeId)
                    .getSingleResult();
        } catch (Exception e) {
            // If query fails, create a new TableType object with fallback values
            TableType fallbackType = new TableType();
            fallbackType.setId(typeId);
            if (typeId.equals("LB001")) {
                fallbackType.setName("Thường");
            } else {
                fallbackType.setName("VIP");
            }
            return fallbackType;
        }
    }

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        tableDAO = new TableDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test data
        testTableId = "TBL-" + UUID.randomUUID().toString().substring(0, 8);
        testTableName = "Test Table " + UUID.randomUUID().toString().substring(0, 8);
        testFloor = 2;
        testReservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8);
        testOrderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);

        // Setup test data
        setupTestTable();
        setupTestReservation();
        setupTestOrder();
    }

    private void setupTestTable() {
        Table testTable = new Table();
        testTable.setId(testTableId);
        testTable.setName(testTableName);
        testTable.setFloor(testFloor);
        testTable.setSeats(4);
        testTable.setStatus(TableStatus.AVAILABLE);
        testTable.setTableType(getTableTypeById("LB001"));

        // Persist in database
        transaction.begin();
        entityManager.persist(testTable);
        transaction.commit();
        entityManager.clear();
    }

    private Customer testCustomer;
    private Employee testEmployee;

    private void setupTestReservation() throws RemoteException {
        // Setup test customer and employee first
        setupTestCustomer();
        setupTestEmployee();

        // First get the table that was created in setupTestTable()
        Table table = entityManager.find(Table.class, testTableId);
        if (table == null) {
            throw new RuntimeException("Test table not found");
        }

        // Create a reservation object
        Reservation reservation = new Reservation();
        reservation.setId(testReservationId);
        reservation.setCustomer(testCustomer);
        reservation.setEmployee(testEmployee);
        reservation.setReservationDate(LocalDate.now());
        reservation.setReceiveDate(LocalDate.now().plusDays(1));
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setDeposit(50000);
        reservation.setPartySize(4);

        // Create a new list for tables and add our test table
        List<Table> tables = new ArrayList<>();
        tables.add(table);
        reservation.setTables(tables);

        // Use ReservationDAO to persist
        ReservationDAO reservationDAO = new ReservationDAO(PersistenceUnit.MARIADB_JPA);
        reservationDAO.add(reservation);
    }

    private void setupTestOrder() throws RemoteException {
        // Create an order object
        Order order = new Order();
        order.setId(testOrderId);
        order.setOrderDate(LocalDate.now());
        order.setOrderTime(LocalTime.now());
        order.setTotalAmount(100000);
        order.setCustomer(testCustomer);
        order.setEmployee(testEmployee);

        // Fetch the table first to ensure it exists
        Table table = entityManager.find(Table.class, testTableId);
        if (table == null) {
            throw new RuntimeException("Test table not found");
        }

        // Add table to order - create a new list
        List<Table> tables = new ArrayList<>();
        tables.add(table);
        order.setTables(tables);

        // Use OrderDAO to persist
        OrderDAO orderDAO = new OrderDAO(PersistenceUnit.MARIADB_JPA);
        orderDAO.addOrder(order);
    }

    private void setupTestCustomer() throws RemoteException {
        // Create a Customer object
        testCustomer = new Customer();
        testCustomer.setId("CUS-" + UUID.randomUUID().toString().substring(0, 8));
        testCustomer.setName("Test Customer");
        testCustomer.setPhoneNumber("0987654321");
        testCustomer.setRegistrationDate(LocalDate.now());

        // Use CustomerDAO to persist
        CustomerDAO customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);
        customerDAO.add(testCustomer);
    }

    private void setupTestEmployee() throws RemoteException {
        // Create an Employee object
        testEmployee = new Employee();
        testEmployee.setId("EMP-" + UUID.randomUUID().toString().substring(0, 8));
        testEmployee.setName("Test Employee");
        testEmployee.setPhoneNumber("0123456789");
        testEmployee.setPosition("Waiter");
        testEmployee.setStatus("Đang làm");

        // Use EmployeeDAO to persist
        EmployeeDAO employeeDAO = new EmployeeDAO(PersistenceUnit.MARIADB_JPA);
        employeeDAO.add(testEmployee);
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getAll();

        // Assert
        assertNotNull(tables);
        assertTrue(tables.size() > 0);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        Table table = tableDAO.getById(testTableId);

        // Assert
        assertNotNull(table);
        assertEquals(testTableId, table.getId());
        assertEquals(testTableName, table.getName());
        assertEquals(testFloor, table.getFloor());
    }

    @Test
    void testGetByName() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getByName(testTableName.substring(5, 10));

        // Assert
        assertNotNull(tables);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetAllByReservationId() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getAllByReservationId(testReservationId);

        // Assert
        assertNotNull(tables);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetAllByOrderId() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getAllByOrderId(testOrderId);

        // Assert
        assertNotNull(tables);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetCountStatisticalOverviewTable() throws RemoteException {
        // Act
        int count = tableDAO.getCountStatisticalOverviewTable();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testGetCountStatisticalOverviewTableEmpty() throws RemoteException {
        // Act
        int count = tableDAO.getCountStatisticalOverviewTableEmpty();

        // Assert
        assertTrue(count >= 0);
    }

    @Test
    void testGetCountStatisticalFloorTable() throws RemoteException {
        // Act
        int count = tableDAO.getCountStatisticalFloorTable(testFloor);

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testGetCountStatisticalFloorTableEmpty() throws RemoteException {
        // Act
        int count = tableDAO.getCountStatisticalFloorTableEmpty(testFloor);

        // Assert
        assertTrue(count >= 0);
    }

    @Test
    void testGetByCriteria() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getByCriteria(
                String.valueOf(testFloor), TableStatus.AVAILABLE.getValue(), "Thường", "4");

        // Assert
        assertNotNull(tables);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetDistinctFloor() throws RemoteException {
        // Act
        List<Integer> floors = tableDAO.getDistinctFloor();

        // Assert
        assertNotNull(floors);
        assertTrue(floors.contains(testFloor));
    }

    @Test
    void testGetDistinctSeat() throws RemoteException {
        // Act
        List<String> seats = tableDAO.getDistinctSeat();

        // Assert
        assertNotNull(seats);
        assertTrue(seats.contains("4"));
    }

    @Test
    void testGetTopFloor() throws RemoteException {
        // Act
        Table table = tableDAO.getTopFloor();

        // Assert
        assertNotNull(table);
    }

    @Test
    void testGetReservedTables() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getReservedTables(LocalDate.now().plusDays(1));

        // Assert
        assertNotNull(tables);
        // There should be at least our test table since we linked it to a reservation
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetDistinctStatuses() throws RemoteException {
        // Act
        List<String> statuses = tableDAO.getDistinctStatuses();

        // Assert
        assertNotNull(statuses);
        assertTrue(statuses.contains(TableStatus.AVAILABLE));
    }

    @Test
    void testGetDistinctFloors() throws RemoteException {
        // Act
        List<Integer> floors = tableDAO.getDistinctFloors();

        // Assert
        assertNotNull(floors);
        assertTrue(floors.contains(testFloor));
    }

    @Test
    void testGetDistinctSeats() throws RemoteException {
        // Act
        List<Integer> seats = tableDAO.getDistinctSeats();

        // Assert
        assertNotNull(seats);
        assertTrue(seats.contains(4));
    }

    @Test
    void testGetDistinctTableType() throws RemoteException {
        // Act
        List<String> types = tableDAO.getDistinctTableType();

        // Assert
        System.out.println("Distinct Table Types: " + types);
        assertNotNull(types);
        assertTrue(types.contains("Thường"));
    }

    @Test
    void testGetLookUpTable() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getLookUpTable(
                testFloor, testTableName.substring(5, 10), 4, "Thường", String.valueOf(TableStatus.AVAILABLE), 0);

        // Assert
        assertNotNull(tables);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetCountLookUpTable() throws RemoteException {
        // Act
        int count = tableDAO.getCountLookUpTable(
                testFloor, testTableName.substring(5, 10), 4, "Thường", String.valueOf(TableStatus.AVAILABLE));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testGetAllWithPagination() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getAllWithPagination(0, 10);

        // Assert
        assertNotNull(tables);
        assertTrue(tables.size() > 0);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetByNameWithPagination() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getByNameWithPagination(testTableName.substring(5, 10), 0, 10);

        // Assert
        assertNotNull(tables);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testGetByFloorWithPagination() throws RemoteException {
        // Act
        List<Table> tables = tableDAO.getByFloorWithPagination(0, 10, testFloor);

        // Assert
        assertNotNull(tables);
        assertTrue(tables.stream().anyMatch(t -> t.getId().equals(testTableId)));
    }

    @Test
    void testCountTotalTables() throws RemoteException {
        // Act
        int count = tableDAO.countTotalTables();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalTablesByFloor() throws RemoteException {
        // Act
        int count = tableDAO.countTotalTablesByFloor(testFloor);

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalTablesByName() throws RemoteException {
        // Act
        int count = tableDAO.countTotalTablesByName(testTableName.substring(5, 10));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testUpdateStatusTable() throws RemoteException {
        // Act
        tableDAO.updateStatusTable(testTableId, TableStatus.RESERVED);

        // Assert
        // Verify status was updated
        Table updatedTable = tableDAO.getById(testTableId);
        assertEquals(TableStatus.RESERVED.toString(), updatedTable.getStatus());
    }

    @Test
    void testAddTable() throws RemoteException {
        // Arrange
        String newTableId = "TBL-" + UUID.randomUUID().toString().substring(0, 8);
        String newTableName = "New Table " + UUID.randomUUID().toString().substring(0, 8);
        
        Table newTable = new Table();
        newTable.setId(newTableId);
        newTable.setName(newTableName);
        newTable.setFloor(3);
        newTable.setSeats(6);
        newTable.setStatus(TableStatus.AVAILABLE);
        newTable.setTableType(getTableTypeById("LB002"));

        // Act
        boolean result = tableDAO.add(newTable);

        // Assert
        assertTrue(result);

        // Verify table was added
        Table addedTable = tableDAO.getById(newTableId);
        assertNotNull(addedTable);
        assertEquals(newTableName, addedTable.getName());
        assertEquals(3, addedTable.getFloor());
        assertEquals(6, addedTable.getSeats());
    }

    @Test
    void testUpdateTable() throws RemoteException {
        // Arrange
        Table table = tableDAO.getById(testTableId);
        table.setName("Updated " + testTableName);
        table.setSeats(8);

        // Act
        boolean result = tableDAO.update(table);

        // Assert
        assertTrue(result);

        // Verify table was updated
        Table updatedTable = tableDAO.getById(testTableId);
        assertEquals("Updated " + testTableName, updatedTable.getName());
        assertEquals(8, updatedTable.getSeats());
    }

    @Test
    void testDeleteTable() throws RemoteException {
        // Arrange
        // Create a temporary table to delete
        String tempTableId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
        
        Table tempTable = new Table();
        tempTable.setId(tempTableId);
        tempTable.setName("Temporary Table");
        tempTable.setFloor(1);
        tempTable.setSeats(2);
        tempTable.setStatus(TableStatus.AVAILABLE);
        tempTable.setTableType(getTableTypeById("LB001"));

        // Add the table first
        transaction.begin();
        entityManager.persist(tempTable);
        transaction.commit();

        // Verify it exists
        Table addedTable = tableDAO.getById(tempTableId);
        assertNotNull(addedTable);

        // Act
        boolean result = tableDAO.delete(addedTable);

        // Assert
        assertTrue(result);

        // Verify table was deleted
        Table deletedTable = tableDAO.getById(tempTableId);
        assertNull(deletedTable);
    }
}