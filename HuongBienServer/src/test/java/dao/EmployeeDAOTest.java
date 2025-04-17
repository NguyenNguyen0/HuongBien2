package dao;

import huongbien.dao.impl.EmployeeDAO;
import huongbien.entity.Employee;
import huongbien.entity.Gender;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    private EmployeeDAO employeeDAO;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private String testEmployeeId;
    private String testPhoneNumber;
    private String testEmployeeName;
    private String testPosition;

    @BeforeEach
    void setUp() throws RemoteException {
        // Initialize with persistence unit
        employeeDAO = new EmployeeDAO(PersistenceUnit.MARIADB_JPA);

        // Get entity manager for test operations
        entityManager = JPAUtil.getEntityManager();
        transaction = entityManager.getTransaction();

        // Create unique test employee data
        testEmployeeId = "EMP-" + UUID.randomUUID().toString().substring(0, 8);
        testPhoneNumber = "0" + (900000000 + (int)(Math.random() * 99999999));
        testEmployeeName = "Test Employee " + UUID.randomUUID().toString().substring(0, 8);
        testPosition = "Waiter";

        // Setup test employee
        setupTestEmployee();
    }

    private void setupTestEmployee() {
        Employee testEmployee = new Employee();
        testEmployee.setId(testEmployeeId);
        testEmployee.setName(testEmployeeName);
        testEmployee.setPhoneNumber(testPhoneNumber);
        testEmployee.setPosition(testPosition);
        testEmployee.setStatus("Đang làm");
        testEmployee.setEmail("test@example.com");
        testEmployee.setAddress("Test Address");
        testEmployee.setBirthday(LocalDate.of(1990, 1, 1));
        testEmployee.setGender(Gender.MALE);
        testEmployee.setCitizenId("123456789012");
        testEmployee.setWorkHours(40.0);
        testEmployee.setSalary(10000000.0);

        // Persist in database
        transaction.begin();
        entityManager.persist(testEmployee);
        transaction.commit();
    }

    @Test
    void testGetAll() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getAll();

        // Assert
        assertNotNull(employees);
        assertTrue(employees.size() > 0);
        assertTrue(employees.stream()
                .anyMatch(e -> e.getId().equals(testEmployeeId) && e.getName().equals(testEmployeeName)));
    }

    @Test
    void testGetOneById() throws RemoteException {
        // Act
        Employee employee = employeeDAO.getOneById(testEmployeeId);

        // Assert
        assertNotNull(employee);
        assertEquals(testEmployeeId, employee.getId());
        assertEquals(testEmployeeName, employee.getName());
    }

    @Test
    void testGetManyById() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getManyById(testEmployeeId.substring(0, 6));

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetByEmail() throws RemoteException {
        // Act
        Employee employee = employeeDAO.getByEmail("test@example.com");

        // Assert
        assertNotNull(employee);
        assertEquals(testEmployeeId, employee.getId());
    }

    @Test
    void testGetByPhoneNumber() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getByPhoneNumber(testPhoneNumber.substring(0, 5));

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetByPosition() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getByPosition(testPosition);

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetByName() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getByName(testEmployeeName.substring(5, 10));

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetByCitizenIDNumber() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getByCitizenIDNumber("123456");

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetByCriteria() throws RemoteException {
        // Act - search by name and employee id
        List<Employee> employees = employeeDAO.getByCriteria(null, testEmployeeName.substring(5, 10), testEmployeeId.substring(0, 6));

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetAllWithPagination() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getAllWithPagination(0, 10);

        // Assert
        assertNotNull(employees);
        assertTrue(employees.size() > 0);
    }

    @Test
    void testGetAllStillWorkingWithPagination() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getAllStillWorkingWithPagination(0, 10);

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetByPositionWithPagination() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getByPositionWithPagination(testPosition, 0, 10);

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetByPhoneNumberWithPagination() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getByPhoneNumberWithPagination(testPhoneNumber.substring(0, 5), 0, 10);

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testGetByNameWithPagination() throws RemoteException {
        // Act
        List<Employee> employees = employeeDAO.getByNameWithPagination(testEmployeeName.substring(5, 10), 0, 10);

        // Assert
        assertNotNull(employees);
        assertTrue(employees.stream().anyMatch(e -> e.getId().equals(testEmployeeId)));
    }

    @Test
    void testCountByPhoneNumber() throws RemoteException {
        // Act
        int count = employeeDAO.countByPhoneNumber(testPhoneNumber.substring(0, 5));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountByName() throws RemoteException {
        // Act
        int count = employeeDAO.countByName(testEmployeeName.substring(5, 10));

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountByPosition() throws RemoteException {
        // Act
        int count = employeeDAO.countByPosition(testPosition);

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountStillWorking() throws RemoteException {
        // Act
        int count = employeeDAO.countStillWorking();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testCountAll() throws RemoteException {
        // Act
        int count = employeeDAO.countAll();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testUpdateStatus() throws RemoteException {
        // Act
        boolean result = employeeDAO.updateStatus(testEmployeeId, "Nghỉ việc");

        // Assert
        assertTrue(result);
        
        // Verify status was updated
        Employee updatedEmployee = employeeDAO.getOneById(testEmployeeId);
        assertEquals("Nghỉ việc", updatedEmployee.getStatus());
    }

    @Test
    void testUpdateEmployeeInfo() throws RemoteException {
        // Arrange
        Employee employee = employeeDAO.getOneById(testEmployeeId);
        String updatedAddress = "Updated address " + UUID.randomUUID().toString();
        employee.setAddress(updatedAddress);

        // Act
        boolean result = employeeDAO.updateEmployeeInfo(employee);

        // Assert
        assertTrue(result);

        // Verify employee was updated
        Employee updatedEmployee = employeeDAO.getOneById(testEmployeeId);
        assertEquals(updatedAddress, updatedEmployee.getAddress());
    }

    @Test
    void testUpdateEmployeeInfoProfile() throws RemoteException {
        // Arrange
        Employee employee = employeeDAO.getOneById(testEmployeeId);
        String updatedName = "Updated Name " + UUID.randomUUID().toString();
        employee.setName(updatedName);

        // Act
        boolean result = employeeDAO.updateEmployeeInfoProfile(employee);

        // Assert
        assertTrue(result);

        // Verify employee profile was updated
        Employee updatedEmployee = employeeDAO.getOneById(testEmployeeId);
        assertEquals(updatedName, updatedEmployee.getName());
    }

    @Test
    void testUpdateWorkHour() throws RemoteException {
        // Arrange
        double newWorkHours = 45.5;

        // Act
        employeeDAO.updateWorkHour(testEmployeeId, newWorkHours);

        // Verify work hours were updated
        Employee updatedEmployee = employeeDAO.getOneById(testEmployeeId);
        assertEquals(newWorkHours, updatedEmployee.getWorkHours());
    }

    @Test
    void testAddEmployee() throws RemoteException {
        // Arrange
        String newEmployeeId = "EMP-" + UUID.randomUUID().toString().substring(0, 8);
        String newPhoneNumber = "0" + (900000000 + (int)(Math.random() * 99999999));
        String newEmployeeName = "New Employee " + UUID.randomUUID().toString().substring(0, 8);

        Employee newEmployee = new Employee();
        newEmployee.setId(newEmployeeId);
        newEmployee.setName(newEmployeeName);
        newEmployee.setPhoneNumber(newPhoneNumber);
        newEmployee.setPosition("Chef");
        newEmployee.setStatus("Đang làm");
        newEmployee.setEmail("new@example.com");
        newEmployee.setAddress("New Address");
        newEmployee.setBirthday(LocalDate.of(1995, 5, 5));
        newEmployee.setGender(Gender.FEMALE);
        newEmployee.setCitizenId("098765432109");
        newEmployee.setWorkHours(40.0);
        newEmployee.setSalary(12000000.0);

        // Act
        boolean result = employeeDAO.add(newEmployee);

        // Assert
        assertTrue(result);

        // Verify employee was added
        Employee addedEmployee = employeeDAO.getOneById(newEmployeeId);
        assertNotNull(addedEmployee);
        assertEquals(newEmployeeName, addedEmployee.getName());
    }

    @Test
    void testDeleteEmployee() throws RemoteException {
        // Arrange
        // Create a temporary employee to delete
        String tempEmployeeId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
        String tempPhoneNumber = "0" + (900000000 + (int)(Math.random() * 99999999));
        
        Employee tempEmployee = new Employee();
        tempEmployee.setId(tempEmployeeId);
        tempEmployee.setName("Temporary Employee");
        tempEmployee.setPhoneNumber(tempPhoneNumber);
        tempEmployee.setPosition("Temporary");
        tempEmployee.setStatus("Đang làm");
        tempEmployee.setEmail("temp@example.com");

        // Add the employee first
        transaction.begin();
        entityManager.persist(tempEmployee);
        transaction.commit();

        // Verify it exists
        Employee addedEmployee = employeeDAO.getOneById(tempEmployeeId);
        assertNotNull(addedEmployee);

        // Act
        boolean result = employeeDAO.delete(addedEmployee);

        // Assert
        assertTrue(result);

        // Verify employee was deleted
        Employee deletedEmployee = employeeDAO.getOneById(tempEmployeeId);
        assertNull(deletedEmployee);
    }
}