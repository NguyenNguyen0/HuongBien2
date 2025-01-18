package huongbien.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import huongbien.jpa.converter.MembershipLevelConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    @Column(name = "promotion_id")
    private String id;
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
    private double discount;
    private String description;

    @Column(name = "minimum_order_amount")
    private double minimumOrderAmount;

    @Convert(converter = MembershipLevelConverter.class)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel;

    @JsonProperty("isAvailable")
    @Column(name = "is_available")
    private boolean isAvailable;
}
