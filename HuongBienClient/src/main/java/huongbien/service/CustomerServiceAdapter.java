package huongbien.service;

import huongbien.entity.Customer;
import huongbien.rmi.RmiClient;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Client-side adapter for the CustomerService.
 * Provides error handling and simplifies the use of the remote CustomerService.
 */
public class CustomerServiceAdapter {
    
    /**
     * Get all customers from the remote service.
     * 
     * @return List of all customers, or an empty list if an error occurs.
     */
    public static List<Customer> getAllCustomers() {
        try {
            return RmiClient.getCustomerService().getAllCustomers();
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving all customers", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get a customer by their ID.
     * 
     * @param id Customer ID to search for.
     * @return Customer if found, null otherwise.
     */
    public static Customer getCustomerById(String id) {
        try {
            return RmiClient.getCustomerService().getCustomerById(id);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving customer with ID: " + id, e);
            return null;
        }
    }
    
    /**
     * Get a customer by phone number.
     * 
     * @param phoneNumber Phone number to search for.
     * @return Customer if found, null otherwise.
     */
    public static Customer getCustomerByPhoneNumber(String phoneNumber) {
        try {
            return RmiClient.getCustomerService().getCustomerByPhoneNumber(phoneNumber);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving customer with phone number: " + phoneNumber, e);
            return null;
        }
    }
    
    /**
     * Get customers by name.
     * 
     * @param name Name to search for.
     * @return List of matching customers, or an empty list if an error occurs.
     */
    public static List<Customer> getCustomersByName(String name) {
        try {
            return RmiClient.getCustomerService().getCustomersByName(name);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving customers with name: " + name, e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get customers with pagination.
     * 
     * @param offset Starting position in the result set.
     * @param limit Maximum number of records to return.
     * @return Paginated list of customers, or an empty list if an error occurs.
     */
    public static List<Customer> getCustomersWithPagination(int offset, int limit) {
        try {
            return RmiClient.getCustomerService().getCustomersWithPagination(offset, limit);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving customers with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get customers by ID with pagination.
     * 
     * @param offset Starting position in the result set.
     * @param limit Maximum number of records to return.
     * @param id ID to search for.
     * @return Paginated list of matching customers, or an empty list if an error occurs.
     */
    public static List<Customer> getCustomersByIdWithPagination(int offset, int limit, String id) {
        try {
            return RmiClient.getCustomerService().getCustomersByIdWithPagination(offset, limit, id);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving customers by ID with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get customers by name with pagination.
     * 
     * @param offset Starting position in the result set.
     * @param limit Maximum number of records to return.
     * @param name Name to search for.
     * @return Paginated list of matching customers, or an empty list if an error occurs.
     */
    public static List<Customer> getCustomersByNameWithPagination(int offset, int limit, String name) {
        try {
            return RmiClient.getCustomerService().getCustomersByNameWithPagination(offset, limit, name);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving customers by name with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get customers by phone number with pagination.
     * 
     * @param offset Starting position in the result set.
     * @param limit Maximum number of records to return.
     * @param phoneNumber Phone number to search for.
     * @return Paginated list of matching customers, or an empty list if an error occurs.
     */
    public static List<Customer> getCustomersByPhoneNumberWithPagination(int offset, int limit, String phoneNumber) {
        try {
            return RmiClient.getCustomerService().getCustomersByPhoneNumberWithPagination(offset, limit, phoneNumber);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving customers by phone number with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get a customer for reservation search.
     * 
     * @param search Search text.
     * @return Customer if found, null otherwise.
     */
    public static Customer getCustomerSearchReservation(String search) {
        try {
            return RmiClient.getCustomerService().getCustomerSearchReservation(search);
        } catch (RemoteException e) {
            handleRemoteException("Error searching for customer reservation: " + search, e);
            return null;
        }
    }
    
    /**
     * Get the total count of customers.
     * 
     * @return Total number of customers, or 0 if an error occurs.
     */
    public static int getTotalCustomerCount() {
        try {
            return RmiClient.getCustomerService().getTotalCustomerCount();
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving total customer count", e);
            return 0;
        }
    }
    
    /**
     * Add a new customer.
     * 
     * @param customer Customer to add.
     * @return true if successful, false otherwise.
     */
    public static boolean addCustomer(Customer customer) {
        try {
            return RmiClient.getCustomerService().addCustomer(customer);
        } catch (RemoteException e) {
            handleRemoteException("Error adding customer", e);
            return false;
        }
    }
    
    /**
     * Update customer information.
     * 
     * @param customer Customer with updated information.
     * @return true if successful, false otherwise.
     */
    public static boolean updateCustomerInfo(Customer customer) {
        try {
            return RmiClient.getCustomerService().updateCustomerInfo(customer);
        } catch (RemoteException e) {
            handleRemoteException("Error updating customer information", e);
            return false;
        }
    }
    
    /**
     * Increase a customer's membership points.
     * 
     * @param id Customer ID.
     * @param point Number of points to add.
     * @return true if successful, false otherwise.
     */
    public static boolean increasePoint(String id, int point) {
        try {
            return RmiClient.getCustomerService().increasePoint(id, point);
        } catch (RemoteException e) {
            handleRemoteException("Error increasing points for customer: " + id, e);
            return false;
        }
    }
    
    /**
     * Handle RemoteException by logging and possibly displaying an error message.
     * 
     * @param message Error message to log.
     * @param e The exception that occurred.
     */
    private static void handleRemoteException(String message, RemoteException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        
        // TODO: Add proper logging and/or user notification
    }
}