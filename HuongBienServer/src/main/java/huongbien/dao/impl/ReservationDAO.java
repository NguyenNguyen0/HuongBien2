package huongbien.dao.impl;

import huongbien.bus.TableBUS;
import huongbien.dao.remote.IReservationDAO;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public class ReservationDAO extends GenericDAO<Reservation> implements IReservationDAO {
    public ReservationDAO() throws RemoteException {
        super();
    }

    public ReservationDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Reservation> getAllByCustomerId(String customerId) throws RemoteException {
        return findMany("SELECT r FROM Reservation r WHERE r.customer.id = ?1", Reservation.class, customerId);
    }

    @Override
    public List<Reservation> getAllByEmployeeId(String employeeId) throws RemoteException {
        return findMany("SELECT r FROM Reservation r WHERE r.employee.id = ?1", Reservation.class, employeeId);
    }

    @Override
    public List<Reservation> getAll() throws RemoteException {
        return findMany("SELECT r FROM Reservation r", Reservation.class);
    }

    @Override
    public List<Reservation> getStatusReservationByDate(LocalDate date, String status, String cusId) throws RemoteException {
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

    @Override
    public int getCountStatusReservationByDate(LocalDate date, String status, String cusId) throws RemoteException {
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
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Reservation> getListWaitedToday() throws RemoteException {
        return findMany("SELECT r FROM Reservation r WHERE r.status = 'Chưa xác nhận' AND r.receiveDate = CURRENT_DATE", Reservation.class);
    }

    @Override
    public Reservation getById(String id) throws RemoteException {
        return findOne("SELECT r FROM Reservation r WHERE r.id = ?1", Reservation.class, id);
    }

    @Override
    public List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate, int pageIndex) throws RemoteException {
        String jpql = "SELECT r FROM Reservation r WHERE r.id LIKE CONCAT('%', ?1, '%') " +
                "AND r.customerId LIKE CONCAT('%', ?2, '%') " +
                "AND r.status = 'Chưa nhận' " +
                (reservationDate != null ? "AND r.reservationDate = ?3 " : "") +
                (receiveDate != null ? "AND r.receiveDate = ?4 " : "") +
                "ORDER BY r.receiveDate DESC";
        return findMany(jpql, Reservation.class, reservationId, reservationCusId, reservationDate, receiveDate);
    }

    @Override
    public int getCountLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate) throws RemoteException {
        String jpql = "SELECT COUNT(r) FROM Reservation r WHERE r.id LIKE CONCAT('%', ?1, '%') " +
                "AND r.customerId LIKE CONCAT('%', ?2, '%') " +
                "AND r.status = 'Chưa nhận' " +
                (reservationDate != null ? "AND r.reservationDate = ?3 " : "") +
                (receiveDate != null ? "AND r.receiveDate = ?4 " : "");
        return count(jpql, reservationId, reservationCusId, reservationDate, receiveDate);
    }

    @Override
    public int countTotal() throws RemoteException {
        return count("SELECT COUNT(r) FROM Reservation r");
    }

    @Override
    public List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException {
        TableBUS tableBUS = new TableBUS();
        return tableBUS.getListTableStatusToday(reservationList);
    }

    @Override
    public boolean add(Reservation reservation) throws RemoteException {
        return super.add(reservation);
    }

    @Override
    public boolean update(Reservation reservation) throws RemoteException {
        return super.update(reservation);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        Reservation reservation = getById(id);
        if (reservation == null) {
            return false;
        }
        return super.delete(reservation);
    }

    @Override
    public void updateStatus(String reservationId, ReservationStatus status) throws RemoteException {
        Reservation reservation = getById(reservationId);
        if (reservation == null) {
            return;
        }
        reservation.setStatus(status);
        update(reservation);
    }

    @Override
    public void updateRefundDeposit(String id, double refundDeposit) throws RemoteException {
        Reservation reservation = getById(id);
        if (reservation == null) {
            return;
        }
        reservation.setRefundDeposit(refundDeposit);
        update(reservation);
    }
}
