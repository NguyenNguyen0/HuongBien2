package huongbien.rmi.interfaces;

import huongbien.entity.Employee;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface for employee services
 */
public interface EmployeeService extends Remote {
    
    // Employee retrieval
    List<Employee> getAllEmployees() throws RemoteException;
    List<Employee> getEmployeeById(String id) throws RemoteException;
    List<Employee> getEmployeeByPosition(String position) throws RemoteException;
    List<Employee> getEmployeeByCriteria(String phoneNumber, String name, String employeeId) throws RemoteException;
    List<Employee> getAllEmployeesWithPagination(int offset, int limit) throws RemoteException;
    List<Employee> getStillWorkingEmployeesWithPagination(int offset, int limit) throws RemoteException;
    List<Employee> getEmployeesByPositionWithPagination(String position, int offset, int limit) throws RemoteException;
    List<Employee> getEmployeesByNameWithPagination(String name, int offset, int limit) throws RemoteException;
    List<Employee> getEmployeesByPhoneNumberWithPagination(String phoneNumber, int offset, int limit) throws RemoteException;
    
    // Employee counts
    int countAllEmployees() throws RemoteException;
    int countStillWorkingEmployees() throws RemoteException;
    int countEmployeesByPosition(String position) throws RemoteException;
    int countEmployeesByPhoneNumber(String phoneNumber) throws RemoteException;
    int countEmployeesByName(String name) throws RemoteException;
    
    // Employee management
    boolean addEmployee(Employee employee) throws RemoteException;
    boolean updateEmployeeInfo(Employee employee) throws RemoteException;
    boolean updateEmployeeStatus(String employeeId, String status) throws RemoteException;
    boolean updateEmployeeInfoProfile(Employee employee) throws RemoteException;
    boolean updateWorkHour(String id, double hours) throws RemoteException;
}