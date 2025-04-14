package huongbien.rmi.impl;

import huongbien.bus.CuisineBUS;
import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;
import huongbien.rmi.interfaces.CuisineService;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Implementation of the CuisineService remote interface.
 * This class delegates to the CuisineBUS business logic layer.
 */
public class CuisineServiceImpl implements CuisineService {

    private final CuisineBUS cuisineBUS;
    
    public CuisineServiceImpl() {
        this.cuisineBUS = new CuisineBUS();
    }
    
    @Override
    public List<Cuisine> getAllCuisine() throws RemoteException {
        return cuisineBUS.getAllCuisine();
    }
    
    @Override
    public Cuisine getCuisineById(String cuisineId) throws RemoteException {
        return cuisineBUS.getCuisineById(cuisineId);
    }
    
    @Override
    public List<Cuisine> getCuisineByName(String name) throws RemoteException {
        return cuisineBUS.getCuisineByName(name);
    }
    
    @Override
    public List<Cuisine> getAllCuisineWithPagination(int offset, int limit) throws RemoteException {
        return cuisineBUS.getAllCuisineWithPagination(offset, limit);
    }
    
    @Override
    public List<Cuisine> getCuisinesByNameWithPagination(int offset, int limit, String name) throws RemoteException {
        return cuisineBUS.getCuisinesByNameWithPagination(offset, limit, name);
    }
    
    @Override
    public List<Cuisine> getCuisinesByCategoryWithPagination(int offset, int limit, String category) throws RemoteException {
        return cuisineBUS.getCuisinesByCategoryWithPagination(offset, limit, category);
    }
    
    @Override
    public List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex) throws RemoteException {
        return cuisineBUS.getLookUpCuisine(name, category, pageIndex);
    }
    
    @Override
    public List<String> getAllCuisineNames() throws RemoteException {
        return cuisineBUS.getAllCuisineNames();
    }
    
    @Override
    public List<String> getCuisineNamesByCategory(String categoryName) throws RemoteException {
        return cuisineBUS.getCuisineNamesByCategory(categoryName);
    }
    
    @Override
    public List<String> getCuisineCategory() throws RemoteException {
        return cuisineBUS.getCuisineCategory();
    }
    
    @Override
    public int countTotalCuisine() throws RemoteException {
        return cuisineBUS.countTotalCuisine();
    }
    
    @Override
    public int countCuisinesByName(String name) throws RemoteException {
        return cuisineBUS.countCuisinesByName(name);
    }
    
    @Override
    public int countCuisinesByCategory(String category) throws RemoteException {
        return cuisineBUS.countCuisinesByCategory(category);
    }
    
    @Override
    public int getCountLookUpCuisine(String name, String category) throws RemoteException {
        return cuisineBUS.getCountLookUpCuisine(name, category);
    }
    
    @Override
    public boolean addCuisine(Cuisine cuisine) throws RemoteException {
        return cuisineBUS.addCuisine(cuisine);
    }
    
    @Override
    public boolean updateCuisineInfo(Cuisine cuisine) throws RemoteException {
        return cuisineBUS.updateCuisineInfo(cuisine);
    }
    
    @Override
    public boolean stopSellCuisine(String cuisineId) throws RemoteException {
        return cuisineBUS.stopSellCuisine(cuisineId);
    }
}