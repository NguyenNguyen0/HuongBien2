package huongbien.bus;

import huongbien.dao.PaymentDAO;
import huongbien.entity.Payment;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

public class PaymentBUS {
    private final PaymentDAO paymentDao;

    public PaymentBUS() {
        paymentDao = new PaymentDAO(PersistenceUnit.MARIADB_JPA);
    }

    public List<Payment> getAllPayment() {
        return paymentDao.getAll();
    }

    public Payment getPaymentById(String paymentId) {
        if (paymentId.isEmpty() || paymentId.isBlank()) return null;
        return paymentDao.getById(paymentId);
    }

    public boolean addPayment(Payment payment) {
        if (payment == null) return false;
        return paymentDao.add(payment);
    }
}
