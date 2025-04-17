package huongbien.dao.impl;

import huongbien.dao.remote.ICategoryDAO;
import huongbien.entity.Category;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDAO extends GenericDAO<Category> implements ICategoryDAO {
    public CategoryDAO() throws RemoteException {
        super();
    }

    public CategoryDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public Category getById(String id) throws RemoteException {
        return findOne("SELECT c FROM Category c WHERE c.id = ?1", Category.class, id);
    }

    @Override
    public List<String> getAllCategoryNames() throws RemoteException {
        return findMany("SELECT distinct c FROM Category c", Category.class).stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategoryName(String name) throws RemoteException {
        return findOne("SELECT c FROM Category c WHERE c.name = ?1", Category.class, name);
    }

    @Override
    public List<Category> getAll() throws RemoteException {
        return findMany("SELECT c FROM Category c", Category.class);
    }

    @Override
    public List<Category> getByName(String name) throws RemoteException {
        return findMany("SELECT c FROM Category c WHERE c.name LIKE ?1", Category.class, "%" + name + "%");
    }

    @Override
    public int countAll() throws RemoteException {
        return count("SELECT COUNT(c) FROM Category c");
    }

    @Override
    public boolean add(Category category) throws RemoteException {
        return super.add(category);
    }

    @Override
    public boolean update(Category category) throws RemoteException {
        return super.update(category);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        Category category = getById(id);
        if (category == null) {
            return false;
        }
        return super.delete(category);
    }
}