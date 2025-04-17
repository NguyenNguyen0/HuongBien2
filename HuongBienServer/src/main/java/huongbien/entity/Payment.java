package huongbien.entity;

import huongbien.jpa.converter.PaymentMethodConverter;
import huongbien.util.Util;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "payment_id")
    private String paymentId;
    private double amount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_time")
    private LocalTime paymentTime;

    @Convert(converter = PaymentMethodConverter.class)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @ToString.Exclude
    @OneToOne(mappedBy = "payment")
    private Reservation reservation;

    @ToString.Exclude
    @OneToOne(mappedBy = "payment")
    private Order order;

    public Payment(String paymentId, double amount, LocalDate paymentDate, LocalTime paymentTime, PaymentMethod paymentMethod) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.paymentMethod = paymentMethod;
    }

    public Payment(double moneyFromCustomer, PaymentMethod paymentMethod) {
        this.amount = moneyFromCustomer;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDate.now();
        this.paymentTime = LocalTime.now();
        this.paymentId = generateId(paymentDate, paymentTime);
    }

    public static String generateId(LocalDate paymentDate, LocalTime paymentTime) {
        LocalDate currentDate = paymentDate == null ? LocalDate.now() : paymentDate;
        LocalTime currentTime = paymentTime == null ? LocalTime.now() : paymentTime;
        return String.format("TT%02d%02d%02d%02d%02d%02d%03d",
                currentDate.getYear() % 100,
                currentDate.getMonthValue(),
                currentDate.getDayOfMonth(),
                currentTime.getHour(),
                currentTime.getMinute(),
                currentTime.getSecond(),
                Util.randomNumber(1, 999)
        );
    }
}
