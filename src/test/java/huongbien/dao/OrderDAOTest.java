package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.Order;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTest {
    private static final OrderDAO orderDAO = new OrderDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    public void add() {
        Order order = DataGenerator.fakeOrder(LocalDate.now());
        assertTrue(orderDAO.add(order));
    }

    @Test
    public void update() {
        Order order = DataGenerator.fakeOrder(LocalDate.now());
        order.setId("HD240205170232305");
        assertTrue(orderDAO.update(order));
    }

    @Test
    public void getAllByCustomerId() {
        List<Order> orders = orderDAO.getAllByCustomerId("KH240102722");
        assertFalse(orders.isEmpty());
    }

    @Test
    public void getAllByEmployeeId() {
        List<Order> orders = orderDAO.getAllByEmployeeId("NV240124698");
        assertFalse(orders.isEmpty());
    }

    @Test
    public void getAllWithPagination() {
        List<Order> orders = orderDAO.getAllWithPagination(0, 10);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void getAllByCustomerPhoneNumberWithPagination() {
        List<Order> orders = orderDAO.getAllByCustomerPhoneNumberWithPagination(0, 3, "(983) 855-1919");
        assertFalse(orders.isEmpty());
    }

    @Test
    public void getAllByEmployeeIdWithPagination() {
        List<Order> orders = orderDAO.getAllByEmployeeIdWithPagination(0, 5, "NV001122003");
        assertFalse(orders.isEmpty());
    }

    @Test
    public void getAllByIdWithPagination() {
        List<Order> orders = orderDAO.getAllByIdWithPagination(0, 1, "HD240108142917420");
        assertFalse(orders.isEmpty());
    }

    @Test
    public void getAll() {
        List<Order> orders = orderDAO.getAll();
        assertNotNull(orders);
    }

    @Test
    public void getById() {
        Order order = orderDAO.getById("HD240104071302265");
        assertNotNull(order);
    }

    @Test
    public void countTotal() {
        int count = orderDAO.countTotal();
        assertTrue(count > 0);
    }

    @Test
    public void countTotalByOrderId() {
        int count = orderDAO.countTotalByOrderId("HD240119070133813");
        assertTrue(count > 0);
    }

    @Test
    public void countTotalByCustomerPhoneNumber() {
        int count = orderDAO.countTotalByCustomerPhoneNumber("(305) 298-0693");
        assertTrue(count > 0);
    }

    @Test
    public void countTotalByEmployeeId() {
        int count = orderDAO.countTotalByEmployeeId("NV240124698");
        assertTrue(count > 0);
    }
}