package huongbien.dao.impl;

import huongbien.dao.remote.IGenericDAO;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public abstract class GenericDAO<T> extends UnicastRemoteObject implements IGenericDAO<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private final EntityManager entityManager;

    public GenericDAO() throws RemoteException {
        super();
        entityManager = JPAUtil.getEntityManager();
    }

    public GenericDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super();
        this.entityManager = JPAUtil.getEntityManager(persistenceUnit);
    }

    @Override
    public boolean add(T object) throws RemoteException {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(object);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(T object) throws RemoteException {
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(object);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(T object) throws RemoteException {
        entityManager.getTransaction().begin();
        try {
            entityManager.remove(entityManager.contains(object) ? object : entityManager.merge(object));
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public T findOne(String jpql, Class<T> clazz, Object... params) throws RemoteException {
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
            setQueryParameters(query, params);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findMany(String jpql, Class<T> clazz, Object... params) throws RemoteException {
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
            setQueryParameters(query, params);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<T> findMany(String jpql, Class<T> clazz, Map<String, Object> parameters) throws RemoteException {
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, clazz);

            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public <R> List<R> findManyWithPagination(String jpql, Class<R> type, Map<String, Object> parameters, int offset, int limit) throws RemoteException {
        TypedQuery<R> query = entityManager.createQuery(jpql, type);

        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public <R> List<R> executeQuery(String query, Class<R> resultClass, Object... params) throws RemoteException {
        try {
            TypedQuery<R> executeQuery = entityManager.createQuery(query, resultClass);
            setQueryParameters(executeQuery, params);
            return executeQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public int count(String jpql, Object... params) throws RemoteException {
        try {
            Query query = entityManager.createQuery(jpql);
            setQueryParameters(query, params);
            return ((Number) query.getSingleResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void setQueryParameters(Query query, Object... params) {
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                query.setParameter(i + 1, params[i]);
            }
        }
    }
}