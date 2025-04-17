package huongbien.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TableStatus {
    AVAILABLE("Bàn Trống"),
    OCCUPIED("Đã đặt"),
    RESERVED("Đang phục vụ"),
    UNAVAILABLE("Không sử dụng");

    private final String value;

    TableStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static TableStatus fromString(String status) {
        for (TableStatus tableStatus : TableStatus.values()) {
            if (tableStatus.value.equalsIgnoreCase(status)) {
                return tableStatus;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
