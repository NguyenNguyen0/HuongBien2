package huongbien.dao;

import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

class OrderDAOTest {
    private static final OrderDAO orderDAO = new OrderDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void getAllByCustomerId() {
    }

    @Test
    void getAllByEmployeeId() {
    }

    @Test
    void getAllWithPagination() {
    }

    @Test
    void getAllByCustomerPhoneNumberWithPagination() {
    }

    @Test
    void getAllByEmployeeIdWithPagination() {
    }

    @Test
    void getAllByIdWithPagination() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void countTotal() {
    }

    @Test
    void countTotalByOrderId() {
    }

    @Test
    void countTotalByCustomerPhoneNumber() {
    }

    @Test
    void countTotalByEmployeeId() {
    }
}