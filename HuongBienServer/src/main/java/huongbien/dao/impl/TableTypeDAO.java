package huongbien.dao.impl;

import huongbien.dao.remote.ITableTypeDAO;
import huongbien.entity.TableType;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.NoResultException;

import java.rmi.RemoteException;
import java.util.List;

public class TableTypeDAO extends GenericDAO<TableType> implements ITableTypeDAO {
    public TableTypeDAO() throws RemoteException {
        super();
    }

    public TableTypeDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<TableType> getAll() throws RemoteException {
        return findMany("SELECT t FROM TableType t", TableType.class);
    }

    @Override
    public TableType getById(String id) throws RemoteException {
        return findOne("SELECT t FROM TableType t WHERE t.id = ?1", TableType.class, id);
    }

    @Override
    public TableType getByName(String name) throws RemoteException {
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

    @Override
    public String getTypeName(String name) throws RemoteException {
        String query = "SELECT t FROM TableType t WHERE t.name LIKE ?1";
        TableType tableType = findOne(query, TableType.class, "%" + name + "%");
        return tableType != null ? tableType.getId() : null;
    }

    @Override
    public List<String> getDistinctTableType() throws RemoteException {
        return executeQuery("SELECT DISTINCT t.name FROM TableType t", String.class);
    }

    @Override
    public boolean add(TableType tableType) throws RemoteException {
        return super.add(tableType);
    }

    @Override
    public boolean update(TableType tableType) throws RemoteException {
        return super.update(tableType);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        TableType tableType = getById(id);
        if (tableType == null) {
            return false;
        }
        return super.delete(tableType);
    }
}