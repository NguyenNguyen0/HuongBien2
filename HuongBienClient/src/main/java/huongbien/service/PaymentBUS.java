package huongbien.service;

import huongbien.dao.remote.IPaymentDAO;
import huongbien.entity.Payment;
import huongbien.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class PaymentBUS {
    private final IPaymentDAO paymentDao;

    public PaymentBUS() throws RemoteException {
        try {
            paymentDao = RMIClient.getInstance().getPaymentDAO();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Payment> getAllPayment() throws RemoteException {
        return paymentDao.getAll();
    }

    public Payment getPaymentById(String paymentId) throws RemoteException {
        if (paymentId.isBlank()) return null;
        return paymentDao.getById(paymentId);
    }

    public boolean addPayment(Payment payment) throws RemoteException {
        if (payment == null) return false;
        return paymentDao.add(payment);
    }
}
