package huongbien.bus;

import huongbien.dao.impl.EmployeeDAO;
import huongbien.entity.Employee;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;

public class EmployeeBUS {
    private final EmployeeDAO employeeDao;

    public EmployeeBUS() throws RemoteException {
        employeeDao = new EmployeeDAO(PersistenceUnit.MARIADB_JPA);
    }

    public int countAllEmployees() throws RemoteException {
        return employeeDao.countAll();
    }

    public int countStillWorkingEmployees() throws RemoteException {
        return employeeDao.countStillWorking();
    }

    public int countEmployeesByPosition(String position) throws RemoteException {
        return employeeDao.countByPosition(position);
    }

    public int countEmployeesByPhoneNumber(String phoneNumber) throws RemoteException {
        return employeeDao.countByPhoneNumber(phoneNumber);
    }

    public int countEmployeesByName(String name) throws RemoteException {
        return employeeDao.countByName(name);
    }

    public List<Employee> getEmployeesByNameWithPagination(String name, int offset, int limit) throws RemoteException {
        return employeeDao.getByNameWithPagination(name, offset, limit);
    }

    public List<Employee> getEmployeesByPhoneNumberWithPagination(String phoneNumber, int offset, int limit) throws RemoteException {
        return employeeDao.getByPhoneNumberWithPagination(phoneNumber, offset, limit);
    }

    public List<Employee> getEmployeesByPositionWithPagination(String position, int offset, int limit) throws RemoteException {
        return employeeDao.getByPositionWithPagination(position, offset, limit);
    }

    public List<Employee> getStillWorkingEmployeesWithPagination(int offset, int limit) throws RemoteException {
        return employeeDao.getAllStillWorkingWithPagination(offset, limit);
    }

    public List<Employee> getAllEmployeesWithPagination(int offset, int limit) throws RemoteException {
        return employeeDao.getAllWithPagination(offset, limit);
    }

    public List<Employee> getAllEmployees() throws RemoteException {
        return employeeDao.getAll();
    }

    public List<Employee> getEmployeeById(String id) throws RemoteException {
        if (id.isBlank()) return null;
        return employeeDao.getManyById(id);
    }

    public List<Employee> getEmployeeByPosition(String position) throws RemoteException {
        if (position.isBlank()) return null;
        return employeeDao.getByPosition(position);
    }

    public List<Employee> getEmployeeByCriteria(String phoneNumber, String name, String employeeId) throws RemoteException {
        return employeeDao.getByCriteria(phoneNumber, name, employeeId);
    }

    public boolean updateEmployeeStatus(String employeeId, String status) throws RemoteException {
        if (employeeId.isBlank()) return false;
        return employeeDao.updateStatus(employeeId, status);
    }

    public boolean updateEmployeeInfo(Employee employee) throws RemoteException {
        if (employee == null) return false;
        return employeeDao.updateEmployeeInfo(employee);
    }

    public boolean addEmployee(Employee employee) throws RemoteException {
        if (employee == null) return false;
        return employeeDao.add(employee);
    }
}