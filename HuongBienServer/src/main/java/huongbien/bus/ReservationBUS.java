package huongbien.bus;

import huongbien.dao.impl.ReservationDAO;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservationBUS {
    private final ReservationDAO reservationDao;

    public ReservationBUS() throws RemoteException {
        reservationDao = new ReservationDAO(PersistenceUnit.MARIADB_JPA);
    }

    public int countTotalReservations() throws RemoteException {
        return reservationDao.countTotal();
    }

    public List<Reservation> getReservationsByCustomerId(String customerId) throws RemoteException {
        if (customerId.isBlank() || customerId.isEmpty()) return null;
        return reservationDao.getAllByCustomerId(customerId);
    }

    public Reservation getReservationById(String reservationId) throws RemoteException {
        if (reservationId.isBlank() || reservationId.isEmpty()) return null;
        return reservationDao.getById(reservationId);
    }

    public boolean addReservation(Reservation reservation) throws RemoteException {
        if (reservation == null) return false;
        return reservationDao.add(reservation);
    }

    public boolean updateReservation(Reservation reservation) throws RemoteException {
        if (reservation == null) return false;
        return reservationDao.update(reservation);
    }

    public List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate, int pageIndex) throws RemoteException {
        return reservationDao.getLookUpReservation(reservationId, reservationCusId, reservationDate, receiveDate, pageIndex);
    }

    public int getCountLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate) throws RemoteException {
        return reservationDao.getCountLookUpReservation(reservationId, reservationCusId, reservationDate, receiveDate);
    }

    public List<Reservation> getListWaitedToday() throws RemoteException {
        return reservationDao.getListWaitedToday();
    }

    public List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException, SQLException {
        return reservationDao.getListTableStatusToday(reservationList);
    }

    public void updateStatus(String reservationId, ReservationStatus status) throws RemoteException {
        reservationDao.updateStatus(reservationId, status);
    }
}