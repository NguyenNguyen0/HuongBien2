package huongbien.dao.remote;

import huongbien.entity.Category;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ICategoryDAO extends Remote {
    Category getById(String id) throws RemoteException;

    List<String> getAllCategoryNames() throws RemoteException;

    Category getCategoryName(String name) throws RemoteException;

    List<Category> getAll() throws RemoteException;

    List<Category> getByName(String name) throws RemoteException;

    int countAll() throws RemoteException;

    boolean add(Category category) throws RemoteException;

    boolean update(Category category) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}