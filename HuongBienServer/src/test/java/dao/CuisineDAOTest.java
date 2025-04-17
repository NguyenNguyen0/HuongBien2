package dao;

import huongbien.dao.impl.CategoryDAO;
import huongbien.dao.impl.CuisineDAO;
import huongbien.entity.Category;
import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CuisineDAOTest {

    private CuisineDAO cuisineDAO;
    private CategoryDAO categoryDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private String testCuisineId;
    private String testCuisineName;
    private String testCategoryId;

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        cuisineDAO = new CuisineDAO(PersistenceUnit.MARIADB_JPA);
        categoryDAO = new CategoryDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test IDs
        testCategoryId = "CAT-" + UUID.randomUUID().toString().substring(0, 8);
        testCuisineId = "CUI-" + UUID.randomUUID().toString().substring(0, 8);
        testCuisineName = "Test Cuisine " + UUID.randomUUID().toString().substring(0, 8);

        // Setup test data
        setupTestCategory();
        setupTestCuisine();
    }

    private void setupTestCategory() {
        Category testCategory = new Category();
        testCategory.setId(testCategoryId);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test category description");

        // Persist in database
        transaction.begin();
        entityManager.persist(testCategory);
        transaction.commit();
    }

    private void setupTestCuisine() {
        // Get the category
        Category category = entityManager.find(Category.class, testCategoryId);
        
        Cuisine testCuisine = new Cuisine();
        testCuisine.setId(testCuisineId);
        testCuisine.setName(testCuisineName);
        testCuisine.setPrice(100000);
        testCuisine.setDescription("Test cuisine description");
        testCuisine.setStatus(CuisineStatus.AVAILABLE);
        testCuisine.setCategory(category);

        // Persist in database
        transaction.begin();
        entityManager.persist(testCuisine);
        transaction.commit();
    }

    @Test
    void testGetAllCuisineNames() throws RemoteException {
        // Act
        List<String> cuisineNames = cuisineDAO.getAllCuisineNames();

        // Assert
        assertNotNull(cuisineNames);
        assertTrue(cuisineNames.size() > 0);
        assertTrue(cuisineNames.contains(testCuisineName));
    }

    @Test
    void testGetCuisineNamesByCategory() throws RemoteException {
        // Act
        List<String> cuisineNames = cuisineDAO.getCuisineNamesByCategory("Test Category");

        // Assert
        assertNotNull(cuisineNames);
        assertTrue(cuisineNames.contains(testCuisineName));
    }

    @Test
    void testGetByName() throws RemoteException {
        // Act
        List<Cuisine> cuisines = cuisineDAO.getByName(testCuisineName.substring(5, 10));

        // Assert
        assertNotNull(cuisines);
        assertTrue(cuisines.size() > 0);
        assertTrue(cuisines.stream().anyMatch(c -> c.getId().equals(testCuisineId)));
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Cuisine> cuisines = cuisineDAO.getAll();

        // Assert
        assertNotNull(cuisines);
        assertTrue(cuisines.size() > 0);
        assertTrue(cuisines.stream()
                .anyMatch(c -> c.getId().equals(testCuisineId) && c.getName().equals(testCuisineName)));
    }

    @Test
    void testGetLookUpCuisine() throws RemoteException {
        // Act
        List<Cuisine> cuisines = cuisineDAO.getLookUpCuisine(testCuisineName.substring(5, 10), testCategoryId);

        // Assert
        assertNotNull(cuisines);
        assertTrue(cuisines.size() > 0);
        assertTrue(cuisines.stream().anyMatch(c -> c.getId().equals(testCuisineId)));
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        Cuisine cuisine = cuisineDAO.getById(testCuisineId);

        // Assert
        assertNotNull(cuisine);
        assertEquals(testCuisineId, cuisine.getId());
        assertEquals(testCuisineName, cuisine.getName());
    }

    @Test
    void testGetCuisineCategory() throws RemoteException {
        // Act
        List<String> categories = cuisineDAO.getCuisineCategory();

        // Assert
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
        assertTrue(categories.contains(testCategoryId));
    }

    @Test
    void testGetByCategoryWithPagination() throws RemoteException {
        // Act
        List<Cuisine> cuisines = cuisineDAO.getByCategoryWithPagination(testCategoryId, 0, 10);

        // Assert
        assertNotNull(cuisines);
        assertTrue(cuisines.stream().anyMatch(c -> c.getId().equals(testCuisineId)));
    }

    @Test
    void testGetByNameWithPagination() throws RemoteException {
        // Act
        List<Cuisine> cuisines = cuisineDAO.getByNameWithPagination(0, 10, testCuisineName.substring(5, 10));

        // Assert
        assertNotNull(cuisines);
        assertTrue(cuisines.stream().anyMatch(c -> c.getId().equals(testCuisineId)));
    }

    @Test
    void testGetAllWithPagination() throws RemoteException {
        // Act
        List<Cuisine> cuisines = cuisineDAO.getAllWithPagination(0, 10);

        // Assert
        assertNotNull(cuisines);
        assertTrue(cuisines.size() > 0);
    }

    @Test
    void testCountCuisinesByName() throws RemoteException {
        // Act
        int count = cuisineDAO.countCuisinesByName(testCuisineName.substring(5, 10));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testUpdateCuisineStatus() throws RemoteException {
        // Act
        boolean result = cuisineDAO.updateCuisineStatus(testCuisineId, CuisineStatus.DISCONTINUED);

        // Assert
        assertTrue(result);
        
        // Verify status was updated
        Cuisine updatedCuisine = cuisineDAO.getById(testCuisineId);
        assertEquals(CuisineStatus.DISCONTINUED, updatedCuisine.getStatus());
    }

    @Test
    void testCountCuisinesByCategory() throws RemoteException {
        // Act
        int count = cuisineDAO.countCuisinesByCategory(testCategoryId);

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotal() throws RemoteException {
        // Act
        int count = cuisineDAO.countTotal();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testAddCuisine() throws RemoteException {
        // Arrange
        String newCuisineId = "CUI-" + UUID.randomUUID().toString().substring(0, 8);
        String newCuisineName = "New Cuisine " + UUID.randomUUID().toString().substring(0, 8);
        
        Category category = entityManager.find(Category.class, testCategoryId);
        
        Cuisine newCuisine = new Cuisine();
        newCuisine.setId(newCuisineId);
        newCuisine.setName(newCuisineName);
        newCuisine.setPrice(150000);
        newCuisine.setDescription("New cuisine description");
        newCuisine.setStatus(CuisineStatus.AVAILABLE);
        newCuisine.setCategory(category);

        // Act
        boolean result = cuisineDAO.add(newCuisine);

        // Assert
        assertTrue(result);

        // Verify cuisine was added
        Cuisine addedCuisine = cuisineDAO.getById(newCuisineId);
        assertNotNull(addedCuisine);
        assertEquals(newCuisineName, addedCuisine.getName());
    }

    @Test
    void testUpdateCuisine() throws RemoteException {
        // Arrange
        Cuisine cuisine = cuisineDAO.getById(testCuisineId);
        String updatedDescription = "Updated description " + UUID.randomUUID().toString();
        cuisine.setDescription(updatedDescription);

        // Act
        boolean result = cuisineDAO.update(cuisine);

        // Assert
        assertTrue(result);

        // Verify cuisine was updated
        Cuisine updatedCuisine = cuisineDAO.getById(testCuisineId);
        assertEquals(updatedDescription, updatedCuisine.getDescription());
    }

    @Test
    void testDeleteCuisine() throws RemoteException {
        // Arrange
        // Create a temporary cuisine to delete
        String tempCuisineId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
        Category category = entityManager.find(Category.class, testCategoryId);
        
        Cuisine tempCuisine = new Cuisine();
        tempCuisine.setId(tempCuisineId);
        tempCuisine.setName("Temporary Cuisine");
        tempCuisine.setPrice(120000);
        tempCuisine.setDescription("Temporary description");
        tempCuisine.setStatus(CuisineStatus.AVAILABLE);
        tempCuisine.setCategory(category);

        // Add the cuisine first
        transaction.begin();
        entityManager.persist(tempCuisine);
        transaction.commit();

        // Verify it exists
        Cuisine addedCuisine = cuisineDAO.getById(tempCuisineId);
        assertNotNull(addedCuisine);

        // Act
        boolean result = cuisineDAO.delete(addedCuisine);

        // Assert
        assertTrue(result);

        // Verify cuisine was deleted
        Cuisine deletedCuisine = cuisineDAO.getById(tempCuisineId);
        assertNull(deletedCuisine);
    }
}