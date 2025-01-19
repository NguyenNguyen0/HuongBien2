package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.FoodOrder;
import huongbien.entity.Reservation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FoodOrderDAOTest {
    private static final FoodOrderDAO foodOrderDAO = new FoodOrderDAO();

    @Test
    void add() {
        FoodOrder foodOrder = DataGenerator.fakeFoodOrders(DataGenerator.fakeReservation(LocalDate.now())).get(0);
        foodOrderDAO.add(foodOrder);
        FoodOrder retrieved = foodOrderDAO.getById(foodOrder.getId());
        assertNotNull(retrieved);
        assertEquals(foodOrder.getId(), retrieved.getId());
    }

    @Test
    void update() {
        FoodOrder foodOrder = DataGenerator.fakeFoodOrders(DataGenerator.fakeReservation(LocalDate.now())).get(0);
        foodOrderDAO.add(foodOrder);
        foodOrder.setQuantity(10);
        foodOrderDAO.update(foodOrder);
        FoodOrder retrieved = foodOrderDAO.getById(foodOrder.getId());
        assertNotNull(retrieved);
        assertEquals(10, retrieved.getQuantity());
    }

    @Test
    void getAll() {
        assertNotNull(foodOrderDAO.getAll());
    }

    @Test
    void getAllByReservationId() {
        assertNotNull(foodOrderDAO.getAllByReservationId("DB240101072525057"));
    }

    @Test
    void getById() {
        assertNull(foodOrderDAO.getById("1"));
        assertNotNull(foodOrderDAO.getById("DB240101072525057DM638"));
    }
}