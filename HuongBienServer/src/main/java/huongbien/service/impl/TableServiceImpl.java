package huongbien.service.impl;

import huongbien.bus.TableBUS;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.entity.TableType;
import huongbien.rmi.interfaces.TableService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Implementation of the TableService RMI interface.
 * Delegates calls to the TableBUS business logic class.
 */
public class TableServiceImpl extends UnicastRemoteObject implements TableService {
    
    private final TableBUS tableBUS;
    
    public TableServiceImpl() throws RemoteException {
        super();
        this.tableBUS = new TableBUS();
    }
    
    @Override
    public List<Table> getAllTables() throws RemoteException {
        System.out.println("RMI: getAllTables() called");
        return tableBUS.getAll();
    }
    
    @Override
    public Table getTableById(String id) throws RemoteException {
        System.out.println("RMI: getTableById() called with ID: " + id);
        try {
            return tableBUS.getById(id);
        } catch (Exception e) {
            System.err.println("Error in getTableById(): " + e.getMessage());
            throw new RemoteException("Failed to get table by ID", e);
        }
    }

    @Override
    public List<Table> getTablesByStatus(TableStatus status) throws RemoteException {
        System.out.println("RMI: getTablesByStatus() called with status: " + status);
        try {
            return tableBUS.getByStatus(status);
        } catch (Exception e) {
            System.err.println("Error in getTablesByStatus(): " + e.getMessage());
            throw new RemoteException("Failed to get tables by status", e);
        }
    }

    @Override
    public List<Table> getTablesByFloor(int floor) throws RemoteException {
        System.out.println("RMI: getTablesByFloor() called with floor: " + floor);
        try {
            return tableBUS.getByFloor(floor);
        } catch (Exception e) {
            System.err.println("Error in getTablesByFloor(): " + e.getMessage());
            throw new RemoteException("Failed to get tables by floor", e);
        }
    }

    @Override
    public List<Table> getTablesBySeatCount(int seatCount) throws RemoteException {
        System.out.println("RMI: getTablesBySeatCount() called with seat count: " + seatCount);
        try {
            return tableBUS.getBySeatCount(seatCount);
        } catch (Exception e) {
            System.err.println("Error in getTablesBySeatCount(): " + e.getMessage());
            throw new RemoteException("Failed to get tables by seat count", e);
        }
    }

    @Override
    public List<Table> getTablesByType(TableType type) throws RemoteException {
        System.out.println("RMI: getTablesByType() called with type: " + type);
        try {
            return tableBUS.getByType(type);
        } catch (Exception e) {
            System.err.println("Error in getTablesByType(): " + e.getMessage());
            throw new RemoteException("Failed to get tables by type", e);
        }
    }

    @Override
    public List<Table> getFreeTables() throws RemoteException {
        System.out.println("RMI: getFreeTables() called");
        try {
            return tableBUS.getFreeTables();
        } catch (Exception e) {
            System.err.println("Error in getFreeTables(): " + e.getMessage());
            throw new RemoteException("Failed to get free tables", e);
        }
    }
    
    @Override
    public List<Table> getTablesByArea(String area) throws RemoteException {
        System.out.println("RMI: getTablesByArea() called with area: " + area);
        return tableBUS.getByArea(area);
    }
    
    @Override
    public List<Table> getTablesByCapacity(int minCapacity, int maxCapacity) throws RemoteException {
        System.out.println("RMI: getTablesByCapacity() called with capacity range: " + minCapacity + " - " + maxCapacity);
        return tableBUS.getByCapacityRange(minCapacity, maxCapacity);
    }
    
    @Override
    public List<Table> getAvailableTables(LocalDate date, LocalTime time, int duration) throws RemoteException {
        System.out.println("RMI: getAvailableTables() called for date: " + date + ", time: " + time + ", duration: " + duration);
        try {
            return tableBUS.getAvailableTables(date, time, duration);
        } catch (Exception e) {
            System.err.println("Error in getAvailableTables(): " + e.getMessage());
            throw new RemoteException("Failed to get available tables", e);
        }
    }
    
    @Override
    public List<Table> getAvailableTablesByCapacity(LocalDate date, LocalTime time, int duration, int minCapacity) throws RemoteException {
        System.out.println("RMI: getAvailableTablesByCapacity() called for date: " + date + 
                          ", time: " + time + ", duration: " + duration + 
                          ", minCapacity: " + minCapacity);
        try {
            return tableBUS.getAvailableTablesByCapacity(date, time, duration, minCapacity);
        } catch (Exception e) {
            System.err.println("Error in getAvailableTablesByCapacity(): " + e.getMessage());
            throw new RemoteException("Failed to get available tables by capacity", e);
        }
    }
    
    @Override
    public boolean reserveTables(List<String> tableIds, String reservationId, LocalDate date, LocalTime time, int duration) throws RemoteException {
        System.out.println("RMI: reserveTables() called for reservation ID: " + reservationId);
        try {
            return tableBUS.reserveTables(tableIds, reservationId, date, time, duration);
        } catch (Exception e) {
            System.err.println("Error in reserveTables(): " + e.getMessage());
            throw new RemoteException("Failed to reserve tables", e);
        }
    }
    
    @Override
    public boolean releaseTables(List<String> tableIds, String reservationId) throws RemoteException {
        System.out.println("RMI: releaseTables() called for reservation ID: " + reservationId);
        try {
            return tableBUS.releaseTables(tableIds, reservationId);
        } catch (Exception e) {
            System.err.println("Error in releaseTables(): " + e.getMessage());
            throw new RemoteException("Failed to release tables", e);
        }
    }

    @Override
    public boolean addTable(Table table) throws RemoteException {
        System.out.println("RMI: addTable() called");
        try {
            Table addedTable = tableBUS.add(table);
            return addedTable != null;
        } catch (Exception e) {
            System.err.println("Error in addTable(): " + e.getMessage());
            throw new RemoteException("Failed to add table", e);
        }
    }
    
    @Override
    public boolean updateTable(Table table) throws RemoteException {
        System.out.println("RMI: updateTable() called for table ID: " + table.getId());
        try {
            return tableBUS.update(table);
        } catch (Exception e) {
            System.err.println("Error in updateTable(): " + e.getMessage());
            throw new RemoteException("Failed to update table", e);
        }
    }

    @Override
    public boolean updateTableStatus(String id, TableStatus status) throws RemoteException {
        System.out.println("RMI: updateTableStatus() called for table ID: " + id + " with status: " + status);
        try {
            return tableBUS.updateStatus(id, status);
        } catch (Exception e) {
            System.err.println("Error in updateTableStatus(): " + e.getMessage());
            throw new RemoteException("Failed to update table status", e);
        }
    }

    @Override
    public List<TableType> getAllTableTypes() throws RemoteException {
        System.out.println("RMI: getAllTableTypes() called");
        try {
            return tableBUS.getAllTableTypes();
        } catch (Exception e) {
            System.err.println("Error in getAllTableTypes(): " + e.getMessage());
            throw new RemoteException("Failed to get all table types", e);
        }
    }
    
    @Override
    public boolean deleteTable(String id) throws RemoteException {
        System.out.println("RMI: deleteTable() called for table ID: " + id);
        try {
            return tableBUS.delete(id);
        } catch (Exception e) {
            System.err.println("Error in deleteTable(): " + e.getMessage());
            throw new RemoteException("Failed to delete table", e);
        }
    }
}