package huongbien2.entity;

import java.time.LocalDate;
import java.util.Objects;

import huongbien2.util.Util;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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

    public Customer() {}

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setAccumulatedPoints(int accumulatedPoints) {
        this.accumulatedPoints = accumulatedPoints;
    }

    public void setMembershipLevel(int membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public int getAccumulatedPoints() {
        return accumulatedPoints;
    }

    public int getMembershipLevel() {
        return membershipLevel;
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
