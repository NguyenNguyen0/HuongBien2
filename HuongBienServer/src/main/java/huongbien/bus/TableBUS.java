package huongbien.bus;

import huongbien.dao.TableDAO;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.entity.TableType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic layer for Table entity
 * Handles all the business logic related to tables
 */
public class TableBUS {
    
    private final TableDAO tableDAO;
    
    public TableBUS() {
        this.tableDAO = new TableDAO();
    }
    
    public List<Table> getAll() {
        return tableDAO.findAll();
    }
    
    public Table getById(String id) {
        return tableDAO.findById(id);
    }
    
    public List<Table> getByStatus(TableStatus status) {
        return tableDAO.findByStatus(status);
    }
    
    public List<Table> getByFloor(int floor) {
        return tableDAO.findByFloor(floor);
    }
    
    public List<Table> getBySeatCount(int seatCount) {
        return tableDAO.findBySeatCount(seatCount);
    }
    
    public List<Table> getByArea(String area) {
        return tableDAO.findByArea(area);
    }
    
    public List<Table> getByType(TableType type) {
        return tableDAO.findByType(type);
    }
    
    public List<Table> getFreeTables() {
        return tableDAO.findByStatus(TableStatus.AVAILABLE);
    }
    
    public List<Table> getByCapacityRange(int minCapacity, int maxCapacity) {
        return tableDAO.findByCapacityRange(minCapacity, maxCapacity);
    }
    
    /**
     * Gets tables available at a specific time
     * 
     * @param date The date of availability
     * @param time The start time
     * @param duration Duration in minutes
     * @return List of available tables
     */
    public List<Table> getAvailableTables(LocalDate date, LocalTime time, int duration) {
        // Get all tables from the database
        List<Table> allTables = getAll();
        
        // Filter tables based on availability for the specific date, time and duration
        // This implementation assumes that reservations are stored in another table
        // and would need to be checked against the requested time period
        return tableDAO.findAvailableTables(date, time, duration);
    }
    
    /**
     * Gets available tables with at least the given capacity
     * 
     * @param date The date of availability
     * @param time The start time
     * @param duration Duration in minutes
     * @param minCapacity Minimum capacity required
     * @return List of available tables meeting the capacity requirement
     */
    public List<Table> getAvailableTablesByCapacity(LocalDate date, LocalTime time, int duration, int minCapacity) {
        List<Table> availableTables = getAvailableTables(date, time, duration);
        
        // Filter tables by capacity
        return availableTables.stream()
                .filter(table -> table.getSeatCount() >= minCapacity)
                .collect(Collectors.toList());
    }
    
    /**
     * Reserves a list of tables for a specific reservation
     * 
     * @param tableIds IDs of the tables to reserve
     * @param reservationId The reservation ID
     * @param date The reservation date
     * @param time The reservation time
     * @param duration Duration in minutes
     * @return true if all tables were successfully reserved
     */
    public boolean reserveTables(List<String> tableIds, String reservationId, LocalDate date, LocalTime time, int duration) {
        boolean allSuccessful = true;
        
        for (String tableId : tableIds) {
            Table table = getById(tableId);
            if (table != null && table.getStatus() == TableStatus.AVAILABLE) {
                table.setStatus(TableStatus.RESERVED);
                // In a real implementation, we would also need to save the reservation details
                allSuccessful = allSuccessful && tableDAO.update(table);
            } else {
                allSuccessful = false;
            }
        }
        
        // In a real implementation, this would be transactional - all succeed or all fail
        return allSuccessful;
    }
    
    /**
     * Releases tables from a reservation
     * 
     * @param tableIds IDs of the tables to release
     * @param reservationId The reservation ID
     * @return true if all tables were successfully released
     */
    public boolean releaseTables(List<String> tableIds, String reservationId) {
        boolean allSuccessful = true;
        
        for (String tableId : tableIds) {
            Table table = getById(tableId);
            if (table != null) {
                table.setStatus(TableStatus.AVAILABLE);
                // In a real implementation, we would also need to update the reservation
                allSuccessful = allSuccessful && tableDAO.update(table);
            } else {
                allSuccessful = false;
            }
        }
        
        return allSuccessful;
    }
    
    /**
     * Adds a new table
     * 
     * @param table The table to add
     * @return The added table with any generated IDs
     */
    public Table add(Table table) {
        return tableDAO.insert(table);
    }
    
    /**
     * Updates an existing table
     * 
     * @param table The table to update
     * @return true if update was successful
     */
    public boolean update(Table table) {
        return tableDAO.update(table);
    }
    
    /**
     * Updates the status of a table
     * 
     * @param id The table ID
     * @param status The new status
     * @return true if update was successful
     */
    public boolean updateStatus(String id, TableStatus status) {
        Table table = getById(id);
        if (table != null) {
            table.setStatus(status);
            return tableDAO.update(table);
        }
        return false;
    }
    
    /**
     * Deletes a table
     * 
     * @param id The table ID to delete
     * @return true if deletion was successful
     */
    public boolean delete(String id) {
        return tableDAO.delete(id);
    }
    
    /**
     * Gets all table types from the database
     * 
     * @return List of all table types
     */
    public List<TableType> getAllTableTypes() {
        return tableDAO.findAllTableTypes();
    }
}