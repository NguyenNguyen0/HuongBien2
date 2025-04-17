package dao;

import huongbien.dao.impl.StatisticsDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsDAOTest {
    private StatisticsDAO statisticsDAO;
    private final int testYear = LocalDate.now().getYear();
    private final int testPreviousYear = testYear - 1;
    private final int testMonth = 1;
    private final int testQuarter = 1;

    @BeforeEach
    void setUp() throws RemoteException {
        statisticsDAO = new StatisticsDAO();
    }

    @AfterEach
    void tearDown() {
        // Clean up resources if needed
        statisticsDAO = null;
    }

    @Test
    @DisplayName("Test getting total revenue with different criteria")
    void testGetTotalRevenue() throws RemoteException {
        // Test with month criteria
        double monthlyRevenue = statisticsDAO.getTotalRevenue("Tháng", testMonth, testYear);
        assertTrue(monthlyRevenue >= 0, "Monthly revenue should be non-negative");

        // Test with quarter criteria
        double quarterlyRevenue = statisticsDAO.getTotalRevenue("Quý", testQuarter, testYear);
        assertTrue(quarterlyRevenue >= 0, "Quarterly revenue should be non-negative");

        // Test with year criteria
        double yearlyRevenue = statisticsDAO.getTotalRevenue("Năm", 0, testYear);
        assertTrue(yearlyRevenue >= 0, "Yearly revenue should be non-negative");
    }

    @Test
    @DisplayName("Test getting total invoices with different criteria")
    void testGetTotalInvoices() throws RemoteException {
        // Test with month criteria
        int monthlyInvoices = statisticsDAO.getTotalInvoices("Tháng", testMonth, testYear);
        assertTrue(monthlyInvoices >= 0, "Monthly invoices should be non-negative");

        // Test with quarter criteria
        int quarterlyInvoices = statisticsDAO.getTotalInvoices("Quý", testQuarter, testYear);
        assertTrue(quarterlyInvoices >= 0, "Quarterly invoices should be non-negative");

        // Test with year criteria
        int yearlyInvoices = statisticsDAO.getTotalInvoices("Năm", 0, testYear);
        assertTrue(yearlyInvoices >= 0, "Yearly invoices should be non-negative");
    }

    @Test
    @DisplayName("Test getting cuisine revenue")
    void testGetCuisineRevenue() throws RemoteException {
        // Assuming "Phở" is a cuisine in the database
        String testCuisine = "Phở";
        
        // Test with month criteria
        int monthlyRevenue = statisticsDAO.getCuisineRevenue("Tháng", testCuisine, testYear, testMonth);
        assertTrue(monthlyRevenue >= 0, "Monthly cuisine revenue should be non-negative");

        // Test with quarter criteria
        int quarterlyRevenue = statisticsDAO.getCuisineRevenue("Quý", testCuisine, testYear, testQuarter);
        assertTrue(quarterlyRevenue >= 0, "Quarterly cuisine revenue should be non-negative");

        // Test with year criteria
        int yearlyRevenue = statisticsDAO.getCuisineRevenue("Năm", testCuisine, testYear, 0);
        assertTrue(yearlyRevenue >= 0, "Yearly cuisine revenue should be non-negative");
    }

    @Test
    @DisplayName("Test getting new customer quantity")
    void testGetNewCustomerQuantity() throws RemoteException {
        // Test with month criteria
        int monthlyCustomers = statisticsDAO.getNewCustomerQuantity("Tháng", testMonth, testYear);
        assertTrue(monthlyCustomers >= 0, "Monthly new customers should be non-negative");

        // Test with quarter criteria
        int quarterlyCustomers = statisticsDAO.getNewCustomerQuantity("Quý", testQuarter, testYear);
        assertTrue(quarterlyCustomers >= 0, "Quarterly new customers should be non-negative");

        // Test with year criteria
        int yearlyCustomers = statisticsDAO.getNewCustomerQuantity("Năm", 0, testYear);
        assertTrue(yearlyCustomers >= 0, "Yearly new customers should be non-negative");
    }

    @Test
    @DisplayName("Test getting personal revenue")
    void testGetPersonalRevenue() throws RemoteException {
        // Assuming "EMP001" is an employee ID in the database
        String testEmployeeId = "EMP001";
        
        // Test with month criteria
        double monthlyRevenue = statisticsDAO.getPersonalRevenue(testEmployeeId, "Tháng", testYear, testMonth);
        assertTrue(monthlyRevenue >= 0, "Monthly personal revenue should be non-negative");

        // Test with quarter criteria
        double quarterlyRevenue = statisticsDAO.getPersonalRevenue(testEmployeeId, "Quý", testYear, testQuarter);
        assertTrue(quarterlyRevenue >= 0, "Quarterly personal revenue should be non-negative");

        // Test with year criteria
        double yearlyRevenue = statisticsDAO.getPersonalRevenue(testEmployeeId, "Năm", testYear, 0);
        assertTrue(yearlyRevenue >= 0, "Yearly personal revenue should be non-negative");
    }

    @Test
    @DisplayName("Test getting personal total orders")
    void testGetPersonalTotalOrder() throws RemoteException {
        // Assuming "EMP001" is an employee ID in the database
        String testEmployeeId = "EMP001";
        
        // Test with month criteria
        int monthlyOrders = statisticsDAO.getPersonalTotalOrder(testEmployeeId, "Tháng", testYear, testMonth);
        assertTrue(monthlyOrders >= 0, "Monthly personal orders should be non-negative");

        // Test with quarter criteria
        int quarterlyOrders = statisticsDAO.getPersonalTotalOrder(testEmployeeId, "Quý", testYear, testQuarter);
        assertTrue(quarterlyOrders >= 0, "Quarterly personal orders should be non-negative");

        // Test with year criteria
        int yearlyOrders = statisticsDAO.getPersonalTotalOrder(testEmployeeId, "Năm", testYear, 0);
        assertTrue(yearlyOrders >= 0, "Yearly personal orders should be non-negative");
    }

    @Test
    @DisplayName("Test getting personal total reservations")
    void testGetPersonalTotalReservation() throws RemoteException {
        // Assuming "EMP001" is an employee ID in the database
        String testEmployeeId = "EMP001";
        
        // Test with month criteria
        int monthlyReservations = statisticsDAO.getPersonalTotalReservation(testEmployeeId, "Tháng", testYear, testMonth);
        assertTrue(monthlyReservations >= 0, "Monthly personal reservations should be non-negative");

        // Test with quarter criteria
        int quarterlyReservations = statisticsDAO.getPersonalTotalReservation(testEmployeeId, "Quý", testYear, testQuarter);
        assertTrue(quarterlyReservations >= 0, "Quarterly personal reservations should be non-negative");

        // Test with year criteria
        int yearlyReservations = statisticsDAO.getPersonalTotalReservation(testEmployeeId, "Năm", testYear, 0);
        assertTrue(yearlyReservations >= 0, "Yearly personal reservations should be non-negative");
    }

    @Test
    @DisplayName("Test getting cuisine total sales")
    void testGetCuisineTotalSell() throws RemoteException {
        // Assuming "Phở" is a cuisine in the database
        String testCuisine = "Phở";
        
        // Test with month criteria
        int monthlySales = statisticsDAO.getCuisineTotalSell("Tháng", testCuisine, testYear, testMonth);
        assertTrue(monthlySales >= 0, "Monthly cuisine sales should be non-negative");

        // Test with quarter criteria
        int quarterlySales = statisticsDAO.getCuisineTotalSell("Quý", testCuisine, testYear, testQuarter);
        assertTrue(quarterlySales >= 0, "Quarterly cuisine sales should be non-negative");

        // Test with year criteria
        int yearlySales = statisticsDAO.getCuisineTotalSell("Năm", testCuisine, testYear, 0);
        assertTrue(yearlySales >= 0, "Yearly cuisine sales should be non-negative");
    }

    @Test
    @DisplayName("Test getting best seller cuisines")
    void testGetBestSellerCuisines() throws RemoteException {
        int limit = 5;
        List<HashMap<String, String>> bestSellers = statisticsDAO.getBestSellerCuisines(testYear, limit);
        
        assertNotNull(bestSellers, "Best sellers list should not be null");
        assertTrue(bestSellers.size() <= limit, "Best sellers list should not exceed the specified limit");
        
        // If there are best sellers, check their structure
        if (!bestSellers.isEmpty()) {
            HashMap<String, String> firstBestSeller = bestSellers.get(0);
            assertTrue(firstBestSeller.containsKey("cuisineId"), "Best seller should have cuisineId");
            assertTrue(firstBestSeller.containsKey("cuisineName"), "Best seller should have cuisineName");
            assertTrue(firstBestSeller.containsKey("categoryName"), "Best seller should have categoryName");
            assertTrue(firstBestSeller.containsKey("salePrice"), "Best seller should have salePrice");
            assertTrue(firstBestSeller.containsKey("totalRevenue"), "Best seller should have totalRevenue");
        }
    }

    @Test
    @DisplayName("Test getting revenue proportion by category")
    void testGetRevenueProportionByCategory() throws RemoteException {
        List<HashMap<String, String>> proportions = statisticsDAO.getRevenueProportionByCategory(testYear);
        
        assertNotNull(proportions, "Revenue proportions should not be null");
        
        // If there are proportions, check their structure
        if (!proportions.isEmpty()) {
            HashMap<String, String> firstProportion = proportions.get(0);
            assertTrue(firstProportion.containsKey("categoryId"), "Proportion should have categoryId");
            assertTrue(firstProportion.containsKey("categoryName"), "Proportion should have categoryName");
            assertTrue(firstProportion.containsKey("categoryRevenue"), "Proportion should have categoryRevenue");
            assertTrue(firstProportion.containsKey("revenuePercentage"), "Proportion should have revenuePercentage");
        }
    }

    @Test
    @DisplayName("Test getting daily revenue")
    void testGetDailyRevenue() throws RemoteException {
        LocalDate today = LocalDate.now();
        HashMap<String, Integer> dailyRevenue = statisticsDAO.getDailyRevenue(today);
        
        assertNotNull(dailyRevenue, "Daily revenue should not be null");
        assertTrue(dailyRevenue.containsKey("totalOrders"), "Daily revenue should contain totalOrders");
        assertTrue(dailyRevenue.containsKey("totalRevenue"), "Daily revenue should contain totalRevenue");
        assertTrue(dailyRevenue.containsKey("totalReservations"), "Daily revenue should contain totalReservations");
        assertTrue(dailyRevenue.containsKey("newCustomers"), "Daily revenue should contain newCustomers");
    }

    @Test
    @DisplayName("Test getting yearly revenue")
    void testGetYearlyRevenue() throws RemoteException {
        HashMap<String, Integer> yearlyRevenue = statisticsDAO.getYearlyRevenue(testYear);
        
        assertNotNull(yearlyRevenue, "Yearly revenue should not be null");
        // The map might be empty if there's no data for the test year
        if (!yearlyRevenue.isEmpty()) {
            assertTrue(yearlyRevenue.containsKey("year"), "Yearly revenue should contain year");
            assertTrue(yearlyRevenue.containsKey("totalRevenue"), "Yearly revenue should contain totalRevenue");
        }
    }

    @Test
    @DisplayName("Test getting monthly revenue")
    void testGetMonthlyRevenue() throws RemoteException {
        HashMap<String, Integer> monthlyRevenue = statisticsDAO.getMonthlyRevenue(testYear, testMonth);
        
        assertNotNull(monthlyRevenue, "Monthly revenue should not be null");
        // The map might be empty if there's no data for the test month
        if (!monthlyRevenue.isEmpty()) {
            assertTrue(monthlyRevenue.containsKey("month"), "Monthly revenue should contain month");
            assertTrue(monthlyRevenue.containsKey("totalRevenue"), "Monthly revenue should contain totalRevenue");
        }
    }

    @Test
    @DisplayName("Test getting quarter revenue")
    void testGetQuarterRevenue() throws RemoteException {
        HashMap<String, Integer> quarterRevenue = statisticsDAO.getQuarterRevenue(testYear, testQuarter);
        
        assertNotNull(quarterRevenue, "Quarter revenue should not be null");
        // The map might be empty if there's no data for the test quarter
        if (!quarterRevenue.isEmpty()) {
            assertTrue(quarterRevenue.containsKey("quarter"), "Quarter revenue should contain quarter");
            assertTrue(quarterRevenue.containsKey("totalRevenue"), "Quarter revenue should contain totalRevenue");
        }
    }

    @Test
    @DisplayName("Test getting yearly stats")
    void testGetYearlyStat() throws RemoteException {
        HashMap<String, String> yearlyStat = statisticsDAO.getYearlyStat(testYear);
        
        assertNotNull(yearlyStat, "Yearly stats should not be null");
        // The map might be empty if there's no data for the test year
        if (!yearlyStat.isEmpty()) {
            assertTrue(yearlyStat.containsKey("year"), "Yearly stats should contain year");
            assertTrue(yearlyStat.containsKey("totalOrders"), "Yearly stats should contain totalOrders");
            assertTrue(yearlyStat.containsKey("totalRevenue"), "Yearly stats should contain totalRevenue");
            assertTrue(yearlyStat.containsKey("growthRate"), "Yearly stats should contain growthRate");
        }
    }

    @Test
    @DisplayName("Test getting growth rate")
    void testGetGrowthRate() throws RemoteException {
        double growthRate = statisticsDAO.getGrowthRate();
        // Can't assert exact value, but can ensure it returns a value
    }

    @Test
    @DisplayName("Test getting average revenue per order")
    void testGetAverageRevenuePerOrder() throws RemoteException {
        double averageRevenue = statisticsDAO.getAverageRevenuePerOrder();
        assertTrue(averageRevenue >= 0, "Average revenue per order should be non-negative");
    }

    @Test
    @DisplayName("Test getting total customers")
    void testGetTotalCustomers() throws RemoteException {
        int totalCustomers = statisticsDAO.getTotalCustomers();
        assertTrue(totalCustomers >= 0, "Total customers should be non-negative");
    }

    @Test
    @DisplayName("Test getting total revenue")
    void testGetTotalRevenueNoParameters() throws RemoteException {
        double totalRevenue = statisticsDAO.getTotalRevenue();
        assertTrue(totalRevenue >= 0, "Total revenue should be non-negative");
    }

    @Test
    @DisplayName("Test getting total orders")
    void testGetTotalOrders() throws RemoteException {
        int totalOrders = statisticsDAO.getTotalOrders();
        assertTrue(totalOrders >= 0, "Total orders should be non-negative");
    }

    @Test
    @DisplayName("Test getting total reservations")
    void testGetTotalReservations() throws RemoteException {
        int totalReservations = statisticsDAO.getTotalReservations();
        assertTrue(totalReservations >= 0, "Total reservations should be non-negative");
    }

    @Test
    @DisplayName("Test getting first year")
    void testGetFirstYear() throws RemoteException {
        int firstYear = statisticsDAO.getFirstYear();
        // First year should be between 1970 and current year
        assertTrue(firstYear == 0 || (firstYear >= 1970 && firstYear <= LocalDate.now().getYear()), 
            "First year should be between 1970 and current year or 0 if no data");
    }
}
