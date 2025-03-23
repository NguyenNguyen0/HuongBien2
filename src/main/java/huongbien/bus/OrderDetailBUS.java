package com.huongbien.bus;

import com.huongbien.dao.OrderDetailDAO;
import com.huongbien.entity.OrderDetail;

import java.util.List;

public class OrderDetailBUS {
    private final OrderDetailDAO orderDetailDao;

    public OrderDetailBUS() {
        orderDetailDao = OrderDetailDAO.getInstance();
    }

    public List<OrderDetail> getAllOrderByOrderId(String orderId) {
        if (orderId.isBlank() || orderId.isEmpty()) return null;
        return orderDetailDao.getAllByOrderId(orderId);
    }

    public boolean addOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null) return false;
        return orderDetailDao.add(orderDetail);
    }

   public int getCountOfUnitsSold(String cuisineId){
        return orderDetailDao.getCountOfUnitsSold(cuisineId);
   }
}
