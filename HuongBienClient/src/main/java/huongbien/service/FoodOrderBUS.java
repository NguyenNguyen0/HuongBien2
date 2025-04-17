package huongbien.service;

import huongbien.dao.remote.IFoodOrderDAO;
import huongbien.entity.FoodOrder;
import huongbien.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class FoodOrderBUS {
    private final IFoodOrderDAO foodOrderDao;

    public FoodOrderBUS() {
        try {
            foodOrderDao = RMIClient.getInstance().getFoodOrderDAO();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FoodOrder> getFoodOrdersByReservationId(String reservationId) throws RemoteException {
        if (reservationId.isBlank()) return null;
        return foodOrderDao.getAllByReservationId(reservationId);
    }

    public FoodOrder getFoodOrderById(String foodOrderId) throws RemoteException {
        if (foodOrderId.isBlank()) return null;
        return foodOrderDao.getById(foodOrderId);
    }
}
