package huongbien.bus;

import huongbien.dao.OrderDetailDAO;
import huongbien.entity.OrderDetail;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

public class OrderDetailBUS {
    private final OrderDetailDAO orderDetailDao;

    public OrderDetailBUS() {
        orderDetailDao = new OrderDetailDAO(PersistenceUnit.MARIADB_JPA);
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
