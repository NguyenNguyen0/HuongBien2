package huongbien.dao.remote;

import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ICuisineDAO extends Remote {
    List<String> getAllCuisineNames() throws RemoteException;

    List<String> getCuisineNamesByCategory(String categoryName) throws RemoteException;

    List<Cuisine> getByName(String name) throws RemoteException;

    List<Cuisine> getAll() throws RemoteException;

    List<Cuisine> getLookUpCuisine(String name, String category) throws RemoteException;

    List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex) throws RemoteException;

    int getCountLookUpCuisine(String name, String category) throws RemoteException;

    Cuisine getById(String id) throws RemoteException;

    List<String> getCuisineCategory() throws RemoteException;

    List<Cuisine> getByCategoryWithPagination(String category, int offset, int limit) throws RemoteException;

    List<Cuisine> getByNameWithPagination(int offset, int limit, String name) throws RemoteException;

    List<Cuisine> getAllWithPagination(int offset, int limit) throws RemoteException;

    int countCuisinesByName(String name) throws RemoteException;

    boolean updateCuisineStatus(String cuisineId, CuisineStatus status) throws RemoteException;

    int countCuisinesByCategory(String category) throws RemoteException;

    int countTotal() throws RemoteException;

    boolean addCuisine(Cuisine cuisine) throws RemoteException;

    boolean updateCuisine(Cuisine cuisine) throws RemoteException;

    boolean add(Cuisine cuisine) throws RemoteException;

    boolean update(Cuisine cuisine) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}