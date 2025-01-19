package huongbien;

import huongbien.dao.CategoryDAO;
import huongbien.data.DataGenerator;
import huongbien.entity.Category;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCategoryDAO {
    private static CategoryDAO categoryDAO;

    @BeforeAll
    public static void setUp() {
        System.out.println("Setting up...");
        DataGenerator.generateData(2025, 2025, PersistenceUnit.MARIADB_JPA_CREATE);
        categoryDAO = new CategoryDAO(PersistenceUnit.MARIADB_JPA);
        System.out.println("Done setting up...");
    }

    @Test
    public void testAdd() {
        assertTrue(categoryDAO.add(DataGenerator.fakeCategory()));
        assertFalse(categoryDAO.add(DataGenerator.getRandomCategory()));
    }

    @Test
    public void testUpdate() {
        Category category = DataGenerator.fakeCategory();
        category.setId("CG001");
        category.setName("Khai vị");
        category.setDescription("Món ăn khai vị");
        assertTrue(categoryDAO.update(category));
    }

    @Test
    public void testGetById() {
        assertNull(categoryDAO.getById("1"));

        assertNotNull(categoryDAO.getById("CG001"));
    }

    @Test
    public void testGetAll() {
        assertNotNull(categoryDAO.getAll());
    }

    @Test
    public void testGetByName() {
        assertNotNull(categoryDAO.getByName("Khai vị"));
    }

    @Test
    public void testCountAll() {
        assertTrue(categoryDAO.countAll() > 0);
    }
}
