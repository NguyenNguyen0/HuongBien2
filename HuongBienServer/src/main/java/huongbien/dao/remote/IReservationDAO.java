package huongbien.dao.remote;

import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface IReservationDAO extends Remote {
    List<Reservation> getAllByCustomerId(String customerId) throws RemoteException;

    List<Reservation> getAllByEmployeeId(String employeeId) throws RemoteException;

    List<Reservation> getAll() throws RemoteException;

    List<Reservation> getStatusReservationByDate(LocalDate date, String status, String cusId) throws RemoteException;

    int getCountStatusReservationByDate(LocalDate date, String status, String cusId) throws RemoteException;

    List<Reservation> getListWaitedToday() throws RemoteException;

    Reservation getById(String id) throws RemoteException;

    List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate, int pageIndex) throws RemoteException;

    int getCountLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate) throws RemoteException;

    int countTotal() throws RemoteException;

    List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException;

    void updateStatus(String reservationId, ReservationStatus status) throws RemoteException;

    void updateRefundDeposit(String id, double refundDeposit) throws RemoteException;

    boolean add(Reservation reservation) throws RemoteException;

    boolean update(Reservation reservation) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}