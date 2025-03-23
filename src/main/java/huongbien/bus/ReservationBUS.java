package huongbien.bus;

import huongbien.dao.ReservationDAO;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.jpa.PersistenceUnit;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservationBUS {
    private final ReservationDAO reservationDao;

    public ReservationBUS() {
        reservationDao = new ReservationDAO(PersistenceUnit.MARIADB_JPA);
    }

    public int countTotalReservations() {
        return reservationDao.countTotal();
    }

    public List<Reservation> getReservationsByCustomerId(String customerId) {
        if (customerId.isBlank() || customerId.isEmpty()) return null;
        return reservationDao.getAllByCustomerId(customerId);
    }

    public Reservation getReservationById(String reservationId) {
        if (reservationId.isBlank() || reservationId.isEmpty()) return null;
        return reservationDao.getById(reservationId);
    }

    public boolean addReservation(Reservation reservation) {
        if (reservation == null) return false;
        return reservationDao.add(reservation);
    }

    public boolean updateReservation(Reservation reservation){
        if (reservation == null) return false;
        return reservationDao.update(reservation);
    }

    public List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate, int pageIndex){
        return reservationDao.getLookUpReservation(reservationId, reservationCusId, reservationDate, receiveDate, pageIndex);
    }

    public int getCountLookUpReservation (String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate){
        return reservationDao.getCountLookUpReservation(reservationId, reservationCusId, reservationDate, receiveDate);
    }

    public List<Reservation> getListWaitedToday(){
        return reservationDao.getListWaitedToday();
    }
    public List<Table> getListTableStatusToday(List<Reservation> reservationList) throws SQLException {
        return reservationDao.getListTableStatusToday(reservationList);
    }

    public void updateStatus (String reservationId, ReservationStatus status){
        reservationDao.updateStatus(reservationId, status);
    }
}
