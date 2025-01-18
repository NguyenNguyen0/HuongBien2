package huongbien.dao;

import huongbien.entity.Order;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.util.List;

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
        return findMany("SELECT o FROM Order o ORDER BY o.orderDate DESC", Order.class, offset, limit);
    }

    public List<Order> getAllByCustomerPhoneNumberWithPagination(int offset, int limit, String customerPhoneNumber) {
        return findMany("SELECT o FROM Order o WHERE o.customer.phoneNumber LIKE ?1 ORDER BY o.orderDate DESC", Order.class, customerPhoneNumber + "%", offset, limit);
    }

    public List<Order> getAllByEmployeeIdWithPagination(int offset, int limit, String employeeId) {
        return findMany("SELECT o FROM Order o WHERE o.employee.id LIKE ?1 ORDER BY o.orderDate DESC", Order.class, employeeId + "%", offset, limit);
    }

    public List<Order> getAllByIdWithPagination(int offset, int limit, String orderId) {
        return findMany("SELECT o FROM Order o WHERE o.id LIKE ?1 ORDER BY o.orderDate DESC", Order.class, orderId + "%", offset, limit);
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
}
