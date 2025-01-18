package huongbien.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TableType tableType = (TableType) o;
        return Objects.equals(id, tableType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
