package huongbien.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String status;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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
}
