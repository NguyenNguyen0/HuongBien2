package huongbien.bus;


import huongbien.dao.impl.CategoryDAO;
import huongbien.entity.Category;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;

public class CategoryBUS {
    private final CategoryDAO categoryDao;

    public CategoryBUS() {
        try {
            categoryDao = new CategoryDAO(PersistenceUnit.MARIADB_JPA);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllCategoryNames() {
        try {
            return categoryDao.getAllCategoryNames();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> getAllCategory() {
        try {
            return categoryDao.getAll();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> getCategoryByName(String name) {
        try {
            return categoryDao.getByName(name);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Category getCategoryById(String categoryId) {
        if (categoryId.isBlank() || categoryId.isEmpty()) return null;
        try {
            return categoryDao.getById(categoryId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCategoryId(String name){
        if(name.equals("Tất cả")){
            return "";
        }
        try {
            return categoryDao.getCategoryName(name).getId();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
