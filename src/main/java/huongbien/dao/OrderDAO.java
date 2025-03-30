package huongbien.dao;

import huongbien.entity.Order;
import huongbien.entity.Table;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import huongbien.util.JacksonUtil;
import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class OrderDAO extends GenericDAO<Order> {
    public OrderDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Order> getAllByCustomerId(String customerId) {
        return findMany("SELECT o FROM Order o WHERE o.customer.id = ?1", Order.class, customerId);
    }

    public List<Order> getAllByEmployeeId(String employeeId) {
        return findMany("SELECT o FROM Order o WHERE o.employee.id = ?1", Order.class, employeeId);
    }

    public List<Order> getAllWithPagination(int offset, int limit) {
        String jpql = "SELECT o FROM Order o ORDER BY o.orderDate DESC";
        return findManyWithPagination(jpql, Order.class, null, offset, limit);
    }

    public List<Order> getAllByCustomerPhoneNumberWithPagination(int offset, int limit, String customerPhoneNumber) {
        String jpql = "SELECT o FROM Order o WHERE o.customer.id IN (SELECT c.id FROM Customer c WHERE c.phoneNumber LIKE :phoneNumber) ORDER BY o.orderDate DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("phoneNumber", customerPhoneNumber + "%");
        return findManyWithPagination(jpql, Order.class, params, offset, limit);
    }

    public List<Order> getAllByEmployeeIdWithPagination(int offset, int limit, String employeeId) {
        String jpql = "SELECT o FROM Order o WHERE o.employee.id LIKE :employeeId ORDER BY o.orderDate DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId + "%");
        return findManyWithPagination(jpql, Order.class, params, offset, limit);
    }

    public List<Order> getAllByIdWithPagination(int offset, int limit, String orderId) {
        String jpql = "SELECT o FROM Order o WHERE o.id LIKE :orderId ORDER BY o.orderDate DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId + "%");
        return findManyWithPagination(jpql, Order.class, params, offset, limit);
    }

    public List<Order> getAll() {
        return findMany("SELECT o FROM Order o", Order.class);
    }

    public Order getById(String id) {
        return findOne("SELECT o FROM Order o WHERE o.id = ?1", Order.class, id);
    }

    public int countTotal() {
        return count("SELECT COUNT(o) FROM Order o");
    }

    public int countTotalByOrderId(String orderId) {
        return count("SELECT COUNT(o) FROM Order o WHERE o.id LIKE ?1", orderId + '%');
    }

    public int countTotalByCustomerPhoneNumber(String customerPhoneNumber) {
        return count("SELECT COUNT(o) FROM Order o WHERE o.customer.phoneNumber LIKE ?1", customerPhoneNumber + "%");
    }

    public int countTotalByEmployeeId(String employeeId) {
        return count("SELECT COUNT(o) FROM Order o WHERE o.employee.id LIKE ?1", employeeId + "%");
    }

    public boolean addOrder(Order order) {
        List<String> tableIds = order.getTables().stream().map(Table::getId).toList();
        order.setTables(new ArrayList<>());
        add(order);

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            for (String tableId : tableIds) {
                em.createNativeQuery("INSERT INTO orders_tables (order_id, table_id) VALUES (:orderId, :tableId)")
                        .setParameter("orderId", order.getId())
                        .setParameter("tableId", tableId)
                        .executeUpdate();
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
