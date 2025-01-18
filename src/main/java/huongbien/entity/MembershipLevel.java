package huongbien.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MembershipLevel {
    BRONZE("Đồng"),
    SILVER("Bạc"),
    GOLD("Vàng"),
    DIAMOND("Kim cương");

    private final String name;

    MembershipLevel(String name) {
        this.name = name;
    }

    @JsonCreator
    public static MembershipLevel fromLevel(int level) {
        return values()[level];
    }

    public static MembershipLevel fromName(String name) {
        for (MembershipLevel level : values()) {
            if (level.name.equalsIgnoreCase(name)) {
                return level;
            }
        }
        return null;
    }

    @JsonValue
    public int getLevel() {
        return ordinal();
    }
}
