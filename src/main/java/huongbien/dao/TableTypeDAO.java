package huongbien.dao;

import huongbien.entity.TableType;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

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
        String query = "SELECT t FROM TableType t WHERE t.name = ?1";
        return findOne(query, TableType.class, name);
    }

    public String getTypeName(String name) {
        String query = "SELECT t FROM TableType t WHERE t.name LIKE ?1";
        TableType tableType = findOne(query, TableType.class, "%" + name + "%");
        return tableType != null ? tableType.getTableId() : null;
    }

    public List<String> getDistinctTableType() {
        return findMany("SELECT DISTINCT t.name FROM TableType t", String.class);
    }
}
