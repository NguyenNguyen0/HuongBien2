package huongbien.bus;

import huongbien.dao.impl.TableDAO;
import huongbien.entity.Reservation;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class TableBUS {
    private final TableDAO tableDao;

    public TableBUS() throws RemoteException {
        tableDao = new TableDAO(PersistenceUnit.MARIADB_JPA);
    }

    public int countTotalTables() throws RemoteException {
        return tableDao.countTotalTables();
    }

    public int countTotalTablesByName(String name) throws RemoteException {
        return tableDao.countTotalTablesByName(name);
    }

    public int countTotalTablesByFloor(int floor) throws RemoteException {
        return tableDao.countTotalTablesByFloor(floor);
    }

    public List<Table> getTablesByFloorWithPagination(int floor, int offset, int limit) throws RemoteException {
        if (floor < 0) return null;
        return tableDao.getByFloorWithPagination(floor, offset, limit);
    }

    public List<Table> getTablesByNameWithPagination(int offset, int limit, String name) throws RemoteException {
        return tableDao.getByNameWithPagination(name, offset, limit);
    }

    public List<Table> getTablesWithPagination(int offset, int limit) throws RemoteException {
        return tableDao.getAllWithPagination(offset, limit);
    }

    public List<Table> getAllTable() throws RemoteException {
        return tableDao.getAll();
    }

    public Table getTable(String tableId) throws RemoteException {
        if (tableId.isBlank() || tableId.isEmpty()) return null;
        return tableDao.getById(tableId);
    }

    public List<Table> getAllTableByOrderId(String orderId) throws RemoteException {
        if (orderId.isBlank() || orderId.isEmpty()) return null;
        return tableDao.getAllByOrderId(orderId);
    }

    public List<Table> getAllTableByReservationId(String reservationId) throws RemoteException {
        if (reservationId.isBlank() || reservationId.isEmpty()) return null;
        return tableDao.getAllByReservationId(reservationId);
    }

    public List<Table> getReservedTables(LocalDate date) throws RemoteException {
        if (date == null) return null;
        return tableDao.getReservedTables(date);
    }

    public List<String> getDistinctStatuses() throws RemoteException {
        return tableDao.getDistinctStatuses();
    }

    public List<Integer> getDistinctFloors() throws RemoteException {
        return tableDao.getDistinctFloors();
    }

    public List<Integer> getDistinctSeats() throws RemoteException {
        return tableDao.getDistinctSeats();
    }

    public List<String> getDistinctTableTypes() throws RemoteException {
        return tableDao.getDistinctTableType();
    }

    public List<Table> getLookUpTable(int floor, String name, int seat, String type, String status, int pageIndex) throws RemoteException {
        return tableDao.getLookUpTable(floor, name, seat, type, status, pageIndex);
    }

    public int getCountLookUpTable(int floor, String name, int seat, String type, String status) throws RemoteException {
        return tableDao.getCountLookUpTable(floor, name, seat, type, status);
    }

    public boolean updateTableInfo(Table table) throws RemoteException {
        if (table == null) return false;
        return tableDao.update(table);
    }

    public boolean addTable(Table table) throws RemoteException {
        if (table == null) return false;
        return tableDao.add(table);
    }

    public void updateStatusTable(String id, TableStatus status) throws RemoteException {
        tableDao.updateStatusTable(id, status);
    }

    public List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException {
        if (reservationList == null) return Collections.emptyList();
        return tableDao.getListTableStatusToday(reservationList);
    }
}