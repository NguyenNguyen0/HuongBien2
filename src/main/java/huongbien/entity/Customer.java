package huongbien.entity;

import huongbien.util.Util;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Customer {
    @Id
    private String id;
    private String name;
    private String address;
    private int gender;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private LocalDate registrationDate;
    private int accumulatedPoints;
    private int membershipLevel;

    public Customer() {
    }

    public Customer(
            String id, String name, String address, int gender,
            String phoneNumber, String email, LocalDate birthday,
            LocalDate registrationDate, int accumulatedPoints, int membershipLevel
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
        this.accumulatedPoints = accumulatedPoints;
        this.membershipLevel = membershipLevel;
    }

    public static String randomId() {
        LocalDate now = LocalDate.now();
        return String.format("KH%02d%02d%02d%03d",
                now.getYear() % 100,
                now.getMonthValue(),
                now.getDayOfMonth(),
                Util.randomNumber(1, 999)
        );
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", registrationDate=" + registrationDate +
                ", accumulatedPoints=" + accumulatedPoints +
                ", membershipLevel=" + membershipLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
