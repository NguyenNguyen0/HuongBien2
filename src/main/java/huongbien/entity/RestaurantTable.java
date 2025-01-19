package huongbien.entity;

import huongbien.jpa.converter.TableStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tables")
public class RestaurantTable {
    @Id
    @Column(name = "table_id")
    private String id;
    private String name;
    private int seats;
    private int floor;

    @Convert(converter = TableStatusConverter.class)
    private TableStatus status;

    @ManyToOne
    private TableType tableType;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTable that = (RestaurantTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
