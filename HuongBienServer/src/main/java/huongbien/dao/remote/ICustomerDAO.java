package huongbien.dao.remote;

import huongbien.entity.Customer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface ICustomerDAO extends Remote {
    List<Customer> getAll() throws RemoteException;

    Customer getByPhoneNumber(String phoneNumber) throws RemoteException;

    List<Customer> getByPhoneNumberPattern(String phoneNumber) throws RemoteException;

    List<Customer> getByName(String name) throws RemoteException;

    List<Customer> getAllWithPagination(int offset, int limit) throws RemoteException;

    List<Customer> getAllWithPaginationByPhoneNumber(String phoneNumber, int offset, int limit) throws RemoteException;

    List<Customer> getAllWithPaginationByName(String name, int offset, int limit) throws RemoteException;

    List<Customer> getAllWithPaginationById(String id, int offset, int limit) throws RemoteException;

    List<Customer> getCustomerInDay(LocalDate date) throws RemoteException;

    List<Customer> getNewCustomersInYear(int year) throws RemoteException;

    List<Customer> getTopMembershipCustomers(int year, int limit) throws RemoteException;

    int countNewCustomerQuantityByYear(int year) throws RemoteException;

    int countTotalById(String id) throws RemoteException;

    int countTotal() throws RemoteException;

    int countTotalByPhoneNumber(String phoneNumber) throws RemoteException;

    int countTotalByName(String name) throws RemoteException;

    List<String> getPhoneNumber() throws RemoteException;

    Customer getById(String customerId) throws RemoteException;

    Customer getCustomerSearchReservation(String search) throws RemoteException;

    boolean increasePoint(String id, int point) throws RemoteException;

    boolean add(Customer customer) throws RemoteException;

    boolean update(Customer customer) throws RemoteException;

    boolean delete(Customer customer) throws RemoteException;
}