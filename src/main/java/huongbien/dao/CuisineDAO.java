package huongbien.dao;

import huongbien.entity.Cuisine;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.util.List;

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

    public List<Long> getCuisineCategory() {
        return executeQuery("SELECT DISTINCT c.category.id FROM Cuisine c", Long.class);
    }

    public List<Cuisine> getByCategoryWithPagination(int offset, int limit, String category) {
        return findMany("SELECT c FROM Cuisine c WHERE c.category.id = ?1 ORDER BY c.id",
                Cuisine.class, category, offset, limit);
    }

    public List<Cuisine> getByNameWithPagination(int offset, int limit, String name) {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE ?1 ORDER BY c.id",
                Cuisine.class, "%" + name + "%", offset, limit);
    }

    public List<Cuisine> getAllWithPagination(int offset, int limit) {
        return findMany("SELECT c FROM Cuisine c ORDER BY c.id", Cuisine.class, offset, limit);
    }

    public int countCuisinesByName(String name) {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.name LIKE ?1", "%" + name + "%");
    }

    public int countCuisinesByCategory(String category) {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.category.id = ?1", category);
    }

    public int countTotal() {
        return count("SELECT COUNT(c) FROM Cuisine c");
    }
}

