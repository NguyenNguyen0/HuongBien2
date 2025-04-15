package huongbien.rmi.interfaces;

import huongbien.entity.Customer;
import huongbien.entity.MembershipLevel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Remote interface for Customer operations.
 * This interface defines the operations that can be performed remotely on Customer entities.
 */
public interface CustomerService extends Remote {
    
    /**
     * Gets all customers.
     * 
     * @return List of all customers
     * @throws RemoteException if a remote communication error occurs
     */
    List<Customer> getAllCustomers() throws RemoteException;
    
    /**
     * Gets a customer by ID.
     * 
     * @param id The customer ID
     * @return The customer with the specified ID or null if not found
     * @throws RemoteException if a remote communication error occurs
     */
    Customer getCustomerById(String id) throws RemoteException;
    
    /**
     * Gets a customer by phone number.
     * 
     * @param phoneNumber The phone number to search for
     * @return The customer with the specified phone number or null if not found
     * @throws RemoteException if a remote communication error occurs
     */
    Customer getCustomerByPhoneNumber(String phoneNumber) throws RemoteException;
    
    /**
     * Gets customers by name.
     * 
     * @param name The name to search for
     * @return List of customers with the specified name
     * @throws RemoteException if a remote communication error occurs
     */
    List<Customer> getCustomersByName(String name) throws RemoteException;
    
    /**
     * Gets customers by name with pagination.
     * 
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param name The name to search for
     * @return List of customers with the specified name
     * @throws RemoteException if a remote communication error occurs
     */
    List<Customer> getCustomersByNameWithPagination(int offset, int limit, String name) throws RemoteException;
    
    /**
     * Gets the total count of customers.
     * 
     * @return The total number of customers
     * @throws RemoteException if a remote communication error occurs
     */
    int getTotalCustomerCount() throws RemoteException;
    
    /**
     * Adds a new customer.
     * 
     * @param customer The customer to add
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean addCustomer(Customer customer) throws RemoteException;
    
    /**
     * Updates customer information.
     * 
     * @param customer The customer with updated information
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean updateCustomerInfo(Customer customer) throws RemoteException;
    
    /**
     * Increases a customer's points.
     * 
     * @param id The customer ID
     * @param points The number of points to add
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean increasePoint(String id, int points) throws RemoteException;
    
    /**
     * Gets new customers in a specific year.
     * 
     * @param year The year to filter by
     * @return List of new customers in the specified year
     * @throws RemoteException if a remote communication error occurs
     */
    List<Customer> getNewCustomersInYear(int year) throws RemoteException;
    
    /**
     * Gets top membership customers in a specific year.
     * 
     * @param year The year to filter by
     * @param limit The maximum number of customers to return
     * @return List of top membership customers
     * @throws RemoteException if a remote communication error occurs
     */
    List<Customer> getTopMembershipCustomers(int year, int limit) throws RemoteException;
    
    /**
     * Gets customers who visited on a specific day.
     * 
     * @param date The date to filter by
     * @return List of customers who visited on the specified date
     * @throws RemoteException if a remote communication error occurs
     */
    List<Customer> getCustomerInDay(LocalDate date) throws RemoteException;
}