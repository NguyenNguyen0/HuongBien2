package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.Payment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDAOTest {
    private static final PaymentDAO paymentDAO = new PaymentDAO();

    @Test
    void add() {
        Payment payment = DataGenerator.fakePayment(1000.0, LocalDate.now(), LocalTime.now());
        paymentDAO.add(payment);
        Payment retrieved = paymentDAO.getById(payment.getPaymentId());
        assertNotNull(retrieved);
        assertEquals(payment.getPaymentId(), retrieved.getPaymentId());
    }

    @Test
    void update() {
        Payment payment = DataGenerator.fakePayment(1000.0, LocalDate.now(), LocalTime.now());
        paymentDAO.add(payment);

        payment.setAmount(2000.0);
        paymentDAO.update(payment);

        Payment updatedPayment = paymentDAO.getById(payment.getPaymentId());
        assertNotNull(updatedPayment);
        assertEquals(2000.0, updatedPayment.getAmount());
    }

    @Test
    void getAll() {
        assertNotNull(paymentDAO.getAll());
    }

    @Test
    void getById() {
        assertNull(paymentDAO.getById("1"));
        assertNotNull(paymentDAO.getById("TT251231145207769"));
    }
}