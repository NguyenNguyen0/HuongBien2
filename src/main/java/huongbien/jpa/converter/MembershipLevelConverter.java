package huongbien.jpa.converter;

import huongbien.entity.MembershipLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MembershipLevelConverter implements AttributeConverter<MembershipLevel, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MembershipLevel attribute) {
        return attribute.getLevel();
    }

    @Override
    public MembershipLevel convertToEntityAttribute(Integer dbData) {
        return MembershipLevel.fromLevel(dbData);
    }
}
