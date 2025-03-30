package huongbien.dao;

import huongbien.bus.OrderBUS;
import huongbien.bus.ReservationBUS;
import huongbien.jpa.JPAUtil;
import huongbien.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsDAO {
    private static EntityManager entityManager = JPAUtil.getEntityManager();

    // Phương thức lấy tổng doanh thu từ bảng Order theo tiêu chí tháng, quý hoặc năm
    public static double getTotalRevenue(String criteria, int period, int year) {
        String jpql = switch (criteria) {
            case "Tháng" -> "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o " +
                    "WHERE FUNCTION('MONTH', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year";
            case "Quý" -> "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o " +
                    "WHERE FUNCTION('QUARTER', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year";
            case "Năm" -> "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o " +
                    "WHERE FUNCTION('YEAR', o.orderDate) = :year";
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        };

        try {
            Query query = entityManager.createQuery(jpql);
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                query.setParameter("period", period);
                query.setParameter("year", year);
            } else if ("Năm".equals(criteria)) {
                query.setParameter("year", year);
            }

            Object result = query.getSingleResult();
            return result == null ? 0 : ((Number) result).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Lấy tổng số hóa đơn từ bảng Order theo tiêu chí tháng, quý hoặc năm
    public static int getTotalInvoices(String criteria, int period, int year) {
        String jpql = switch (criteria) {
            case "Tháng" -> "SELECT COUNT(o) FROM Order o " +
                    "WHERE FUNCTION('MONTH', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year";
            case "Quý" -> "SELECT COUNT(o) FROM Order o " +
                    "WHERE FUNCTION('QUARTER', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year";
            case "Năm" -> "SELECT COUNT(o) FROM Order o " +
                    "WHERE FUNCTION('YEAR', o.orderDate) = :year";
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        };

        try {
            Query query = entityManager.createQuery(jpql);
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                query.setParameter("period", period);
                query.setParameter("year", year);
            } else if ("Năm".equals(criteria)) {
                query.setParameter("year", year);
            }

            Object result = query.getSingleResult();
            return result == null ? 0 : ((Number) result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Phương thức lấy tổng số lượng món ăn từ OrderDetail
    public static int getCuisineRevenue(String criteria, String cuisineName, int year, int period) {
        String jpql = switch (criteria) {
            case "Tháng" -> "SELECT COALESCE(SUM(od.quantity * od.salePrice), 0) FROM OrderDetail od " +
                    "JOIN od.order o JOIN od.cuisine c " +
                    "WHERE FUNCTION('MONTH', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year AND c.name = :name";
            case "Quý" -> "SELECT COALESCE(SUM(od.quantity * od.salePrice), 0) FROM OrderDetail od " +
                    "JOIN od.order o JOIN od.cuisine c " +
                    "WHERE FUNCTION('QUARTER', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year AND c.name = :name";
            case "Năm" -> "SELECT COALESCE(SUM(od.quantity * od.salePrice), 0) FROM OrderDetail od " +
                    "JOIN od.order o JOIN od.cuisine c " +
                    "WHERE FUNCTION('YEAR', o.orderDate) = :year AND c.name = :name";
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        };

        try {
            Query query = entityManager.createQuery(jpql);
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                query.setParameter("period", period);
                query.setParameter("year", year);
                query.setParameter("name", cuisineName);
            } else if ("Năm".equals(criteria)) {
                query.setParameter("year", year);
                query.setParameter("name", cuisineName);
            }

            Object result = query.getSingleResult();
            return result == null ? 0 : ((Number) result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getNewCustomerQuantity(String criteria, int period, int year) {
        String jpql = switch (criteria) {
            case "Tháng" -> "SELECT COUNT(c) FROM Customer c " +
                    "WHERE FUNCTION('MONTH', c.registrationDate) = :period AND FUNCTION('YEAR', c.registrationDate) = :year";
            case "Quý" -> "SELECT COUNT(c) FROM Customer c " +
                    "WHERE FUNCTION('QUARTER', c.registrationDate) = :period AND FUNCTION('YEAR', c.registrationDate) = :year";
            case "Năm" -> "SELECT COUNT(c) FROM Customer c " +
                    "WHERE FUNCTION('YEAR', c.registrationDate) = :year";
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        };

        try {
            Query query = entityManager.createQuery(jpql);
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                query.setParameter("period", period);
                query.setParameter("year", year);
            } else if ("Năm".equals(criteria)) {
                query.setParameter("year", year);
            }

            Object result = query.getSingleResult();
            return result == null ? 0 : ((Number) result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double getPersonalRevenue(String id, String criteria, int year, int period) {
        String jpql = switch (criteria) {
            case "Tháng" -> "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o JOIN o.employee e " +
                    "WHERE FUNCTION('MONTH', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year AND e.id = :id";
            case "Quý" -> "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o JOIN o.employee e " +
                    "WHERE FUNCTION('QUARTER', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year AND e.id = :id";
            case "Năm" -> "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o JOIN o.employee e " +
                    "WHERE FUNCTION('YEAR', o.orderDate) = :year AND e.id = :id";
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        };

        try {
            Query query = entityManager.createQuery(jpql);
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                query.setParameter("period", period);
                query.setParameter("year", year);
                query.setParameter("id", id);
            } else if ("Năm".equals(criteria)) {
                query.setParameter("year", year);
                query.setParameter("id", id);
            }

            Object result = query.getSingleResult();
            return result == null ? 0 : ((Number) result).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getPersonalTotalOrder(String id, String criteria, int year, int period) {
        String jpql = switch (criteria) {
            case "Tháng" -> "SELECT COUNT(o) FROM Order o JOIN o.employee e " +
                    "WHERE FUNCTION('MONTH', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year AND e.id = :id";
            case "Quý" -> "SELECT COUNT(o) FROM Order o JOIN o.employee e " +
                    "WHERE FUNCTION('QUARTER', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year AND e.id = :id";
            case "Năm" -> "SELECT COUNT(o) FROM Order o JOIN o.employee e " +
                    "WHERE FUNCTION('YEAR', o.orderDate) = :year AND e.id = :id";
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        };

        try {
            Query query = entityManager.createQuery(jpql);
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                query.setParameter("period", period);
                query.setParameter("year", year);
                query.setParameter("id", id);
            } else if ("Năm".equals(criteria)) {
                query.setParameter("year", year);
                query.setParameter("id", id);
            }

            Object result = query.getSingleResult();
            return result == null ? 0 : ((Number) result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getPersonalTotalReservation(String id, String criteria, int year, int period) {
        String jpql = switch (criteria) {
            case "Tháng" -> "SELECT COUNT(r) FROM Reservation r JOIN r.employee e " +
                    "WHERE FUNCTION('MONTH', r.reservationDate) = :period AND FUNCTION('YEAR', r.reservationDate) = :year AND e.id = :id";
            case "Quý" -> "SELECT COUNT(r) FROM Reservation r JOIN r.employee e " +
                    "WHERE FUNCTION('QUARTER', r.reservationDate) = :period AND FUNCTION('YEAR', r.reservationDate) = :year AND e.id = :id";
            case "Năm" -> "SELECT COUNT(r) FROM Reservation r JOIN r.employee e " +
                    "WHERE FUNCTION('YEAR', r.reservationDate) = :year AND e.id = :id";
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        };

        try {
            Query query = entityManager.createQuery(jpql);
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                query.setParameter("period", period);
                query.setParameter("year", year);
                query.setParameter("id", id);
            } else if ("Năm".equals(criteria)) {
                query.setParameter("year", year);
                query.setParameter("id", id);
            }

            Object result = query.getSingleResult();
            return result == null ? 0 : ((Number) result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static int getCuisineTotalSell(String criteria, String cuisineName, int year, int period) {
        String jpql = switch (criteria) {
            case "Tháng" -> "SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od " +
                    "JOIN od.order o JOIN od.cuisine c " +
                    "WHERE FUNCTION('MONTH', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year AND c.name = :name";
            case "Quý" -> "SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od " +
                    "JOIN od.order o JOIN od.cuisine c " +
                    "WHERE FUNCTION('QUARTER', o.orderDate) = :period AND FUNCTION('YEAR', o.orderDate) = :year AND c.name = :name";
            case "Năm" -> "SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od " +
                    "JOIN od.order o JOIN od.cuisine c " +
                    "WHERE FUNCTION('YEAR', o.orderDate) = :year AND c.name = :name";
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        };

        try {
            Query query = entityManager.createQuery(jpql);
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                query.setParameter("period", period);
                query.setParameter("year", year);
                query.setParameter("name", cuisineName);
            } else if ("Năm".equals(criteria)) {
                query.setParameter("year", year);
                query.setParameter("name", cuisineName);
            }

            Object result = query.getSingleResult();
            return result == null ? 0 : ((Number) result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static List<HashMap<String, String>> getBestSellerCuisines(int year, int limit) {
        String jpql = "SELECT c.id as cuisineId, c.name as cuisineName, cat.name as categoryName, " +
                     "c.price as salePrice, SUM(od.quantity * od.salePrice) as totalRevenue " +
                     "FROM OrderDetail od " +
                     "JOIN od.cuisine c " +
                     "JOIN c.category cat " +
                     "JOIN od.order o " +
                     "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                     "GROUP BY c.id, c.name, cat.name, c.price " +
                     "ORDER BY totalRevenue DESC";
    
        try {
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setParameter("year", year)
                    .setMaxResults(limit)
                    .getResultList();
            
            List<HashMap<String, String>> bestSellers = new ArrayList<>();
            for (Object[] row : results) {
                HashMap<String, String> bestSeller = new HashMap<>();
                bestSeller.put("cuisineId", String.valueOf(row[0]));
                bestSeller.put("cuisineName", (String) row[1]);
                bestSeller.put("categoryName", (String) row[2]);
                bestSeller.put("salePrice", Utils.formatMoney((Double) row[3]));
                bestSeller.put("totalRevenue", Utils.formatMoney((Double) row[4]));
                bestSellers.add(bestSeller);
            }
    
            return bestSellers;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static List<HashMap<String, String>> getRevenueProportionByCategory(int year) {
        // First, get total revenue for the year
        String totalRevenueJpql = "SELECT SUM(od.quantity * od.salePrice) " +
                                 "FROM OrderDetail od " +
                                 "JOIN od.order o " +
                                 "WHERE FUNCTION('YEAR', o.orderDate) = :year";
        
        Double totalYearRevenue = entityManager.createQuery(totalRevenueJpql, Double.class)
                .setParameter("year", year)
                .getSingleResult();
        
        if (totalYearRevenue == null) {
            totalYearRevenue = 0.0;
        }
        
        // Then get revenue by category
        String categoryRevenueJpql = "SELECT cat.id as categoryId, cat.name as categoryName, " +
                                    "SUM(od.quantity * od.salePrice) as totalRevenue " +
                                    "FROM OrderDetail od " +
                                    "JOIN od.cuisine c " +
                                    "JOIN c.category cat " +
                                    "JOIN od.order o " +
                                    "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                                    "GROUP BY cat.id, cat.name " +
                                    "ORDER BY totalRevenue DESC";
        
        try {
            List<Object[]> results = entityManager.createQuery(categoryRevenueJpql, Object[].class)
                    .setParameter("year", year)
                    .getResultList();
            
            List<HashMap<String, String>> proportions = new ArrayList<>();
            for (Object[] row : results) {
                HashMap<String, String> proportion = new HashMap<>();
                proportion.put("categoryId", String.valueOf(row[0]));
                proportion.put("categoryName", (String) row[1]);
                Double categoryRevenue = (Double) row[2];
                proportion.put("categoryRevenue", Utils.formatMoney(categoryRevenue));
                
                // Calculate percentage
                double percentage = totalYearRevenue > 0 
                    ? Math.round((categoryRevenue * 100.0 / totalYearRevenue) * 10) / 10.0 
                    : 0.0;
                proportion.put("revenuePercentage", String.valueOf(percentage));
                
                proportions.add(proportion);
            }
    
            return proportions;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Integer> getDailyRevenue(LocalDate date) {
        HashMap<String, Integer> revenue = new HashMap<>();
        
        try {
            // Get daily orders info
            String orderJpql = "SELECT COUNT(o), COALESCE(SUM(o.totalAmount), 0) " +
                             "FROM Order o " +
                             "WHERE FUNCTION('DATE', o.orderDate) = :date";
            
            Object[] orderResult = (Object[]) entityManager.createQuery(orderJpql)
                    .setParameter("date", date)
                    .getSingleResult();
            
            revenue.put("totalOrders", ((Number) orderResult[0]).intValue());
            revenue.put("totalRevenue", ((Number) orderResult[1]).intValue());
            
            // Get daily reservations count
            String reservationJpql = "SELECT COUNT(r) " +
                                  "FROM Reservation r " +
                                  "WHERE FUNCTION('DATE', r.reservationDate) = :date";
            
            Long reservationCount = entityManager.createQuery(reservationJpql, Long.class)
                    .setParameter("date", date)
                    .getSingleResult();
            
            revenue.put("totalReservations", reservationCount.intValue());
            
            // Get new customers count
            String customerJpql = "SELECT COUNT(c) " +
                               "FROM Customer c " +
                               "WHERE FUNCTION('DATE', c.registrationDate) = :date";
            
            Long newCustomerCount = entityManager.createQuery(customerJpql, Long.class)
                    .setParameter("date", date)
                    .getSingleResult();
            
            revenue.put("newCustomers", newCustomerCount.intValue());
            
            return revenue;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static HashMap<String, Integer> getYearlyRevenue(int year) {
        HashMap<String, Integer> revenue = new HashMap<>();
        
        try {
            String jpql = "SELECT FUNCTION('YEAR', o.orderDate), SUM(o.totalAmount) " +
                        "FROM Order o " +
                        "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                        "GROUP BY FUNCTION('YEAR', o.orderDate)";
            
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setParameter("year", year)
                    .getResultList();
            
            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                revenue.put("year", ((Number) result[0]).intValue());
                revenue.put("totalRevenue", ((Number) result[1]).intValue());
            }
            
            return revenue;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static HashMap<String, Integer> getMonthlyRevenue(int year, int month) {
        HashMap<String, Integer> revenue = new HashMap<>();
        
        try {
            String jpql = "SELECT FUNCTION('MONTH', o.orderDate), SUM(o.totalAmount) " +
                        "FROM Order o " +
                        "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                        "AND FUNCTION('MONTH', o.orderDate) = :month " +
                        "GROUP BY FUNCTION('MONTH', o.orderDate)";
            
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setParameter("year", year)
                    .setParameter("month", month)
                    .getResultList();
            
            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                revenue.put("month", ((Number) result[0]).intValue());
                revenue.put("totalRevenue", ((Number) result[1]).intValue());
            }
            
            return revenue;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Integer> getQuarterRevenue(int year, int quarter) {
        HashMap<String, Integer> revenue = new HashMap<>();
    
        try {
            String jpql = "SELECT FUNCTION('QUARTER', o.orderDate), SUM(o.totalAmount) " +
                         "FROM Order o " +
                         "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                         "AND FUNCTION('QUARTER', o.orderDate) = :quarter " +
                         "GROUP BY FUNCTION('QUARTER', o.orderDate) " +
                         "ORDER BY FUNCTION('QUARTER', o.orderDate)";
    
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setParameter("year", year)
                    .setParameter("quarter", quarter)
                    .getResultList();
    
            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                revenue.put("quarter", ((Number) result[0]).intValue());
                revenue.put("totalRevenue", ((Number) result[1]).intValue());
            }
    
            return revenue;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static HashMap<String, String> getYearlyStat(int year) {
        HashMap<String, String> stats = new HashMap<>();
    
        try {
            // Get current year stats
            String currentYearJpql = "SELECT FUNCTION('YEAR', o.orderDate), COUNT(o), SUM(o.totalAmount) " +
                                  "FROM Order o " +
                                  "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
                                  "GROUP BY FUNCTION('YEAR', o.orderDate)";
            
            List<Object[]> currentYearResults = entityManager.createQuery(currentYearJpql, Object[].class)
                    .setParameter("year", year)
                    .getResultList();
            
            if (!currentYearResults.isEmpty()) {
                Object[] currentYearResult = currentYearResults.get(0);
                
                Integer currentYear = ((Number) currentYearResult[0]).intValue();
                Integer totalOrders = ((Number) currentYearResult[1]).intValue();
                Double totalRevenue = ((Number) currentYearResult[2]).doubleValue();
                
                stats.put("year", String.valueOf(currentYear));
                stats.put("totalOrders", String.valueOf(totalOrders));
                stats.put("totalRevenue", Utils.formatMoney(totalRevenue));
                
                // Get previous year revenue for growth calculation
                String previousYearJpql = "SELECT SUM(o.totalAmount) " +
                                       "FROM Order o " +
                                       "WHERE FUNCTION('YEAR', o.orderDate) = :prevYear";
                
                Object prevYearRevenue = entityManager.createQuery(previousYearJpql)
                        .setParameter("prevYear", year - 1)
                        .getSingleResult();
                
                // Calculate growth rate
                if (prevYearRevenue != null && ((Number) prevYearRevenue).doubleValue() > 0) {
                    double previousRevenue = ((Number) prevYearRevenue).doubleValue();
                    double growthRate = ((totalRevenue - previousRevenue) / previousRevenue) * 100;
                    stats.put("growthRate", String.format("%.2f%%", growthRate));
                } else {
                    stats.put("growthRate", "0.00%");
                }
            }
            
            return stats;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static double getGrowthRate() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        double currentRevenue = getTotalRevenue("Năm", 0, currentYear);
        double previousRevenue = getTotalRevenue("Năm", 0, currentYear - 1);
        return previousRevenue == 0 ? 0 : (currentRevenue - previousRevenue) / previousRevenue * 100;
    }

    public static double getAverageRevenuePerOrder() {
        double totalRevenue = getTotalRevenue();
        int totalInvoices = getTotalOrders();
        return totalInvoices == 0 ? 0 : totalRevenue / totalInvoices;
    }

    // Các phương thức tính tổng toàn bộ hệ thống
    public static int getTotalCustomers() {
        try {
            String jpql = "SELECT COUNT(c) FROM Customer c";
            
            Long result = entityManager.createQuery(jpql, Long.class)
                    .getSingleResult();
            
            return result != null ? result.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static double getTotalRevenue() {
        try {
            String jpql = "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o";
            
            Double result = entityManager.createQuery(jpql, Double.class)
                    .getSingleResult();
            
            return result != null ? result : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static int getTotalOrders() {
        OrderBUS orderBUS = new OrderBUS();
        return orderBUS.countTotalOrders();
    }

    public static int getTotalReservations() {
        ReservationBUS reservationBUS = new ReservationBUS();
        return reservationBUS.countTotalReservations();
    }

    public static int getFirstYear() {
        try {
            String jpql = "SELECT MIN(FUNCTION('YEAR', o.orderDate)) FROM Order o";
            Integer result = entityManager.createQuery(jpql, Integer.class)
                    .getSingleResult();
            return result != null ? result : 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
