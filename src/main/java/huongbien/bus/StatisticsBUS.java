package com.huongbien.bus;

import com.huongbien.dao.StatisticsDAO;
import com.huongbien.entity.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsBUS {
    public static int getTotalCustomerQuantity() {
        return StatisticsDAO.getTotalCustomers();
    }

    public static int getTotalReservationQuantity() {
        return StatisticsDAO.getTotalReservations();
    }

    public static int getTotalOrderQuantity() {
        return StatisticsDAO.getTotalOrders();
    }

    public static double getTotalRevenue() {
        return StatisticsDAO.getTotalRevenue();
    }

    public static double getAverageRevenuePerOrder() {
        return StatisticsDAO.getAverageRevenuePerOrder();
    }

    public static double getCurrentYearGrowthRate() {
        return StatisticsDAO.getGrowthRate();
    }

    public static HashMap<String, String> getYearStat(int year) {
        return StatisticsDAO.getYearlyStat(year);
    }

    public static HashMap<String, Integer> getDailyRevenue(LocalDate date) {
        return StatisticsDAO.getDailyRevenue(date);
    }

    public static List<HashMap<String, String>> getYearStats(int from, int to) {
        List<HashMap<String, String>> yearStats = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            yearStats.add(StatisticsDAO.getYearlyStat(i));
        }
        return yearStats;
    }

    public static List<HashMap<String, Integer>> getQuarterRevenues(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            monthStats.add(StatisticsDAO.getQuarterRevenue(year, i));
        }
        return monthStats;
    }

    public static List<HashMap<String, Integer>> getMonthlyRevenues(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthStats.add(StatisticsDAO.getMonthlyRevenue(year, i));
        }
        return monthStats;
    }

    public static List<HashMap<String, Integer>> getYearlyRevenues(int from, int to) {
        List<HashMap<String, Integer>> dayStats = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            dayStats.add(StatisticsDAO.getYearlyRevenue(i));
        }
        return dayStats;
    }

    public static List<HashMap<String, Integer>> getQuarterOrderQuantity(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            HashMap<String, Integer> quarterStat = new HashMap<>();
            quarterStat.put("quarter", i);
            quarterStat.put("orderQuantity", StatisticsDAO.getTotalInvoices("Quý", i, year));
            monthStats.add(quarterStat);
        }
        return monthStats;
    }

    public static List<HashMap<String, Integer>> getMonthlyOrderQuantity(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            HashMap<String, Integer> monthStat = new HashMap<>();
            monthStat.put("month", i);
            monthStat.put("orderQuantity", StatisticsDAO.getTotalInvoices("Tháng", i, year));
            monthStats.add(monthStat);
        }
        return monthStats;
    }

    public static List<HashMap<String, Integer>> getYearlyOrderQuantity(int from, int to) {
        List<HashMap<String, Integer>> yearStats = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            HashMap<String, Integer> yearStat = new HashMap<>();
            yearStat.put("year", i);
            yearStat.put("orderQuantity", StatisticsDAO.getTotalInvoices("Năm", 0, i));
            yearStats.add(yearStat);
        }
        return yearStats;
    }

    public static List<HashMap<String, String>> getBestSellerCuisines(int year, int limit) {
        return StatisticsDAO.getBestSellerCuisines(year, limit);
    }

    public static List<HashMap<String, String>> getRevenueProportionByCategory(int year) {
        return StatisticsDAO.getRevenueProportionByCategory(year);
    }

    public static List<HashMap<String, Integer>> getQuarterCuisineRevenues(String cuisineName, int year) {
        List<HashMap<String, Integer>> quarterStats = new ArrayList<>();
        for (int quarter = 1; quarter <= 4; quarter++) {
            HashMap<String, Integer> quarterStat = new HashMap<>();
            quarterStat.put("quarter", quarter);
            quarterStat.put("revenue", StatisticsDAO.getCuisineRevenue("Quý", cuisineName, year, quarter));
            quarterStats.add(quarterStat);
        }
        return quarterStats;
    }

    public static List<HashMap<String, Integer>> getMonthlyCuisineRevenues(String cuisineName, int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            HashMap<String, Integer> monthStat = new HashMap<>();
            monthStat.put("month", month);
            monthStat.put("revenue", StatisticsDAO.getCuisineRevenue("Tháng", cuisineName, year, month));
            monthStats.add(monthStat);
        }
        return monthStats;
    }

    public static List<HashMap<String, Integer>> getYearlyCuisineRevenues(String cuisineName, int from, int to) {
        List<HashMap<String, Integer>> yearStats = new ArrayList<>();
        for (int year = from; year <= to; year++) {
            HashMap<String, Integer> yearStat = new HashMap<>();
            yearStat.put("year", year);
            yearStat.put("revenue", StatisticsDAO.getCuisineRevenue("Năm", cuisineName, year, 0));
            yearStats.add(yearStat);
        }
        return yearStats;
    }

    public static int getCuisineRevenue(String cuisineName, String categoryName, String criteria, int year) {
        CuisineBUS cuisineBUS = new CuisineBUS();
        if (criteria.equals("Năm") && cuisineName.equals("Tất cả")) {
            List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
            int totalRevenue = 0;
            for (int i = 2021; i < LocalDate.now().getYear(); i++) {
                for (String name : cuisineNames) {
                    totalRevenue += StatisticsDAO.getCuisineRevenue(criteria, name, i, 0);
                }
            }
            return totalRevenue;
        } else if (!criteria.equals("Năm") && cuisineName.equals("Tất cả")) {
            List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
            int totalRevenue = 0;
            for (String name : cuisineNames) {
                totalRevenue += StatisticsDAO.getCuisineRevenue("Năm", name, year, 0);
            }
            return totalRevenue;
        } else if (criteria.equals("Năm")) {
            int totalRevenue = 0;
            for (int i = 2021; i < LocalDate.now().getYear(); i++) {
                totalRevenue += StatisticsDAO.getCuisineRevenue(criteria, cuisineName, i, 0);
            }
            return totalRevenue;
        }

        return StatisticsDAO.getCuisineRevenue("Năm", cuisineName, year, 0);
    }

    public static int getCuisineTotalSell(String cuisineName, String categoryName, String criteria, int year) {
        CuisineBUS cuisineBUS = new CuisineBUS();
        if (criteria.equals("Năm") && cuisineName.equals("Tất cả")) {
            List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
            int totalRevenue = 0;
            for (int i = 2021; i <= LocalDate.now().getYear(); i++) {
                for (String name : cuisineNames) {
                    totalRevenue += StatisticsDAO.getCuisineTotalSell(criteria, name, i, 0);
                }
            }
            return totalRevenue;
        } else if (!criteria.equals("Năm") && cuisineName.equals("Tất cả")) {
            List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
            int totalRevenue = 0;
            for (String name : cuisineNames) {
                totalRevenue += StatisticsDAO.getCuisineTotalSell("Năm", name, year, 0);
            }
            return totalRevenue;
        } else if (criteria.equals("Năm")) {
            int totalRevenue = 0;
            for (int i = 2021; i < LocalDate.now().getYear(); i++) {
                totalRevenue += StatisticsDAO.getCuisineTotalSell(criteria, cuisineName, i, 0);
            }
            return totalRevenue;
        }

        return StatisticsDAO.getCuisineTotalSell("Năm", cuisineName, year, 0);
    }

    public static List<Customer> getNewCustomers(int year) {
        CustomerBUS customerBUS = new CustomerBUS();
        return customerBUS.getNewCustomersInYear(year);
    }

    public static List<Customer> getTopMembershipCustomers(int year, int limit) {
        CustomerBUS customerBUS = new CustomerBUS();
        return customerBUS.getTopMembershipCustomers(year, limit);
    }

    public static int getTotalCustomersQuantityByYear(int year) {
        CustomerBUS customerBUS = new CustomerBUS();
        return customerBUS.getTotalCustomersQuantityByYear(year);
    }

    public static List<HashMap<String, Integer>> getMonthlyNewCustomers(int year) {
        List<HashMap<String, Integer>> monthStats = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            HashMap<String, Integer> monthStat = new HashMap<>();
            monthStat.put("month", i);
            monthStat.put("newCustomerQuantity", StatisticsDAO.getNewCustomerQuantity("Tháng", i, year));
            monthStats.add(monthStat);
        }
        return monthStats;
    }

    public static List<HashMap<String, Integer>> getQuarterNewCustomer(int year) {
        List<HashMap<String, Integer>> quarterStats = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            HashMap<String, Integer> quarterStat = new HashMap<>();
            quarterStat.put("quarter", i);
            quarterStat.put("newCustomerQuantity", StatisticsDAO.getNewCustomerQuantity("Quý", i, year));
            quarterStats.add(quarterStat);
        }
        return quarterStats;
    }

    public static List<HashMap<String, Integer>> getYearlyNewCustomer(int from, int to) {
        List<HashMap<String, Integer>> yearStats = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            HashMap<String, Integer> yearStat = new HashMap<>();
            yearStat.put("year", i);
            yearStat.put("newCustomerQuantity", StatisticsDAO.getNewCustomerQuantity("Năm", 0, i));
            yearStats.add(yearStat);
        }
        return yearStats;
    }

    public static double getPersonalRevenue(String id, int year) {
        return StatisticsDAO.getPersonalRevenue(id, "Năm", year, 0);
    }

    public static int getPersonalTotalOrder(String id, int year) {
        return StatisticsDAO.getPersonalTotalOrder(id, "Năm", year, 0);
    }

    public static int getPersonalTotalReservation(String id, int year) {
        return StatisticsDAO.getPersonalTotalReservation(id, "Năm", year, 0);
    }

    public static List<HashMap<String, String>> getPersonalRevenueAndOrder(String id, String criteria, int year) {
        List<HashMap<String, String>> stats = new ArrayList<>();

        switch (criteria) {
            case "Quý" -> {
                for (int quarter = 1; quarter <= 4; quarter++) {
                    double revenue = StatisticsDAO.getPersonalRevenue(id, criteria, year, quarter);
                    int totalOrder = StatisticsDAO.getPersonalTotalOrder(id, criteria, year, quarter);
                    int totalReservation = StatisticsDAO.getPersonalTotalReservation(id, criteria, year, quarter);
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
                    double revenue = StatisticsDAO.getPersonalRevenue(id, criteria, year, month);
                    int totalOrder = StatisticsDAO.getPersonalTotalOrder(id, criteria, year, month);
                    int totalReservation = StatisticsDAO.getPersonalTotalReservation(id, criteria, year, month);
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
                    double revenue = StatisticsDAO.getPersonalRevenue(id, criteria, year, y);
                    int totalOrder = StatisticsDAO.getPersonalTotalOrder(id, criteria, year, y);
                    int totalReservation = StatisticsDAO.getPersonalTotalReservation(id, criteria, year, y);
                    HashMap<String, String> stat = new HashMap<>();
                    stat.put("year", String.valueOf(y));
                    stat.put("revenue", String.valueOf(revenue));
                    stat.put("totalOrder", String.valueOf(totalOrder));
                    stat.put("totalReservation", String.valueOf(totalReservation));
                    stats.add(stat);
                }
            }
        }

        return stats;
    }
}
