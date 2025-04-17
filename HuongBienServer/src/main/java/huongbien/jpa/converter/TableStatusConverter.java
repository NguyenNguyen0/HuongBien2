package huongbien.jpa.converter;

import huongbien.entity.TableStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TableStatusConverter implements AttributeConverter<TableStatus, String> {

    @Override
    public String convertToDatabaseColumn(TableStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public TableStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (TableStatus status : TableStatus.values()) {
            if (status.getValue().equals(dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + dbData);
    }
}
