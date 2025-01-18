package huongbien.entity;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    PENDING("Chưa xác nhận"),
    CONFIRMED("Đã xác nhận"),
    CANCELLED("Đã hủy"),
    COMPLETED("Đã hoàn thành");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

    public static ReservationStatus fromStatus(String status) {
        for (ReservationStatus rs : ReservationStatus.values()) {
            if (rs.status.equals(status)) {
                return rs;
            }
        }
        return null;
    }
}
