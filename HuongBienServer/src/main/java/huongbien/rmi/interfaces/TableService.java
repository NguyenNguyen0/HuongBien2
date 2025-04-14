package huongbien.rmi.interfaces;

import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.entity.Reservation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Remote interface for table management services
 */
public interface TableService extends Remote {
    
    // Table retrieval
    List<Table> getAllTables() throws RemoteException;
    Table getTableById(String tableId) throws RemoteException;
    List<Table> getTablesWithPagination(int offset, int limit) throws RemoteException;
    List<Table> getTablesByFloorWithPagination(int floor, int offset, int limit) throws RemoteException;
    List<Table> getTablesByNameWithPagination(int offset, int limit, String name) throws RemoteException;
    List<Table> getAllTableByOrderId(String orderId) throws RemoteException;
    List<Table> getAllTableByReservationId(String reservationId) throws RemoteException;
    
    // Table lookup and filtering
    List<Table> getLookUpTable(int floor, String name, int seat, String type, String status, int pageIndex) throws RemoteException;
    List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException;
    List<Table> getReservedTables(LocalDate date) throws RemoteException;
    
    // Table metadata retrieval
    List<String> getDistinctStatuses() throws RemoteException;
    List<Integer> getDistinctFloors() throws RemoteException;
    List<Integer> getDistinctSeats() throws RemoteException;
    List<String> getDistinctTableTypes() throws RemoteException;
    
    // Table counts
    int countTotalTables() throws RemoteException;
    int countTotalTablesByName(String name) throws RemoteException;
    int countTotalTablesByFloor(int floor) throws RemoteException;
    int getCountLookUpTable(int floor, String name, int seat, String type, String status) throws RemoteException;
    
    // Table management
    boolean updateTableInfo(Table table) throws RemoteException;
    boolean addTable(Table table) throws RemoteException;
    void updateStatusTable(String id, TableStatus status) throws RemoteException;
}