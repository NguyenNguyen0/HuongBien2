package huongbien.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
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
    private boolean gender;
    private String status;
    private String role;
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
    private Employee manager;

    @OneToMany(mappedBy = "manager", targetEntity = Employee.class)
    private List<Employee> subordinates;

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
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", hireDate=" + hireDate +
                ", position='" + position + '\'' +
                ", workHours=" + workHours +
                ", hourlyPay=" + hourlyPay +
                ", salary=" + salary +
                ", profileImage=" + (profileImage == null ? "null" : String.format("byte[%d]", profileImage.length)) +
                ", manager=" + manager +
                '}';
    }
}
