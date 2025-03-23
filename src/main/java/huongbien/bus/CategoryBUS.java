package huongbien.bus;


import huongbien.dao.CategoryDAO;
import huongbien.entity.Category;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

public class CategoryBUS {
    private final CategoryDAO categoryDao;

    public CategoryBUS() {
        categoryDao = new CategoryDAO(PersistenceUnit.MARIADB_JPA);
    }

    public List<String> getAllCategoryNames() {
        return categoryDao.getAllCategoryNames();
    }

    public List<Category> getAllCategory() {
        return categoryDao.getAll();
    }

    public List<Category> getCategoryByName(String name) {
        return categoryDao.getByName(name);
    }

    public Category getCategoryById(String categoryId) {
        if (categoryId.isBlank() || categoryId.isEmpty()) return null;
        return categoryDao.getById(categoryId);
    }

    public String getCategoryId(String name){
        if(name.equals("Tất cả")){
            return "";
        }
        return categoryDao.getCategoryName(name).getId();
    }
}
