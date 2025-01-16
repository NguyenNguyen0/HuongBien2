package huongbien.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@Entity
@Table(name = "table_types")
public class TableType {
    @Id
    @Column(name = "table_type_id")
    private String id;
    private String name;
    private String description;

    @ToString.Exclude
    @OneToMany(mappedBy = "tableType")
    private List<RestaurantTable> tables;
}
