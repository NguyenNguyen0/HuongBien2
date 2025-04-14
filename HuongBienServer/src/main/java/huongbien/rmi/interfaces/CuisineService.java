package huongbien.rmi.interfaces;

import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface for cuisine management services
 */
public interface CuisineService extends Remote {
    
    // Cuisine retrieval
    List<Cuisine> getAllCuisine() throws RemoteException;
    Cuisine getCuisineById(String cuisineId) throws RemoteException;
    List<Cuisine> getCuisineByName(String name) throws RemoteException;
    List<Cuisine> getAllCuisineWithPagination(int offset, int limit) throws RemoteException;
    List<Cuisine> getCuisinesByNameWithPagination(int offset, int limit, String name) throws RemoteException;
    List<Cuisine> getCuisinesByCategoryWithPagination(int offset, int limit, String category) throws RemoteException;
    
    // Cuisine lookup and filtering
    List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex) throws RemoteException;
    
    // Names and categories retrieval
    List<String> getAllCuisineNames() throws RemoteException;
    List<String> getCuisineNamesByCategory(String categoryName) throws RemoteException;
    List<String> getCuisineCategory() throws RemoteException;
    
    // Cuisine counts
    int countTotalCuisine() throws RemoteException;
    int countCuisinesByName(String name) throws RemoteException;
    int countCuisinesByCategory(String category) throws RemoteException;
    int getCountLookUpCuisine(String name, String category) throws RemoteException;
    
    // Cuisine management
    boolean addCuisine(Cuisine cuisine) throws RemoteException;
    boolean updateCuisineInfo(Cuisine cuisine) throws RemoteException;
    boolean stopSellCuisine(String cuisineId) throws RemoteException;
}