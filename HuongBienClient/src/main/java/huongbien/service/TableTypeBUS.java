package huongbien.service;

import huongbien.dao.remote.ITableTypeDAO;
import huongbien.entity.TableType;
import huongbien.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class TableTypeBUS {
    private final ITableTypeDAO tableTypeDao;

    public TableTypeBUS() {
        try {
            tableTypeDao = RMIClient.getInstance().getTableTypeDAO();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TableType> getAllTableType() throws RemoteException {
        return tableTypeDao.getAll();
    }

    public TableType getTableTypeName(String tableTypeId) throws RemoteException {

        return tableTypeDao.getById(tableTypeId);
    }

    public String getTableTypeId(String tableTypeName) throws RemoteException {
        return tableTypeDao.getTypeName(tableTypeName);
    }

    public TableType getTableType(String tableTypeId) throws RemoteException {
        if (tableTypeId.isBlank()) return null;
        return tableTypeDao.getById(tableTypeId);
    }

    public boolean addTableType(TableType tableType) throws RemoteException {
        if (tableType == null) return false;
        return tableTypeDao.add(tableType);
    }
}
