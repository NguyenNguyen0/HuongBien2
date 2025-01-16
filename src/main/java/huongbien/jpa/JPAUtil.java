package huongbien.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;

public class JPAUtil implements AutoCloseable {
    private static final HashMap<String, EntityManagerFactory> entityManagerFactories = new HashMap<>();

    public static EntityManager getEntityManager(PersistenceUnit persistenceUnit) {
        EntityManagerFactory entityManagerFactory = entityManagerFactories.get(persistenceUnit.getValue());
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit.getValue());
            entityManagerFactories.put(persistenceUnit.getValue(), entityManagerFactory);
        }
        return entityManagerFactory.createEntityManager();
    }

    public static EntityManager getEntityManager() {
        return getEntityManager(PersistenceUnit.MARIADB_JPA);
    }

    @Override
    public void close() throws Exception {
        for (EntityManagerFactory entityManagerFactory : entityManagerFactories.values()) {
            entityManagerFactory.close();
        }
    }
}