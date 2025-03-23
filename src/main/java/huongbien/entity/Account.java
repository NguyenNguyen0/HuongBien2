package huongbien.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    private String username;

    @ToString.Exclude
    @OneToOne
    @MapsId
    @JoinColumn(name = "username")
    private Employee employee;

    private String hashcode;
    private String role;
    private String email;

    @Column(name = "is_active")
    private boolean isActive;

    public Account(String username, String hashcode, String role, String email, boolean isActive) {
        this.username = username;
        this.hashcode = hashcode;
        this.role = role;
        this.email = email;
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(username, account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
