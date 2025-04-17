package huongbien.jpa.converter;

import huongbien.entity.PaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {

    @Override
    public String convertToDatabaseColumn(PaymentMethod attribute) {
        return attribute.getValue();
    }

    @Override
    public PaymentMethod convertToEntityAttribute(String dbData) {
        return PaymentMethod.fromValue(dbData);
    }
}
