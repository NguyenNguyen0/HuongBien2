package huongbien.rmi.impl;

import huongbien.bus.TableBUS;
import huongbien.entity.Reservation;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.rmi.interfaces.TableService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the TableService remote interface.
 * This class delegates to the TableBUS business logic layer.
 */
public class TableServiceImpl implements TableService {

    private final TableBUS tableBUS;
    
    public TableServiceImpl() {
        this.tableBUS = new TableBUS();
    }
    
    @Override
    public List<Table> getAllTables() throws RemoteException {
        return tableBUS.getAllTable();
    }
    
    @Override
    public Table getTableById(String tableId) throws RemoteException {
        return tableBUS.getTable(tableId);
    }
    
    @Override
    public List<Table> getTablesWithPagination(int offset, int limit) throws RemoteException {
        return tableBUS.getTablesWithPagination(offset, limit);
    }
    
    @Override
    public List<Table> getTablesByFloorWithPagination(int floor, int offset, int limit) throws RemoteException {
        return tableBUS.getTablesByFloorWithPagination(floor, offset, limit);
    }
    
    @Override
    public List<Table> getTablesByNameWithPagination(int offset, int limit, String name) throws RemoteException {
        return tableBUS.getTablesByNameWithPagination(offset, limit, name);
    }
    
    @Override
    public List<Table> getAllTableByOrderId(String orderId) throws RemoteException {
        return tableBUS.getAllTableByOrderId(orderId);
    }
    
    @Override
    public List<Table> getAllTableByReservationId(String reservationId) throws RemoteException {
        return tableBUS.getAllTableByReservationId(reservationId);
    }
    
    @Override
    public List<Table> getLookUpTable(int floor, String name, int seat, String type, String status, int pageIndex) throws RemoteException {
        return tableBUS.getLookUpTable(floor, name, seat, type, status, pageIndex);
    }
    
    @Override
    public List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException {
        return tableBUS.getListTableStatusToday(reservationList);
    }
    
    @Override
    public List<Table> getReservedTables(LocalDate date) throws RemoteException {
        return tableBUS.getReservedTables(date);
    }
    
    @Override
    public List<String> getDistinctStatuses() throws RemoteException {
        return tableBUS.getDistinctStatuses();
    }
    
    @Override
    public List<Integer> getDistinctFloors() throws RemoteException {
        return tableBUS.getDistinctFloors();
    }
    
    @Override
    public List<Integer> getDistinctSeats() throws RemoteException {
        return tableBUS.getDistinctSeats();
    }
    
    @Override
    public List<String> getDistinctTableTypes() throws RemoteException {
        return tableBUS.getDistinctTableTypes();
    }
    
    @Override
    public int countTotalTables() throws RemoteException {
        return tableBUS.countTotalTables();
    }
    
    @Override
    public int countTotalTablesByName(String name) throws RemoteException {
        return tableBUS.countTotalTablesByName(name);
    }
    
    @Override
    public int countTotalTablesByFloor(int floor) throws RemoteException {
        return tableBUS.countTotalTablesByFloor(floor);
    }
    
    @Override
    public int getCountLookUpTable(int floor, String name, int seat, String type, String status) throws RemoteException {
        return tableBUS.getCountLookUpTable(floor, name, seat, type, status);
    }
    
    @Override
    public boolean updateTableInfo(Table table) throws RemoteException {
        return tableBUS.updateTableInfo(table);
    }
    
    @Override
    public boolean addTable(Table table) throws RemoteException {
        return tableBUS.addTable(table);
    }
    
    @Override
    public void updateStatusTable(String id, TableStatus status) throws RemoteException {
        tableBUS.updateStatusTable(id, status);
    }
}