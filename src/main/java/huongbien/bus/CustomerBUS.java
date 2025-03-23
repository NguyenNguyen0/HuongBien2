package huongbien.bus;


import huongbien.dao.CustomerDAO;
import huongbien.entity.Customer;
import huongbien.jpa.PersistenceUnit;

import java.time.LocalDate;
import java.util.List;

public class CustomerBUS {
    private final CustomerDAO customerDao;

    public CustomerBUS() {
        customerDao = new CustomerDAO(PersistenceUnit.MARIADB_JPA);
    }

    public int getTotalCustomerCount() {
        return customerDao.countTotal();
    }

    public int getTotalCustomersCountByPhoneNumber(String phoneNumber) {
        return customerDao.countTotalByPhoneNumber(phoneNumber);
    }

    public int getTotalCustomersCountByName(String name) {
        return customerDao.countTotalByName(name);
    }

    public int getTotalCustomersCountById(String id) {
        return customerDao.countTotalById(id);
    }

    public List<Customer> getCustomersByIdWithPagination(int offset, int limit, String id) {
        return customerDao.getAllWithPaginationById(id, offset, limit);
    }

    public List<Customer> getCustomersByNameWithPagination(int offset, int limit, String name) {
        return customerDao.getAllWithPaginationByName(name, offset, limit);
    }

    public List<Customer> getCustomersByPhoneNumberWithPagination(int offset, int limit, String phoneNumber) {
        return customerDao.getAllWithPaginationByPhoneNumber(phoneNumber, offset, limit);
    }

    public List<Customer> getAllCustomersWithPagination(int offset, int limit) {
        return customerDao.getAllWithPagination(offset, limit);
    }

    public List<Customer> getNewCustomersInYear(int year) {
        return customerDao.getNewCustomersInYear(year);
    }

    public List<Customer> getTopMembershipCustomers(int year, int limit) {
        return customerDao.getTopMembershipCustomers(year, limit);
    }

    public int getTotalCustomersQuantityByYear(int year) {
        return customerDao.countNewCustomerQuantityByYear(year);
    }

    public List<Customer> getCustomerInDay(LocalDate date) {
        return customerDao.getCustomerInDay(date);
    }

    public List<Customer> getAllCustomer() {
        return customerDao.getAll();
    }

    public List<Customer> getCustomerByName(String name) {
        return customerDao.getByName(name);
    }

//    public List<Customer> getCustomerByPhoneNumber(String phoneNumber) {
//        return customerDao.getByManyPhoneNumber(phoneNumber);
//    }

    public Customer getCustomerById(String id) {
        return customerDao.getById(id);
    }

    public Customer getCustomer(String customerId) {
        return customerDao.getById(customerId);
    }

    public boolean updateCustomerInfo(Customer customer) {
        return customerDao.update(customer);
    }

    public boolean addCustomer(Customer customer) {
        return customerDao.add(customer);
    }

    public Customer getCustomerSearchReservation(String search){
        return customerDao.getCustomerSearchReservation(search);
    }

    public boolean increasePoint(String id, int point){
        return customerDao.increasePoint(id, point);
    }
}
