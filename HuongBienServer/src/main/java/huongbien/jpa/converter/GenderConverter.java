package huongbien.jpa.converter;

import huongbien.entity.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        if (gender == null) return Gender.OTHER.getValue();
        return gender.getValue();
    }

    @Override
    public Gender convertToEntityAttribute(String s) {
        if (s == null) return Gender.OTHER;
        return Gender.fromValue(s);
    }
}
