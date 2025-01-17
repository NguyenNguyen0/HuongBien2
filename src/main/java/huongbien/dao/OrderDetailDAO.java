package huongbien.dao;

import huongbien.entity.OrderDetail;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class OrderDetailDAO extends GenericDAO<OrderDetail> {
    public OrderDetailDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<OrderDetail> getAllByOrderId(String orderId) {
        return findMany("SELECT o FROM OrderDetail o WHERE o.order.id = ?1", OrderDetail.class, orderId);
    }

    public OrderDetail getById(String id) {
        return findOne("SELECT o FROM OrderDetail o WHERE o.id = ?1", OrderDetail.class, id);
    }

    public int getCountOfUnitsSold(String cuisineId) {
        return count( "SELECT COALESCE(SUM(o.quantity), 0) FROM OrderDetail o WHERE o.cuisine.id = ?1", cuisineId);
    }
}
