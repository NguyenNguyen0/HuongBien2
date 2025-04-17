package huongbien.service;


import huongbien.dao.remote.ICuisineDAO;
import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;
import huongbien.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class CuisineBUS {
    private final ICuisineDAO cuisineDao;

    public CuisineBUS() throws RemoteException {
        try {
            cuisineDao = RMIClient.getInstance().getCuisineDAO();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllCuisineNames() throws RemoteException {
        return cuisineDao.getAllCuisineNames();
    }

    public List<String> getCuisineNamesByCategory(String categoryName) throws RemoteException {
        return cuisineDao.getCuisineNamesByCategory(categoryName);
    }

    public int countTotalCuisine() throws RemoteException {
        return cuisineDao.countTotal();
    }

    public int countCuisinesByName(String name) throws RemoteException {
        return cuisineDao.countCuisinesByName(name);
    }

    public int countCuisinesByCategory(String category) throws RemoteException {
        return cuisineDao.countCuisinesByCategory(category);
    }

    public List<Cuisine> getCuisinesByCategoryWithPagination(int offset, int limit, String category) throws RemoteException {
        return cuisineDao.getByCategoryWithPagination(category, offset, limit);
    }

    public List<Cuisine> getCuisinesByNameWithPagination(int offset, int limit, String name) throws RemoteException {
        return cuisineDao.getByNameWithPagination(offset, limit, name);
    }

    public List<Cuisine> getAllCuisineWithPagination(int offset, int limit) throws RemoteException {
        return cuisineDao.getAllWithPagination(offset, limit);
    }

    public List<Cuisine> getAllCuisine() throws RemoteException {
        return cuisineDao.getAll();
    }

    public List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex) throws RemoteException {
        return cuisineDao.getLookUpCuisine(name, category, pageIndex);
    }

    public int getCountLookUpCuisine(String name, String category) throws RemoteException {
        return cuisineDao.getCountLookUpCuisine(name, category);
    }

    public List<Cuisine> getCuisineByName(String name) throws RemoteException {
        return cuisineDao.getByName(name);
    }

    public Cuisine getCuisineById(String cuisineId) throws RemoteException {
        return cuisineDao.getById(cuisineId);
    }

    public List<String> getCuisineCategory() throws RemoteException {
        return cuisineDao.getCuisineCategory();
    }

    public boolean stopSellCuisine(String cuisineId) throws RemoteException {
        if (cuisineId == null) {
            return false;
        }
        return cuisineDao.updateCuisineStatus(cuisineId, CuisineStatus.DISCONTINUED);
    }

    public boolean updateCuisineInfo(Cuisine cuisine) throws RemoteException {
        if (cuisine == null) {
            return false;
        }
        return cuisineDao.update(cuisine);
    }

    public boolean addCuisine(Cuisine cuisine) throws RemoteException {
        if (cuisine == null) {
            return false;
        }
        return cuisineDao.add(cuisine);
    }
}
