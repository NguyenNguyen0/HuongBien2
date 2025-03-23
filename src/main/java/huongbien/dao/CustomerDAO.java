package huongbien.dao;

import huongbien.entity.Customer;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class CustomerDAO extends GenericDAO<Customer> {

    public CustomerDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Customer> getAll() {
        return findMany("SELECT c FROM Customer c", Customer.class);
    }

    public Customer getByPhoneNumber(String phoneNumber) {
        return findOne("SELECT c FROM Customer c WHERE c.phoneNumber = ?1", Customer.class, phoneNumber);
    }

    public List<Customer> getByPhoneNumberPattern(String phoneNumber) {
        return findMany("SELECT c FROM Customer c WHERE c.phoneNumber LIKE ?1", Customer.class, phoneNumber + "%");
    }

    public List<Customer> getByName(String name) {
        return findMany("SELECT c FROM Customer c WHERE c.name LIKE ?1", Customer.class, "%" + name + "%");
    }

    public List<Customer> getAllWithPagination(int offset, int limit) {
        return findManyWithPagination("SELECT c FROM Customer c ORDER BY c.id", Customer.class, null, offset, limit);
    }

    public List<Customer> getAllWithPaginationByPhoneNumber(String phoneNumber, int offset, int limit) {
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

    public List<Customer> getAllWithPaginationByName(String name, int offset, int limit) {
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

    public List<Customer> getAllWithPaginationById(String id, int offset, int limit) {
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

    public List<Customer> getCustomerInDay(LocalDate date) {
        return findMany("SELECT c FROM Customer c WHERE c.registrationDate = ?1", Customer.class, date);
    }

    public List<Customer> getNewCustomersInYear(int year) {
        return findMany("SELECT c FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = ?1", Customer.class, year);
    }

    public List<Customer> getTopMembershipCustomers(int year, int limit) {
        return findMany("SELECT c FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = ?1 ORDER BY c.membershipLevel DESC", Customer.class, limit, year);
    }

    public int countNewCustomerQuantityByYear(int year) {
        return count("SELECT COUNT(c) FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = ?1", year);
    }

    public int countTotalById(String id) {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.id = ?1", id);
    }

    public int countTotal() {
        return count("SELECT COUNT(c) FROM Customer c");
    }

    public int countTotalByPhoneNumber(String phoneNumber) {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.phoneNumber LIKE ?1", phoneNumber + "%");
    }

    public int countTotalByName(String name) {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.name LIKE ?1", "%" + name + "%");
    }

    public List<String> getPhoneNumber() {
        return executeQuery("SELECT c.phoneNumber FROM Customer c", String.class);
    }

    public Customer getById(String customerId) {
        return findOne("SELECT c FROM Customer c WHERE c.id = ?1", Customer.class, customerId);
    }

    public Customer getCustomerSearchReservation(String search) {
        return findOne("SELECT c FROM Customer c WHERE c.phoneNumber LIKE ?1", Customer.class, "%" + search + "%");
    }

    public boolean increasePoint(String id, int point) {
        Customer customer = getById(id);
        if (customer == null) return false;
        customer.setAccumulatedPoints(customer.getAccumulatedPoints() + point);
        return update(customer);
    }
}
