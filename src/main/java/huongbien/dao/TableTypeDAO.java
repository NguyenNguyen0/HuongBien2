package huongbien.dao;

import huongbien.entity.TableType;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.NoResultException;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class TableTypeDAO extends GenericDAO<TableType> {
    public TableTypeDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<TableType> getAll() {
        return findMany("SELECT t FROM TableType t", TableType.class);
    }

    public TableType getById(String id) {
        return findOne("SELECT t FROM TableType t WHERE t.id = ?1", TableType.class, id);
    }

    // TODO: viết lại hàm và chuyển logic xử lý vào bus
    public TableType getByName(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new NullPointerException("Name is null");
        }

        // First try exact match
        String exactQuery = "SELECT t FROM TableType t WHERE t.name = :name";
        TableType result = findOneWithNamedParam(exactQuery, TableType.class, "name", name);

        if (result == null) {
            // Try case-insensitive match if exact match fails
            String caseInsensitiveQuery = "SELECT t FROM TableType t WHERE LOWER(t.name) = LOWER(:name)";
            result = findOneWithNamedParam(caseInsensitiveQuery, TableType.class, "name", name);
        }

        return result;
    }

    protected <T> T findOneWithNamedParam(String jpql, Class<T> resultClass, String paramName, Object paramValue) {
        try {
            return JPAUtil.getEntityManager().createQuery(jpql, resultClass)
                    .setParameter(paramName, paramValue)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public String getTypeName(String name) {
        String query = "SELECT t FROM TableType t WHERE t.name LIKE ?1";
        TableType tableType = findOne(query, TableType.class, "%" + name + "%");
        return tableType != null ? tableType.getId() : null;
    }

    public List<String> getDistinctTableType() {
        return executeQuery("SELECT DISTINCT t.name FROM TableType t", String.class);
    }
}
