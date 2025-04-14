package huongbien.rmi.impl;

import huongbien.bus.ReservationBUS;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.rmi.interfaces.ReservationService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the ReservationService remote interface.
 * This class delegates to the ReservationBUS business logic layer.
 */
public class ReservationServiceImpl implements ReservationService {

    private final ReservationBUS reservationBUS;
    
    public ReservationServiceImpl() {
        this.reservationBUS = new ReservationBUS();
    }
    
    @Override
    public Reservation getReservationById(String reservationId) throws RemoteException {
        return reservationBUS.getReservationById(reservationId);
    }
    
    @Override
    public List<Reservation> getReservationsByCustomerId(String customerId) throws RemoteException {
        return reservationBUS.getReservationsByCustomerId(customerId);
    }
    
    @Override
    public List<Reservation> getListWaitedToday() throws RemoteException {
        return reservationBUS.getListWaitedToday();
    }
    
    @Override
    public List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, 
                                                LocalDate reservationDate, LocalDate receiveDate, 
                                                int pageIndex) throws RemoteException {
        return reservationBUS.getLookUpReservation(reservationId, reservationCusId, reservationDate, receiveDate, pageIndex);
    }
    
    @Override
    public int countTotalReservations() throws RemoteException {
        return reservationBUS.countTotalReservations();
    }
    
    @Override
    public int getCountLookUpReservation(String reservationId, String reservationCusId, 
                                       LocalDate reservationDate, LocalDate receiveDate) throws RemoteException {
        return reservationBUS.getCountLookUpReservation(reservationId, reservationCusId, reservationDate, receiveDate);
    }
    
    @Override
    public boolean addReservation(Reservation reservation) throws RemoteException {
        return reservationBUS.addReservation(reservation);
    }
    
    @Override
    public boolean updateReservation(Reservation reservation) throws RemoteException {
        return reservationBUS.updateReservation(reservation);
    }
    
    @Override
    public void updateStatus(String reservationId, ReservationStatus status) throws RemoteException {
        reservationBUS.updateStatus(reservationId, status);
    }
    
    @Override
    public List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException, SQLException {
        return reservationBUS.getListTableStatusToday(reservationList);
    }
}