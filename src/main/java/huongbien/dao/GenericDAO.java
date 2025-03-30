package huongbien.dao;

import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Map;

public abstract class GenericDAO<T> {
    private final EntityManager entityManager;

    public GenericDAO() {
        entityManager = JPAUtil.getEntityManager();
    }

    public GenericDAO(PersistenceUnit persistenceUnit) {
        this.entityManager = JPAUtil.getEntityManager(persistenceUnit);
    }

    public boolean add(T object) {
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

    public boolean update(T object) {
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

    public T findOne(String jpql, Class<T> clazz, Object... params) {
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
            setQueryParameters(query, params);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> findMany(String jpql, Class<T> clazz, Object... params) {
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
            setQueryParameters(query, params);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> findMany(String jpql, Class<T> clazz, Map<String, Object> parameters) {
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
            return null;
        }
    }

    public <T> List<T> findManyWithPagination(String jpql, Class<T> type, Map<String, Object> parameters, int offset, int limit) {
        TypedQuery<T> query = entityManager.createQuery(jpql, type);

        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    public <T> List<T> executeQuery(String query, Class<T> resultClass, Object... params) {
        try {
            TypedQuery<T> executeQuery = entityManager.createQuery(query, resultClass);
            setQueryParameters(executeQuery, params);
            return executeQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int count(String jpql, Object... params) {
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
            query.setParameter(i + 1, params[i]);
        }
    }
}
