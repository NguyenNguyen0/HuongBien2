package huongbien.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @Column(name = "reservation_id")
    private String id;

    @Column(name = "party_type")
    private String partyType;

    @Column(name = "party_size")
    private int partySize;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Column(name = "reservation_time")
    private LocalTime reservationTime;

    @Column(name = "receive_date")
    private LocalDate receiveDate;

    @Column(name = "receive_time")
    private LocalTime receiveTime;
    private String status;
    private double deposit;

    @Column(name = "refund_deposit")
    private double refundDeposit;
    private String note;

    @OneToOne
    private Payment payment;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.PERSIST)
    private List<FoodOrder> foodOrders;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "reservation_table",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    @ToString.Exclude
    private List<RestaurantTable> tables;

    public void addFoodOrder(FoodOrder foodOrder) {
        if (foodOrders == null) {
            foodOrders = new ArrayList<>();
        }
        foodOrders.add(foodOrder);
        foodOrder.setReservation(this);
    }
}
