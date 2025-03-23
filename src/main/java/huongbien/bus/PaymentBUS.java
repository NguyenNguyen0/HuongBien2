package com.huongbien.bus;

import com.huongbien.dao.PaymentDAO;
import com.huongbien.entity.Payment;

import java.util.List;

public class PaymentBUS {
    private final PaymentDAO paymentDao;

    public PaymentBUS() {
        paymentDao = PaymentDAO.getInstance();
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
