package huongbien.dao.impl;

import huongbien.dao.remote.IEmployeeDAO;
import huongbien.entity.Employee;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class EmployeeDAO extends GenericDAO<Employee> implements IEmployeeDAO {
    public EmployeeDAO() throws RemoteException {
    }

    public EmployeeDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Employee> getAll() throws RemoteException {
        return findMany("SELECT e FROM Employee e", Employee.class);
    }

    @Override
    public Employee getOneById(String id) throws RemoteException {
        return findOne("SELECT e FROM Employee e WHERE e.id = ?1", Employee.class, id);
    }

    @Override
    public List<Employee> getManyById(String id) throws RemoteException {
        return findMany("SELECT e FROM Employee e WHERE e.id LIKE ?1", Employee.class, id + "%");
    }

    @Override
    public Employee getByEmail(String email) throws RemoteException {
        return findOne("SELECT e FROM Employee e WHERE e.email = ?1", Employee.class, email);
    }

    @Override
    public List<Employee> getByPhoneNumber(String phoneNumber) throws RemoteException {
        return findMany("SELECT e FROM Employee e WHERE e.phoneNumber LIKE ?1", Employee.class, phoneNumber + "%");
    }

    @Override
    public List<Employee> getByPosition(String position) throws RemoteException {
        return findMany("SELECT e FROM Employee e WHERE e.position = ?1", Employee.class, position);
    }

    @Override
    public List<Employee> getByName(String name) throws RemoteException {
        return findMany("SELECT e FROM Employee e WHERE e.name LIKE ?1", Employee.class, name + "%");
    }

    @Override
    public List<Employee> getByCitizenIDNumber(String citizenIDNumber) throws RemoteException {
        return findMany("SELECT e FROM Employee e WHERE e.citizen_id LIKE ?1", Employee.class, citizenIDNumber + "%");
    }

    @Override
    public List<Employee> getByCriteria(String phoneNumber, String name, String employeeId) throws RemoteException {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT e FROM Employee e WHERE 1=1");
        int parameterCase = 0;

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            jpqlBuilder.append(" AND e.phoneNumber LIKE ?1");
            phoneNumber += "%";
            parameterCase += 4;
        }
        if (name != null && !name.isEmpty()) {
            jpqlBuilder.append(" AND e.name LIKE ?2");
            name = "%" + name + "%";
            parameterCase += 2;
        }
        if (employeeId != null && !employeeId.isEmpty()) {
            jpqlBuilder.append(" AND e.id LIKE ?3");
            employeeId += "%";
            parameterCase += 1;
        }

        String jpql = jpqlBuilder.toString();

        return switch (parameterCase) {
            case 1 -> findMany(jpql, Employee.class, employeeId);
            case 2 -> findMany(jpql, Employee.class, name);
            case 3 -> findMany(jpql, Employee.class, name, employeeId);
            case 4 -> findMany(jpql, Employee.class, phoneNumber);
            case 5 -> findMany(jpql, Employee.class, phoneNumber, employeeId);
            case 6 -> findMany(jpql, Employee.class, phoneNumber, name);
            case 7 -> findMany(jpql, Employee.class, phoneNumber, name, employeeId);
            default -> throw new RuntimeException("Invalid arguments");
        };
    }

    @Override
    public List<Employee> getAllWithPagination(int offset, int limit) throws RemoteException {
        return findManyWithPagination("SELECT e FROM Employee e ORDER BY e.position",
                Employee.class, null, offset, limit);
    }

    @Override
    public List<Employee> getAllStillWorkingWithPagination(int offset, int limit) throws RemoteException {
        Map<String, Object> params = Map.of("status", "Đang làm");
        return findManyWithPagination("SELECT e FROM Employee e WHERE e.status = :status ORDER BY e.position", Employee.class, params, offset, limit);
    }

    @Override
    public List<Employee> getByPositionWithPagination(String position, int offset, int limit) throws RemoteException {
        Map<String, Object> params = Map.of("position", "%" + position + "%");
        return findManyWithPagination("SELECT e FROM Employee e WHERE e.position LIKE :position ORDER BY e.status",
                Employee.class, params, offset, limit);
    }

    @Override
    public List<Employee> getByPhoneNumberWithPagination(String phoneNumber, int offset, int limit) throws RemoteException {
        Map<String, Object> params = Map.of("phoneNumber", phoneNumber + "%");
        return findManyWithPagination("SELECT e FROM Employee e WHERE e.phoneNumber LIKE :phoneNumber ORDER BY e.status",
                Employee.class, params, offset, limit);
    }

    @Override
    public List<Employee> getByNameWithPagination(String name, int offset, int limit) throws RemoteException {
        Map<String, Object> params = Map.of("name", "%" + name + "%");
        return findManyWithPagination("SELECT e FROM Employee e WHERE e.name LIKE :name ORDER BY e.status",
                Employee.class, params, offset, limit);
    }

    @Override
    public int countByPhoneNumber(String phoneNumber) throws RemoteException {
        return count("SELECT COUNT(e) FROM Employee e WHERE e.phoneNumber LIKE ?1", phoneNumber + "%");
    }

    @Override
    public int countByName(String name) throws RemoteException {
        return count("SELECT COUNT(e) FROM Employee e WHERE e.name LIKE ?1", "%" + name + "%");
    }

    @Override
    public int countByPosition(String position) throws RemoteException {
        return count("SELECT COUNT(e) FROM Employee e WHERE e.position LIKE ?1", "%" + position + "%");
    }

    @Override
    public int countStillWorking() throws RemoteException {
        return count("SELECT COUNT(e) FROM Employee e WHERE e.status = ?1", "Đang làm");
    }

    @Override
    public int countAll() throws RemoteException {
        return count("SELECT COUNT(e) FROM Employee e");
    }

    @Override
    public boolean add(Employee employee) throws RemoteException {
        return super.add(employee);
    }

    @Override
    public boolean update(Employee employee) throws RemoteException {
        return super.update(employee);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        Employee employee = getOneById(id);
        if (employee == null) {
            return false;
        }
        return super.delete(employee);
    }

    @Override
    public boolean updateStatus(String employeeId, String status) throws RemoteException {
        Employee employee = getOneById(employeeId);
        if (employee == null) return false;
        employee.setStatus(status);
        return update(employee);
    }

    @Override
    public boolean updateEmployeeInfo(Employee employee) throws RemoteException {
        return update(employee);
    }

    @Override
    public boolean updateEmployeeInfoProfile(Employee employee) throws RemoteException {
        Employee employeeInDB = getOneById(employee.getId());
        if (employeeInDB == null) return false;
        employeeInDB.setName(employee.getName());
        employeeInDB.setGender(employee.getGender());
        employeeInDB.setBirthday(employee.getBirthday());
        employeeInDB.setCitizenId(employee.getCitizenId());
        employeeInDB.setPhoneNumber(employee.getPhoneNumber());
        employeeInDB.setEmail(employee.getEmail());
        employeeInDB.setAddress(employee.getAddress());
        employeeInDB.setProfileImage(employee.getProfileImage());
        return update(employeeInDB);
    }

    @Override
    public void updateWorkHour(String id, double hours) throws RemoteException {
        Employee employee = getOneById(id);
        if (employee == null) return;
        employee.setWorkHours(hours);
        update(employee);
    }
}