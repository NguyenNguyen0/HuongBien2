package huongbien.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import huongbien.util.Util;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail implements Serializable {
    @Id
    @Column(name = "order_detail_id")
    private String id;
    private int quantity;
    private String note;

    @Column(name = "sale_price")
    private double salePrice;

    @ManyToOne
    private Cuisine cuisine;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderDetail(String id, int quantity, String note, double money, Cuisine cuisine) {
        if (id != null) {
            setId(id);
        }
        this.quantity = quantity;
        this.note = note;
        this.salePrice = money;
        this.cuisine = cuisine;
    }

    public static String generateId(String orderId) {
        return String.format("%sCT%03d", orderId, Util.randomNumber(1, 999));
    }

    public double calculateSubTotal() {
        return salePrice * quantity;
    }

    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            this.id = generateId(order.getId());
        } else {
            this.id = generateId(id);
        }
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
