package huongbien.bus;


import huongbien.dao.CuisineDAO;
import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

public class CuisineBUS {
    private final CuisineDAO cuisineDao;

    public CuisineBUS() {
        cuisineDao = new CuisineDAO(PersistenceUnit.MARIADB_JPA);
    }

    public List<String> getAllCuisineNames() {
        return cuisineDao.getAllCuisineNames();
    }

    public List<String> getCuisineNamesByCategory(String categoryName) {
        return cuisineDao.getCuisineNamesByCategory(categoryName);
    }

    public int countTotalCuisine() {
        return cuisineDao.countTotal();
    }

    public int countCuisinesByName(String name) {
        return cuisineDao.countCuisinesByName(name);
    }

    public int countCuisinesByCategory(String category) {
        return cuisineDao.countCuisinesByCategory(category);
    }

    public List<Cuisine> getCuisinesByCategoryWithPagination(int offset, int limit, String category) {
        return cuisineDao.getByCategoryWithPagination(category, offset, limit);
    }

    public List<Cuisine> getCuisinesByNameWithPagination(int offset, int limit, String name) {
        return cuisineDao.getByNameWithPagination(offset, limit, name);
    }

    public List<Cuisine> getAllCuisineWithPagination(int offset, int limit) {
        return cuisineDao.getAllWithPagination(offset, limit);
    }

    public List<Cuisine> getAllCuisine() { return cuisineDao.getAll(); }

    public List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex){ return cuisineDao.getLookUpCuisine(name, category, pageIndex); }

    public int getCountLookUpCuisine(String name, String category){ return cuisineDao.getCountLookUpCuisine(name, category); }

    public List<Cuisine> getCuisineByName(String name) {
        return cuisineDao.getByName(name);
    }

    public Cuisine getCuisineById(String cuisineId) {
        return cuisineDao.getById(cuisineId);
    }

    public  List<String> getCuisineCategory(){ return cuisineDao.getCuisineCategory();}

    public boolean stopSellCuisine(String cuisineId) {
        if (cuisineId == null) {
            return false;
        }
        return cuisineDao.updateCuisineStatus(cuisineId, CuisineStatus.DISCONTINUED);
    }

    public boolean updateCuisineInfo(Cuisine cuisine) {
        if (cuisine == null) {
            return false;
        }
        return cuisineDao.update(cuisine);
    }

    public boolean addCuisine(Cuisine cuisine) {
        if (cuisine == null) {
            return false;
        }
        return cuisineDao.add(cuisine);
    }
}
