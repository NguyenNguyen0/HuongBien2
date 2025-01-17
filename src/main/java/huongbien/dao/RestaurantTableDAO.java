package huongbien.dao;

import huongbien.entity.RestaurantTable;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
public class RestaurantTableDAO extends GenericDAO<RestaurantTable> {
    public RestaurantTableDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<RestaurantTable> getAll() {
        return findMany("SELECT t FROM RestaurantTable t", RestaurantTable.class);
    }

    public RestaurantTable getById(String id) {
        return findOne("SELECT t FROM RestaurantTable t WHERE t.id = ?1", RestaurantTable.class, id);
    }

    public List<RestaurantTable> getByName(String name) {
        return findMany("SELECT t FROM RestaurantTable t WHERE t.name LIKE ?1", RestaurantTable.class, name + "%");
    }

    public List<RestaurantTable> getAllByReservationId(String reservationId) {
        return findMany("SELECT t FROM RestaurantTable t WHERE t.id IN (SELECT rt.table.id FROM ReservationTable rt WHERE rt.reservation.id = ?1)", RestaurantTable.class, reservationId);
    }

    public List<RestaurantTable> getAllByOrderId(String orderId) {
        return findMany("SELECT t FROM RestaurantTable t WHERE t.id IN (SELECT ot.table.id FROM OrderTable ot WHERE ot.order.id = ?1)", RestaurantTable.class, orderId);
    }

    public int getCountStatisticalOverviewTable() {
        return count("SELECT COUNT(t) FROM RestaurantTable t WHERE t.status != 'Bàn đóng'");
    }

    public int getCountStatisticalOverviewTableEmpty() {
        return count("SELECT COUNT(t) FROM RestaurantTable t WHERE t.status = 'Bàn trống'");
    }

    public int getCountStatisticalFloorTable(int floor) {
        return count("SELECT COUNT(t) FROM RestaurantTable t WHERE t.status != 'Bàn đóng' AND t.floor = ?1", floor);
    }

    public int getCountStatisticalFloorTableEmpty(int floor) {
        return count("SELECT COUNT(t) FROM RestaurantTable t WHERE t.status = 'Bàn trống' AND t.floor = ?1", floor);
    }

    public int getCountStatisticalFloorTablePreOrder(int floor) {
        return count("SELECT COUNT(t) FROM RestaurantTable t WHERE t.status = 'Đặt trước' AND t.floor = ?1", floor);
    }

    public int getCountStatisticalFloorTableOpen(int floor) {
        return count("SELECT COUNT(t) FROM RestaurantTable t WHERE t.status = 'Phục vụ' AND t.floor = ?1", floor);
    }

    public List<RestaurantTable> getByCriteria(String floor, String status, String typeID, String seat) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM RestaurantTable t WHERE t.status != 'Bàn đóng'");

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

        return findMany(jpqlBuilder.toString(), RestaurantTable.class, floor, status, typeID, seat);
    }

    public List<String> getDistinctFloor() {
        return executeQuery("SELECT DISTINCT t.floor FROM RestaurantTable t", String.class);
    }

    public List<String> getDistinctSeat() {
        return executeQuery("SELECT DISTINCT t.seats FROM RestaurantTable t", String.class);
    }

    public RestaurantTable getTopFloor() {
        return findOne("SELECT t FROM RestaurantTable t ORDER BY t.floor ASC", RestaurantTable.class);
    }

    public List<RestaurantTable> getReservedTables(LocalDate receiveDate) {
        return findMany("SELECT t FROM RestaurantTable t WHERE t.reservations.receiveDate = ?1", RestaurantTable.class, receiveDate);
    }

    public List<String> getDistinctStatuses() {
        return executeQuery("SELECT DISTINCT t.status FROM RestaurantTable t WHERE t.status != 'Bàn đóng'", String.class);
    }

    public List<Integer> getDistinctFloors() {
        return executeQuery("SELECT DISTINCT t.floor FROM RestaurantTable t", Integer.class);
    }

    public List<Integer> getDistinctSeats() {
        return executeQuery("SELECT DISTINCT t.seats FROM RestaurantTable t", Integer.class);
    }

    public List<String> getDistinctTableType() {
        return executeQuery("SELECT DISTINCT t.tableType.id FROM RestaurantTable t", String.class);
    }

    public List<RestaurantTable> getLookUpTable(int floor, String name, int seat, String type, String status, int pageIndex) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM RestaurantTable t WHERE t.name LIKE ?1 AND t.tableType.id LIKE ?2 AND t.status LIKE ?3");

        if (floor != -1) {
            jpqlBuilder.append(" AND t.floor = ?4");
        }
        if (seat != -1) {
            jpqlBuilder.append(" AND t.seats = ?5");
        }

        return findMany(jpqlBuilder.toString(), RestaurantTable.class, "%" + name + "%", "%" + type + "%", "%" + status + "%", floor, seat);
    }

    public int getCountLookUpTable(int floor, String name, int seat, String type, String status) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT COUNT(t) FROM RestaurantTable t WHERE t.name LIKE ?1 AND t.tableType.id LIKE ?2 AND t.status LIKE ?3");

        if (floor != -1) {
            jpqlBuilder.append(" AND t.floor = ?4");
        }
        if (seat != -1) {
            jpqlBuilder.append(" AND t.seats = ?5");
        }

        return count(jpqlBuilder.toString(), "%" + name + "%", "%" + type + "%", "%" + status + "%", floor, seat);
    }

    public List<RestaurantTable> getAllWithPagination(int offset, int limit) {
        return findMany("SELECT t FROM RestaurantTable t ORDER BY t.floor", RestaurantTable.class, offset, limit);
    }

    public List<RestaurantTable> getByNameWithPagination(String name, int offset, int limit) {
        return findMany("SELECT t FROM RestaurantTable t WHERE t.name LIKE ?1 ORDER BY t.floor", RestaurantTable.class, "%" + name + "%", offset, limit);
    }

    public List<RestaurantTable> getByFloorWithPagination(int offset, int limit, int floor) {
        return findMany("SELECT t FROM RestaurantTable t WHERE t.floor = ?1 ORDER BY t.floor", RestaurantTable.class, floor, offset, limit);
    }

    public int countTotalTables() {
        return count("SELECT COUNT(t) FROM RestaurantTable t");
    }

    public int countTotalTablesByFloor(int floor) {
        return count("SELECT COUNT(t) FROM RestaurantTable t WHERE t.floor = ?1", floor);
    }

    public int countTotalTablesByName(String name) {
        return count("SELECT COUNT(t) FROM RestaurantTable t WHERE t.name LIKE ?1", "%" + name + "%");
    }
}