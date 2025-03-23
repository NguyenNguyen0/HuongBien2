package com.huongbien.bus;


import com.huongbien.dao.CategoryDAO;
import com.huongbien.entity.Category;

import java.util.List;

public class CategoryBUS {
    private final CategoryDAO categoryDao;

    public CategoryBUS() {
        categoryDao = CategoryDAO.getInstance();
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
        return categoryDao.getCategoryName(name).getCategoryId();
    }
}
