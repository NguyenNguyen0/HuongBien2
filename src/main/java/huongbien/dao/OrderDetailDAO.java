package huongbien.dao;

import huongbien.entity.OrderDetail;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

public class OrderDetailDAO extends GenericDAO<OrderDetail> {
    public OrderDetailDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<OrderDetail> getAllByOrderId(String orderId) {
        return findMany("SELECT o FROM OrderDetail o WHERE o.order.id = :orderId", OrderDetail.class, orderId);
    }

    public OrderDetail getById(String id) {
        return findOne("SELECT o FROM OrderDetail o WHERE o.id = :id", OrderDetail.class, id);
    }

    public int getCountOfUnitsSold(String cuisineId) {
        String jpql = "SELECT COALESCE(SUM(o.quantity), 0) FROM OrderDetail o WHERE o.cuisine.id = :cuisineId";
        return count(jpql, cuisineId);
    }
}
