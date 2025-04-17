package huongbien.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import huongbien.config.Constant;
import huongbien.util.Util;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@ToString
@Entity
@NoArgsConstructor
@jakarta.persistence.Table(name = "orders")
public class Order implements Serializable {
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
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderDetail> orderDetails;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "orders_tables",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    private List<Table> tables;

    public Order(String notes, Employee employee, Customer customer, Payment payment, Promotion promotion, ArrayList<OrderDetail> orderDetails, ArrayList<Table> tables) {
        this.id = generateId(null, null);
        this.orderDate = LocalDate.now();
        this.orderTime = LocalTime.now();
        this.notes = notes;
        this.employee = employee;
        this.customer = customer;
        this.payment = payment;
        this.promotion = promotion;
        this.orderDetails = orderDetails;
        this.tables = tables;
    }

    public static String generateId(LocalDate orderDate, LocalTime orderTime) {
        LocalDate currentDate = orderDate == null ? LocalDate.now() : orderDate;
        LocalTime currentTime = orderTime == null ? LocalTime.now() : orderTime;
        return String.format("HD%02d%02d%02d%02d%02d%02d%03d",
                currentDate.getYear() % 100,
                currentDate.getMonthValue(),
                currentDate.getDayOfMonth(),
                currentTime.getHour(),
                currentTime.getMinute(),
                currentTime.getSecond(),
                Util.randomNumber(1, 999)
        );
    }

    public double calculateTotalAmount() {
        return Math.round(orderDetails
                .stream()
                .map((OrderDetail::calculateSubTotal))
                .reduce(0.0, Double::sum));
    }

    public double calculateReducedAmount() {
        double discount = (promotion == null ? 0 : promotion.getDiscount());
        return Math.round(calculateTotalAmount() * discount);
    }

    public double calculateVatTaxAmount() {
        return Math.round((calculateTotalAmount() - calculateReducedAmount()) * vatTax);
    }

    public double calculateGrandTotal() {
        double reducedAmount = calculateReducedAmount();
        return Math.round((calculateTotalAmount() - reducedAmount) + calculateVatTaxAmount());
    }

    public void setPayment(Payment payment) {
        if (payment == null) {
            return;
        }
        this.payment = payment;
        payment.setOrder(this);
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        if (orderDetails == null) {
            return;
        }
        this.orderDetails = orderDetails;
        orderDetails.forEach(orderDetail -> orderDetail.setOrder(this));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
