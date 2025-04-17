package dao;

import huongbien.dao.impl.CategoryDAO;
import huongbien.entity.Category;
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

class CategoryDAOTest {

    private CategoryDAO categoryDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private String testCategoryId;
    private String testCategoryName;

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        categoryDAO = new CategoryDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test category for each test
        testCategoryId = "CAT-" + UUID.randomUUID().toString().substring(0, 8);
        testCategoryName = "Test Category " + UUID.randomUUID().toString().substring(0, 8);

        // Setup and persist test category
        setupTestCategory();
    }

    private void setupTestCategory() {
        Category testCategory = new Category();
        testCategory.setId(testCategoryId);
        testCategory.setName(testCategoryName);
        testCategory.setDescription("Test category description");

        // Persist in database
        transaction.begin();
        entityManager.persist(testCategory);
        transaction.commit();
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        Category category = categoryDAO.getById(testCategoryId);

        // Assert
        assertNotNull(category);
        assertEquals(testCategoryId, category.getId());
        assertEquals(testCategoryName, category.getName());
    }

    @Test
    void testGetById_notFound() throws RemoteException {
        // Act
        Category category = categoryDAO.getById("NONEXISTENT");

        // Assert
        assertNull(category);
    }

    @Test
    void testGetAllCategoryNames() throws RemoteException {
        // Act
        List<String> categoryNames = categoryDAO.getAllCategoryNames();

        // Assert
        assertNotNull(categoryNames);
        assertTrue(categoryNames.size() > 0);
        assertTrue(categoryNames.contains(testCategoryName));
    }

    @Test
    void testGetCategoryName() throws RemoteException {
        // Act
        Category category = categoryDAO.getCategoryName(testCategoryName);

        // Assert
        assertNotNull(category);
        assertEquals(testCategoryId, category.getId());
        assertEquals(testCategoryName, category.getName());
    }

    @Test
    void testGetCategoryName_notFound() throws RemoteException {
        // Act
        Category category = categoryDAO.getCategoryName("Nonexistent Category");

        // Assert
        assertNull(category);
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Category> categories = categoryDAO.getAll();

        // Assert
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
        assertTrue(categories.stream()
                .anyMatch(c -> c.getId().equals(testCategoryId) && c.getName().equals(testCategoryName)));
    }

    @Test
    void testGetByName() throws RemoteException {
        // Act
        List<Category> categories = categoryDAO.getByName(testCategoryName.substring(5, 10));

        // Assert
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
        assertTrue(categories.stream().anyMatch(c -> c.getId().equals(testCategoryId)));
    }

    @Test
    void testGetByName_noMatches() throws RemoteException {
        // Act
        List<Category> categories = categoryDAO.getByName(UUID.randomUUID().toString());

        // Assert
        assertNotNull(categories);
        assertEquals(0, categories.size());
    }

    @Test
    void testCountAll() throws RemoteException {
        // Act
        int count = categoryDAO.countAll();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testAddCategory() throws RemoteException {
        // Arrange
        String newCategoryId = "CAT-" + UUID.randomUUID().toString().substring(0, 8);
        String newCategoryName = "New Category " + UUID.randomUUID().toString().substring(0, 8);

        Category newCategory = new Category();
        newCategory.setId(newCategoryId);
        newCategory.setName(newCategoryName);
        newCategory.setDescription("New category description");

        // Act
        boolean result = categoryDAO.add(newCategory);

        // Assert
        assertTrue(result);

        // Verify category was added
        Category addedCategory = categoryDAO.getById(newCategoryId);
        assertNotNull(addedCategory);
        assertEquals(newCategoryName, addedCategory.getName());
    }

    @Test
    void testUpdateCategory() throws RemoteException {
        // Arrange
        Category category = categoryDAO.getById(testCategoryId);
        String updatedDescription = "Updated description " + UUID.randomUUID().toString();
        category.setDescription(updatedDescription);

        // Act
        boolean result = categoryDAO.update(category);

        // Assert
        assertTrue(result);

        // Verify category was updated
        Category updatedCategory = categoryDAO.getById(testCategoryId);
        assertEquals(updatedDescription, updatedCategory.getDescription());
    }

    @Test
    void testDeleteCategory() throws RemoteException {
        // Arrange
        // Create a temporary category to delete
        String tempCategoryId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
        Category tempCategory = new Category();
        tempCategory.setId(tempCategoryId);
        tempCategory.setName("Temporary Category");
        tempCategory.setDescription("Temporary description");

        // Add the category first
        transaction.begin();
        entityManager.persist(tempCategory);
        transaction.commit();

        // Verify it exists
        Category addedCategory = categoryDAO.getById(tempCategoryId);
        assertNotNull(addedCategory);

        // Act
        boolean result = categoryDAO.delete(addedCategory);

        // Assert
        assertTrue(result);

        // Verify category was deleted
        Category deletedCategory = categoryDAO.getById(tempCategoryId);
        assertNull(deletedCategory);
    }
}