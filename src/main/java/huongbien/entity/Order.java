package huongbien.entity;

import huongbien.config.Constant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    private String id;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "order_time")
    private LocalTime orderTime;
    private String notes;

    @Column(name = "payment_amount")
    private double paymentAmount;

    @Column(name = "dispensed_amount")
    private double dispensedAmount;

    @Column(name = "total_amount")
    private double totalAmount;
    private double discount;

    @Column(name = "vat_tax")
    private final double vatTax = Constant.VAT.getValue();

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Promotion promotion;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "orders_tables",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    @ToString.Exclude
    private List<RestaurantTable> tables;
}
