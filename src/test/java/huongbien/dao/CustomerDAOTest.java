package huongbien.dao;

import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

class CustomerDAOTest {
    private static final CustomerDAO customerDAO = new CustomerDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getByPhoneNumber() {
    }

    @Test
    void getByPhoneNumberPattern() {
    }

    @Test
    void getByName() {
    }

    @Test
    void getAllWithPagination() {
    }

    @Test
    void getAllWithPaginationByPhoneNumber() {
    }

    @Test
    void getAllWithPaginationByName() {
    }

    @Test
    void getAllWithPaginationById() {
    }

    @Test
    void getCustomerInDay() {
    }

    @Test
    void getNewCustomersInYear() {
    }

    @Test
    void getTopMembershipCustomers() {
    }

    @Test
    void countNewCustomerQuantityByYear() {
    }

    @Test
    void countTotalById() {
    }

    @Test
    void countTotal() {
    }

    @Test
    void countTotalByPhoneNumber() {
    }

    @Test
    void countTotalByName() {
    }

    @Test
    void getPhoneNumber() {
    }

    @Test
    void getById() {
    }

    @Test
    void getCustomerSearchReservation() {
    }
}