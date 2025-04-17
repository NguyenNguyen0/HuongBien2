package dao;

import huongbien.dao.impl.PaymentDAO;
import huongbien.entity.Payment;
import huongbien.entity.PaymentMethod;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDAOTest {

    private PaymentDAO paymentDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    
    private String testPaymentId;

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        paymentDAO = new PaymentDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test payment ID
        testPaymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);

        // Setup test payment
        setupTestPayment();
    }

    private void setupTestPayment() {
        Payment testPayment = new Payment();
        testPayment.setPaymentId(testPaymentId);
        testPayment.setPaymentMethod(PaymentMethod.CASH);
        testPayment.setPaymentDate(LocalDate.now());
        testPayment.setAmount(150000);

        // Persist in database
        transaction.begin();
        entityManager.persist(testPayment);
        transaction.commit();
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Payment> payments = paymentDAO.getAll();

        // Assert
        assertNotNull(payments);
        assertTrue(payments.size() > 0);
        assertTrue(payments.stream().anyMatch(p -> p.getPaymentId().equals(testPaymentId)));
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        Payment payment = paymentDAO.getById(testPaymentId);

        // Assert
        assertNotNull(payment);
        assertEquals(testPaymentId, payment.getPaymentId());
        assertEquals(PaymentMethod.CASH, payment.getPaymentMethod());
        assertEquals(150000, payment.getAmount());
    }

    @Test
    void testAddPayment() throws RemoteException {
        // Arrange
        String newPaymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
        
        Payment newPayment = new Payment();
        newPayment.setPaymentId(newPaymentId);
        newPayment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        newPayment.setPaymentDate(LocalDate.now());
        newPayment.setAmount(200000);

        // Act
        boolean result = paymentDAO.add(newPayment);

        // Assert
        assertTrue(result);

        // Verify payment was added
        Payment addedPayment = paymentDAO.getById(newPaymentId);
        assertNotNull(addedPayment);
        assertEquals(PaymentMethod.CREDIT_CARD, addedPayment.getPaymentMethod());
        assertEquals(200000, addedPayment.getAmount());
    }

    @Test
    void testUpdatePayment() throws RemoteException {
        // Arrange
        Payment payment = paymentDAO.getById(testPaymentId);
        payment.setAmount(180000);
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        // Act
        boolean result = paymentDAO.update(payment);

        // Assert
        assertTrue(result);

        // Verify payment was updated
        Payment updatedPayment = paymentDAO.getById(testPaymentId);
        assertEquals(180000, updatedPayment.getAmount());
        assertEquals(PaymentMethod.CREDIT_CARD, updatedPayment.getPaymentMethod());
    }

    @Test
    void testDeletePayment() throws RemoteException {
        // Arrange
        // Create a temporary payment to delete
        String tempPaymentId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
        
        Payment tempPayment = new Payment();
        tempPayment.setPaymentId(tempPaymentId);
        tempPayment.setPaymentMethod(PaymentMethod.CASH);
        tempPayment.setPaymentDate(LocalDate.now());
        tempPayment.setAmount(100000);

        // Add the payment first
        transaction.begin();
        entityManager.persist(tempPayment);
        transaction.commit();

        // Verify it exists
        Payment addedPayment = paymentDAO.getById(tempPaymentId);
        assertNotNull(addedPayment);

        // Act
        boolean result = paymentDAO.delete(addedPayment);

        // Assert
        assertTrue(result);

        // Verify payment was deleted
        Payment deletedPayment = paymentDAO.getById(tempPaymentId);
        assertNull(deletedPayment);
    }
}