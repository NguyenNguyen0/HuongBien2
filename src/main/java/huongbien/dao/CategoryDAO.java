package huongbien.dao;

import huongbien.entity.Category;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CategoryDAO extends GenericDAO<Category> {

//    tạo constructor để truyền vào persistenceUnit
    public CategoryDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public Category getById(String id) {
        return findOne("SELECT c FROM Category c WHERE c.id = ?1", Category.class, id);
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

    public List<Category> getAll() {
        return findMany("SELECT c FROM Category c", Category.class);
    }

    public List<Category> getByName(String name) {
        return findMany("SELECT c FROM Category c WHERE c.name LIKE ?1", Category.class, "%" + name + "%");
    }

    public int countAll() {
        return count("SELECT COUNT(c) FROM Category c");
    }
}
