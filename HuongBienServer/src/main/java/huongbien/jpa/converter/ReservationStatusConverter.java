package huongbien.jpa.converter;

import huongbien.entity.ReservationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReservationStatusConverter implements AttributeConverter<ReservationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ReservationStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(String dbData) {
        return ReservationStatus.fromStatus(dbData);
    }
}
