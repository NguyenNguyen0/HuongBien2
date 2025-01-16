package huongbien.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
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
    private Reservation reservation;
}
