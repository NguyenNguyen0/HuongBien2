package huongbien.service;

import huongbien.entity.Table;
import huongbien.rmi.RmiClient;
import huongbien.util.ExceptionHandler;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for TableService remote interface.
 * Provides a simplified API for UI components and handles remote exceptions.
 */
public class TableServiceAdapter {
    
    /**
     * Gets all tables from the server.
     * 
     * @return List of all tables or empty list if an error occurs
     */
    public static List<Table> getAllTables() {
        try {
            return RmiClient.getTableService().getAllTables();
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to retrieve tables", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets a table by ID from the server.
     * 
     * @param id The table ID
     * @return The table with the specified ID or null if not found or an error occurs
     */
    public static Table getTableById(String id) {
        try {
            return RmiClient.getTableService().getTableById(id);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to retrieve table with ID: " + id, 
                e
            );
            return null;
        }
    }
    
    /**
     * Gets tables by area from the server.
     * 
     * @param area The area to filter by
     * @return List of tables in the specified area or empty list if an error occurs
     */
    public static List<Table> getTablesByArea(String area) {
        try {
            return RmiClient.getTableService().getTablesByArea(area);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to retrieve tables by area: " + area, 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets tables by capacity from the server.
     * 
     * @param minCapacity The minimum capacity
     * @param maxCapacity The maximum capacity
     * @return List of tables within the capacity range or empty list if an error occurs
     */
    public static List<Table> getTablesByCapacity(int minCapacity, int maxCapacity) {
        try {
            return RmiClient.getTableService().getTablesByCapacity(minCapacity, maxCapacity);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to retrieve tables by capacity range", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets available tables for a specific date and time from the server.
     * 
     * @param date The date to check
     * @param time The time to check
     * @param duration The expected duration in minutes
     * @return List of available tables or empty list if an error occurs
     */
    public static List<Table> getAvailableTables(LocalDate date, LocalTime time, int duration) {
        try {
            return RmiClient.getTableService().getAvailableTables(date, time, duration);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to retrieve available tables", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets available tables for a specific date and time with minimum capacity from the server.
     * 
     * @param date The date to check
     * @param time The time to check
     * @param duration The expected duration in minutes
     * @param minCapacity The minimum required capacity
     * @return List of available tables meeting the capacity requirement or empty list if an error occurs
     */
    public static List<Table> getAvailableTablesByCapacity(LocalDate date, LocalTime time, int duration, int minCapacity) {
        try {
            return RmiClient.getTableService().getAvailableTablesByCapacity(date, time, duration, minCapacity);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to retrieve available tables by capacity", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Reserves tables for a specific date and time on the server.
     * 
     * @param tableIds List of table IDs to reserve
     * @param reservationId The reservation ID
     * @param date The date of the reservation
     * @param time The time of the reservation
     * @param duration The expected duration in minutes
     * @return true if successful, false otherwise
     */
    public static boolean reserveTables(List<String> tableIds, String reservationId, LocalDate date, LocalTime time, int duration) {
        try {
            return RmiClient.getTableService().reserveTables(tableIds, reservationId, date, time, duration);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to reserve tables", 
                e
            );
            return false;
        }
    }
    
    /**
     * Releases tables from a reservation on the server.
     * 
     * @param tableIds List of table IDs to release
     * @param reservationId The reservation ID
     * @return true if successful, false otherwise
     */
    public static boolean releaseTables(List<String> tableIds, String reservationId) {
        try {
            return RmiClient.getTableService().releaseTables(tableIds, reservationId);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to release tables", 
                e
            );
            return false;
        }
    }
    
    /**
     * Updates a table's information on the server.
     * 
     * @param table The updated table information
     * @return true if successful, false otherwise
     */
    public static boolean updateTable(Table table) {
        try {
            return RmiClient.getTableService().updateTable(table);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to update table", 
                e
            );
            return false;
        }
    }
    
    /**
     * Adds a new table to the server.
     * 
     * @param table The table to add
     * @return The added table with generated ID or null if an error occurs
     */
    public static Table addTable(Table table) {
        try {
            return RmiClient.getTableService().addTable(table);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to add table", 
                e
            );
            return null;
        }
    }
    
    /**
     * Deletes a table from the server.
     * 
     * @param id The ID of the table to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteTable(String id) {
        try {
            return RmiClient.getTableService().deleteTable(id);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Table Service Error", 
                "Failed to delete table with ID: " + id, 
                e
            );
            return false;
        }
    }
}