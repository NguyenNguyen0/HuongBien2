package huongbien.dao;

import huongbien.entity.Reservation;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class TableDAO extends GenericDAO<Table> {
    public TableDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Table> getAll() {
        return findMany("SELECT t FROM Table t", Table.class);
    }

    public Table getById(String id) {
        return findOne("SELECT t FROM Table t WHERE t.id = ?1", Table.class, id);
    }

    public List<Table> getByName(String name) {
        return findMany("SELECT t FROM Table t WHERE t.name LIKE ?1", Table.class, name + "%");
    }

    public List<Table> getAllByReservationId(String reservationId) {
        return findMany("SELECT t FROM Table t WHERE t.id IN (SELECT rt.table.id FROM ReservationTable rt WHERE rt.reservation.id = ?1)", Table.class, reservationId);
    }

    public List<Table> getAllByOrderId(String orderId) {
        return findMany("SELECT t FROM Table t WHERE t.id IN (SELECT ot.table.id FROM OrderTable ot WHERE ot.order.id = ?1)", Table.class, orderId);
    }

    public int getCountStatisticalOverviewTable() {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status != 'Bàn đóng'");
    }

    public int getCountStatisticalOverviewTableEmpty() {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status = 'Bàn trống'");
    }

    public int getCountStatisticalFloorTable(int floor) {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status != 'Bàn đóng' AND t.floor = ?1", floor);
    }

    public int getCountStatisticalFloorTableEmpty(int floor) {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status = 'Bàn trống' AND t.floor = ?1", floor);
    }

    public int getCountStatisticalFloorTablePreOrder(int floor) {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status = 'Đặt trước' AND t.floor = ?1", floor);
    }

    public int getCountStatisticalFloorTableOpen(int floor) {
        return count("SELECT COUNT(t) FROM Table t WHERE t.status = 'Phục vụ' AND t.floor = ?1", floor);
    }

    public List<Table> getByCriteria(String floor, String status, String typeID, String seat) {
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
//        System.out.println("seat: " + seat + " typeID: " + typeID + " status: " + status + " floor: " + floor);
        return findMany(jpqlBuilder.toString(), Table.class, params);
    }

    public List<Integer> getDistinctFloor() {
        return executeQuery("SELECT DISTINCT t.floor FROM Table t", Integer.class);
    }

    public List<String> getDistinctSeat() {
        return executeQuery("SELECT DISTINCT t.seats FROM Table t", Integer.class).stream().map(Object::toString).collect(Collectors.toList());
    }

    public Table getTopFloor() {
        return findOne("SELECT t FROM Table t ORDER BY t.floor ASC;", Table.class);
    }

    public List<Table> getReservedTables(LocalDate receiveDate) {
        return findMany("SELECT t FROM Table t WHERE t.reservations.receiveDate = ?1", Table.class, receiveDate);
    }

    public List<String> getDistinctStatuses() {
        List<TableStatus> statusEnums = executeQuery("SELECT DISTINCT t.status FROM Table t", TableStatus.class);
        List<String> statusStrings = new ArrayList<>();
        for (TableStatus status : statusEnums) {
            statusStrings.add(status.toString());
        }
        return statusStrings;
    }

    public List<Integer> getDistinctFloors() {
        return executeQuery("SELECT DISTINCT t.floor FROM Table t", Integer.class);
    }

    public List<Integer> getDistinctSeats() {
        return executeQuery("SELECT DISTINCT t.seats FROM Table t", Integer.class);
    }

    public List<String> getDistinctTableType() {
        return executeQuery("SELECT DISTINCT t.tableType.id FROM Table t", String.class);
    }

    public List<Table> getLookUpTable(int floor, String name, int seat, String type, String status, int pageIndex) {
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

        // Handle status parameter
        if (status != null && !status.isEmpty()) {
            try {
                TableStatus tableStatus = TableStatus.fromString(status);
                jpqlBuilder.append(" AND t.status = :status");
                params.put("status", tableStatus);
            } catch (IllegalArgumentException e) {
                // If status string doesn't match any enum value, don't filter by status
                jpqlBuilder.append(" AND 1=1");
            }
        } else {
            jpqlBuilder.append(" AND 1=1");
        }

        // Calculate pagination
        int offset = pageIndex * 10; // Assuming 10 items per page
        int limit = 10;

        return findManyWithPagination(jpqlBuilder.toString(), Table.class, params, offset, limit);
    }

    public int getCountLookUpTable(int floor, String name, int seat, String type, String status) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM Table t WHERE t.name LIKE :name AND t.tableType.id LIKE :type");

        Map<String, Object> params = new java.util.HashMap<>();
        params.put("name", "%" + name + "%");
        params.put("type", "%" + type + "%");

        // Handle the status parameter separately
        if (status != null && !status.isEmpty()) {
            try {
                // Use the fromString method instead of valueOf for conversion
                TableStatus tableStatus = TableStatus.fromString(status);
                jpqlBuilder.append(" AND t.status = :status");
                params.put("status", tableStatus);
            } catch (IllegalArgumentException e) {
                // Use a string-based condition for wildcard matching
                jpqlBuilder.append(" AND CAST(t.status AS string) LIKE :statusStr");
                params.put("statusStr", "%");
            }
        } else {
            // For null or empty status, match any status using a different approach
            jpqlBuilder.append(" AND 1=1"); // Always true condition
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

    public List<Table> getAllWithPagination(int offset, int limit) {
        String jpql = "SELECT t FROM Table t ORDER BY t.floor";
        return findManyWithPagination(jpql, Table.class, null, offset, limit);
    }

    public List<Table> getByNameWithPagination(String name, int offset, int limit) {
        String jpql = "SELECT t FROM Table t WHERE t.name LIKE :name ORDER BY t.floor";
        return findManyWithPagination(jpql, Table.class, Map.of("name", "%" + name + "%"), offset, limit);
    }

    public List<Table> getByFloorWithPagination(int offset, int limit, int floor) {
        String jpql = "SELECT t FROM Table t WHERE t.floor = :floor ORDER BY t.floor";
        return findManyWithPagination(jpql, Table.class, Map.of("floor", floor), offset, limit);
    }


    public int countTotalTables() {
        return count("SELECT COUNT(t) FROM Table t");
    }

    public int countTotalTablesByFloor(int floor) {
        return count("SELECT COUNT(t) FROM Table t WHERE t.floor = ?1", floor);
    }

    public int countTotalTablesByName(String name) {
        return count("SELECT COUNT(t) FROM Table t WHERE t.name LIKE ?1", "%" + name + "%");
    }

    public List<Table> getListTableStatusToday(List<Reservation> reservationList) {
        List<Table> tableList = new ArrayList<>();
        if (reservationList != null && !reservationList.isEmpty()) {
            try {
                reservationList.forEach(reservation -> {
                    List<Table> tables = findMany("SELECT * FROM reservation_tables WHERE reservation_id ", Table.class, reservation.getId());
                    tables.addAll(tables);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return tableList;
    }

    public void updateStatusTable(String id, TableStatus status) {
        Table table = getById(id);
        table.setStatus(status);
        update(table);
    }
}