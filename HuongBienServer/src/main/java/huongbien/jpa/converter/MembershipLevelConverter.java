package huongbien.jpa.converter;

import huongbien.entity.MembershipLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MembershipLevelConverter implements AttributeConverter<MembershipLevel, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MembershipLevel attribute) {
        if (attribute == null) {
            return MembershipLevel.BRONZE.getLevel();
        }
        return attribute.getLevel();
    }

    @Override
    public MembershipLevel convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return MembershipLevel.BRONZE;
        }
        return MembershipLevel.fromLevel(dbData);
    }
}
