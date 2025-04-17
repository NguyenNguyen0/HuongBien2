package huongbien.dao.impl;

import huongbien.dao.remote.IOrderDAO;
import huongbien.entity.Order;
import huongbien.entity.Table;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO extends GenericDAO<Order> implements IOrderDAO {
    public OrderDAO() throws RemoteException {
        super();
    }

    public OrderDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Order> getAllByCustomerId(String customerId) throws RemoteException {
        return findMany("SELECT o FROM Order o WHERE o.customer.id = ?1", Order.class, customerId);
    }

    @Override
    public List<Order> getAllByEmployeeId(String employeeId) throws RemoteException {
        return findMany("SELECT o FROM Order o WHERE o.employee.id = ?1", Order.class, employeeId);
    }

    @Override
    public List<Order> getAllWithPagination(int offset, int limit) throws RemoteException {
        String jpql = "SELECT o FROM Order o ORDER BY o.orderDate DESC";
        return findManyWithPagination(jpql, Order.class, null, offset, limit);
    }

    @Override
    public List<Order> getAllByCustomerPhoneNumberWithPagination(int offset, int limit, String customerPhoneNumber) throws RemoteException {
        String jpql = "SELECT o FROM Order o WHERE o.customer.id IN (SELECT c.id FROM Customer c WHERE c.phoneNumber LIKE :phoneNumber) ORDER BY o.orderDate DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("phoneNumber", customerPhoneNumber + "%");
        return findManyWithPagination(jpql, Order.class, params, offset, limit);
    }

    @Override
    public List<Order> getAllByEmployeeIdWithPagination(int offset, int limit, String employeeId) throws RemoteException {
        String jpql = "SELECT o FROM Order o WHERE o.employee.id LIKE :employeeId ORDER BY o.orderDate DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId + "%");
        return findManyWithPagination(jpql, Order.class, params, offset, limit);
    }

    @Override
    public List<Order> getAllByIdWithPagination(int offset, int limit, String orderId) throws RemoteException {
        String jpql = "SELECT o FROM Order o WHERE o.id LIKE :orderId ORDER BY o.orderDate DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId + "%");
        return findManyWithPagination(jpql, Order.class, params, offset, limit);
    }

    @Override
    public List<Order> getAll() throws RemoteException {
        return findMany("SELECT o FROM Order o", Order.class);
    }

    @Override
    public Order getById(String id) throws RemoteException {
        return findOne("SELECT o FROM Order o WHERE o.id = ?1", Order.class, id);
    }

    @Override
    public int countTotal() throws RemoteException {
        return count("SELECT COUNT(o) FROM Order o");
    }

    @Override
    public int countTotalByOrderId(String orderId) throws RemoteException {
        return count("SELECT COUNT(o) FROM Order o WHERE o.id LIKE ?1", orderId + '%');
    }

    @Override
    public int countTotalByCustomerPhoneNumber(String customerPhoneNumber) throws RemoteException {
        return count("SELECT COUNT(o) FROM Order o WHERE o.customer.phoneNumber LIKE ?1", customerPhoneNumber + "%");
    }

    @Override
    public int countTotalByEmployeeId(String employeeId) throws RemoteException {
        return count("SELECT COUNT(o) FROM Order o WHERE o.employee.id LIKE ?1", employeeId + "%");
    }

    @Override
    public boolean addOrder(Order order) throws RemoteException {
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

    @Override
    public boolean add(Order order) throws RemoteException {
        return super.add(order);
    }

    @Override
    public boolean update(Order order) throws RemoteException {
        return super.update(order);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        Order order = getById(id);
        if (order == null) {
            return false;
        }
        return super.delete(order);
    }
}