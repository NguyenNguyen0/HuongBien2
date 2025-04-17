package huongbien.bus;

import huongbien.dao.impl.PaymentDAO;
import huongbien.entity.Payment;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;

public class PaymentBUS {
    private final PaymentDAO paymentDao;

    public PaymentBUS() throws RemoteException {
        paymentDao = new PaymentDAO(PersistenceUnit.MARIADB_JPA);
    }

    public List<Payment> getAllPayment() throws RemoteException {
        return paymentDao.getAll();
    }

    public Payment getPaymentById(String paymentId) throws RemoteException {
        if (paymentId.isEmpty() || paymentId.isBlank()) return null;
        return paymentDao.getById(paymentId);
    }

    public boolean addPayment(Payment payment) throws RemoteException {
        if (payment == null) return false;
        return paymentDao.add(payment);
    }
}
