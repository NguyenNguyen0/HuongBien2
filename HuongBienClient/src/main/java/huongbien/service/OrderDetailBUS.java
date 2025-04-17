package huongbien.service;

import huongbien.dao.remote.IOrderDetailDAO;
import huongbien.entity.OrderDetail;
import huongbien.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class OrderDetailBUS {
    private final IOrderDetailDAO orderDetailDao;

    public OrderDetailBUS() throws RemoteException {
        try {
            orderDetailDao = RMIClient.getInstance().getOrderDetailDAO();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
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
