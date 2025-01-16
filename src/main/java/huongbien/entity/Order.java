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
    @Column(name = "vat_tax")
    private final double vatTax = Constant.VAT.getValue();
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

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Promotion promotion;

    @OneToOne
    private Payment payment;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "order_table",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    @ToString.Exclude
    private List<RestaurantTable> tables;
}
