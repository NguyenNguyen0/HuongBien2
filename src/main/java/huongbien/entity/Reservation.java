package huongbien.entity;

import huongbien.jpa.converter.ReservationStatusConverter;
import huongbien.util.Util;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
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

    @Convert(converter = ReservationStatusConverter.class)
    private ReservationStatus status;
    private double deposit;

    @Column(name = "refund_deposit")
    private double refundDeposit;
    private String note;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Customer customer;

    @ToString.Exclude
    @OneToMany(mappedBy = "reservation", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FoodOrder> foodOrders;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "reservations_tables",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    @ToString.Exclude
    private List<RestaurantTable> tables;

    public static String generateId(LocalDate localDate, LocalTime localTime) {
        LocalDate currentDate = localDate == null ? LocalDate.now() : localDate;
        LocalTime currentTime = localTime == null ? LocalTime.now() : localTime;
        return String.format("DB%02d%02d%02d%02d%02d%02d%03d",
                currentDate.getYear() % 100,
                currentDate.getMonthValue(),
                currentDate.getDayOfMonth(),
                currentTime.getHour(),
                currentTime.getMinute(),
                currentTime.getSecond(),
                Util.randomNumber(1, 999)
        );
    }

    public void setPayment(Payment payment) {
        if (payment == null) {
            return;
        }
        this.payment = payment;
        payment.setReservation(this);
    }

    public void setFoodOrders(List<FoodOrder> foodOrders) {
        if (foodOrders == null) {
            return;
        }
        this.foodOrders = foodOrders;
        foodOrders.forEach(foodOrder -> foodOrder.setReservation(this));
    }
}
