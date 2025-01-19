package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.TableType;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TableTypeDAOTest {
    private static final TableTypeDAO tableTypeDAO = new TableTypeDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    public void add() {
        assertTrue(tableTypeDAO.add(DataGenerator.fakeTableType()));
        assertFalse(tableTypeDAO.add(DataGenerator.getRandomTableType()));
    }

    @Test
    public void update() {
        TableType tableType = DataGenerator.fakeTableType();
        tableType.setId("TT001");
        tableType.setName("New Table Type");
        assertTrue(tableTypeDAO.update(tableType));
    }

    @Test
    public void getAll() {
        assertNotNull(tableTypeDAO.getAll());
    }

    @Test
    public void getById() {
        assertNull(tableTypeDAO.getById("1"));
        assertNotNull(tableTypeDAO.getById("TT001"));
    }

    @Test
    public void getByName() {
        assertNotNull(tableTypeDAO.getByName("New Table Type"));
    }

    @Test
    public void getTypeName() {
        assertEquals("TT001", tableTypeDAO.getTypeName("New Table Type"));
    }

    @Test
    public void getDistinctTableType() {
        assertNotNull(tableTypeDAO.getDistinctTableType());
    }
}