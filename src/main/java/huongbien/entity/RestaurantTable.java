package huongbien.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "tables")
public class RestaurantTable {
    @Id
    @Column(name = "table_id")
    private String id;
    private String name;
    private int seats;
    private int floor;
    private String status;

    @ManyToOne
    private TableType tableType;
}
