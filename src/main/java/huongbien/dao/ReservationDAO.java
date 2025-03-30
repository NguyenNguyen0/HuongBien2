package huongbien.dao;

import huongbien.bus.TableBUS;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        try {
            ReservationStatus enumStatus = ReservationStatus.fromStatus(status);
            String jpql = "SELECT r FROM Reservation r WHERE r.status = :status " +
                    "AND r.receiveDate = :date AND r.id LIKE :customerId";

            EntityManager em = JPAUtil.getEntityManager();
            Query query = em.createQuery(jpql, Reservation.class);
            query.setParameter("status", enumStatus);
            query.setParameter("date", date);
            query.setParameter("customerId", "%" + cusId + "%");

            return query.getResultList();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return List.of();
        }
    }
    public int getCountStatusReservationByDate(LocalDate date, String status, String cusId) {
        String jpql = "SELECT COUNT(r) FROM Reservation r WHERE r.status = :status " +
                     "AND r.receiveDate = :date AND r.id LIKE :customerId";

        EntityManager em = JPAUtil.getEntityManager();
        try {
            ReservationStatus enumStatus = ReservationStatus.valueOf(status);

            Query query = em.createQuery(jpql);
            query.setParameter("status", enumStatus);
            query.setParameter("date", date);
            query.setParameter("customerId", "%" + cusId + "%");

            Object result = query.getSingleResult();
            return result instanceof Number ? ((Number) result).intValue() : 0;
        } catch (IllegalArgumentException e) {
            // Handle case where status string doesn't match enum
            // You could log an error or use a different approach
            e.getStackTrace();
            return 0;
        }
    }

    public List<Reservation> getListWaitedToday() {
        return findMany("SELECT r FROM Reservation r WHERE r.status = 'Chưa xác nhận' AND r.receiveDate = CURRENT_DATE", Reservation.class);
    }

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

    public List<Table> getListTableStatusToday(List<Reservation> reservationList) {
        TableBUS tableBUS = new TableBUS();
        return tableBUS.getListTableStatusToday(reservationList);
    }

    public void updateStatus(String reservationId, ReservationStatus status) {
        Reservation reservation = getById(reservationId);
        if (reservation == null) {
            return;
        }
        reservation.setStatus(status);
        update(reservation);
    }

    public void updateRefundDeposit(String id, double refundDeposit) {
        Reservation reservation = getById(id);
        if (reservation == null) {
            return;
        }
        reservation.setRefundDeposit(refundDeposit);
        update(reservation);
    }
}
