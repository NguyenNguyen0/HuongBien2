package huongbien.dao.remote;

import huongbien.entity.Employee;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IEmployeeDAO extends Remote {
    List<Employee> getAll() throws RemoteException;

    Employee getOneById(String id) throws RemoteException;

    List<Employee> getManyById(String id) throws RemoteException;

    Employee getByEmail(String email) throws RemoteException;

    List<Employee> getByPhoneNumber(String phoneNumber) throws RemoteException;

    List<Employee> getByPosition(String position) throws RemoteException;

    List<Employee> getByName(String name) throws RemoteException;

    List<Employee> getByCitizenIDNumber(String citizenIDNumber) throws RemoteException;

    List<Employee> getByCriteria(String phoneNumber, String name, String employeeId) throws RemoteException;

    List<Employee> getAllWithPagination(int offset, int limit) throws RemoteException;

    List<Employee> getAllStillWorkingWithPagination(int offset, int limit) throws RemoteException;

    List<Employee> getByPositionWithPagination(String position, int offset, int limit) throws RemoteException;

    List<Employee> getByPhoneNumberWithPagination(String phoneNumber, int offset, int limit) throws RemoteException;

    List<Employee> getByNameWithPagination(String name, int offset, int limit) throws RemoteException;

    int countByPhoneNumber(String phoneNumber) throws RemoteException;

    int countByName(String name) throws RemoteException;

    int countByPosition(String position) throws RemoteException;

    int countStillWorking() throws RemoteException;

    int countAll() throws RemoteException;

    boolean updateStatus(String employeeId, String status) throws RemoteException;

    boolean updateEmployeeInfo(Employee employee) throws RemoteException;

    boolean updateEmployeeInfoProfile(Employee employee) throws RemoteException;

    void updateWorkHour(String id, double hours) throws RemoteException;

    boolean add(Employee employee) throws RemoteException;

    boolean update(Employee employee) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}