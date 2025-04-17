package huongbien.dao.impl;

import huongbien.dao.remote.ITableDAO;
import huongbien.entity.Reservation;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableDAO extends GenericDAO<Table> implements ITableDAO {
    public TableDAO() throws RemoteException {
        super();
    }

    public TableDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Table> getAll() throws RemoteException {
        return findMany("SELECT t FROM Table t", Table.class);
    }

    @Override
    public Table getById(String id) throws RemoteException {
        return findOne("SELECT t FROM Table t WHERE t.id = ?1", Table.class, id);
    }

    @Override
    public List<Table> getByName(String name) throws RemoteException {
        return findMany("SELECT t FROM Table t WHERE t.name LIKE ?1", Table.class, name + "%");
    }

    @Override
    public List<Table> getAllByReservationId(String reservationId) throws RemoteException {
        return findMany("SELECT t FROM Table t WHERE t.id IN (SELECT rt.table.id FROM ReservationTable rt WHERE rt.reservation.id = ?1)", Table.class, reservationId);
    }

    @Override
    public List<Table> getAllByOrderId(String orderId) throws RemoteException {
        return findMany("SELECT t FROM Table t WHERE t.id IN (SELECT ot.table.id FROM OrderTable ot WHERE ot.order.id = ?1)", Table.class, orderId);
    }

    @Override
    public int getCountStatisticalOverviewTable() throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status != 'Bàn đóng'");
    }

    @Override
    public int getCountStatisticalOverviewTableEmpty() throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status = 'Bàn trống'");
    }

    @Override
    public int getCountStatisticalFloorTable(int floor) throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status != 'Bàn đóng' AND t.floor = ?1", floor);
    }

    @Override
    public int getCountStatisticalFloorTableEmpty(int floor) throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status = 'Bàn trống' AND t.floor = ?1", floor);
    }

    @Override
    public int getCountStatisticalFloorTablePreOrder(int floor) throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status = 'Đặt trước' AND t.floor = ?1", floor);
    }

    @Override
    public int getCountStatisticalFloorTableOpen(int floor) throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status = 'Phục vụ' AND t.floor = ?1", floor);
    }

    @Override
    public List<Table> getByCriteria(String floor, String status, String typeID, String seat) throws RemoteException {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM Table t WHERE t.status != 'Bàn đóng'");
        Map<String, Object> params = new HashMap<>();

        if (floor != null && !floor.isEmpty()) {
            jpqlBuilder.append(" AND t.floor = :floor");
            params.put("floor", Integer.valueOf(floor));
        }

        if (status != null && !status.isEmpty()) {
            jpqlBuilder.append(" AND t.status = :status");
            params.put("status", TableStatus.fromString(status));
        }

        if (typeID != null && !typeID.isEmpty()) {
            jpqlBuilder.append(" AND t.tableType.id = :typeID");
            params.put("typeID", typeID);
        }

        if (seat != null && !seat.isEmpty()) {
            jpqlBuilder.append(" AND t.seats = :seat");
            params.put("seat", Integer.valueOf(seat));
        }
        return findMany(jpqlBuilder.toString(), Table.class, params);
    }

    @Override
    public List<Integer> getDistinctFloor() throws RemoteException {
        return executeQuery("SELECT DISTINCT t.floor FROM Table t", Integer.class);
    }

    @Override
    public List<String> getDistinctSeat() throws RemoteException {
        return executeQuery("SELECT DISTINCT t.seats FROM Table t", Integer.class).stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public Table getTopFloor() throws RemoteException {
        return findOne("SELECT t FROM Table t ORDER BY t.floor ASC;", Table.class);
    }

    @Override
    public List<Table> getReservedTables(LocalDate receiveDate) throws RemoteException {
        return findMany("SELECT t FROM Table t WHERE t.reservations.receiveDate = ?1", Table.class, receiveDate);
    }

    @Override
    public List<String> getDistinctStatuses() throws RemoteException {
        List<TableStatus> statusEnums = executeQuery("SELECT DISTINCT t.status FROM Table t", TableStatus.class);
        List<String> statusStrings = new ArrayList<>();
        for (TableStatus status : statusEnums) {
            statusStrings.add(status.toString());
        }
        return statusStrings;
    }

    @Override
    public List<Integer> getDistinctFloors() throws RemoteException {
        return executeQuery("SELECT DISTINCT t.floor FROM Table t", Integer.class);
    }

    @Override
    public List<Integer> getDistinctSeats() throws RemoteException {
        return executeQuery("SELECT DISTINCT t.seats FROM Table t", Integer.class);
    }

    @Override
    public List<String> getDistinctTableType() throws RemoteException {
        return executeQuery("SELECT DISTINCT t.tableType.id FROM Table t", String.class);
    }

    @Override
    public List<Table> getLookUpTable(int floor, String name, int seat, String type, String status, int pageIndex) throws RemoteException {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM Table t WHERE t.name LIKE :name AND t.tableType.id LIKE :type");

        Map<String, Object> params = new java.util.HashMap<>();
        params.put("name", "%" + name + "%");
        params.put("type", "%" + type + "%");

        if (floor != -1) {
            jpqlBuilder.append(" AND t.floor = :floor");
            params.put("floor", floor);
        }
        if (seat != -1) {
            jpqlBuilder.append(" AND t.seats = :seat");
            params.put("seat", seat);
        }

        if (status != null && !status.isEmpty()) {
            try {
                TableStatus tableStatus = TableStatus.fromString(status);
                jpqlBuilder.append(" AND t.status = :status");
                params.put("status", tableStatus);
            } catch (IllegalArgumentException e) {
                jpqlBuilder.append(" AND 1=1");
            }
        } else {
            jpqlBuilder.append(" AND 1=1");
        }

        int offset = pageIndex * 10;
        int limit = 10;

        return findManyWithPagination(jpqlBuilder.toString(), Table.class, params, offset, limit);
    }

    @Override
    public int getCountLookUpTable(int floor, String name, int seat, String type, String status) throws RemoteException {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM Table t WHERE t.name LIKE :name AND t.tableType.id LIKE :type");

        Map<String, Object> params = new java.util.HashMap<>();
        params.put("name", "%" + name + "%");
        params.put("type", "%" + type + "%");

        if (status != null && !status.isEmpty()) {
            try {
                TableStatus tableStatus = TableStatus.fromString(status);
                jpqlBuilder.append(" AND t.status = :status");
                params.put("status", tableStatus);
            } catch (IllegalArgumentException e) {
                jpqlBuilder.append(" AND CAST(t.status AS string) LIKE :statusStr");
                params.put("statusStr", "%");
            }
        } else {
            jpqlBuilder.append(" AND 1=1");
        }

        if (floor != -1) {
            jpqlBuilder.append(" AND t.floor = :floor");
            params.put("floor", floor);
        }
        if (seat != -1) {
            jpqlBuilder.append(" AND t.seats = :seat");
            params.put("seat", seat);
        }

        List<Table> resultList = findMany(jpqlBuilder.toString(), Table.class, params);
        return resultList != null ? resultList.size() : 0;
    }

    @Override
    public List<Table> getAllWithPagination(int offset, int limit) throws RemoteException {
        String jpql = "SELECT t FROM Table t ORDER BY t.floor";
        return findManyWithPagination(jpql, Table.class, null, offset, limit);
    }

    @Override
    public List<Table> getByNameWithPagination(String name, int offset, int limit) throws RemoteException {
        String jpql = "SELECT t FROM Table t WHERE t.name LIKE :name ORDER BY t.floor";
        return findManyWithPagination(jpql, Table.class, Map.of("name", "%" + name + "%"), offset, limit);
    }

    @Override
    public List<Table> getByFloorWithPagination(int offset, int limit, int floor) throws RemoteException {
        String jpql = "SELECT t FROM Table t WHERE t.floor = :floor ORDER BY t.floor";
        return findManyWithPagination(jpql, Table.class, Map.of("floor", floor), offset, limit);
    }

    @Override
    public int countTotalTables() throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t");
    }

    @Override
    public int countTotalTablesByFloor(int floor) throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t WHERE t.floor = ?1", floor);
    }

    @Override
    public int countTotalTablesByName(String name) throws RemoteException {
        return count("SELECT COUNT(t) FROM Table t WHERE t.name LIKE ?1", "%" + name + "%");
    }

    @Override
    public List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException {
        List<Table> tableList = new ArrayList<>();
        if (reservationList != null && !reservationList.isEmpty()) {
            try {
                reservationList.forEach(reservation -> {
                    List<Table> tables = null;
                    try {
                        tables = findMany("SELECT * FROM reservation_tables WHERE reservation_id ", Table.class, reservation.getId());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    tables.addAll(tables);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return tableList;
    }

    @Override
    public boolean add(Table table) throws RemoteException {
        return super.add(table);
    }

    @Override
    public boolean update(Table table) throws RemoteException {
        return super.update(table);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        Table table = getById(id);
        if (table == null) {
            return false;
        }
        return super.delete(table);
    }

    @Override
    public void updateStatusTable(String id, TableStatus status) throws RemoteException {
        Table table = getById(id);
        table.setStatus(status);
        update(table);
    }
}