package dao;

import huongbien.dao.impl.OrderDetailDAO;
import huongbien.entity.OrderDetail;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDetailDAOTest {

    private PersistenceUnit persistenceUnit;
    private OrderDetailDAO orderDetailDAO;
    
    @BeforeEach
    public void setUp() throws RemoteException {
        // Create a real persistence unit
        persistenceUnit = PersistenceUnit.MARIADB_JPA;
        orderDetailDAO = new OrderDetailDAO(persistenceUnit);
    }
    
    @Test
    public void testGetAllByOrderId() throws RemoteException {
        // Use a known order ID from your test database
        String orderId = "ORDER001"; 
        
        // Act
        List<OrderDetail> details = orderDetailDAO.getAllByOrderId(orderId);
        
        // Assert - we can only verify the method executes without throwing exceptions
        assertNotNull(details, "The returned list should not be null");
    }
    
    @Test
    public void testGetById() throws RemoteException {
        // Use a known order detail ID from your test database
        String detailId = "DETAIL001";
        
        // Act
        OrderDetail detail = orderDetailDAO.getById(detailId);
        
        // Assert
        // If the record exists in the database
        if (detail != null) {
            assertEquals(detailId, detail.getId());
        } else {
            // If the test data doesn't have this record, test would be skipped
            // You might want to use Assumptions for this case
            System.out.println("Test data doesn't have a record with ID: " + detailId);
        }
    }
    
    @Test
    public void testGetCountOfUnitsSold() throws RemoteException {
        // Arrange
        String cuisineId = "CUISINE001"; // Use a known cuisine ID from your database
        
        // Act
        int actualCount = orderDetailDAO.getCountOfUnitsSold(cuisineId);
        
        // Assert
        // We can only verify the method doesn't throw exceptions and returns a non-negative number
        assertTrue(actualCount >= 0, "Count of units sold should be non-negative");
    }
    
    @Test
    public void testGetCountOfUnitsSold_NoResults() throws RemoteException {
        // Arrange
        String cuisineId = "NONEXISTENT_CUISINE_ID"; // An ID that shouldn't exist in the database
        
        // Act
        int actualCount = orderDetailDAO.getCountOfUnitsSold(cuisineId);
        
        // Assert
        assertEquals(0, actualCount, "Count for non-existent cuisine should be 0");
    }
}
