package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.RestaurantTable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTableDAOTest {
    private static final RestaurantTableDAO restaurantTableDAO = new RestaurantTableDAO();

    @Test
    void add() {
        assertTrue(restaurantTableDAO.add(DataGenerator.fakeTable()));
        assertFalse(restaurantTableDAO.add(DataGenerator.getRandomTables(1).get(0)));
    }

    @Test
    void update() {
        RestaurantTable table = DataGenerator.fakeTable();
        table.setId("T001");
        table.setName("Updated Table");
        assertTrue(restaurantTableDAO.update(table));
    }

    @Test
    void getAll() {
        List<RestaurantTable> tables = restaurantTableDAO.getAll();
        assertFalse(tables.isEmpty());
    }

    @Test
    void getById() {
        assertNotNull(restaurantTableDAO.getById("T001"));
    }

    @Test
    void getByName() {
        List<RestaurantTable> tables = restaurantTableDAO.getByName("Bàn 07");
        assertFalse(tables.isEmpty());
    }

    @Test
    void getAllByReservationId() {
        List<RestaurantTable> tables = restaurantTableDAO.getAllByReservationId("DB240114033512660");
        assertFalse(tables.isEmpty());
    }

    @Test
    void getAllByOrderId() {
        List<RestaurantTable> tables = restaurantTableDAO.getAllByOrderId("HD240108150452214");
        assertFalse(tables.isEmpty());
    }

    @Test
    void getCountStatisticalOverviewTable() {
        int count = restaurantTableDAO.getCountStatisticalOverviewTable();
        assertTrue(count > 0);
    }

    @Test
    void getCountStatisticalOverviewTableEmpty() {
        int count = restaurantTableDAO.getCountStatisticalOverviewTableEmpty();
        assertTrue(count >= 0);
    }

    @Test
    void getCountStatisticalFloorTable() {
        int count = restaurantTableDAO.getCountStatisticalFloorTable(1);
        assertTrue(count > 0);
    }

    @Test
    void getCountStatisticalFloorTableEmpty() {
        int count = restaurantTableDAO.getCountStatisticalFloorTableEmpty(1);
        assertTrue(count >= 0);
    }

    @Test
    void getCountStatisticalFloorTablePreOrder() {
        int count = restaurantTableDAO.getCountStatisticalFloorTablePreOrder(1);
        assertTrue(count >= 0);
    }

    @Test
    void getCountStatisticalFloorTableOpen() {
        int count = restaurantTableDAO.getCountStatisticalFloorTableOpen(1);
        assertTrue(count >= 0);
    }

    @Test
    void getByCriteria() {
        List<RestaurantTable> tables = restaurantTableDAO.getByCriteria("1", "Available", "Type1", "4");
        assertFalse(tables.isEmpty());
    }

    @Test
    void getDistinctFloor() {
        List<String> floors = restaurantTableDAO.getDistinctFloor();
        assertFalse(floors.isEmpty());
    }

    @Test
    void getDistinctSeat() {
        List<String> seats = restaurantTableDAO.getDistinctSeat();
        assertFalse(seats.isEmpty());
    }

    @Test
    void getTopFloor() {
    }

    @Test
    void getReservedTables() {
    }

    @Test
    void getDistinctStatuses() {
        List<String> statuses = restaurantTableDAO.getDistinctStatuses();
        assertFalse(statuses.isEmpty());
    }

    @Test
    void getDistinctFloors() {
        List<Integer> floors = restaurantTableDAO.getDistinctFloors();
        assertFalse(floors.isEmpty());
    }

    @Test
    void getDistinctSeats() {
        List<Integer> seats = restaurantTableDAO.getDistinctSeats();
        assertFalse(seats.isEmpty());
    }

    @Test
    void getDistinctTableType() {
        List<String> tableTypes = restaurantTableDAO.getDistinctTableType();
        assertFalse(tableTypes.isEmpty());
    }

    @Test
    void getLookUpTable() {
        List<RestaurantTable> tables = restaurantTableDAO.getLookUpTable(1, "Table 1", 4, "Type1", "Available", 0);
        assertFalse(tables.isEmpty());
    }

    @Test
    void getCountLookUpTable() {
        int count = restaurantTableDAO.getCountLookUpTable(1, "Table 1", 4, "Type1", "Available");
        assertTrue(count >= 0);
    }

    @Test
    void getAllWithPagination() {
        List<RestaurantTable> tables = restaurantTableDAO.getAllWithPagination(0, 1);
        assertFalse(tables.isEmpty());
    }

    @Test
    void getByNameWithPagination() {
        List<RestaurantTable> tables = restaurantTableDAO.getByNameWithPagination("Bàn 10", 1, 10);
        assertFalse(tables.isEmpty());
    }

    @Test
    void getByFloorWithPagination() {
        List<RestaurantTable> tables = restaurantTableDAO.getByFloorWithPagination(0, 1, 10);
        assertFalse(tables.isEmpty());
    }

    @Test
    void countTotalTables() {
        int count = restaurantTableDAO.countTotalTables();
        assertTrue(count > 0);
    }

    @Test
    void countTotalTablesByFloor() {
        int count = restaurantTableDAO.countTotalTablesByFloor(2);
        assertTrue(count > 0);
    }

    @Test
    void countTotalTablesByName() {
        int count = restaurantTableDAO.countTotalTablesByName("Bàn 10");
        assertTrue(count > 0);
    }
}