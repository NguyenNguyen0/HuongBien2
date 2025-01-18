package huongbien.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Nam"), FEMALE("Nữ"), OTHER("Khác");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender fromValue(String value) {
        for (Gender gender : values()) {
            if (gender.value.equalsIgnoreCase(value)) {
                return gender;
            }
        }

        return null;
    }
}
