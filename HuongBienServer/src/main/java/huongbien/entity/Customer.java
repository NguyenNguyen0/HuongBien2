package huongbien.entity;

import huongbien.jpa.converter.GenderConverter;
import huongbien.jpa.converter.MembershipLevelConverter;
import huongbien.util.Util;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "customer_id")
    private String id;
    private String name;
    private String address;

    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    private LocalDate birthday;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "accumulated_points")
    private int accumulatedPoints;

    @Convert(converter = MembershipLevelConverter.class)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel;

    public Customer(String name, String address, Gender gender, String phone, String email, LocalDate birthday) {
        setId(generateId(LocalDate.now()));
        setName(name);
        setAddress(address);
        setGender(gender);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setBirthday(birthday);
        setRegistrationDate(LocalDate.now());
        setAccumulatedPoints(0);
        setMembershipLevel(MembershipLevel.BRONZE);
    }

    public static String generateId(LocalDate registrationDate) {
        if (registrationDate == null) {
            registrationDate = LocalDate.now();

        }
        return String.format("KH%02d%02d%02d%03d",
                registrationDate.getYear() % 100,
                registrationDate.getMonthValue(),
                registrationDate.getDayOfMonth(),
                Util.randomNumber(1, 999)
        );
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
