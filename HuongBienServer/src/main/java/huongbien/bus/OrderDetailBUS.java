package huongbien.bus;

import huongbien.dao.impl.OrderDetailDAO;
import huongbien.entity.OrderDetail;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;

public class OrderDetailBUS {
    private final OrderDetailDAO orderDetailDao;

    public OrderDetailBUS() throws RemoteException {
        orderDetailDao = new OrderDetailDAO(PersistenceUnit.MARIADB_JPA);
    }

    public List<OrderDetail> getAllOrderByOrderId(String orderId) throws RemoteException {
        if (orderId.isBlank() || orderId.isEmpty()) return null;
        return orderDetailDao.getAllByOrderId(orderId);
    }

    public boolean addOrderDetail(OrderDetail orderDetail) throws RemoteException {
        if (orderDetail == null) return false;
        return orderDetailDao.add(orderDetail);
    }

   public int getCountOfUnitsSold(String cuisineId) throws RemoteException {
        return orderDetailDao.getCountOfUnitsSold(cuisineId);
   }
}
