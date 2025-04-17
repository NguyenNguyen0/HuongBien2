package huongbien.bus;

import huongbien.dao.impl.FoodOrderDAO;
import huongbien.entity.FoodOrder;

import java.rmi.RemoteException;
import java.util.List;

public class FoodOrderBUS {
    private final FoodOrderDAO foodOrderDao;

    public FoodOrderBUS() {
        try {
            foodOrderDao = new FoodOrderDAO();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FoodOrder> getFoodOrdersByReservationId(String reservationId) throws RemoteException {
        if (reservationId.isBlank() || reservationId.isEmpty()) return null;
        return foodOrderDao.getAllByReservationId(reservationId);
    }

    public FoodOrder getFoodOrderById(String foodOrderId) throws RemoteException {
        if (foodOrderId.isBlank() || foodOrderId.isEmpty()) return null;
        return foodOrderDao.getById(foodOrderId);
    }
}
