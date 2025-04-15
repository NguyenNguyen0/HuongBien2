package huongbien.service;

import huongbien.entity.Customer;
import huongbien.rmi.RmiClient;
import huongbien.util.ExceptionHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for CustomerService remote interface.
 * Provides a simplified API for UI components and handles remote exceptions.
 */
public class CustomerServiceAdapter {
    
    /**
     * Gets all customers from the server.
     * 
     * @return List of all customers or empty list if an error occurs
     */
    public static List<Customer> getAllCustomers() {
        try {
            return RmiClient.getCustomerService().getAllCustomers();
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to retrieve customers", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets a customer by ID from the server.
     * 
     * @param id The customer ID
     * @return The customer with the specified ID or null if not found or an error occurs
     */
    public static Customer getCustomerById(String id) {
        try {
            return RmiClient.getCustomerService().getCustomerById(id);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to retrieve customer with ID: " + id, 
                e
            );
            return null;
        }
    }
    
    /**
     * Searches for customers by name.
     * 
     * @param name The name to search for
     * @return List of customers matching the name or empty list if an error occurs
     */
    public static List<Customer> searchCustomersByName(String name) {
        try {
            return RmiClient.getCustomerService().searchCustomersByName(name);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to search customers by name: " + name, 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Searches for customers by phone.
     * 
     * @param phone The phone number to search for
     * @return List of customers matching the phone number or empty list if an error occurs
     */
    public static List<Customer> searchCustomersByPhone(String phone) {
        try {
            return RmiClient.getCustomerService().searchCustomersByPhone(phone);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to search customers by phone: " + phone, 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Adds a new customer to the server.
     * 
     * @param customer The customer to add
     * @return true if successful, false otherwise
     */
    public static boolean addCustomer(Customer customer) {
        try {
            return RmiClient.getCustomerService().addCustomer(customer);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to add customer", 
                e
            );
            return false;
        }
    }
    
    /**
     * Updates an existing customer on the server.
     * 
     * @param customer The customer with updated information
     * @return true if successful, false otherwise
     */
    public static boolean updateCustomer(Customer customer) {
        try {
            return RmiClient.getCustomerService().updateCustomer(customer);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to update customer", 
                e
            );
            return false;
        }
    }
    
    /**
     * Deletes a customer from the server.
     * 
     * @param id The ID of the customer to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteCustomer(String id) {
        try {
            return RmiClient.getCustomerService().deleteCustomer(id);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to delete customer with ID: " + id, 
                e
            );
            return false;
        }
    }
    
    /**
     * Gets the total number of customers in the system.
     * 
     * @return The total number of customers or 0 if an error occurs
     */
    public static int getTotalCustomerCount() {
        try {
            return RmiClient.getCustomerService().getTotalCustomerCount();
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to get total customer count", 
                e
            );
            return 0;
        }
    }
    
    /**
     * Gets customers registered in the last X days.
     * 
     * @param days Number of days to look back
     * @return List of customers registered in the specified period or empty list if an error occurs
     */
    public static List<Customer> getRecentCustomers(int days) {
        try {
            return RmiClient.getCustomerService().getRecentCustomers(days);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Customer Service Error", 
                "Failed to retrieve recent customers", 
                e
            );
            return new ArrayList<>();
        }
    }
}