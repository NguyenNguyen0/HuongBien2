package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.Customer;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CustomerDAOTest {
    private static final CustomerDAO customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    public void add() {
        assertTrue(customerDAO.add(DataGenerator.fakeCustomer(LocalDate.now())));
        assertFalse(customerDAO.add(DataGenerator.getRandomCustomer()));
    }

    @Test
    public void update() {
        Customer customer = DataGenerator.fakeCustomer(LocalDate.now());
        customer.setId("C001");
        customer.setName("Updated Name");
        assertTrue(customerDAO.update(customer));
    }

    @Test
    public void getAll() {
        assertNotNull(customerDAO.getAll());
    }

    @Test
    public void getByPhoneNumber() {
        assertNotNull(customerDAO.getByPhoneNumber("(472) 427-8162"));
    }

    @Test
    public void getByPhoneNumberPattern() {
        List<Customer> customers = customerDAO.getByPhoneNumberPattern("(305) 299-5503");
        assertFalse(customers.isEmpty());
    }

    @Test
    public void getByName() {
        List<Customer> customers = customerDAO.getByName("John");
        assertFalse(customers.isEmpty());
    }

    @Test
    public void getAllWithPagination() {
        List<Customer> customers = customerDAO.getAllWithPagination(0, 10);
        assertFalse(customers.isEmpty());
    }

    @Test
    public void getAllWithPaginationByPhoneNumber() {
        List<Customer> customers = customerDAO.getAllWithPaginationByPhoneNumber("(730) 647-8582", 0, 10);
        assertFalse(customers.isEmpty());
    }

    @Test
    public void getAllWithPaginationByName() {
        List<Customer> customers = customerDAO.getAllWithPaginationByName("John", 0, 10);
        assertFalse(customers.isEmpty());
    }

    @Test
    public void getAllWithPaginationById() {
        List<Customer> customers = customerDAO.getAllWithPaginationById("C001", 0, 10);
        assertFalse(customers.isEmpty());
    }

    @Test
    public void getCustomerInDay() {
        List<Customer> customers = customerDAO.getCustomerInDay(LocalDate.now());
        assertFalse(customers.isEmpty());
    }

    @Test
    public void getNewCustomersInYear() {
        List<Customer> customers = customerDAO.getNewCustomersInYear(2024);
        assertFalse(customers.isEmpty());
    }

    @Test
    public void getTopMembershipCustomers() {
        List<Customer> customers = customerDAO.getTopMembershipCustomers(2024, 5);
        assertFalse(customers.isEmpty());
    }

    @Test
    public void countNewCustomerQuantityByYear() {
        assertTrue(customerDAO.countNewCustomerQuantityByYear(2024) > 0);
    }

    @Test
    public void countTotalById() {
        assertTrue(customerDAO.countTotalById("C001") > 0);
    }

    @Test
    public void countTotal() {
        assertTrue(customerDAO.countTotal() > 0);
    }

    @Test
    public void countTotalByPhoneNumber() {
        assertTrue(customerDAO.countTotalByPhoneNumber("(983) 855-1919") > 0);
    }

    @Test
    public void countTotalByName() {
        assertTrue(customerDAO.countTotalByName("John") > 0);
    }

    @Test
    public void getPhoneNumber() {
        List<String> phoneNumbers = customerDAO.getPhoneNumber();
        assertFalse(phoneNumbers.isEmpty());
    }

    @Test
    public void getById() {
        assertNotNull(customerDAO.getById("C001"));
    }

    @Test
    public void getCustomerSearchReservation() {
        assertNotNull(customerDAO.getCustomerSearchReservation(""));
    }
}