package huongbien.dao.remote;

import huongbien.entity.TableType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ITableTypeDAO extends Remote {
    List<TableType> getAll() throws RemoteException;

    TableType getById(String id) throws RemoteException;

    TableType getByName(String name) throws RemoteException;

    String getTypeName(String name) throws RemoteException;

    List<String> getDistinctTableType() throws RemoteException;

    boolean add(TableType tableType) throws RemoteException;

    boolean update(TableType tableType) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}