package huongbien.service;


import huongbien.dao.remote.ICustomerDAO;
import huongbien.entity.Customer;
import huongbien.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public class CustomerBUS {
    private final ICustomerDAO customerDao;

    public CustomerBUS() throws RemoteException {
        try {
            customerDao = RMIClient.getInstance().getCustomerDAO();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTotalCustomerCount() throws RemoteException {
        return customerDao.countTotal();
    }

    public int getTotalCustomersCountByPhoneNumber(String phoneNumber) throws RemoteException {
        return customerDao.countTotalByPhoneNumber(phoneNumber);
    }

    public int getTotalCustomersCountByName(String name) throws RemoteException {
        return customerDao.countTotalByName(name);
    }

    public int getTotalCustomersCountById(String id) throws RemoteException {
        return customerDao.countTotalById(id);
    }

    public List<Customer> getCustomersByIdWithPagination(int offset, int limit, String id) throws RemoteException {
        return customerDao.getAllWithPaginationById(id, offset, limit);
    }

    public List<Customer> getCustomersByNameWithPagination(int offset, int limit, String name) throws RemoteException {
        return customerDao.getAllWithPaginationByName(name, offset, limit);
    }

    public List<Customer> getCustomersByPhoneNumberWithPagination(int offset, int limit, String phoneNumber) throws RemoteException {
        return customerDao.getAllWithPaginationByPhoneNumber(phoneNumber, offset, limit);
    }

    public List<Customer> getAllCustomersWithPagination(int offset, int limit) throws RemoteException {
        return customerDao.getAllWithPagination(offset, limit);
    }

    public List<Customer> getNewCustomersInYear(int year) throws RemoteException {
        return customerDao.getNewCustomersInYear(year);
    }

    public List<Customer> getTopMembershipCustomers(int year, int limit) throws RemoteException {
        return customerDao.getTopMembershipCustomers(year, limit);
    }

    public int getTotalCustomersQuantityByYear(int year) throws RemoteException {
        return customerDao.countNewCustomerQuantityByYear(year);
    }

    public List<Customer> getCustomerInDay(LocalDate date) throws RemoteException {
        return customerDao.getCustomerInDay(date);
    }

    public List<Customer> getAllCustomer() throws RemoteException {
        return customerDao.getAll();
    }

    public List<Customer> getCustomerByName(String name) throws RemoteException {
        return customerDao.getByName(name);
    }

//    public List<Customer> getCustomerByPhoneNumber(String phoneNumber) {
//        return customerDao.getByManyPhoneNumber(phoneNumber);
//    }

    public Customer getCustomerById(String id) throws RemoteException {
        return customerDao.getById(id);
    }

    public Customer getCustomer(String customerId) throws RemoteException {
        return customerDao.getById(customerId);
    }

    public boolean updateCustomerInfo(Customer customer) throws RemoteException {
        return customerDao.update(customer);
    }

    public boolean addCustomer(Customer customer) throws RemoteException {
        return customerDao.add(customer);
    }

    public Customer getCustomerSearchReservation(String search) throws RemoteException {
        return customerDao.getCustomerSearchReservation(search);
    }

    public boolean increasePoint(String id, int point) throws RemoteException {
        return customerDao.increasePoint(id, point);
    }
}
