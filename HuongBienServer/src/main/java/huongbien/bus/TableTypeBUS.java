package huongbien.bus;

import huongbien.dao.impl.TableTypeDAO;
import huongbien.entity.TableType;

import java.rmi.RemoteException;
import java.util.List;

public class TableTypeBUS {
    private final TableTypeDAO tableTypeDao;

    public TableTypeBUS() {
        try {
            tableTypeDao = new TableTypeDAO();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TableType> getAllTableType() throws RemoteException {
        return tableTypeDao.getAll();
    }

    public TableType getTableTypeName (String tableTypeId) throws RemoteException {

        return tableTypeDao.getById(tableTypeId); }

    public String getTableTypeId (String tableTypeName) throws RemoteException { return tableTypeDao.getTypeName(tableTypeName); }

    public TableType getTableType(String tableTypeId) throws RemoteException {
        if (tableTypeId.isBlank() || tableTypeId.isEmpty()) return null;
        return tableTypeDao.getById(tableTypeId);
    }

    public boolean addTableType(TableType tableType) throws RemoteException {
        if (tableType == null) return false;
        return tableTypeDao.add(tableType);
    }
}
