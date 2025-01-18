package huongbien.entity;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("Tiền mặt"),
    CREDIT_CARD("Thẻ ngân hàng"),
    QR_CODE("QR Code");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
