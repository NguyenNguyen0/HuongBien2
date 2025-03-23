package huongbien.bus;

import huongbien.dao.FoodOrderDAO;
import huongbien.entity.FoodOrder;

import java.util.List;

public class FoodOrderBUS {
    private final FoodOrderDAO foodOrderDao;

    public FoodOrderBUS() {
        foodOrderDao = new FoodOrderDAO();
    }

    public List<FoodOrder> getFoodOrdersByReservationId(String reservationId) {
        if (reservationId.isBlank() || reservationId.isEmpty()) return null;
        return foodOrderDao.getAllByReservationId(reservationId);
    }

    public FoodOrder getFoodOrderById(String foodOrderId) {
        if (foodOrderId.isBlank() || foodOrderId.isEmpty()) return null;
        return foodOrderDao.getById(foodOrderId);
    }
}
