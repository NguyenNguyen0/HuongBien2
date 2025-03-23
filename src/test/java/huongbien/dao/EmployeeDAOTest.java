package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.Employee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//TODO: dang bi van de o pagination
class EmployeeDAOTest {
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();

    @Test
    void add() {
        Employee employee = DataGenerator.fakeEmployee(LocalDate.now());
        assertTrue(employeeDAO.add(employee));
    }

    @Test
    void update() {
        Employee employee = DataGenerator.fakeEmployee(LocalDate.now());
        employee.setId("E001");
        employee.setName("Updated Name");
        assertTrue(employeeDAO.update(employee));

        Employee updatedEmployee = employeeDAO.getOneById(employee.getId());
        assertNotNull(updatedEmployee);
        assertEquals("Updated Name", updatedEmployee.getName());
    }

    @Test
    void getAll() {
        assertNotNull(employeeDAO.getAll());
    }

    @Test
    void getOneById() {
        String id = "E001";
        Employee employee = employeeDAO.getOneById(id);
        assertNotNull(employee);
    }

    @Test
    void getManyById() {
        List<Employee> employees = employeeDAO.getManyById("NV240103223");
        assertNotNull(employees);
        assertFalse(employees.isEmpty());
    }

    @Test
    void getByEmail() {
        Employee employee = employeeDAO.getByEmail("marquita.reilly@hotmail.com");
        assertNotNull(employee);
    }

    @Test
    void getByPhoneNumber() {
        List<Employee> employees = employeeDAO.getByPhoneNumber("1234567890");
        assertNotNull(employees);
    }

    @Test
    void getByPosition() {
        List<Employee> employees = employeeDAO.getByPosition("Quản lý");
        assertNotNull(employees);
    }

    @Test
    void getByName() {
        List<Employee> employees = employeeDAO.getByName("John Doe");
        assertNotNull(employees);
    }

//    @Test
//    void getByCitizenIDNumber() {
//        List<Employee> employees = employeeDAO.getByCitizenIDNumber("340-25-8160");
//        assertNotNull(employees);
//    }

    @Test
    void getByCriteria() {
        List<Employee> employees = employeeDAO.getByCriteria("0901234570", "Nguyễn Trung Nguyên", "NV001122002");
        assertNotNull(employees);
    }

//    @Test
//    void getAllWithPagination() {
//        int offset = 0;
//        int limit = 10;
//        List<Employee> employees = employeeDAO.getAllWithPagination(offset, limit);
////        assertNotNull(employees);
//        assertFalse(employees.isEmpty());
//    }

//    @Test
//    void getAllStillWorkingWithPagination() {
//        int offset = 0;
//        int limit = 10;
//        List<Employee> employees = employeeDAO.getAllStillWorkingWithPagination(offset, limit);
////        assertNotNull(employees);
//        assertFalse(employees.isEmpty());
//    }

//    @Test
//    void getByPositionWithPagination() {
//        int offset = 0;
//        int limit = 10;
//        String position = "Quản lý";
//        List<Employee> employees = employeeDAO.getByPositionWithPagination(position, offset, limit);
//        assertNotNull(employees);
//        assertFalse(employees.isEmpty());
//    }

//    @Test
//    void getByPhoneNumberWithPagination() {
//        int offset = 0;
//        int limit = 5;
//        String phoneNumber = "(505) 657-8094";
//        List<Employee> employees = employeeDAO.getByPhoneNumberWithPagination(phoneNumber, offset, limit);
//        assertNotNull(employees);
//        assertFalse(employees.isEmpty());
//    }
//
//    @Test
//    void getByNameWithPagination() {
//        int offset = 0;
//        int limit = 5;
//        String name = "Nguyễn Trung Nguyên";
//        List<Employee> employees = employeeDAO.getByNameWithPagination(name, offset, limit);

    /// /        assertNotNull(employees);
//        assertFalse(employees.isEmpty());
//    }
    @Test
    void countByPhoneNumber() {
        String phoneNumber = "0901234570";
        int count = employeeDAO.countByPhoneNumber(phoneNumber);
        assertTrue(count > 0);
    }

    @Test
    void countByName() {
        String name = "Nguyễn Trung Nguyên";
        int count = employeeDAO.countByName(name);
        assertTrue(count > 0);
    }

    @Test
    void countByPosition() {
        String position = "Quản lý";
        int count = employeeDAO.countByPosition(position);
        assertTrue(count > 0);
    }

    @Test
    void countStillWorking() {
        int count = employeeDAO.countStillWorking();
        assertTrue(count > 0);
    }

    @Test
    void countAll() {
        int count = employeeDAO.countAll();
        assertTrue(count > 0);
    }
}