package huongbien.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table
public class OrderDetail {
    @Id
    private String id;
    private int quantity;
    private String note;

    @Column(name = "sale_price")
    private double salePrice;

    @ManyToOne
    private Cuisine cuisine;

    @ToString.Exclude
    @ManyToOne
    private Order order;
}
