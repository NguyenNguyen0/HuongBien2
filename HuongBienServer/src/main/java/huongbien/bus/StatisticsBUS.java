package huongbien.bus;

import huongbien.dao.impl.StatisticsDAO;
import huongbien.entity.Customer;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsBUS {
    private final StatisticsDAO statisticsDAO;

    public StatisticsBUS() {
        try {
            this.statisticsDAO = new StatisticsDAO();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    // Constructor for dependency injection (useful for testing)
    public StatisticsBUS(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    public int getFirstYear() {
        try {
            return statisticsDAO.getFirstYear();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getTotalCustomerQuantity() {
        try {
            return statisticsDAO.getTotalCustomers();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getTotalReservationQuantity() {
        try {
            return statisticsDAO.getTotalReservations();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getTotalOrderQuantity() {
        try {
            return statisticsDAO.getTotalOrders();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getTotalRevenue() {
        try {
            return statisticsDAO.getTotalRevenue();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getAverageRevenuePerOrder() {
        try {
            return statisticsDAO.getAverageRevenuePerOrder();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getCurrentYearGrowthRate() {
        try {
            return statisticsDAO.getGrowthRate();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public HashMap<String, String> getYearStat(int year) {
        try {
            return statisticsDAO.getYearlyStat(year);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public HashMap<String, Integer> getDailyRevenue(LocalDate date) {
        try {
            return statisticsDAO.getDailyRevenue(date);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public List<HashMap<String, String>> getYearStats(int from, int to) {
        List<HashMap<String, String>> yearStats = new ArrayList<>();
        try {
            for (int i = from; i <= to; i++) {
                yearStats.add(statisticsDAO.getYearlyStat(i));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return yearStats;
    }

    public List<HashMap<String, Integer>> getQuarterRevenues(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        try {
            for (int i = 1; i <= 4; i++) {
                monthStats.add(statisticsDAO.getQuarterRevenue(year, i));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return monthStats;
    }

    public List<HashMap<String, Integer>> getMonthlyRevenues(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        try {
            for (int i = 1; i <= 12; i++) {
                monthStats.add(statisticsDAO.getMonthlyRevenue(year, i));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return monthStats;
    }

    public List<HashMap<String, Integer>> getYearlyRevenues(int from, int to) {
        List<HashMap<String, Integer>> dayStats = new ArrayList<>();
        try {
            for (int i = from; i <= to; i++) {
                dayStats.add(statisticsDAO.getYearlyRevenue(i));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return dayStats;
    }

    public List<HashMap<String, Integer>> getQuarterOrderQuantity(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        try {
            for (int i = 1; i <= 4; i++) {
                HashMap<String, Integer> quarterStat = new HashMap<>();
                quarterStat.put("quarter", i);
                quarterStat.put("orderQuantity", statisticsDAO.getTotalInvoices("Quý", i, year));
                monthStats.add(quarterStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return monthStats;
    }

    public List<HashMap<String, Integer>> getMonthlyOrderQuantity(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        try {
            for (int i = 1; i <= 12; i++) {
                HashMap<String, Integer> monthStat = new HashMap<>();
                monthStat.put("month", i);
                monthStat.put("orderQuantity", statisticsDAO.getTotalInvoices("Tháng", i, year));
                monthStats.add(monthStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return monthStats;
    }

    public List<HashMap<String, Integer>> getYearlyOrderQuantity(int from, int to) {
        List<HashMap<String, Integer>> yearStats = new ArrayList<>();
        try {
            for (int i = from; i <= to; i++) {
                HashMap<String, Integer> yearStat = new HashMap<>();
                yearStat.put("year", i);
                yearStat.put("orderQuantity", statisticsDAO.getTotalInvoices("Năm", 0, i));
                yearStats.add(yearStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return yearStats;
    }

    public List<HashMap<String, String>> getBestSellerCuisines(int year, int limit) {
        try {
            return statisticsDAO.getBestSellerCuisines(year, limit);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<HashMap<String, String>> getRevenueProportionByCategory(int year) {
        try {
            return statisticsDAO.getRevenueProportionByCategory(year);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<HashMap<String, Integer>> getQuarterCuisineRevenues(String cuisineName, int year) {
        List<HashMap<String, Integer>> quarterStats = new ArrayList<>();
        try {
            for (int quarter = 1; quarter <= 4; quarter++) {
                HashMap<String, Integer> quarterStat = new HashMap<>();
                quarterStat.put("quarter", quarter);
                quarterStat.put("revenue", statisticsDAO.getCuisineRevenue("Quý", cuisineName, year, quarter));
                quarterStats.add(quarterStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return quarterStats;
    }

    public List<HashMap<String, Integer>> getMonthlyCuisineRevenues(String cuisineName, int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        try {
            for (int month = 1; month <= 12; month++) {
                HashMap<String, Integer> monthStat = new HashMap<>();
                monthStat.put("month", month);
                monthStat.put("revenue", statisticsDAO.getCuisineRevenue("Tháng", cuisineName, year, month));
                monthStats.add(monthStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return monthStats;
    }

    public List<HashMap<String, Integer>> getYearlyCuisineRevenues(String cuisineName, int from, int to) {
        List<HashMap<String, Integer>> yearStats = new ArrayList<>();
        try {
            for (int year = from; year <= to; year++) {
                HashMap<String, Integer> yearStat = new HashMap<>();
                yearStat.put("year", year);
                yearStat.put("revenue", statisticsDAO.getCuisineRevenue("Năm", cuisineName, year, 0));
                yearStats.add(yearStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return yearStats;
    }

    public int getCuisineRevenue(String cuisineName, String categoryName, String criteria, int year) {
        try {
            CuisineBUS cuisineBUS = new CuisineBUS();
            if (criteria.equals("Năm") && cuisineName.equals("Tất cả")) {
                List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
                int totalRevenue = 0;
                for (int i = 2021; i < LocalDate.now().getYear(); i++) {
                    for (String name : cuisineNames) {
                        totalRevenue += statisticsDAO.getCuisineRevenue(criteria, name, i, 0);
                    }
                }
                return totalRevenue;
            } else if (!criteria.equals("Năm") && cuisineName.equals("Tất cả")) {
                List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
                int totalRevenue = 0;
                for (String name : cuisineNames) {
                    totalRevenue += statisticsDAO.getCuisineRevenue("Năm", name, year, 0);
                }
                return totalRevenue;
            } else if (criteria.equals("Năm")) {
                int totalRevenue = 0;
                for (int i = 2021; i < LocalDate.now().getYear(); i++) {
                    totalRevenue += statisticsDAO.getCuisineRevenue(criteria, cuisineName, i, 0);
                }
                return totalRevenue;
            }
            return statisticsDAO.getCuisineRevenue("Năm", cuisineName, year, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getCuisineTotalSell(String cuisineName, String categoryName, String criteria, int year) {
        try {
            CuisineBUS cuisineBUS = new CuisineBUS();
            if (criteria.equals("Năm") && cuisineName.equals("Tất cả")) {
                List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
                int totalRevenue = 0;
                for (int i = 2021; i <= LocalDate.now().getYear(); i++) {
                    for (String name : cuisineNames) {
                        totalRevenue += statisticsDAO.getCuisineTotalSell(criteria, name, i, 0);
                    }
                }
                return totalRevenue;
            } else if (!criteria.equals("Năm") && cuisineName.equals("Tất cả")) {
                List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
                int totalRevenue = 0;
                for (String name : cuisineNames) {
                    totalRevenue += statisticsDAO.getCuisineTotalSell("Năm", name, year, 0);
                }
                return totalRevenue;
            } else if (criteria.equals("Năm")) {
                int totalRevenue = 0;
                for (int i = 2021; i < LocalDate.now().getYear(); i++) {
                    totalRevenue += statisticsDAO.getCuisineTotalSell(criteria, cuisineName, i, 0);
                }
                return totalRevenue;
            }
            return statisticsDAO.getCuisineTotalSell("Năm", cuisineName, year, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Customer> getNewCustomers(int year) {
        try {
            CustomerBUS customerBUS = new CustomerBUS();
            return customerBUS.getNewCustomersInYear(year);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Customer> getTopMembershipCustomers(int year, int limit) {
        try {
            CustomerBUS customerBUS = new CustomerBUS();
            return customerBUS.getTopMembershipCustomers(year, limit);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public int getTotalCustomersQuantityByYear(int year) {
        try {
            CustomerBUS customerBUS = new CustomerBUS();
            return customerBUS.getTotalCustomersQuantityByYear(year);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<HashMap<String, Integer>> getMonthlyNewCustomers(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        try {
            for (int i = 1; i <= 12; i++) {
                HashMap<String, Integer> monthStat = new HashMap<>();
                monthStat.put("month", i);
                monthStat.put("newCustomerQuantity", statisticsDAO.getNewCustomerQuantity("Tháng", i, year));
                monthStats.add(monthStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return monthStats;
    }

    public List<HashMap<String, Integer>> getQuarterNewCustomer(int year) {
        List<HashMap<String, Integer>> quarterStats = new ArrayList<>();
        try {
            for (int i = 1; i <= 4; i++) {
                HashMap<String, Integer> quarterStat = new HashMap<>();
                quarterStat.put("quarter", i);
                quarterStat.put("newCustomerQuantity", statisticsDAO.getNewCustomerQuantity("Quý", i, year));
                quarterStats.add(quarterStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return quarterStats;
    }

    public List<HashMap<String, Integer>> getYearlyNewCustomer(int from, int to) {
        List<HashMap<String, Integer>> yearStats = new ArrayList<>();
        try {
            for (int i = from; i <= to; i++) {
                HashMap<String, Integer> yearStat = new HashMap<>();
                yearStat.put("year", i);
                yearStat.put("newCustomerQuantity", statisticsDAO.getNewCustomerQuantity("Năm", 0, i));
                yearStats.add(yearStat);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return yearStats;
    }

    public double getPersonalRevenue(String id, int year) {
        try {
            return statisticsDAO.getPersonalRevenue(id, "Năm", year, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getPersonalTotalOrder(String id, int year) {
        try {
            return statisticsDAO.getPersonalTotalOrder(id, "Năm", year, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getPersonalTotalReservation(String id, int year) {
        try {
            return statisticsDAO.getPersonalTotalReservation(id, "Năm", year, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<HashMap<String, String>> getPersonalRevenueAndOrder(String id, String criteria, int year) {
        List<HashMap<String, String>> stats = new ArrayList<>();
        try {
            switch (criteria) {
                case "Quý" -> {
                    for (int quarter = 1; quarter <= 4; quarter++) {
                        double revenue = statisticsDAO.getPersonalRevenue(id, criteria, year, quarter);
                        int totalOrder = statisticsDAO.getPersonalTotalOrder(id, criteria, year, quarter);
                        int totalReservation = statisticsDAO.getPersonalTotalReservation(id, criteria, year, quarter);
                        HashMap<String, String> stat = new HashMap<>();
                        stat.put("quarter", String.valueOf(quarter));
                        stat.put("revenue", String.valueOf(revenue));
                        stat.put("totalOrder", String.valueOf(totalOrder));
                        stat.put("totalReservation", String.valueOf(totalReservation));
                        stats.add(stat);
                    }
                }
                case "Tháng" -> {
                    for (int month = 1; month <= 12; month++) {
                        double revenue = statisticsDAO.getPersonalRevenue(id, criteria, year, month);
                        int totalOrder = statisticsDAO.getPersonalTotalOrder(id, criteria, year, month);
                        int totalReservation = statisticsDAO.getPersonalTotalReservation(id, criteria, year, month);
                        HashMap<String, String> stat = new HashMap<>();
                        stat.put("month", String.valueOf(month));
                        stat.put("revenue", String.valueOf(revenue));
                        stat.put("totalOrder", String.valueOf(totalOrder));
                        stat.put("totalReservation", String.valueOf(totalReservation));
                        stats.add(stat);
                    }
                }
                case "Năm" -> {
                    for (int y = 2021; y <= LocalDate.now().getYear(); y++) {
                        double revenue = statisticsDAO.getPersonalRevenue(id, criteria, year, y);
                        int totalOrder = statisticsDAO.getPersonalTotalOrder(id, criteria, year, y);
                        int totalReservation = statisticsDAO.getPersonalTotalReservation(id, criteria, year, y);
                        HashMap<String, String> stat = new HashMap<>();
                        stat.put("year", String.valueOf(y));
                        stat.put("revenue", String.valueOf(revenue));
                        stat.put("totalOrder", String.valueOf(totalOrder));
                        stat.put("totalReservation", String.valueOf(totalReservation));
                        stats.add(stat);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return stats;
    }
}