package huongbien.dao;

import huongbien.entity.Employee;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class EmployeeDAO extends GenericDAO<Employee> {
    public EmployeeDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Employee> getAll() {
        return findMany("SELECT e FROM Employee e", Employee.class);
    }

    public Employee getOneById(String id) {
        return findOne("SELECT e FROM Employee e WHERE e.id = ?1", Employee.class, id);
    }

    public List<Employee> getManyById(String id) {
        return findMany("SELECT e FROM Employee e WHERE e.id LIKE ?1", Employee.class, id + "%");
    }

    public Employee getByEmail(String email) {
        return findOne("SELECT e FROM Employee e WHERE e.email = ?1", Employee.class, email);
    }

    public List<Employee> getByPhoneNumber(String phoneNumber) {
        return findMany("SELECT e FROM Employee e WHERE e.phoneNumber LIKE ?1", Employee.class, phoneNumber + "%");
    }

    public List<Employee> getByPosition(String position) {
        return findMany("SELECT e FROM Employee e WHERE e.position = ?1", Employee.class, position);
    }

    public List<Employee> getByName(String name) {
        return findMany("SELECT e FROM Employee e WHERE e.name LIKE ?1", Employee.class, name + "%");
    }

    public List<Employee> getByCitizenIDNumber(String citizenIDNumber) {
        return findMany("SELECT e FROM Employee e WHERE e.citizen_id LIKE ?1", Employee.class, citizenIDNumber + "%");
    }

    public List<Employee> getByCriteria(String phoneNumber, String name, String employeeId) {
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

    public List<Employee> getAllWithPagination(int offset, int limit) {
        return findMany("SELECT e FROM Employee e ORDER BY e.position", Employee.class, offset, limit);
    }

    public List<Employee> getAllStillWorkingWithPagination(int offset, int limit) {
        Map<String, Object> params = Map.of("status", "Đang làm");
        return findManyWithPagination("SELECT e FROM Employee e WHERE e.status = :status ORDER BY e.position", Employee.class, params, offset, limit);
    }

    public List<Employee> getByPositionWithPagination(String position, int offset, int limit) {
        return findMany("SELECT e FROM Employee e WHERE e.position LIKE ?1 ORDER BY e.status", Employee.class, offset, limit, "%" + position + "%");
    }

    public List<Employee> getByPhoneNumberWithPagination(String phoneNumber, int offset, int limit) {
        return findMany("SELECT e FROM Employee e WHERE e.phoneNumber LIKE ?1 ORDER BY e.status", Employee.class, offset, limit, phoneNumber + "%");
    }

    public List<Employee> getByNameWithPagination(String name, int offset, int limit) {
        return findMany("SELECT e FROM Employee e WHERE e.name LIKE ?1 ORDER BY e.status", Employee.class, offset, limit, "%" + name + "%");
    }

    public int countByPhoneNumber(String phoneNumber) {
        return count("SELECT COUNT(e) FROM Employee e WHERE e.phoneNumber LIKE ?1", phoneNumber + "%");
    }

    public int countByName(String name) {
        return count("SELECT COUNT(e) FROM Employee e WHERE e.name LIKE ?1", "%" + name + "%");
    }

    public int countByPosition(String position) {
        return count("SELECT COUNT(e) FROM Employee e WHERE e.position LIKE ?1", "%" + position + "%");
    }

    public int countStillWorking() {
        return count("SELECT COUNT(e) FROM Employee e WHERE e.status = ?1", "Đang làm");
    }

    public int countAll() {
        return count("SELECT COUNT(e) FROM Employee e");
    }

    public boolean updateStatus(String employeeId, String status) {
        Employee employee = getOneById(employeeId);
        if (employee == null) return false;
        employee.setStatus(status);
        return update(employee);
    }

    public boolean updateEmployeeInfo(Employee employee) {
        return update(employee);
    }

    public boolean updateEmployeeInfoProfile(Employee employee) {
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

    public void updateWorkHour(String id, double v) {
        Employee employee = getOneById(id);
        if (employee == null) return;
        employee.setWorkHours(v);
        update(employee);
    }
}