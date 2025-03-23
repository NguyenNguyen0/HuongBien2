package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.Cuisine;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Cuisine cuisine = DataGenerator.fakeCuisine();
        cuisineDAO.add(cuisine);

        cuisine.setName("Updated Name");
        assertTrue(cuisineDAO.update(cuisine));

        Cuisine updatedCuisine = cuisineDAO.getById(cuisine.getId());
        assertNotNull(updatedCuisine);
        assertEquals("Updated Name", updatedCuisine.getName());
    }

    @Test
    void getAllCuisineNames() {
        assertNotNull(cuisineDAO.getAllCuisineNames());
    }

    @Test
    void getCuisineNamesByCategory() {
        String categoryName = "Some Category";
        List<String> cuisineNames = cuisineDAO.getCuisineNamesByCategory(categoryName);
        assertNotNull(cuisineNames);
    }

    @Test
    void getByName() {
        String name = "Some Name";
        List<Cuisine> cuisines = cuisineDAO.getByName(name);
        assertNotNull(cuisines);
    }

    @Test
    void getAll() {
        assertNotNull(cuisineDAO.getAll());
    }

    @Test
    void getLookUpCuisine() {
        String name = "Cá hồi tươi trộn rau củ và nước sốt.";
        String category = "Khai vị";
        List<Cuisine> cuisines = cuisineDAO.getLookUpCuisine(name, category);
        assertNotNull(cuisines);
    }

    @Test
    void getCountLookUpCuisine() {
        String name = "Cá hồi tươi trộn rau củ và nước sốt.";
        String category = "Khai vị";
        int count = cuisineDAO.getCountLookUpCuisine(name, category);
        assertTrue(count > 0);
    }

    @Test
    void getById() {
        String id = "M001";
        Cuisine cuisine = cuisineDAO.getById(id);
        assertNotNull(cuisine);
    }

    @Test
    void getCuisineCategory() {
        List<String> categories = cuisineDAO.getCuisineCategory();
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
    }

    @Test
    void getByCategoryWithPagination() {
    }

    @Test
    void getByNameWithPagination() {
    }

    @Test
    void getAllWithPagination() {
        int offset = 0;
        int limit = 5;
        List<Cuisine> cuisines = cuisineDAO.getAllWithPagination(offset, limit);
        assertNotNull(cuisines);
        assertFalse(cuisines.isEmpty());
    }

    @Test
    void countCuisinesByName() {
        String name = "Gỏi tôm";
        int count = cuisineDAO.countCuisinesByName(name);
        assertTrue(count > 0);
    }

    @Test
    void countCuisinesByCategory() {
        String category = "CG001";
        int count = cuisineDAO.countCuisinesByCategory(category);
        assertTrue(count > 0);
    }

    @Test
    void countTotal() {
        int count = cuisineDAO.countTotal();
        assertTrue(count > 0);
    }
}