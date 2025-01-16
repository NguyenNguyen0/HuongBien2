package huongbien.dao;

import huongbien.entity.Category;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CategoryDAO {
    private final GenericDAO<Category> genericDAO;

    public CategoryDAO() {
        genericDAO = new GenericDAO<>();
    }

    public CategoryDAO(PersistenceUnit persistenceUnit) {
        genericDAO = new GenericDAO<>(persistenceUnit);
    }

    public void add(Category category) {
        genericDAO.add(category);
    }

    public void update(Category category) {
        genericDAO.update(category);
    }

    public Category getById(String id) {
        return genericDAO.findOne("SELECT c FROM Category c WHERE c.id = ?1", Category.class, id);
    }

    public Category getOne(String name, String id) {
        TypedQuery<Category> query = JPAUtil.getEntityManager().createQuery(
                "SELECT c FROM Category c WHERE c.name = :name AND c.id = :id",
                Category.class
        );
        query.setParameter("name", name);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<Category> getByName(String name) {
        return genericDAO.findMany("SELECT c FROM Category c WHERE c.name LIKE ?1", Category.class, "%" + name + "%");
    }
}
