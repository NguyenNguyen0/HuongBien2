package huongbien.rmi.interfaces;

import huongbien.entity.Customer;
import huongbien.entity.MembershipLevel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Remote interface for customer services
 */
public interface CustomerService extends Remote {
    
    // Customer retrieval
    List<Customer> getAllCustomers() throws RemoteException;
    Customer getCustomerById(String id) throws RemoteException;
    Customer getCustomerByPhoneNumber(String phoneNumber) throws RemoteException;
    List<Customer> getCustomersByName(String name) throws RemoteException;
    List<Customer> getCustomersWithPagination(int offset, int limit) throws RemoteException;
    
    // Customer search operations
    List<Customer> getCustomersByIdWithPagination(int offset, int limit, String id) throws RemoteException;
    List<Customer> getCustomersByNameWithPagination(int offset, int limit, String name) throws RemoteException;
    List<Customer> getCustomersByPhoneNumberWithPagination(int offset, int limit, String phoneNumber) throws RemoteException;
    Customer getCustomerSearchReservation(String search) throws RemoteException;
    
    // Customer statistics and counts
    int getTotalCustomerCount() throws RemoteException;
    int getTotalCustomersCountByPhoneNumber(String phoneNumber) throws RemoteException;
    int getTotalCustomersCountByName(String name) throws RemoteException;
    int getTotalCustomersCountById(String id) throws RemoteException;
    List<Customer> getNewCustomersInYear(int year) throws RemoteException;
    List<Customer> getTopMembershipCustomers(int year, int limit) throws RemoteException;
    int getTotalCustomersQuantityByYear(int year) throws RemoteException;
    List<Customer> getCustomerInDay(LocalDate date) throws RemoteException;
    
    // Customer management
    boolean addCustomer(Customer customer) throws RemoteException;
    boolean updateCustomerInfo(Customer customer) throws RemoteException;
    boolean increasePoint(String id, int point) throws RemoteException;
}