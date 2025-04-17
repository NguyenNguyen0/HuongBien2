package huongbien.service;

import huongbien.dao.remote.IOrderDAO;
import huongbien.entity.Order;
import huongbien.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class OrderBUS {
    private final IOrderDAO orderDao;

    public OrderBUS() throws RemoteException {
        try {
            orderDao = RMIClient.getInstance().getOrderDAO();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int countTotalOrders() throws RemoteException {
        return orderDao.countTotal();
    }

    public int countTotalOrdersByEmployeeId(String employeeId) throws RemoteException {
        return orderDao.countTotalByEmployeeId(employeeId);
    }

    public int countTotalOrdersByCustomerPhoneNumber(String customerPhoneNumber) throws RemoteException {
        return orderDao.countTotalByCustomerPhoneNumber(customerPhoneNumber);
    }

    public int countTotalOrdersByOrderId(String orderId) throws RemoteException {
        return orderDao.countTotalByOrderId(orderId);
    }

    public List<Order> getOrdersByIdWithPagination(int offset, int limit, String orderId) throws RemoteException {
        return orderDao.getAllByIdWithPagination(offset, limit, orderId);
    }

    public List<Order> getOrdersByEmployeeIdWithPagination(int offset, int limit, String employeeId) throws RemoteException {
        return orderDao.getAllByEmployeeIdWithPagination(offset, limit, employeeId);
    }

    public List<Order> getOrdersByCustomerPhoneNumberWithPagination(int offset, int limit, String customerPhoneNumber) throws RemoteException {
        return orderDao.getAllByCustomerPhoneNumberWithPagination(offset, limit, customerPhoneNumber);
    }

    public List<Order> getAllWithPagination(int offset, int limit) throws RemoteException {
        return orderDao.getAllWithPagination(offset, limit);
    }

    public List<Order> getAllOrder() throws RemoteException {
        return orderDao.getAll();
    }

    public boolean addOrder(Order order) throws RemoteException {
        if (order == null) return false;
        return orderDao.addOrder(order);
    }
}