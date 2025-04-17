package huongbien.dao.remote;

import huongbien.entity.Reservation;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface ITableDAO extends Remote {
    List<Table> getAll() throws RemoteException;

    Table getById(String id) throws RemoteException;

    List<Table> getByName(String name) throws RemoteException;

    List<Table> getAllByReservationId(String reservationId) throws RemoteException;

    List<Table> getAllByOrderId(String orderId) throws RemoteException;

    int getCountStatisticalOverviewTable() throws RemoteException;

    int getCountStatisticalOverviewTableEmpty() throws RemoteException;

    int getCountStatisticalFloorTable(int floor) throws RemoteException;

    int getCountStatisticalFloorTableEmpty(int floor) throws RemoteException;

    int getCountStatisticalFloorTablePreOrder(int floor) throws RemoteException;

    int getCountStatisticalFloorTableOpen(int floor) throws RemoteException;

    List<Table> getByCriteria(String floor, String status, String typeID, String seat) throws RemoteException;

    List<Integer> getDistinctFloor() throws RemoteException;

    List<String> getDistinctSeat() throws RemoteException;

    Table getTopFloor() throws RemoteException;

    List<Table> getReservedTables(LocalDate receiveDate) throws RemoteException;

    List<String> getDistinctStatuses() throws RemoteException;

    List<Integer> getDistinctFloors() throws RemoteException;

    List<Integer> getDistinctSeats() throws RemoteException;

    List<String> getDistinctTableType() throws RemoteException;

    List<Table> getLookUpTable(int floor, String name, int seat, String type, String status, int pageIndex) throws RemoteException;

    int getCountLookUpTable(int floor, String name, int seat, String type, String status) throws RemoteException;

    List<Table> getAllWithPagination(int offset, int limit) throws RemoteException;

    List<Table> getByNameWithPagination(String name, int offset, int limit) throws RemoteException;

    List<Table> getByFloorWithPagination(int offset, int limit, int floor) throws RemoteException;

    int countTotalTables() throws RemoteException;

    int countTotalTablesByFloor(int floor) throws RemoteException;

    int countTotalTablesByName(String name) throws RemoteException;

    List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException;

    void updateStatusTable(String id, TableStatus status) throws RemoteException;

    boolean add(Table table) throws RemoteException;

    boolean update(Table table) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}