package huongbien.dao.impl;

import huongbien.dao.remote.ICustomerDAO;
import huongbien.entity.Customer;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDAO extends GenericDAO<Customer> implements ICustomerDAO {
    public CustomerDAO() throws RemoteException {
        super();
    }

    public CustomerDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Customer> getAll() throws RemoteException {
        return findMany("SELECT c FROM Customer c", Customer.class);
    }

    @Override
    public Customer getByPhoneNumber(String phoneNumber) throws RemoteException {
        return findOne("SELECT c FROM Customer c WHERE c.phoneNumber = ?1", Customer.class, phoneNumber);
    }

    @Override
    public List<Customer> getByPhoneNumberPattern(String phoneNumber) throws RemoteException {
        return findMany("SELECT c FROM Customer c WHERE c.phoneNumber LIKE ?1", Customer.class, phoneNumber + "%");
    }

    @Override
    public List<Customer> getByName(String name) throws RemoteException {
        return findMany("SELECT c FROM Customer c WHERE c.name LIKE ?1", Customer.class, "%" + name + "%");
    }

    @Override
    public List<Customer> getAllWithPagination(int offset, int limit) throws RemoteException {
        return findManyWithPagination("SELECT c FROM Customer c ORDER BY c.id", Customer.class, null, offset, limit);
    }

    @Override
    public List<Customer> getAllWithPaginationByPhoneNumber(String phoneNumber, int offset, int limit) throws RemoteException {
        Map<String, Object> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber + "%");
        return findManyWithPagination(
                "SELECT c FROM Customer c WHERE c.phoneNumber LIKE :phoneNumber ORDER BY c.id",
                Customer.class,
                params,
                offset,
                limit
        );
    }

    @Override
    public List<Customer> getAllWithPaginationByName(String name, int offset, int limit) throws RemoteException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "%" + name + "%");
        return findManyWithPagination(
                "SELECT c FROM Customer c WHERE c.name LIKE :name ORDER BY c.id",
                Customer.class,
                params,
                offset,
                limit
        );
    }

    @Override
    public List<Customer> getAllWithPaginationById(String id, int offset, int limit) throws RemoteException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id + "%");
        return findManyWithPagination(
                "SELECT c FROM Customer c WHERE c.id LIKE :id ORDER BY c.id",
                Customer.class,
                params,
                offset,
                limit
        );
    }

    @Override
    public List<Customer> getCustomerInDay(LocalDate date) throws RemoteException {
        return findMany("SELECT c FROM Customer c WHERE c.registrationDate = ?1", Customer.class, date);
    }

    @Override
    public List<Customer> getNewCustomersInYear(int year) throws RemoteException {
        return findMany("SELECT c FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = ?1", Customer.class, year);
    }

    @Override
    public List<Customer> getTopMembershipCustomers(int year, int limit) throws RemoteException {
        String jpql = "SELECT c FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = ?1 ORDER BY c.membershipLevel DESC";

        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
        query.setParameter(1, year);
        query.setMaxResults(limit); // Apply the limit here

        return query.getResultList();
    }

    @Override
    public int countNewCustomerQuantityByYear(int year) throws RemoteException {
        return count("SELECT COUNT(c) FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = ?1", year);
    }

    @Override
    public int countTotalById(String id) throws RemoteException {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.id = ?1", id);
    }

    @Override
    public int countTotal() throws RemoteException {
        return count("SELECT COUNT(c) FROM Customer c");
    }

    @Override
    public int countTotalByPhoneNumber(String phoneNumber) throws RemoteException {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.phoneNumber LIKE ?1", phoneNumber + "%");
    }

    @Override
    public int countTotalByName(String name) throws RemoteException {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.name LIKE ?1", "%" + name + "%");
    }

    @Override
    public List<String> getPhoneNumber() throws RemoteException {
        return executeQuery("SELECT c.phoneNumber FROM Customer c", String.class);
    }

    @Override
    public Customer getById(String customerId) throws RemoteException {
        return findOne("SELECT c FROM Customer c WHERE c.id = ?1", Customer.class, customerId);
    }

    @Override
    public Customer getCustomerSearchReservation(String search) throws RemoteException {
        return findOne("SELECT c FROM Customer c WHERE c.phoneNumber LIKE ?1", Customer.class, "%" + search + "%");
    }

    @Override
    public boolean increasePoint(String id, int point) throws RemoteException {
        Customer customer = getById(id);
        if (customer == null) return false;
        customer.setAccumulatedPoints(customer.getAccumulatedPoints() + point);
        return update(customer);
    }

    @Override
    public boolean add(Customer customer) throws RemoteException {
        return super.add(customer);
    }

    @Override
    public boolean update(Customer customer) throws RemoteException {
        return super.update(customer);
    }

    @Override
    public boolean delete(Customer customer) throws RemoteException {
        return super.delete(customer);
    }
}