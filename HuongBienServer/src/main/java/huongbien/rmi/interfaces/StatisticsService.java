package huongbien.rmi.interfaces;

import huongbien.entity.Customer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Remote interface for statistics and reporting services
 */
public interface StatisticsService extends Remote {
    
    // Basic statistics
    int getFirstYear() throws RemoteException;
    int getTotalCustomerQuantity() throws RemoteException;
    int getTotalReservationQuantity() throws RemoteException;
    int getTotalOrderQuantity() throws RemoteException;
    double getTotalRevenue() throws RemoteException;
    double getAverageRevenuePerOrder() throws RemoteException;
    double getCurrentYearGrowthRate() throws RemoteException;
    
    // Revenue statistics
    HashMap<String, String> getYearStat(int year) throws RemoteException;
    HashMap<String, Integer> getDailyRevenue(LocalDate date) throws RemoteException;
    List<HashMap<String, String>> getYearStats(int from, int to) throws RemoteException;
    List<HashMap<String, Integer>> getQuarterRevenues(int year) throws RemoteException;
    List<HashMap<String, Integer>> getMonthlyRevenues(int year) throws RemoteException;
    List<HashMap<String, Integer>> getYearlyRevenues(int from, int to) throws RemoteException;
    
    // Order statistics
    List<HashMap<String, Integer>> getQuarterOrderQuantity(int year) throws RemoteException;
    List<HashMap<String, Integer>> getMonthlyOrderQuantity(int year) throws RemoteException;
    List<HashMap<String, Integer>> getYearlyOrderQuantity(int from, int to) throws RemoteException;
    
    // Cuisine statistics
    List<HashMap<String, String>> getBestSellerCuisines(int year, int limit) throws RemoteException;
    List<HashMap<String, String>> getRevenueProportionByCategory(int year) throws RemoteException;
    List<HashMap<String, Integer>> getQuarterCuisineRevenues(String cuisineName, int year) throws RemoteException;
    List<HashMap<String, Integer>> getMonthlyCuisineRevenues(String cuisineName, int year) throws RemoteException;
    List<HashMap<String, Integer>> getYearlyCuisineRevenues(String cuisineName, int from, int to) throws RemoteException;
    int getCuisineRevenue(String cuisineName, String categoryName, String criteria, int year) throws RemoteException;
    int getCuisineTotalSell(String cuisineName, String categoryName, String criteria, int year) throws RemoteException;
    
    // Customer statistics
    List<Customer> getNewCustomers(int year) throws RemoteException;
    List<Customer> getTopMembershipCustomers(int year, int limit) throws RemoteException;
    int getTotalCustomersQuantityByYear(int year) throws RemoteException;
    List<HashMap<String, Integer>> getMonthlyNewCustomers(int year) throws RemoteException;
    List<HashMap<String, Integer>> getQuarterNewCustomer(int year) throws RemoteException;
    List<HashMap<String, Integer>> getYearlyNewCustomer(int from, int to) throws RemoteException;
    
    // Employee performance statistics
    double getPersonalRevenue(String id, int year) throws RemoteException;
    int getPersonalTotalOrder(String id, int year) throws RemoteException;
    int getPersonalTotalReservation(String id, int year) throws RemoteException;
    List<HashMap<String, String>> getPersonalRevenueAndOrder(String id, String criteria, int year) throws RemoteException;
}