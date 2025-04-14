package huongbien.rmi.impl;

import huongbien.bus.CustomerBUS;
import huongbien.entity.Customer;
import huongbien.rmi.interfaces.CustomerService;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the CustomerService remote interface.
 * This class delegates to the CustomerBUS business logic layer.
 */
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerBUS customerBUS;
    
    public CustomerServiceImpl() {
        this.customerBUS = new CustomerBUS();
    }
    
    @Override
    public List<Customer> getAllCustomers() throws RemoteException {
        return customerBUS.getAllCustomer();
    }
    
    @Override
    public Customer getCustomerById(String id) throws RemoteException {
        return customerBUS.getCustomerById(id);
    }
    
    @Override
    public Customer getCustomerByPhoneNumber(String phoneNumber) throws RemoteException {
        // Redirect to appropriate method based on your existing implementation
        // This might need to be adjusted based on your actual DAO/BUS structure
        List<Customer> customers = customerBUS.getCustomersByPhoneNumberWithPagination(0, 1, phoneNumber);
        return customers != null && !customers.isEmpty() ? customers.get(0) : null;
    }
    
    @Override
    public List<Customer> getCustomersByName(String name) throws RemoteException {
        return customerBUS.getCustomerByName(name);
    }
    
    @Override
    public List<Customer> getCustomersWithPagination(int offset, int limit) throws RemoteException {
        return customerBUS.getAllCustomersWithPagination(offset, limit);
    }
    
    @Override
    public List<Customer> getCustomersByIdWithPagination(int offset, int limit, String id) throws RemoteException {
        return customerBUS.getCustomersByIdWithPagination(offset, limit, id);
    }
    
    @Override
    public List<Customer> getCustomersByNameWithPagination(int offset, int limit, String name) throws RemoteException {
        return customerBUS.getCustomersByNameWithPagination(offset, limit, name);
    }
    
    @Override
    public List<Customer> getCustomersByPhoneNumberWithPagination(int offset, int limit, String phoneNumber) throws RemoteException {
        return customerBUS.getCustomersByPhoneNumberWithPagination(offset, limit, phoneNumber);
    }
    
    @Override
    public Customer getCustomerSearchReservation(String search) throws RemoteException {
        return customerBUS.getCustomerSearchReservation(search);
    }
    
    @Override
    public int getTotalCustomerCount() throws RemoteException {
        return customerBUS.getTotalCustomerCount();
    }
    
    @Override
    public int getTotalCustomersCountByPhoneNumber(String phoneNumber) throws RemoteException {
        return customerBUS.getTotalCustomersCountByPhoneNumber(phoneNumber);
    }
    
    @Override
    public int getTotalCustomersCountByName(String name) throws RemoteException {
        return customerBUS.getTotalCustomersCountByName(name);
    }
    
    @Override
    public int getTotalCustomersCountById(String id) throws RemoteException {
        return customerBUS.getTotalCustomersCountById(id);
    }
    
    @Override
    public List<Customer> getNewCustomersInYear(int year) throws RemoteException {
        return customerBUS.getNewCustomersInYear(year);
    }
    
    @Override
    public List<Customer> getTopMembershipCustomers(int year, int limit) throws RemoteException {
        return customerBUS.getTopMembershipCustomers(year, limit);
    }
    
    @Override
    public int getTotalCustomersQuantityByYear(int year) throws RemoteException {
        return customerBUS.getTotalCustomersQuantityByYear(year);
    }
    
    @Override
    public List<Customer> getCustomerInDay(LocalDate date) throws RemoteException {
        return customerBUS.getCustomerInDay(date);
    }
    
    @Override
    public boolean addCustomer(Customer customer) throws RemoteException {
        return customerBUS.addCustomer(customer);
    }
    
    @Override
    public boolean updateCustomerInfo(Customer customer) throws RemoteException {
        return customerBUS.updateCustomerInfo(customer);
    }
    
    @Override
    public boolean increasePoint(String id, int point) throws RemoteException {
        return customerBUS.increasePoint(id, point);
    }
}