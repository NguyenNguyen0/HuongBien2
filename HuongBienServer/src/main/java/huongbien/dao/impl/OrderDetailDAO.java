package huongbien.dao.impl;

import huongbien.dao.remote.IOrderDetailDAO;
import huongbien.entity.OrderDetail;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;

public class OrderDetailDAO extends GenericDAO<OrderDetail> implements IOrderDetailDAO {
    public OrderDetailDAO() throws RemoteException {
        super();
    }

    public OrderDetailDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<OrderDetail> getAllByOrderId(String orderId) throws RemoteException {
        return findMany("SELECT o FROM OrderDetail o WHERE o.order.id = ?1", OrderDetail.class, orderId);
    }

    @Override
    public OrderDetail getById(String id) throws RemoteException {
        return findOne("SELECT o FROM OrderDetail o WHERE o.id = ?1", OrderDetail.class, id);
    }

    @Override
    public int getCountOfUnitsSold(String cuisineId) throws RemoteException {
        return count("SELECT COALESCE(SUM(o.quantity), 0) FROM OrderDetail o WHERE o.cuisine.id = ?1", cuisineId);
    }

    @Override
    public boolean add(OrderDetail orderDetail) throws RemoteException {
        return super.add(orderDetail);
    }

    @Override
    public boolean update(OrderDetail orderDetail) throws RemoteException {
        return super.update(orderDetail);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        OrderDetail orderDetail = getById(id);
        if (orderDetail == null) {
            return false;
        }
        return super.delete(orderDetail);
    }
}
