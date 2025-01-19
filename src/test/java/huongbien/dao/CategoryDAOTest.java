package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.Category;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDAOTest {
    private static final CategoryDAO categoryDAO = new CategoryDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    public void add() {
        assertTrue(categoryDAO.add(DataGenerator.fakeCategory()));
        assertFalse(categoryDAO.add(DataGenerator.getRandomCategory()));
    }

    @Test
    public void update() {
        Category category = DataGenerator.fakeCategory();
        category.setId("CG001");
        category.setName("Khai vị");
        category.setDescription("Món ăn khai vị");
        assertTrue(categoryDAO.update(category));
    }

    @Test
    public void getById() {
        assertNull(categoryDAO.getById("1"));

        assertNotNull(categoryDAO.getById("CG001"));
    }

    @Test
    public void getAll() {
        assertNotNull(categoryDAO.getAll());
    }

    @Test
    public void getByName() {
        assertNotNull(categoryDAO.getByName("Khai vị"));
    }

    @Test
    public void countAll() {
        assertTrue(categoryDAO.countAll() > 0);
    }
}
