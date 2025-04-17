package huongbien.dao.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface IStatisticsDAO extends Remote {
    double getTotalRevenue(String criteria, int period, int year) throws RemoteException;

    int getTotalInvoices(String criteria, int period, int year) throws RemoteException;

    int getCuisineRevenue(String criteria, String cuisineName, int year, int period) throws RemoteException;

    int getNewCustomerQuantity(String criteria, int period, int year) throws RemoteException;

    double getPersonalRevenue(String id, String criteria, int year, int period) throws RemoteException;

    int getPersonalTotalOrder(String id, String criteria, int year, int period) throws RemoteException;

    int getPersonalTotalReservation(String id, String criteria, int year, int period) throws RemoteException;

    int getCuisineTotalSell(String criteria, String cuisineName, int year, int period) throws RemoteException;

    List<HashMap<String, String>> getBestSellerCuisines(int year, int limit) throws RemoteException;

    List<HashMap<String, String>> getRevenueProportionByCategory(int year) throws RemoteException;

    HashMap<String, Integer> getDailyRevenue(LocalDate date) throws RemoteException;

    HashMap<String, Integer> getYearlyRevenue(int year) throws RemoteException;

    HashMap<String, Integer> getMonthlyRevenue(int year, int month) throws RemoteException;

    HashMap<String, Integer> getQuarterRevenue(int year, int quarter) throws RemoteException;

    HashMap<String, String> getYearlyStat(int year) throws RemoteException;

    double getGrowthRate() throws RemoteException;

    double getAverageRevenuePerOrder() throws RemoteException;

    int getTotalCustomers() throws RemoteException;

    double getTotalRevenue() throws RemoteException;

    int getTotalOrders() throws RemoteException;

    int getTotalReservations() throws RemoteException;

    int getFirstYear() throws RemoteException;
}