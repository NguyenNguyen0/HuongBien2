package huongbien.entity;

import huongbien.util.Util;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Setter
@Getter
@ToString
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @Column(name = "order_detail_id")
    private String id;
    private int quantity;
    private String note;

    @Column(name = "sale_price")
    private double salePrice;

    @ManyToOne
    private Cuisine cuisine;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public double calculateSubTotal() {
        return salePrice * quantity;
    }

    public static String generateId(String orderId) {
        return String.format("%sCT%03d", orderId, Util.randomNumber(1, 999));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
