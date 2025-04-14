package huongbien.service;

import huongbien.entity.Reservation;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.rmi.RmiClient;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Client-side adapter for the TableService.
 * Provides error handling and simplifies the use of the remote TableService.
 */
public class TableServiceAdapter {

    /**
     * Get all tables from the remote service.
     * 
     * @return List of all tables, or an empty list if an error occurs.
     */
    public static List<Table> getAllTables() {
        try {
            return RmiClient.getTableService().getAllTables();
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving all tables", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get a table by its ID.
     * 
     * @param tableId Table ID to retrieve.
     * @return The table if found, null otherwise.
     */
    public static Table getTableById(String tableId) {
        try {
            return RmiClient.getTableService().getTableById(tableId);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving table with ID: " + tableId, e);
            return null;
        }
    }
    
    /**
     * Get tables with pagination.
     * 
     * @param offset Starting index.
     * @param limit Maximum number of records to return.
     * @return List of tables, or an empty list if an error occurs.
     */
    public static List<Table> getTablesWithPagination(int offset, int limit) {
        try {
            return RmiClient.getTableService().getTablesWithPagination(offset, limit);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving tables with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get tables by floor with pagination.
     * 
     * @param floor Floor number.
     * @param offset Starting index.
     * @param limit Maximum number of records to return.
     * @return List of tables, or an empty list if an error occurs.
     */
    public static List<Table> getTablesByFloorWithPagination(int floor, int offset, int limit) {
        try {
            return RmiClient.getTableService().getTablesByFloorWithPagination(floor, offset, limit);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving tables by floor with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get tables by name with pagination.
     * 
     * @param offset Starting index.
     * @param limit Maximum number of records to return.
     * @param name Name to search for.
     * @return List of tables, or an empty list if an error occurs.
     */
    public static List<Table> getTablesByNameWithPagination(int offset, int limit, String name) {
        try {
            return RmiClient.getTableService().getTablesByNameWithPagination(offset, limit, name);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving tables by name with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get all tables associated with an order.
     * 
     * @param orderId Order ID.
     * @return List of tables, or an empty list if an error occurs.
     */
    public static List<Table> getAllTableByOrderId(String orderId) {
        try {
            return RmiClient.getTableService().getAllTableByOrderId(orderId);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving tables for order ID: " + orderId, e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get all tables associated with a reservation.
     * 
     * @param reservationId Reservation ID.
     * @return List of tables, or an empty list if an error occurs.
     */
    public static List<Table> getAllTableByReservationId(String reservationId) {
        try {
            return RmiClient.getTableService().getAllTableByReservationId(reservationId);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving tables for reservation ID: " + reservationId, e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Perform a table lookup with filters.
     * 
     * @param floor Floor number filter.
     * @param name Name filter.
     * @param seat Seat count filter.
     * @param type Table type filter.
     * @param status Status filter.
     * @param pageIndex Page index for pagination.
     * @return List of matching tables, or an empty list if an error occurs.
     */
    public static List<Table> getLookUpTable(int floor, String name, int seat, String type, String status, int pageIndex) {
        try {
            return RmiClient.getTableService().getLookUpTable(floor, name, seat, type, status, pageIndex);
        } catch (RemoteException e) {
            handleRemoteException("Error performing table lookup", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Update a table's information.
     * 
     * @param table Table with updated information.
     * @return true if successful, false otherwise.
     */
    public static boolean updateTableInfo(Table table) {
        try {
            return RmiClient.getTableService().updateTableInfo(table);
        } catch (RemoteException e) {
            handleRemoteException("Error updating table information", e);
            return false;
        }
    }
    
    /**
     * Add a new table.
     * 
     * @param table Table to add.
     * @return true if successful, false otherwise.
     */
    public static boolean addTable(Table table) {
        try {
            return RmiClient.getTableService().addTable(table);
        } catch (RemoteException e) {
            handleRemoteException("Error adding table", e);
            return false;
        }
    }
    
    /**
     * Update a table's status.
     * 
     * @param id Table ID.
     * @param status New status.
     */
    public static void updateStatusTable(String id, TableStatus status) {
        try {
            RmiClient.getTableService().updateStatusTable(id, status);
        } catch (RemoteException e) {
            handleRemoteException("Error updating table status", e);
        }
    }
    
    /**
     * Count total tables.
     * 
     * @return Total number of tables, or 0 if an error occurs.
     */
    public static int countTotalTables() {
        try {
            return RmiClient.getTableService().countTotalTables();
        } catch (RemoteException e) {
            handleRemoteException("Error counting total tables", e);
            return 0;
        }
    }
    
    /**
     * Get distinct table statuses.
     * 
     * @return List of distinct statuses, or an empty list if an error occurs.
     */
    public static List<String> getDistinctStatuses() {
        try {
            return RmiClient.getTableService().getDistinctStatuses();
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving distinct table statuses", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Handle RemoteException by logging and possibly displaying an error message.
     * 
     * @param message Error message to log.
     * @param e The exception that occurred.
     */
    private static void handleRemoteException(String message, RemoteException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        
        // TODO: Add proper logging and/or user notification
    }
}