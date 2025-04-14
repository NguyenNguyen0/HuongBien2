package huongbien.rmi.interfaces;

import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Remote interface for reservation services
 */
public interface ReservationService extends Remote {
    
    // Reservation retrieval
    Reservation getReservationById(String reservationId) throws RemoteException;
    List<Reservation> getReservationsByCustomerId(String customerId) throws RemoteException;
    List<Reservation> getListWaitedToday() throws RemoteException;
    
    // Reservation lookup
    List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, 
                                          LocalDate reservationDate, LocalDate receiveDate, 
                                          int pageIndex) throws RemoteException;
    
    // Reservation counts
    int countTotalReservations() throws RemoteException;
    int getCountLookUpReservation(String reservationId, String reservationCusId, 
                                 LocalDate reservationDate, LocalDate receiveDate) throws RemoteException;
                                          
    // Reservation management
    boolean addReservation(Reservation reservation) throws RemoteException;
    boolean updateReservation(Reservation reservation) throws RemoteException;
    void updateStatus(String reservationId, ReservationStatus status) throws RemoteException;
    
    // Table status related to reservations
    List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException, SQLException;
}