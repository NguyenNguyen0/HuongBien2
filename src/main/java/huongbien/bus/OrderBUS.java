package com.huongbien.bus;

import com.huongbien.dao.OrderDAO;
import com.huongbien.entity.Order;

import java.util.List;

public class OrderBUS {
    private final OrderDAO orderDao;

    public OrderBUS() {
        orderDao = OrderDAO.getInstance();
    }

    public int countTotalOrders() {
        return orderDao.countTotal();
    }

    public int countTotalOrdersByEmployeeId(String employeeId) {
        return orderDao.countTotalByEmployeeId(employeeId);
    }

    public int countTotalOrdersByCustomerPhoneNumber(String customerPhoneNumber) {
        return orderDao.countTotalByCustomerPhoneNumber(customerPhoneNumber);
    }

    public int countTotalOrdersByOrderId(String orderId) {
        return orderDao.countTotalByOrderId(orderId);
    }

    public List<Order> getOrdersByIdWithPagination(int offset, int limit, String orderId) {
        return orderDao.getAllByIdWithPagination(offset, limit, orderId);
    }

    public List<Order> getOrdersByEmployeeIdWithPagination(int offset, int limit, String employeeId) {
        return orderDao.getAllByEmployeeIdWithPagination(offset, limit, employeeId);
    }

    public List<Order> getOrdersByCustomerPhoneNumberWithPagination(int offset, int limit, String customerPhoneNumber) {
        return orderDao.getAllByCustomerPhoneNumberWithPagination(offset, limit, customerPhoneNumber);
    }

    public List<Order> getAllWithPagination(int offset, int limit) {
        return orderDao.getAllWithPagination(offset, limit);
    }

    public List<Order> getAllOrder() {
        return orderDao.getAll();
    }

    public boolean addOrder(Order order) {
        if (order == null) return false;
        return orderDao.add(order);
    }
}
