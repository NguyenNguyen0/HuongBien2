package huongbien.dao;

import huongbien.entity.Category;
import huongbien.entity.FoodOrder;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

public class FoodOrderDAO extends GenericDAO<FoodOrder> {
    public FoodOrderDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<FoodOrder> getAll() {
        return findMany("SELECT f FROM FoodOrder f", FoodOrder.class);
    }

    public List<FoodOrder> getAllByReservationId(String reservationId) {
        return findMany("SELECT f FROM FoodOrder f WHERE f.reservation.id = :reservationId", FoodOrder.class, reservationId);
    }

    public FoodOrder getById(String foodOrderId) {
        return findOne("SELECT f FROM FoodOrder f WHERE f.id = :foodOrderId", FoodOrder.class, foodOrderId);
    }
}