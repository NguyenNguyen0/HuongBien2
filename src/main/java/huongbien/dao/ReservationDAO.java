package huongbien.dao;

import huongbien.entity.Reservation;
import huongbien.entity.RestaurantTable;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ReservationDAO extends GenericDAO<Reservation> {
    public ReservationDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Reservation> getAllByCustomerId(String customerId) {
        return findMany("SELECT r FROM Reservation r WHERE r.customerId = ?1", Reservation.class, customerId);
    }

    public List<Reservation> getAllByEmployeeId(String employeeId) {
        return findMany("SELECT r FROM Reservation r WHERE r.employeeId = ?1", Reservation.class, employeeId);
    }

    public List<Reservation> getAll() {
        return findMany("SELECT r FROM Reservation r", Reservation.class);
    }

    public List<Reservation> getStatusReservationByDate(LocalDate date, String status, String cusId) {
        return findMany("SELECT r FROM Reservation r WHERE r.status = ?1 AND r.receiveDate = ?2 AND r.customerId LIKE CONCAT('%', ?3, '%')",
                Reservation.class, status, date, cusId);
    }

    public int getCountStatusReservationByDate(LocalDate date, String status, String cusId) {
        return count("SELECT COUNT(r) FROM Reservation r WHERE r.status = ?1 AND r.receiveDate = ?2 AND r.customerId LIKE CONCAT('%', ?3, '%')",
                status, date, cusId);
    }

    public List<Reservation> getListWaitedToday() {
        return findMany("SELECT r FROM Reservation r WHERE r.status = 'Chưa nhận' AND r.receiveDate = CURRENT_DATE", Reservation.class);
    }

//    public List<RestaurantTable> getListTableStatusToday(List<Reservation> reservationList) {
//        List<RestaurantTable> tableList = new ArrayList<>();
//        if (reservationList != null) {
//            for (Reservation reservation : reservationList) {
//                String jpql = "SELECT rt FROM Reservation r JOIN r.tables rt WHERE r.id = ?1";
//                List<RestaurantTable> tables = findMany(jpql, RestaurantTable.class, reservation.getId());
//                tableList.addAll(tables);
//            }
//        }
//        return tableList;
//    }

    public Reservation getById(String id) {
        return findOne("SELECT r FROM Reservation r WHERE r.id = ?1", Reservation.class, id);
    }

    public List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate, int pageIndex) {
        String jpql = "SELECT r FROM Reservation r WHERE r.id LIKE CONCAT('%', ?1, '%') " +
                "AND r.customerId LIKE CONCAT('%', ?2, '%') " +
                "AND r.status = 'Chưa nhận' " +
                (reservationDate != null ? "AND r.reservationDate = ?3 " : "") +
                (receiveDate != null ? "AND r.receiveDate = ?4 " : "") +
                "ORDER BY r.receiveDate DESC";
        return findMany(jpql, Reservation.class, reservationId, reservationCusId, reservationDate, receiveDate);
    }

    public int getCountLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate) {
        String jpql = "SELECT COUNT(r) FROM Reservation r WHERE r.id LIKE CONCAT('%', ?1, '%') " +
                "AND r.customerId LIKE CONCAT('%', ?2, '%') " +
                "AND r.status = 'Chưa nhận' " +
                (reservationDate != null ? "AND r.reservationDate = ?3 " : "") +
                (receiveDate != null ? "AND r.receiveDate = ?4 " : "");
        return count(jpql, reservationId, reservationCusId, reservationDate, receiveDate);
    }

    public int countTotal() {
        return count("SELECT COUNT(r) FROM Reservation r");
    }
}
