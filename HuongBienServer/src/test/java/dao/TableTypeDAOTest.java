package dao;

import huongbien.dao.impl.TableTypeDAO;
import huongbien.entity.TableType;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TableTypeDAOTest {
    private TableTypeDAO tableTypeDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private String testTypeId;
    private String testTypeName;

    @BeforeEach
    void setUp() throws RemoteException {
        tableTypeDAO = new TableTypeDAO(PersistenceUnit.MARIADB_JPA);
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test IDs
        testTypeId = "TT-" + UUID.randomUUID().toString().substring(0, 8);
        testTypeName = "Test Type " + UUID.randomUUID().toString().substring(0, 5);

        // Set up test data
        setupTestTableType();
    }

    @AfterEach
    void tearDown() {
        // Clean up test data
        cleanupTestTableType();

        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    private void setupTestTableType() {
        TableType testType = new TableType();
        testType.setId(testTypeId);
        testType.setName(testTypeName);

        transaction.begin();
        entityManager.persist(testType);
        transaction.commit();
        entityManager.clear();
    }

    private void cleanupTestTableType() {
        transaction.begin();
        entityManager.createQuery("DELETE FROM TableType t WHERE t.id = :id")
                .setParameter("id", testTypeId)
                .executeUpdate();
        transaction.commit();
        entityManager.clear();
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<TableType> tableTypes = tableTypeDAO.getAll();

        // Assert
        assertNotNull(tableTypes);
        assertFalse(tableTypes.isEmpty());
        assertTrue(tableTypes.stream().anyMatch(t -> t.getId().equals(testTypeId)));
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        TableType tableType = tableTypeDAO.getById(testTypeId);

        // Assert
        assertNotNull(tableType);
        assertEquals(testTypeId, tableType.getId());
        assertEquals(testTypeName, tableType.getName());
    }

    @Test
    void testGetByIdNotFound() throws RemoteException {
        // Act
        TableType tableType = tableTypeDAO.getById("NON_EXISTENT_ID");

        // Assert
        assertNull(tableType);
    }

    @Test
    void testGetByNameExactMatch() throws RemoteException {
        // Act
        TableType tableType = tableTypeDAO.getByName(testTypeName);

        // Assert
        assertNotNull(tableType);
        assertEquals(testTypeId, tableType.getId());
        assertEquals(testTypeName, tableType.getName());
    }

    @Test
    void testGetByNameCaseInsensitive() throws RemoteException {
        // Act
        TableType tableType = tableTypeDAO.getByName(testTypeName.toLowerCase());

        // Assert
        assertNotNull(tableType);
        assertEquals(testTypeId, tableType.getId());
        assertEquals(testTypeName, tableType.getName());
    }

    @Test
    void testGetByNameNotFound() throws RemoteException {
        // Act
        TableType tableType = tableTypeDAO.getByName("NON_EXISTENT_NAME");

        // Assert
        assertNull(tableType);
    }

    @Test
    void testGetByNameNullOrEmpty() {
        // Assert
        assertThrows(NullPointerException.class, () -> tableTypeDAO.getByName(null));
        assertThrows(NullPointerException.class, () -> tableTypeDAO.getByName(""));
        assertThrows(NullPointerException.class, () -> tableTypeDAO.getByName("  "));
    }

    @Test
    void testGetTypeName() throws RemoteException {
        // Act
        String typeId = tableTypeDAO.getTypeName(testTypeName.substring(2, 6));

        // Assert
        assertNotNull(typeId);
        assertEquals(testTypeId, typeId);
    }

    @Test
    void testGetTypeNameNotFound() throws RemoteException {
        // Act
        String typeId = tableTypeDAO.getTypeName("NON_EXISTENT_NAME");

        // Assert
        assertNull(typeId);
    }

    @Test
    void testGetDistinctTableType() throws RemoteException {
        // Act
        List<String> distinctTypes = tableTypeDAO.getDistinctTableType();

        // Assert
        assertNotNull(distinctTypes);
        assertFalse(distinctTypes.isEmpty());
        assertTrue(distinctTypes.contains(testTypeName));
    }

    @Test
    void testAdd() throws RemoteException {
        // Arrange
        String newTypeId = "TT-NEW-" + UUID.randomUUID().toString().substring(0, 5);
        TableType newType = new TableType();
        newType.setId(newTypeId);
        newType.setName("New Test Type");

        try {
            // Act
            boolean result = tableTypeDAO.add(newType);

            // Assert
            assertTrue(result);

            // Verify
            TableType addedType = tableTypeDAO.getById(newTypeId);
            assertNotNull(addedType);
            assertEquals("New Test Type", addedType.getName());
        } finally {
            // Cleanup
            transaction.begin();
            entityManager.createQuery("DELETE FROM TableType t WHERE t.id = :id")
                    .setParameter("id", newTypeId)
                    .executeUpdate();
            transaction.commit();
        }
    }

    @Test
    void testUpdate() throws RemoteException {
        // Arrange
        TableType typeToUpdate = tableTypeDAO.getById(testTypeId);
        String updatedName = "Updated Type Name " + UUID.randomUUID().toString().substring(0, 5);
        typeToUpdate.setName(updatedName);

        // Act
        boolean result = tableTypeDAO.update(typeToUpdate);

        // Assert
        assertTrue(result);

        // Verify
        entityManager.clear();
        TableType updatedType = tableTypeDAO.getById(testTypeId);
        assertNotNull(updatedType);
        assertEquals(updatedName, updatedType.getName());
    }

    @Test
    void testDelete() throws RemoteException {
        // Arrange - create a new table type to delete
        String deleteTypeId = "TT-DEL-" + UUID.randomUUID().toString().substring(0, 5);
        TableType typeToDelete = new TableType();
        typeToDelete.setId(deleteTypeId);
        typeToDelete.setName("Type to Delete");

        transaction.begin();
        entityManager.persist(typeToDelete);
        transaction.commit();
        entityManager.clear();

        // Verify it exists
        assertNotNull(tableTypeDAO.getById(deleteTypeId));

        // Act
        boolean result = tableTypeDAO.delete(deleteTypeId);

        // Assert
        assertTrue(result);

        // Verify deletion
        assertNull(tableTypeDAO.getById(deleteTypeId));
    }

    @Test
    void testDeleteNonExistent() throws RemoteException {
        // Act
        boolean result = tableTypeDAO.delete("NON_EXISTENT_ID");

        // Assert
        assertFalse(result);
    }
}