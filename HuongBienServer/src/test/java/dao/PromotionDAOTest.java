package dao;

import huongbien.dao.impl.PromotionDAO;
import huongbien.entity.MembershipLevel;
import huongbien.entity.Promotion;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PromotionDAOTest {

    private PromotionDAO promotionDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    
    private String testPromotionId;
    private String testPromotionName;
    private double testDiscount;

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        promotionDAO = new PromotionDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test data
        testPromotionId = "PROMO-" + UUID.randomUUID().toString().substring(0, 8);
        testPromotionName = "Test Promotion " + UUID.randomUUID().toString().substring(0, 8);
        testDiscount = 15.0;

        // Setup test promotion
        setupTestPromotion();
    }

    private void setupTestPromotion() {
        Promotion testPromotion = new Promotion();
        testPromotion.setId(testPromotionId);
        testPromotion.setName(testPromotionName);
        testPromotion.setDiscount(testDiscount);
        testPromotion.setMembershipLevel(MembershipLevel.SILVER);
        testPromotion.setMinimumOrderAmount(100000);
        testPromotion.setStartDate(LocalDate.now().minusDays(5));
        testPromotion.setEndDate(LocalDate.now().plusDays(30));
        testPromotion.setAvailable(true);
        testPromotion.setDescription("Test promotion description");

        // Persist in database
        transaction.begin();
        entityManager.persist(testPromotion);
        transaction.commit();
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getAll();

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.size() > 0);
        assertTrue(promotions.stream().anyMatch(p -> p.getId().equals(testPromotionId)));
    }

    @Test
    void testGetAllById() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getAllById(testPromotionId.substring(0, 6));

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.stream().anyMatch(p -> p.getId().equals(testPromotionId)));
    }

    @Test
    void testGetForCustomer() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getForCustomer(MembershipLevel.SILVER.getLevel(), 150000);

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.stream().anyMatch(p -> p.getId().equals(testPromotionId)));
    }

    @Test
    void testGetByStatus() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getByStatus("Còn hiệu lực");

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.stream().anyMatch(p -> p.getId().equals(testPromotionId)));
    }

    @Test
    void testGetByStatusWithPagination() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getByStatusWithPagination(0, 10, "Còn hiệu lực");

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.stream().anyMatch(p -> p.getId().equals(testPromotionId)));
    }

    @Test
    void testGetAllWithPagination() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getAllWithPagination(0, 10);

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.size() > 0);
    }

    @Test
    void testGetAllByIdWithPagination() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getAllByIdWithPagination(0, 10, testPromotionId.substring(0, 6));

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.stream().anyMatch(p -> p.getId().equals(testPromotionId)));
    }

    @Test
    void testGetById() throws RemoteException {
        // Act
        Promotion promotion = promotionDAO.getById(testPromotionId);

        // Assert
        assertNotNull(promotion);
        assertEquals(testPromotionId, promotion.getId());
        assertEquals(testPromotionName, promotion.getName());
        assertEquals(testDiscount, promotion.getDiscount());
    }

    @Test
    void testCountTotal() throws RemoteException {
        // Act
        int count = promotionDAO.countTotal();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountTotalByStatus() throws RemoteException {
        // Act
        int count = promotionDAO.countTotalByStatus("Còn hiệu lực");

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testGetDistinctPromotionDiscount() throws RemoteException {
        // Act
        List<String> discounts = promotionDAO.getDistinctPromotionDiscount();

        // Assert
        assertNotNull(discounts);
        assertTrue(discounts.size() > 0);
        assertTrue(discounts.contains(String.valueOf(testDiscount)) || 
                 discounts.stream().anyMatch(d -> d.equals(String.valueOf(testDiscount))));
    }

    @Test
    void testGetDistinctPromotionStatus() throws RemoteException {
        // Act
        List<String> statuses = promotionDAO.getDistinctPromotionStatus();

        // Assert
        assertNotNull(statuses);
        assertTrue(statuses.size() > 0);
        assertTrue(statuses.contains("Còn hiệu lực"));
    }

    @Test
    void testGetDistinctPromotionMinimumOrderAmount() throws RemoteException {
        // Act
        List<String> amounts = promotionDAO.getDistinctPromotionMinimumOrderAmount();

        // Assert
        assertNotNull(amounts);
        assertTrue(amounts.size() > 0);
        assertTrue(amounts.contains("100000.0") || 
                 amounts.stream().anyMatch(a -> Double.parseDouble(a) == 100000.0));
    }

    @Test
    void testGetLookUpPromotion() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getLookUpPromotion(
                testPromotionName.substring(5, 10), null, null, testDiscount, 0, "Còn hiệu lực", 0);

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.stream().anyMatch(p -> p.getId().equals(testPromotionId)));
    }

    @Test
    void testGetCountLookUpPromotion() throws RemoteException {
        // Act
        int count = promotionDAO.getCountLookUpPromotion(
                testPromotionName.substring(5, 10), null, null, testDiscount, 0, "Còn hiệu lực");

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testGetPaymentPromotion() throws RemoteException {
        // Act
        List<Promotion> promotions = promotionDAO.getPaymentPromotion(MembershipLevel.SILVER.getLevel(), 150000);

        // Assert
        assertNotNull(promotions);
        assertTrue(promotions.stream().anyMatch(p -> p.getId().equals(testPromotionId)));
    }

    @Test
    void testCountTotalById() throws RemoteException {
        // Act
        int count = promotionDAO.countTotalById(testPromotionId.substring(0, 6));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testAddPromotion() throws RemoteException {
        // Arrange
        String newPromotionId = "PROMO-" + UUID.randomUUID().toString().substring(0, 8);
        String newPromotionName = "New Promotion " + UUID.randomUUID().toString().substring(0, 8);
        
        Promotion newPromotion = new Promotion();
        newPromotion.setId(newPromotionId);
        newPromotion.setName(newPromotionName);
        newPromotion.setDiscount(20.0);
        newPromotion.setMembershipLevel(MembershipLevel.GOLD);
        newPromotion.setMinimumOrderAmount(200000);
        newPromotion.setStartDate(LocalDate.now());
        newPromotion.setEndDate(LocalDate.now().plusDays(60));
        newPromotion.setAvailable(true);
        newPromotion.setDescription("New promotion description");

        // Act
        boolean result = promotionDAO.add(newPromotion);

        // Assert
        assertTrue(result);

        // Verify promotion was added
        Promotion addedPromotion = promotionDAO.getById(newPromotionId);
        assertNotNull(addedPromotion);
        assertEquals(newPromotionName, addedPromotion.getName());
        assertEquals(20.0, addedPromotion.getDiscount());
    }

    @Test
    void testUpdatePromotion() throws RemoteException {
        // Arrange
        Promotion promotion = promotionDAO.getById(testPromotionId);
        promotion.setDiscount(25.0);
        promotion.setDescription("Updated description");

        // Act
        boolean result = promotionDAO.update(promotion);

        // Assert
        assertTrue(result);

        // Verify promotion was updated
        Promotion updatedPromotion = promotionDAO.getById(testPromotionId);
        assertEquals(25.0, updatedPromotion.getDiscount());
        assertEquals("Updated description", updatedPromotion.getDescription());
    }

    @Test
    void testDeletePromotion() throws RemoteException {
        // Arrange
        // Create a temporary promotion to delete
        String tempPromotionId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
        
        Promotion tempPromotion = new Promotion();
        tempPromotion.setId(tempPromotionId);
        tempPromotion.setName("Temporary Promotion");
        tempPromotion.setDiscount(10.0);
        tempPromotion.setMembershipLevel(MembershipLevel.BRONZE);
        tempPromotion.setMinimumOrderAmount(50000);
        tempPromotion.setStartDate(LocalDate.now());
        tempPromotion.setEndDate(LocalDate.now().plusDays(10));
        tempPromotion.setAvailable(true);

        // Add the promotion first
        transaction.begin();
        entityManager.persist(tempPromotion);
        transaction.commit();

        // Verify it exists
        Promotion addedPromotion = promotionDAO.getById(tempPromotionId);
        assertNotNull(addedPromotion);

        // Act
        boolean result = promotionDAO.delete(addedPromotion);

        // Assert
        assertTrue(result);

        // Verify promotion was deleted
        Promotion deletedPromotion = promotionDAO.getById(tempPromotionId);
        assertNull(deletedPromotion);
    }
}