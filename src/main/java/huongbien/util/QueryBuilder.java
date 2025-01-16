package huongbien.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class QueryBuilder {
    public static <T> List<T> buildQuery(String jpql, Object... params) {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<T> query = em.createQuery(jpql, (Class<T>) Object.class);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }
}
