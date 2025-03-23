package com.huongbien.bus;

import com.huongbien.dao.FoodOrderDAO;
import com.huongbien.entity.FoodOrder;

import java.util.List;

public class FoodOrderBUS {
    private final FoodOrderDAO foodOrderDao;

    public FoodOrderBUS() {
        foodOrderDao = FoodOrderDAO.getInstance();
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
