package huongbien.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CuisineStatus {
    AVAILABLE("Còn bán"),
    DISCONTINUED("Ngừng bán");

    private final String value;

    CuisineStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static CuisineStatus fromString(String status) {
        for (CuisineStatus cuisineStatus : CuisineStatus.values()) {
            if (cuisineStatus.value.equalsIgnoreCase(status)) {
                return cuisineStatus;
            }
        }
        return null;
    }

    @JsonValue
    public String getStatus() {
        return value;
    }
}
