package huongbien.dao;

import huongbien.entity.Cuisine;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

import static huongbien.jpa.JPAUtil.getEntityManager;

@NoArgsConstructor
public class CuisineDAO extends GenericDAO<Cuisine> {

    public CuisineDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    // Hỗ trợ các kiểu trả về không phải thực thể.
    private <T> List<T> executeQuery(String jpql, Class<T> clazz, Object... params) {
        TypedQuery<T> query = getEntityManager().createQuery(jpql, clazz);
        setQueryParameters(query, params);
        return query.getResultList();
    }

    private void setQueryParameters(Query query, Object... params) {
        for (int i = 0; i < params.length; i++) query.setParameter(i + 1, params[i]);
    }

    public List<String> getAllCuisineNames() {
        return executeQuery("SELECT c.name FROM Cuisine c", String.class);
    }

    public List<String> getCuisineNamesByCategory(String categoryName) {
        return executeQuery("SELECT c.name FROM Cuisine c WHERE c.category.id IN " +
                "(SELECT ct.id FROM Category ct WHERE ct.name LIKE :categoryName)", String.class, "%" + categoryName + "%");
    }

    public List<Cuisine> getByName(String name) {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE :name", Cuisine.class, "%" + name + "%");
    }

    public List<Cuisine> getAll() {
        return findMany("SELECT c FROM Cuisine c", Cuisine.class);
    }

    public List<Cuisine> getLookUpCuisine(String name, String category) {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE :name AND c.category.id LIKE :category",
                Cuisine.class, "%" + name + "%", "%" + category + "%");
    }

    public List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex) {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE :name AND c.category.id LIKE :category ORDER BY c.category.id",
                Cuisine.class, pageIndex, 7, "%" + name + "%", "%" + category + "%");
    }

    public int getCountLookUpCuisine(String name, String category) {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.name LIKE :name AND c.category.id LIKE :category",
                "%" + name + "%", "%" + category + "%");
    }

    public Cuisine getById(String id) {
        return findOne("SELECT c FROM Cuisine c WHERE c.id = :id", Cuisine.class, id);
    }

    public List<String> getCuisineCategory() {
        return executeQuery("SELECT DISTINCT c.category.id FROM Cuisine c", String.class);
    }

    public List<Cuisine> getByCategoryWithPagination(int offset, int limit, String category) {
        return findMany("SELECT c FROM Cuisine c WHERE c.category.id IN " +
                        "(SELECT ct.id FROM Category ct WHERE ct.name LIKE :category) ORDER BY c.id",
                Cuisine.class, offset, limit, "%" + category + "%");
    }

    public List<Cuisine> getByNameWithPagination(int offset, int limit, String name) {
        return findMany("SELECT c FROM Cuisine c WHERE c.name LIKE :name ORDER BY c.id",
                Cuisine.class, offset, limit, "%" + name + "%");
    }

    public List<Cuisine> getAllWithPagination(int offset, int limit) {
        return findMany("SELECT c FROM Cuisine c ORDER BY c.id", Cuisine.class, offset, limit);
    }

    public int countCuisinesByName(String name) {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.name LIKE :name", "%" + name + "%");
    }

    public int countCuisinesByCategory(String category) {
        return count("SELECT COUNT(c) FROM Cuisine c WHERE c.category.id IN " +
                "(SELECT ct.id FROM Category ct WHERE ct.name LIKE :category)", "%" + category + "%");
    }

    public int countTotal() {
        return count("SELECT COUNT(c) FROM Cuisine c");
    }
}
