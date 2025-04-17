package huongbien.jpa.converter;

import huongbien.entity.CuisineStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CuisineStatusConverter implements AttributeConverter<CuisineStatus, String> {

    @Override
    public String convertToDatabaseColumn(CuisineStatus status) {
        if (status == null) {
            return null;
        }
        return status.getStatus();
    }

    @Override
    public CuisineStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (CuisineStatus status : CuisineStatus.values()) {
            if (status.getStatus().equals(dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + dbData);
    }
}
