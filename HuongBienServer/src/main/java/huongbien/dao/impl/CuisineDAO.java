package huongbien.dao.impl;

import huongbien.dao.remote.ICuisineDAO;
import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class CuisineDAO extends GenericDAO<Cuisine> implements ICuisineDAO {
    public CuisineDAO() throws RemoteException {
        super();
    }

    public CuisineDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<String> getAllCuisineNames() throws RemoteException {
        return executeQuery("SELECT c.name FROM Cuisine c", String.class);
    }

    @Override
    public List<String> getCuisineNamesByCategory(String categoryName) throws RemoteException {
        return executeQuery("SELECT c.name FROM Cuisine c WHERE c.category.name = ?1",
                String.class, categoryName);
    }

    @Override
    public List<Cuisine> getByName(String name) throws RemoteException {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE ?1", Cuisine.class, "%" + name + "%");
    }

    @Override
    public List<Cuisine> getAll() throws RemoteException {
        return findMany("SELECT c FROM Cuisine c", Cuisine.class);
    }

    @Override
    public List<Cuisine> getLookUpCuisine(String name, String category) throws RemoteException {
        System.out.println("getLookUpCuisine in Dao: name = " + name + ", category = " + category);
        if ((category == null || category.isEmpty()) && (name == null || name.isEmpty())) {
            return getAll();
        }

        if (category == null || category.isEmpty()) {
            return getByName(name);
        }

        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE ?1 AND c.category.id = ?2",
                Cuisine.class, "%" + name + "%", category);
    }

    @Override
    public List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex) throws RemoteException {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE ?1 AND c.category.id LIKE ?2 ORDER BY c.category.id",
                Cuisine.class, "%" + name + "%", "%" + category + "%");
    }

    @Override
    public int getCountLookUpCuisine(String name, String category) throws RemoteException {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.name LIKE ?1 AND c.category.id = ?2",
                "%" + name + "%", category);
    }

    @Override
    public Cuisine getById(String id) throws RemoteException {
        return findOne("SELECT c FROM Cuisine c WHERE c.id = ?1", Cuisine.class, id);
    }

    @Override
    public List<String> getCuisineCategory() throws RemoteException {
        return executeQuery("SELECT DISTINCT c.category.id FROM Cuisine c", String.class);
    }

    @Override
    public List<Cuisine> getByCategoryWithPagination(String category, int offset, int limit) throws RemoteException {
        Map<String, Object> params = Map.of("category", category);
        return findManyWithPagination(
                "SELECT c FROM Cuisine c WHERE c.category.id = :category ORDER BY c.id",
                Cuisine.class, params, offset, limit
        );
    }

    @Override
    public List<Cuisine> getByNameWithPagination(int offset, int limit, String name) throws RemoteException {
        Map<String, Object> params = Map.of("name", "%" + name + "%");
        return findManyWithPagination("SELECT c FROM Cuisine c WHERE c.name LIKE :name ORDER BY c.id",
                Cuisine.class, params, offset, limit);
    }

    @Override
    public List<Cuisine> getAllWithPagination(int offset, int limit) throws RemoteException {
        return findManyWithPagination("SELECT c FROM Cuisine c ORDER BY c.id", Cuisine.class, null, offset, limit);
    }

    @Override
    public int countCuisinesByName(String name) throws RemoteException {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.name LIKE ?1", "%" + name + "%");
    }

    @Override
    public boolean updateCuisineStatus(String cuisineId, CuisineStatus status) throws RemoteException {
        Cuisine cuisine = getById(cuisineId);
        if (cuisine == null) {
            return false;
        }
        cuisine.setStatus(status);
        return update(cuisine);
    }

    @Override
    public int countCuisinesByCategory(String category) throws RemoteException {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.category.id = ?1", category);
    }

    @Override
    public int countTotal() throws RemoteException {
        return count("SELECT COUNT(c) FROM Cuisine c");
    }

    @Override
    public boolean addCuisine(Cuisine cuisine) throws RemoteException {
        return add(cuisine);
    }

    @Override
    public boolean updateCuisine(Cuisine cuisine) throws RemoteException {
        return update(cuisine);
    }

    @Override
    public boolean add(Cuisine cuisine) throws RemoteException {
        return super.add(cuisine);
    }

    @Override
    public boolean update(Cuisine cuisine) throws RemoteException {
        return super.update(cuisine);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        Cuisine cuisine = getById(id);
        if (cuisine == null) {
            return false;
        }
        return super.delete(cuisine);
    }
}