package huongbien.bus;

import huongbien.dao.TableTypeDAO;
import huongbien.entity.TableType;

import java.util.List;

public class TableTypeBUS {
    private final TableTypeDAO tableTypeDao;

    public TableTypeBUS() {
        tableTypeDao = new TableTypeDAO();
    }

    public List<TableType> getAllTableType() {
        return tableTypeDao.getAll();
    }

    public TableType getTableTypeName (String tableTypeId){ return tableTypeDao.getById(tableTypeId); }

    public String getTableTypeId (String tableTypeName){ return tableTypeDao.getTypeName(tableTypeName); }

    public TableType getTableType(String tableTypeId) {
        if (tableTypeId.isBlank() || tableTypeId.isEmpty()) return null;
        return tableTypeDao.getById(tableTypeId);
    }

    public boolean addTableType(TableType tableType) {
        if (tableType == null) return false;
        return tableTypeDao.add(tableType);
    }
}
