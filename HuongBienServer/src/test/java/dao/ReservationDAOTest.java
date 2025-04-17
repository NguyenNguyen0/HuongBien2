package dao;

import huongbien.dao.impl.CustomerDAO;
import huongbien.dao.impl.EmployeeDAO;
import huongbien.dao.impl.ReservationDAO;
import huongbien.entity.*;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDAOTest {

    private ReservationDAO reservationDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    
    private String testReservationId;
    private Customer testCustomer;
    private Employee testEmployee;
    private String testTableId;
    // Get customer and employee from database with specific IDs
    private Customer getCustomer() throws RemoteException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.id = :id", Customer.class)
                    .setParameter("id", "KH230102547")
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private Employee getEmployee() throws RemoteException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employee e WHERE e.id = :id", Employee.class)
                    .setParameter("id", "NV001122003")
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        reservationDAO = new ReservationDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test data
        testReservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8);
        testCustomer = getCustomer();
        testEmployee = getEmployee();
        testTableId = "TBL-" + UUID.randomUUID().toString().substring(0, 8);

        // Setup test data
        setupTestCustomer();
        setupTestEmployee();
        setupTestTable();
        setupTestReservation();
    }

    private void setupTestCustomer() throws RemoteException {
        // Create a Customer object
        Customer customer = new Customer();
        customer.setId(testCustomer.getId());
        customer.setName("Test Customer");
        customer.setPhoneNumber("0987654321");
        customer.setRegistrationDate(LocalDate.now());

        // Use CustomerDAO to persist
        CustomerDAO customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);
        customerDAO.add(customer);
    }

    private void setupTestEmployee() throws RemoteException {
        // Create an Employee object
        Employee employee = new Employee();
        employee.setId(testEmployee.getId());
        employee.setName("Test Employee");
        employee.setPhoneNumber("0123456789");
        employee.setPosition("Waiter");
        employee.setStatus("Đang làm");

        // Use EmployeeDAO to persist
        EmployeeDAO employeeDAO = new EmployeeDAO(PersistenceUnit.MARIADB_JPA);
        employeeDAO.add(employee);
    }

    private void setupTestTable() {
        // Create a test table
        Table table = new Table();
        table.setId(testTableId);
        table.setName("Test Table");
        table.setFloor(1);
        table.setSeats(4);
        table.setStatus(TableStatus.AVAILABLE);

        transaction.begin();
        entityManager.persist(table);
        transaction.commit();
    }

    private void setupTestReservation() {
        // Create a test reservation
        Reservation reservation = new Reservation();
        reservation.setId(testReservationId);
        reservation.setCustomer(testCustomer);
        reservation.setEmployee(testEmployee);
        reservation.setReservationDate(LocalDate.now());
        reservation.setReceiveDate(LocalDate.now().plusDays(2));
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setDeposit(50000);
        reservation.setNote("Test reservation");
        reservation.setPartySize(4);

        List<Table> tables = new ArrayList<>();
        tables.add(entityManager.find(Table.class, testTableId));
        reservation.setTables(tables);

        // Persist in database
        transaction.begin();
        entityManager.persist(reservation);
        
        // Create reservation-table relationship
        entityManager.createNativeQuery(
                "INSERT INTO reservations_tables (reservation_id, table_id) VALUES (?, ?)")
                .setParameter(1, testReservationId)
                .setParameter(2, testTableId)
                .executeUpdate();
                
        transaction.commit();
    }

    @Test
    void testGetAllByCustomerId() throws RemoteException {
        // Act
        List<Reservation> reservations = reservationDAO.getAllByCustomerId(testCustomer.getId());

        // Assert
        assertNotNull(reservations);
        assertTrue(reservations.size() > 0);
        assertTrue(reservations.stream().anyMatch(r -> r.getId().equals(testReservationId)));
    }

    @Test
    void testGetAllByEmployeeId() throws RemoteException {
        // Act
        List<Reservation> reservations = reservationDAO.getAllByEmployeeId(testEmployee.getId());

        // Assert
        assertNotNull(reservations);
        assertTrue(reservations.size() > 0);
        assertTrue(reservations.stream().anyMatch(r -> r.getId().equals(testReservationId)));
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Reservation> reservations = reservationDAO.getAll();

        // Assert
        assertNotNull(reservations);
        assertTrue(reservations.size() > 0);
        assertTrue(reservations.stream().anyMatch(r -> r.getId().equals(testReservationId)));
    }

    @Test
    void testGetStatusReservationByDate() throws RemoteException {
        // Act
        List<Reservation> reservations = reservationDAO.getStatusReservationByDate(
                LocalDate.now().plusDays(2), "Chưa xác nhận", testCustomer.getId());

        // Assert
        assertNotNull(reservations);
    }

    @Test
    void testGetCountStatusReservationByDate() throws RemoteException {
        // Act
        int count = reservationDAO.getCountStatusReservationByDate(
                LocalDate.now().plusDays(2), "WAITING", testCustomer.getId());

        // Assert
        assertTrue(count >= 0); // At least 0, might be more if there are existing reservations
    }

    @Test
    void testGetListWaitedToday() throws RemoteException {
        // Act
        List<Reservation> reservations = reservationDAO.getListWaitedToday();

        // Assert
        assertNotNull(reservations);
        // We might not have a waiting reservation for today in our test data
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        Reservation reservation = reservationDAO.getById(testReservationId);

        // Assert
        assertNotNull(reservation);
        assertEquals(testReservationId, reservation.getId());
        assertEquals(testCustomer, reservation.getCustomer().getId());
        assertEquals(testEmployee, reservation.getEmployee().getId());
    }

    @Test
    void testGetLookUpReservation() throws RemoteException {
        // Act
        List<Reservation> reservations = reservationDAO.getLookUpReservation(
                testReservationId.substring(0, 5), testCustomer.getId().substring(0, 5), null,
                LocalDate.now().plusDays(2), 0);

        // Assert
        assertNotNull(reservations);
    }

    @Test
    void testGetCountLookUpReservation() throws RemoteException {
        // Act
        int count = reservationDAO.getCountLookUpReservation(
                testReservationId.substring(0, 5), testCustomer.getId().substring(0, 5), null,
                LocalDate.now().plusDays(2));

        // Assert
        assertTrue(count >= 0);
    }

    @Test
    void testCountTotal() throws RemoteException {
        // Act
        int count = reservationDAO.countTotal();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testGetListTableStatusToday() throws RemoteException {
        // Act
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservationDAO.getById(testReservationId));
        
        List<Table> tables = reservationDAO.getListTableStatusToday(reservations);

        // Assert
        assertNotNull(tables);
    }

    @Test
    void testUpdateStatus() throws RemoteException {
        // Act
        reservationDAO.updateStatus(testReservationId, ReservationStatus.CONFIRMED);

        // Verify status was updated
        Reservation updatedReservation = reservationDAO.getById(testReservationId);
        assertEquals(ReservationStatus.CONFIRMED, updatedReservation.getStatus());
    }

    @Test
    void testUpdateRefundDeposit() throws RemoteException {
        // Arrange
        double refundDeposit = 30000;

        // Act
        reservationDAO.updateRefundDeposit(testReservationId, refundDeposit);

        // Assert
        // Verify refund deposit was updated
        Reservation updatedReservation = reservationDAO.getById(testReservationId);
        assertEquals(refundDeposit, updatedReservation.getRefundDeposit());
    }

    @Test
    void testAddReservation() throws RemoteException {
        // Arrange
        String newReservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8);
        
        Reservation newReservation = new Reservation();
        newReservation.setId(newReservationId);
        newReservation.setCustomer(testCustomer);
        newReservation.setEmployee(testEmployee);
        newReservation.setReservationDate(LocalDate.now());
        newReservation.setReceiveDate(LocalDate.now().plusDays(3));
        newReservation.setStatus(ReservationStatus.PENDING);
        newReservation.setDeposit(70000);
        newReservation.setNote("New test reservation");
        newReservation.setPartySize(6);

        List<Table> tables = new ArrayList<>();
        tables.add(entityManager.find(Table.class, testTableId));
        newReservation.setTables(tables);

        // Act
        boolean result = reservationDAO.add(newReservation);

        // Assert
        assertTrue(result);

        // Verify reservation was added
        Reservation addedReservation = reservationDAO.getById(newReservationId);
        assertNotNull(addedReservation);
        assertEquals(newReservationId, addedReservation.getId());
        assertEquals(70000, addedReservation.getDeposit());
    }

    @Test
    void testUpdateReservation() throws RemoteException {
        // Arrange
        Reservation reservation = reservationDAO.getById(testReservationId);
        String updatedNote = "Updated note " + UUID.randomUUID().toString();
        reservation.setNote(updatedNote);

        // Act
        boolean result = reservationDAO.update(reservation);

        // Assert
        assertTrue(result);

        // Verify reservation was updated
        Reservation updatedReservation = reservationDAO.getById(testReservationId);
        assertEquals(updatedNote, updatedReservation.getNote());
    }

    @Test
    void testDeleteReservation() throws RemoteException {
        // Arrange
        // Create a temporary reservation to delete
        String tempReservationId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
        
        Reservation tempReservation = new Reservation();
        tempReservation.setId(tempReservationId);
        tempReservation.setCustomer(testCustomer);
        tempReservation.setEmployee(testEmployee);
        tempReservation.setReservationDate(LocalDate.now());
        tempReservation.setReceiveDate(LocalDate.now().plusDays(1));
        tempReservation.setStatus(ReservationStatus.PENDING);
        tempReservation.setDeposit(20000);

        // Add the reservation first
        transaction.begin();
        entityManager.persist(tempReservation);
        transaction.commit();

        // Verify it exists
        Reservation addedReservation = reservationDAO.getById(tempReservationId);
        assertNotNull(addedReservation);

        // Act
        boolean result = reservationDAO.delete(addedReservation);

        // Assert
        assertTrue(result);

        // Verify reservation was deleted
        Reservation deletedReservation = reservationDAO.getById(tempReservationId);
        assertNull(deletedReservation);
    }
}