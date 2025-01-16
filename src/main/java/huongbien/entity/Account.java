package huongbien.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
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
    private String isActive;
}
