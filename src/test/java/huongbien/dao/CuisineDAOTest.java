package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuisineDAOTest {
    private static final CuisineDAO cuisineDAO = new CuisineDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    void add() {
        assertTrue(cuisineDAO.add(DataGenerator.fakeCuisine()));
        assertFalse(cuisineDAO.add(DataGenerator.getRandomCuisine()));
    }

    @Test
    void update() {
    }

    @Test
    void getAllCuisineNames() {
        assertNotNull(cuisineDAO.getAllCuisineNames());
    }

    @Test
    void getCuisineNamesByCategory() {
    }

    @Test
    void getByName() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getLookUpCuisine() {
    }

    @Test
    void testGetLookUpCuisine() {
    }

    @Test
    void getCountLookUpCuisine() {
    }

    @Test
    void getById() {
    }

    @Test
    void getCuisineCategory() {
    }

    @Test
    void getByCategoryWithPagination() {
    }

    @Test
    void getByNameWithPagination() {
    }

    @Test
    void getAllWithPagination() {
    }

    @Test
    void countCuisinesByName() {
    }

    @Test
    void countCuisinesByCategory() {
    }

    @Test
    void countTotal() {
    }
}