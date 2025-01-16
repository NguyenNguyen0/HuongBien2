package huongbien.dao;

import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public abstract class GenericDAO <T> {
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

    public T findOne(String jpql, Class<T> clazz, Object ...params) {
        TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
        setQueryParameters(query, params);
        return query.getSingleResult();
    }

    public List<T> findMany(String jpql, Class<T> clazz, Object ...params) {
        TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
        setQueryParameters(query, params);
        return query.getResultList();
    }

    public int count(String jpql, Object ...params) {
        Query query = entityManager.createQuery(jpql);
        setQueryParameters(query, params);
        return ((Number) query.getSingleResult()).intValue();
    }

    private void setQueryParameters(Query query, Object... params) {
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
    }
}
