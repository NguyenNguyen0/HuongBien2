package huongbien.dao;

import huongbien.entity.Customer;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static huongbien.jpa.JPAUtil.getEntityManager;

@NoArgsConstructor
public class CustomerDAO extends GenericDAO<Customer> {

    public CustomerDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Customer> getAll() {
        return findMany("SELECT c FROM Customer c", Customer.class);
    }

    public Customer getByOnePhoneNumber(String phoneNumber) {
        return findOne("SELECT c FROM Customer c WHERE c.phoneNumber = :phoneNumber", Customer.class, "phoneNumber", phoneNumber);
    }

    public List<Customer> getByManyPhoneNumber(String phoneNumber) {
        return findMany("SELECT c FROM Customer c WHERE c.phoneNumber LIKE :phoneNumber", Customer.class, "phoneNumber", phoneNumber + "%");
    }

    public List<Customer> getByName(String name) {
        return findMany("SELECT c FROM Customer c WHERE c.name LIKE :name", Customer.class, "name", "%" + name + "%");
    }

    public List<Customer> getAllWithPagination(int offset, int limit) {
        return findMany("SELECT c FROM Customer c ORDER BY c.id", Customer.class, offset, limit);
    }

    public List<Customer> getAllWithPaginationByPhoneNumber(String phoneNumber, int offset, int limit) {
        return findMany("SELECT c FROM Customer c WHERE c.phoneNumber LIKE :phoneNumber ORDER BY c.id", Customer.class, offset, limit, "phoneNumber", phoneNumber + "%");
    }

    public List<Customer> getAllWithPaginationByName(String name, int offset, int limit) {
        return findMany("SELECT c FROM Customer c WHERE c.name LIKE :name ORDER BY c.id", Customer.class, offset, limit, "name", "%" + name + "%");
    }

    public List<Customer> getAllWithPaginationById(String id, int offset, int limit) {
        return findMany("SELECT c FROM Customer c WHERE c.id LIKE :id ORDER BY c.id", Customer.class, offset, limit, "id", id + "%");
    }

    public List<Customer> getCustomerInDay(LocalDate date) {
        return findMany("SELECT c FROM Customer c WHERE c.registrationDate = :date", Customer.class, "date", date);
    }

    public List<Customer> getNewCustomersInYear(int year) {
        return findMany("SELECT c FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = :year", Customer.class, "year", year);
    }

    public List<Customer> getTopMembershipCustomers(int year, int limit) {
        return findMany("SELECT c FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = :year ORDER BY c.membershipLevel DESC", Customer.class, limit, "year", year);
    }

    public int countNewCustomerQuantityByYear(int year) {
        return count("SELECT COUNT(c) FROM Customer c WHERE FUNCTION('YEAR', c.registrationDate) = :year", "year", year);
    }

    public int countTotalById(String id) {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.id LIKE :id", "id", id + "%");
    }

    public int countTotal() {
        return count("SELECT COUNT(c) FROM Customer c");
    }

    public int countTotalByPhoneNumber(String phoneNumber) {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.phoneNumber LIKE :phoneNumber", "phoneNumber", phoneNumber + "%");
    }

    public int countTotalByName(String name) {
        return count("SELECT COUNT(c) FROM Customer c WHERE c.name LIKE :name", "name", "%" + name + "%");
    }

    public List<String> getPhoneNumber() {
        EntityManager em = getEntityManager();
        String jpql = "SELECT c.phoneNumber FROM Customer c";
        Query query = em.createQuery(jpql);
        List<String> customersPhone = query.getResultList();
        return customersPhone;
    }

    public Customer getById(String customerId) {
        return findOne("SELECT c FROM Customer c WHERE c.id = :customerId", Customer.class, "customerId", customerId);
    }

    public Customer getCustomerSearchReservation(String search) {
        return findOne("SELECT c FROM Customer c WHERE c.phoneNumber LIKE :search", Customer.class, "search", "%" + search + "%");
    }
}
