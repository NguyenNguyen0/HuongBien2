package huongbien.dao;

import huongbien.entity.OrderDetail;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderDetailDAOTest {
    private static final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

    @Test
    void add() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId("OD001");
        orderDetail.setQuantity(10);
        orderDetail.setSalePrice(100.0);
        orderDetail.setNote("Test note");

        orderDetailDAO.add(orderDetail);

        OrderDetail retrievedOrderDetail = orderDetailDAO.getById("OD001");
        assertNotNull(retrievedOrderDetail);
        assertEquals(10, retrievedOrderDetail.getQuantity());
        assertEquals(100.0, retrievedOrderDetail.getSalePrice());
        assertEquals("Test note", retrievedOrderDetail.getNote());
    }

    @Test
    void update() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId("OD002");
        orderDetail.setQuantity(5);
        orderDetail.setSalePrice(50.0);
        orderDetail.setNote("Initial note");

        orderDetailDAO.add(orderDetail);

        orderDetail.setQuantity(15);
        orderDetail.setSalePrice(75.0);
        orderDetail.setNote("Updated note");

        orderDetailDAO.update(orderDetail);

        OrderDetail updatedOrderDetail = orderDetailDAO.getById("OD002");
        assertNotNull(updatedOrderDetail);
        assertEquals(15, updatedOrderDetail.getQuantity());
        assertEquals(75.0, updatedOrderDetail.getSalePrice());
        assertEquals("Updated note", updatedOrderDetail.getNote());
    }

    @Test
    void getAllByOrderId() {
        String orderId = "O001";
        List<OrderDetail> orderDetails = orderDetailDAO.getAllByOrderId(orderId);
        assertNotNull(orderDetails);
    }

    @Test
    void getById() {
        OrderDetail orderDetail = orderDetailDAO.getById("OD001");
        assertNotNull(orderDetail);
        assertEquals("OD001", orderDetail.getId());
    }

    @Test
    void getCountOfUnitsSold() {
        int count = orderDetailDAO.getCountOfUnitsSold("M002");
        assertEquals(25, count);
    }
}