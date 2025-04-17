package huongbien.dao.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface IGenericDAO<T> extends Remote {
    boolean add(T object) throws RemoteException;

    boolean update(T object) throws RemoteException;

    boolean delete(T object) throws RemoteException;

    T findOne(String jpql, Class<T> clazz, Object... params) throws RemoteException;

    List<T> findMany(String jpql, Class<T> clazz, Object... params) throws RemoteException;

    List<T> findMany(String jpql, Class<T> clazz, Map<String, Object> parameters) throws RemoteException;

    <R> List<R> findManyWithPagination(String jpql, Class<R> type, Map<String, Object> parameters, int offset, int limit) throws RemoteException;

    <R> List<R> executeQuery(String query, Class<R> resultClass, Object... params) throws RemoteException;

    int count(String jpql, Object... params) throws RemoteException;
}