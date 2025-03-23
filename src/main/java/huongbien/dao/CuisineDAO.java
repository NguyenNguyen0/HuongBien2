package huongbien.dao;

import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class CuisineDAO extends GenericDAO<Cuisine> {

    public CuisineDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<String> getAllCuisineNames() {
        return executeQuery("SELECT c.name FROM Cuisine c", String.class);
    }

    public List<String> getCuisineNamesByCategory(String categoryName) {
        return executeQuery("SELECT c.name FROM Cuisine c WHERE c.category.id = ?1",
                String.class, categoryName);
    }

    public List<Cuisine> getByName(String name) {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE ?1", Cuisine.class, "%" + name + "%");
    }

    public List<Cuisine> getAll() {
        return findMany("SELECT c FROM Cuisine c", Cuisine.class);
    }

    public List<Cuisine> getLookUpCuisine(String name, String category) {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE ?1 AND c.category.id = ?2",
                Cuisine.class, "%" + name + "%", category);
    }

    public List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex) {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE ?1 AND c.category.id LIKE ?2 ORDER BY c.category.id",
                Cuisine.class, "%" + name + "%", "%" + category + "%");
    }

    public int getCountLookUpCuisine(String name, String category) {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.name LIKE ?1 AND c.category.id = ?2",
                "%" + name + "%", category);
    }

    public Cuisine getById(String id) {
        return findOne("SELECT c FROM Cuisine c WHERE c.id = ?1", Cuisine.class, id);
    }

    public List<String> getCuisineCategory() {
        return executeQuery("SELECT DISTINCT c.category.id FROM Cuisine c", String.class);
    }

    public List<Cuisine> getByCategoryWithPagination(int offset, int limit, String category) {
        return findMany("SELECT c FROM Cuisine c WHERE c.category.id = ?1 ORDER BY c.id",
                Cuisine.class, category, offset, limit);
    }

    public List<Cuisine> getByNameWithPagination(int offset, int limit, String name) {
        Map<String, Object> params = Map.of("name", "%" + name + "%");
        return findManyWithPagination("SELECT c FROM Cuisine c WHERE c.name LIKE :name ORDER BY c.id",
                Cuisine.class, params, offset, limit);
    }

    public List<Cuisine> getAllWithPagination(int offset, int limit) {
        return findManyWithPagination("SELECT c FROM Cuisine c ORDER BY c.id", Cuisine.class, null, offset, limit);
    }

    public int countCuisinesByName(String name) {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.name LIKE ?1", "%" + name + "%");
    }

    public boolean updateCuisineStatus(String cuisineId, CuisineStatus status) {
        Cuisine cuisine = getById(cuisineId);
        if (cuisine == null) {
            return false;
        }
        cuisine.setStatus(status);
        return update(cuisine);
    }

    public int countCuisinesByCategory(String category) {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.category.id = ?1", category);
    }

    public int countTotal() {
        return count("SELECT COUNT(c) FROM Cuisine c");
    }
}

