package huongbien.bus;

import huongbien.dao.EmployeeDAO;
import huongbien.entity.Employee;
import huongbien.jpa.PersistenceUnit;

import java.util.List;

public class EmployeeBUS {
    private final EmployeeDAO employeeDao;

    public EmployeeBUS() {
        employeeDao = new EmployeeDAO(PersistenceUnit.MARIADB_JPA);
    }

    public int countAllEmployees() {
        return employeeDao.countAll();
    }

    public int countStillWorkingEmployees() {
        return employeeDao.countStillWorking();
    }

    public int countEmployeesByPosition(String position) {
        return employeeDao.countByPosition(position);
    }

    public int countEmployeesByPhoneNumber(String phoneNumber) {
        return employeeDao.countByPhoneNumber(phoneNumber);
    }

    public int countEmployeesByName(String name) {
        return employeeDao.countByName(name);
    }

    public List<Employee> getEmployeesByNameWithPagination(String name, int offset, int limit) {
        return employeeDao.getByNameWithPagination(name, offset, limit);
    }

    public List<Employee> getEmployeesByPhoneNumberWithPagination(String phoneNumber, int offset, int limit) {
        return employeeDao.getByPhoneNumberWithPagination(phoneNumber, offset, limit);
    }

    public List<Employee> getEmployeesByPositionWithPagination(String position, int offset, int limit) {
        return employeeDao.getByPositionWithPagination(position, offset, limit);
    }

    public List<Employee> getStillWorkingEmployeesWithPagination(int offset, int limit) {
        return employeeDao.getAllStillWorkingWithPagination(offset, limit);
    }

    public List<Employee> getAllEmployeesWithPagination(int offset, int limit) {
        return employeeDao.getAllWithPagination(offset, limit);
    }

    public List<Employee> getAllEmployees() {
        return employeeDao.getAll();
    }

    public List<Employee> getEmployeeById(String id) {
        if (id.isBlank()) return null;
        return employeeDao.getManyById(id);
    }

    public List<Employee> getEmployeeByPosition(String position) {
        if (position.isBlank()) return null;
        return employeeDao.getByPosition(position);
    }

    public List<Employee> getEmployeeByCriteria(String phoneNumber, String name, String employeeId) {
        return employeeDao.getByCriteria(phoneNumber, name, employeeId);
    }

    public boolean updateEmployeeStatus(String employeeId, String status) {
        if (employeeId.isBlank()) return false;
        return employeeDao.updateStatus(employeeId, status);
    }

    public boolean updateEmployeeInfo(Employee employee) {
        if (employee == null) return false;
        return employeeDao.updateEmployeeInfo(employee);
    }

    public boolean addEmployee(Employee employee) {
        if (employee == null) return false;
        return employeeDao.add(employee);
    }
}
