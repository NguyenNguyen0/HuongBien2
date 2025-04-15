package huongbien.service.impl;

import huongbien.bus.CustomerBUS;
import huongbien.entity.Customer;
import huongbien.rmi.interfaces.CustomerService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the CustomerService RMI interface.
 * Delegates calls to the CustomerBUS business logic class.
 */
public class CustomerServiceImpl extends UnicastRemoteObject implements CustomerService {
    
    private final CustomerBUS customerBUS;
    
    public CustomerServiceImpl() throws RemoteException {
        super();
        this.customerBUS = new CustomerBUS();
    }
    
    @Override
    public List<Customer> getAllCustomers() throws RemoteException {
        System.out.println("RMI: getAllCustomers() called");
        return customerBUS.getAllCustomers();
    }
    
    @Override
    public Customer getCustomerById(String id) throws RemoteException {
        System.out.println("RMI: getCustomerById() called with ID: " + id);
        return customerBUS.getCustomerById(id);
    }
    
    @Override
    public Customer getCustomerByPhoneNumber(String phoneNumber) throws RemoteException {
        System.out.println("RMI: getCustomerByPhoneNumber() called with phone: " + phoneNumber);
        return customerBUS.getCustomerByPhoneNumber(phoneNumber);
    }
    
    @Override
    public List<Customer> getCustomersByName(String name) throws RemoteException {
        System.out.println("RMI: getCustomersByName() called with name: " + name);
        return customerBUS.getCustomersByName(name);
    }
    
    @Override
    public List<Customer> getCustomersByNameWithPagination(int offset, int limit, String name) throws RemoteException {
        System.out.println("RMI: getCustomersByNameWithPagination() called with offset: " + offset + ", limit: " + limit + ", name: " + name);
        return customerBUS.getCustomersByNameWithPagination(offset, limit, name);
    }
    
    @Override
    public int getTotalCustomerCount() throws RemoteException {
        System.out.println("RMI: getTotalCustomerCount() called");
        return customerBUS.getTotalCustomerCount();
    }
    
    @Override
    public boolean addCustomer(Customer customer) throws RemoteException {
        System.out.println("RMI: addCustomer() called for customer: " + customer.getName());
        return customerBUS.addCustomer(customer);
    }
    
    @Override
    public boolean updateCustomerInfo(Customer customer) throws RemoteException {
        System.out.println("RMI: updateCustomerInfo() called for customer ID: " + customer.getId());
        return customerBUS.updateCustomerInfo(customer);
    }
    
    @Override
    public boolean increasePoint(String id, int points) throws RemoteException {
        System.out.println("RMI: increasePoint() called for customer ID: " + id + " with points: " + points);
        return customerBUS.increasePoint(id, points);
    }
    
    @Override
    public List<Customer> getNewCustomersInYear(int year) throws RemoteException {
        System.out.println("RMI: getNewCustomersInYear() called for year: " + year);
        return customerBUS.getNewCustomersInYear(year);
    }
    
    @Override
    public List<Customer> getTopMembershipCustomers(int year, int limit) throws RemoteException {
        System.out.println("RMI: getTopMembershipCustomers() called for year: " + year + " with limit: " + limit);
        return customerBUS.getTopMembershipCustomers(year, limit);
    }
    
    @Override
    public List<Customer> getCustomerInDay(LocalDate date) throws RemoteException {
        System.out.println("RMI: getCustomerInDay() called for date: " + date);
        return customerBUS.getCustomersInDay(date);
    }
}