package huongbien.dao.impl;

import huongbien.dao.remote.IFoodOrderDAO;
import huongbien.entity.FoodOrder;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;

public class FoodOrderDAO extends GenericDAO<FoodOrder> implements IFoodOrderDAO {
    public FoodOrderDAO() throws RemoteException {
        super();
    }

    public FoodOrderDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<FoodOrder> getAll() throws RemoteException {
        return findMany("SELECT f FROM FoodOrder f", FoodOrder.class);
    }

    @Override
    public List<FoodOrder> getAllByReservationId(String reservationId) throws RemoteException {
        return findMany("SELECT f FROM FoodOrder f WHERE f.reservation.id = ?1", FoodOrder.class, reservationId);
    }

    @Override
    public FoodOrder getById(String foodOrderId) throws RemoteException {
        return findOne("SELECT f FROM FoodOrder f WHERE f.id = ?1", FoodOrder.class, foodOrderId);
    }

    @Override
    public boolean add(FoodOrder foodOrder) throws RemoteException {
        return super.add(foodOrder);
    }

    @Override
    public boolean update(FoodOrder foodOrder) throws RemoteException {
        return super.update(foodOrder);
    }

    @Override
    public boolean delete(FoodOrder foodOrder) throws RemoteException {
        return super.delete(foodOrder);
    }
}