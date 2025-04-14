package huongbien.service;

import huongbien.entity.Cuisine;
import huongbien.rmi.RmiClient;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

/**
 * Client-side adapter for the CuisineService.
 * Provides error handling and simplifies the use of the remote CuisineService.
 */
public class CuisineServiceAdapter {
    
    /**
     * Get all cuisines from the remote service.
     * 
     * @return List of all cuisines, or an empty list if an error occurs.
     */
    public static List<Cuisine> getAllCuisine() {
        try {
            return RmiClient.getCuisineService().getAllCuisine();
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving all cuisines", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get a cuisine by its ID.
     * 
     * @param cuisineId Cuisine ID to retrieve.
     * @return The cuisine if found, null otherwise.
     */
    public static Cuisine getCuisineById(String cuisineId) {
        try {
            return RmiClient.getCuisineService().getCuisineById(cuisineId);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving cuisine with ID: " + cuisineId, e);
            return null;
        }
    }
    
    /**
     * Get cuisines by name.
     * 
     * @param name Name to search for.
     * @return List of matching cuisines, or an empty list if an error occurs.
     */
    public static List<Cuisine> getCuisineByName(String name) {
        try {
            return RmiClient.getCuisineService().getCuisineByName(name);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving cuisines by name: " + name, e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get cuisines with pagination.
     * 
     * @param offset Starting index.
     * @param limit Maximum number of records to return.
     * @return Paginated list of cuisines, or an empty list if an error occurs.
     */
    public static List<Cuisine> getAllCuisineWithPagination(int offset, int limit) {
        try {
            return RmiClient.getCuisineService().getAllCuisineWithPagination(offset, limit);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving cuisines with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get cuisines by name with pagination.
     * 
     * @param offset Starting index.
     * @param limit Maximum number of records to return.
     * @param name Name to search for.
     * @return Paginated list of matching cuisines, or an empty list if an error occurs.
     */
    public static List<Cuisine> getCuisinesByNameWithPagination(int offset, int limit, String name) {
        try {
            return RmiClient.getCuisineService().getCuisinesByNameWithPagination(offset, limit, name);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving cuisines by name with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get cuisines by category with pagination.
     * 
     * @param offset Starting index.
     * @param limit Maximum number of records to return.
     * @param category Category to filter by.
     * @return Paginated list of matching cuisines, or an empty list if an error occurs.
     */
    public static List<Cuisine> getCuisinesByCategoryWithPagination(int offset, int limit, String category) {
        try {
            return RmiClient.getCuisineService().getCuisinesByCategoryWithPagination(offset, limit, category);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving cuisines by category with pagination", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Perform a cuisine lookup with filters.
     * 
     * @param name Name filter.
     * @param category Category filter.
     * @param pageIndex Page index for pagination.
     * @return List of matching cuisines, or an empty list if an error occurs.
     */
    public static List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex) {
        try {
            return RmiClient.getCuisineService().getLookUpCuisine(name, category, pageIndex);
        } catch (RemoteException e) {
            handleRemoteException("Error performing cuisine lookup", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get all cuisine names.
     * 
     * @return List of all cuisine names, or an empty list if an error occurs.
     */
    public static List<String> getAllCuisineNames() {
        try {
            return RmiClient.getCuisineService().getAllCuisineNames();
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving all cuisine names", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get cuisine names by category.
     * 
     * @param categoryName Category name.
     * @return List of cuisine names in the specified category, or an empty list if an error occurs.
     */
    public static List<String> getCuisineNamesByCategory(String categoryName) {
        try {
            return RmiClient.getCuisineService().getCuisineNamesByCategory(categoryName);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving cuisine names by category", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get all cuisine categories.
     * 
     * @return List of cuisine categories, or an empty list if an error occurs.
     */
    public static List<String> getCuisineCategory() {
        try {
            return RmiClient.getCuisineService().getCuisineCategory();
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving cuisine categories", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Update a cuisine's information.
     * 
     * @param cuisine Cuisine with updated information.
     * @return true if successful, false otherwise.
     */
    public static boolean updateCuisineInfo(Cuisine cuisine) {
        try {
            return RmiClient.getCuisineService().updateCuisineInfo(cuisine);
        } catch (RemoteException e) {
            handleRemoteException("Error updating cuisine information", e);
            return false;
        }
    }
    
    /**
     * Add a new cuisine.
     * 
     * @param cuisine Cuisine to add.
     * @return true if successful, false otherwise.
     */
    public static boolean addCuisine(Cuisine cuisine) {
        try {
            return RmiClient.getCuisineService().addCuisine(cuisine);
        } catch (RemoteException e) {
            handleRemoteException("Error adding cuisine", e);
            return false;
        }
    }
    
    /**
     * Stop selling a cuisine.
     * 
     * @param cuisineId ID of the cuisine to stop selling.
     * @return true if successful, false otherwise.
     */
    public static boolean stopSellCuisine(String cuisineId) {
        try {
            return RmiClient.getCuisineService().stopSellCuisine(cuisineId);
        } catch (RemoteException e) {
            handleRemoteException("Error stopping sale of cuisine", e);
            return false;
        }
    }
    
    /**
     * Handle RemoteException by logging and possibly displaying an error message.
     * 
     * @param message Error message to log.
     * @param e The exception that occurred.
     */
    private static void handleRemoteException(String message, RemoteException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        
        // TODO: Add proper logging and/or user notification
    }
}