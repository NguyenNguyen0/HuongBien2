package huongbien.dao;

import huongbien.entity.Reservation;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

        if (!floor.isEmpty()) {
            jpqlBuilder.append(" AND t.floor = ?1");
        }

        if (!status.isEmpty()) {
            jpqlBuilder.append(" AND t.status = ?2");
        }

        if (!typeID.isEmpty()) {
            jpqlBuilder.append(" AND t.tableType.id = ?3");
        }

        if (!seat.isEmpty()) {
            jpqlBuilder.append(" AND t.seats = ?4");
        }

        return findMany(jpqlBuilder.toString(), Table.class, floor, TableStatus.fromString(status), typeID, 0);
    }

    public List<String> getDistinctFloor() {
        return executeQuery("SELECT DISTINCT t.floor FROM Table t", String.class);
    }

    public List<String> getDistinctSeat() {
        return executeQuery("SELECT DISTINCT t.seats FROM Table t", String.class);
    }

    public Table getTopFloor() {
        return findOne("SELECT t FROM Table t ORDER BY t.floor ASC;", Table.class);
    }

    public List<Table> getReservedTables(LocalDate receiveDate) {
        return findMany("SELECT t FROM Table t WHERE t.reservations.receiveDate = ?1", Table.class, receiveDate);
    }

    public List<String> getDistinctStatuses() {
        return executeQuery("SELECT DISTINCT t.status FROM Table t WHERE t.status != 'Bàn đóng'", String.class);
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
        StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM Table t WHERE t.name LIKE ?1 AND t.tableType.id LIKE ?2 AND t.status LIKE ?3");

        if (floor != -1) {
            jpqlBuilder.append(" AND t.floor = ?4");
        }
        if (seat != -1) {
            jpqlBuilder.append(" AND t.seats = ?5");
        }
        try {
            type = TableStatus.valueOf(type).getValue();
        } catch (Exception e) {
            type = "";
        }

        return findMany(jpqlBuilder.toString(), Table.class, "%" + name + "%", type, "%" + status + "%", floor, seat);
    }

    public int getCountLookUpTable(int floor, String name, int seat, String type, String status) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT COUNT(t) FROM Table t WHERE t.name LIKE ?1 AND t.tableType.id LIKE ?2");

        // Handle the status parameter separately
        if (status != null && !status.isEmpty()) {
            // Convert string to TableStatus enum
            jpqlBuilder.append(" AND t.status = ?3");
        } else {
            jpqlBuilder.append(" AND t.status LIKE ?3");
            status = "";
        }

        if (floor != -1) {
            jpqlBuilder.append(" AND t.floor = ?4");
        }
        if (seat != -1) {
            jpqlBuilder.append(" AND t.seats = ?5");
        }

        Object statusParam = status.equals("") ? "" : TableStatus.valueOf(status);
        return count(jpqlBuilder.toString(), "%" + name + "%", "%" + type + "%", statusParam, floor, seat);
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
//        TODO: Implement this method
        return null;
    }

    public void updateStatusTable(String id, TableStatus status) {
        Table table = getById(id);
        table.setStatus(status);
        update(table);
    }
}