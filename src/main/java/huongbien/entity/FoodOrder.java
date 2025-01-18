package huongbien.entity;

import huongbien.util.Util;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food_orders")
public class FoodOrder {
    @Id
    @Column(name = "order_id")
    private String id;
    private int quantity;
    private String note;

    @Column(name = "sale_price")
    private double salePrice;

    @ManyToOne
    private Cuisine cuisine;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public static String generateId(String foodOrderId) {
        if (foodOrderId != null && foodOrderId.length() == 17) {
            return String.format("%sDM%03d", foodOrderId, Util.randomNumber(1, 999));
        }
        if (foodOrderId == null || !foodOrderId.matches("^DB\\d{15}DM\\d{3}$")) {
            throw new IllegalArgumentException("Invalid food order ID format");
        }
        return foodOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FoodOrder foodOrder = (FoodOrder) o;
        return Objects.equals(id, foodOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
