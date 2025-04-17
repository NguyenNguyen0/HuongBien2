package huongbien.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@jakarta.persistence.Table(name = "table_types")
public class TableType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "table_type_id")
    private String id;
    private String name;
    private String description;

    @ToString.Exclude
    @OneToMany(mappedBy = "tableType")
    private List<Table> tables;

    public TableType(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

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
