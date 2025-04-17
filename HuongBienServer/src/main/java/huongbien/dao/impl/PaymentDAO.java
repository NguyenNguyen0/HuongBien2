package huongbien.dao.impl;

import huongbien.dao.remote.IPaymentDAO;
import huongbien.entity.Payment;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;

public class PaymentDAO extends GenericDAO<Payment> implements IPaymentDAO {
    public PaymentDAO() throws RemoteException {
        super();
    }

    public PaymentDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Payment> getAll() throws RemoteException {
        return findMany("SELECT p FROM Payment p", Payment.class);
    }

    @Override
    public Payment getById(String id) throws RemoteException {
        return findOne("SELECT p FROM Payment p WHERE p.id = ?1", Payment.class, id);
    }

    @Override
    public boolean add(Payment payment) throws RemoteException {
        if (payment == null) return false;
        return super.add(payment);
    }

    @Override
    public boolean update(Payment payment) throws RemoteException {
        if (payment == null) return false;
        return super.update(payment);
    }

    @Override
    public boolean delete(Payment payment) throws RemoteException {
        if (payment == null) return false;
        return super.delete(payment);
    }
}