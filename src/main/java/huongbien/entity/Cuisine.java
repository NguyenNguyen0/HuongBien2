package huongbien.entity;

import huongbien.jpa.converter.CuisineStatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuisines")
public class Cuisine {
    @Id
    @Column(name = "cuisine_id")
    private String id;
    private String name;
    private String description;
    private double price;

    @Convert(converter = CuisineStatusConverter.class)
    private CuisineStatus status;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Cuisine(String id, String name, String description, double price, CuisineStatus status, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Cuisine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", image=" + (image == null ? "null" : String.format("byte[%d]", image.length)) +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cuisine cuisine = (Cuisine) o;
        return Objects.equals(id, cuisine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
