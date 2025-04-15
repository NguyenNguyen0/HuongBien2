package huongbien.rmi.interfaces;

import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.entity.TableType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Remote interface for Table operations.
 * This interface defines the operations that can be performed remotely on Table entities.
 */
public interface TableService extends Remote {
    
    /**
     * Gets all tables.
     * 
     * @return List of all tables
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getAllTables() throws RemoteException;
    
    /**
     * Gets a table by ID.
     * 
     * @param id The table ID
     * @return The table with the specified ID or null if not found
     * @throws RemoteException if a remote communication error occurs
     */
    Table getTableById(String id) throws RemoteException;
    
    /**
     * Gets tables by status.
     * 
     * @param status The table status
     * @return List of tables with the specified status
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getTablesByStatus(TableStatus status) throws RemoteException;
    
    /**
     * Gets tables by floor.
     * 
     * @param floor The floor number
     * @return List of tables on the specified floor
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getTablesByFloor(int floor) throws RemoteException;
    
    /**
     * Gets tables by seat count.
     * 
     * @param seatCount The seat count
     * @return List of tables with the specified seat count
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getTablesBySeatCount(int seatCount) throws RemoteException;
    
    /**
     * Gets tables by type.
     * 
     * @param type The table type
     * @return List of tables with the specified type
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getTablesByType(TableType type) throws RemoteException;
    
    /**
     * Gets all free tables.
     * 
     * @return List of tables that are currently free
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getFreeTables() throws RemoteException;
    
    /**
     * Gets tables by area.
     * 
     * @param area The area to filter by
     * @return List of tables in the specified area
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getTablesByArea(String area) throws RemoteException;
    
    /**
     * Gets tables by capacity.
     * 
     * @param minCapacity The minimum capacity
     * @param maxCapacity The maximum capacity
     * @return List of tables within the capacity range
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getTablesByCapacity(int minCapacity, int maxCapacity) throws RemoteException;
    
    /**
     * Gets available tables for a specific date and time.
     * 
     * @param date The date to check
     * @param time The time to check
     * @param duration The expected duration in minutes
     * @return List of available tables
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getAvailableTables(LocalDate date, LocalTime time, int duration) throws RemoteException;
    
    /**
     * Gets available tables for a specific date and time with minimum capacity.
     * 
     * @param date The date to check
     * @param time The time to check
     * @param duration The expected duration in minutes
     * @param minCapacity The minimum required capacity
     * @return List of available tables meeting the capacity requirement
     * @throws RemoteException if a remote communication error occurs
     */
    List<Table> getAvailableTablesByCapacity(LocalDate date, LocalTime time, int duration, int minCapacity) throws RemoteException;
    
    /**
     * Reserves tables for a specific date and time.
     * 
     * @param tableIds List of table IDs to reserve
     * @param reservationId The reservation ID
     * @param date The date of the reservation
     * @param time The time of the reservation
     * @param duration The expected duration in minutes
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean reserveTables(List<String> tableIds, String reservationId, LocalDate date, LocalTime time, int duration) throws RemoteException;
    
    /**
     * Releases tables from a reservation.
     * 
     * @param tableIds List of table IDs to release
     * @param reservationId The reservation ID
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean releaseTables(List<String> tableIds, String reservationId) throws RemoteException;
    
    /**
     * Adds a new table.
     * 
     * @param table The table to add
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean addTable(Table table) throws RemoteException;
    
    /**
     * Updates a table's information.
     * 
     * @param table The updated table information
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean updateTable(Table table) throws RemoteException;
    
    /**
     * Updates a table's status.
     * 
     * @param id The table ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean updateTableStatus(String id, TableStatus status) throws RemoteException;
    
    /**
     * Gets all table types.
     * 
     * @return List of all table types
     * @throws RemoteException if a remote communication error occurs
     */
    List<TableType> getAllTableTypes() throws RemoteException;
    
    /**
     * Deletes a table.
     * 
     * @param id The ID of the table to delete
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean deleteTable(String id) throws RemoteException;
}