package huongbien.entity;

import huongbien.jpa.converter.GenderConverter;
import huongbien.util.Util;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @Column(name = "employee_id")
    private String id;
    private String name;
    private String address;

    @Column(name = "citizen_id")
    private String citizenId;

    @Column(name = "phone_number")
    private String phoneNumber;
    private LocalDate birthday;

    @Convert(converter = GenderConverter.class)
    private Gender gender;
    private String status;
    private String email;

    @Column(name = "hire_date")
    private LocalDate hireDate;
    private String position;

    @Column(name = "work_hours")
    private double workHours;

    @Column(name = "hourly_pay")
    private double hourlyPay;
    private double salary;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager", targetEntity = Employee.class)
    private List<Employee> subordinates;

    public Employee(String id, String name, String address, String citizenId, String phoneNumber, LocalDate birthday, Gender gender, String status, String email, LocalDate hireDate, String position, double workHours, double hourlyPay, double salary, byte[] profileImage, Employee manager) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.citizenId = citizenId;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
        this.status = status;
        this.email = email;
        this.hireDate = hireDate;
        this.position = position;
        this.workHours = workHours;
        this.hourlyPay = hourlyPay;
        this.salary = salary;
        this.profileImage = profileImage;
        this.manager = manager;
    }

    public Employee(String employeeId, String name, String phone, String citizenId, int gender, String address, LocalDate birthDate, String email, String status, LocalDate hireDate, String position, double workHours, double hourPay, double salary, Employee managerId, byte[] profileImage) {
        this.id = employeeId;
        this.name = name;
        this.phoneNumber = phone;
        this.citizenId = citizenId;
        this.gender = gender == 1 ? Gender.MALE : gender == 2 ? Gender.FEMALE : Gender.OTHER;
        this.address = address;
        this.birthday = birthDate;
        this.email = email;
        this.status = status;
        this.hireDate = hireDate;
        this.position = position;
        this.workHours = workHours;
        this.hourlyPay = hourPay;
        this.salary = salary;
        this.manager = managerId;
        this.profileImage = profileImage;
    }

    public static String generateId(LocalDate hireDate) {
        if (hireDate == null) {
            hireDate = LocalDate.now();
        }
        return String.format("NV%02d%02d%02d%03d",
                hireDate.getYear() % 100,
                hireDate.getMonthValue(),
                hireDate.getDayOfMonth(),
                Util.randomNumber(1, 999)
        );
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", citizenId='" + citizenId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", status='" + status + '\'' +
                ", email='" + email + '\'' +
                ", hireDate=" + hireDate +
                ", position='" + position + '\'' +
                ", workHours=" + workHours +
                ", hourlyPay=" + hourlyPay +
                ", salary=" + salary +
                ", profileImage=" + (profileImage == null ? "null" : String.format("byte[%d]", profileImage.length)) +
                ", manager=" + (manager == null ? null : manager.getId()) +
                '}';
    }
}
