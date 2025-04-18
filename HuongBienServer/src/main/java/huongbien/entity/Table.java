package huongbien.entity;

import huongbien.jpa.converter.TableStatusConverter;
import huongbien.util.Utils;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@jakarta.persistence.Table(name = "tables")
public class Table implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "table_id")
    private String id;
    private String name;
    private int seats;
    private int floor;

    @Convert(converter = TableStatusConverter.class)
    private TableStatus status;

    @ManyToOne
    @JoinColumn(name = "table_type_id")
    private TableType tableType;

    public Table(String name, int seats, int floor, String status, TableType selectedTableType) {
        this.id = generateId(floor);
        this.name = name;
        this.seats = seats;
        this.floor = floor;
        this.status = TableStatus.valueOf(status);
        this.tableType = selectedTableType;
    }

    public static String generateId(int floor) {
        return String.format("T%01dB%03d", floor, Utils.randomNumber(1, 999));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Table that = (Table) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
