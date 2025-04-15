package huongbien.service;

import huongbien.entity.Category;
import huongbien.entity.Cuisine;
import huongbien.entity.CuisineStatus;
import huongbien.rmi.RmiClient;
import huongbien.util.ExceptionHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for CuisineService remote interface.
 * Provides a simplified API for UI components and handles remote exceptions.
 */
public class CuisineServiceAdapter {

    /**
     * Gets all cuisines.
     * 
     * @return List of all cuisines or empty list if an error occurs
     */
    public static List<Cuisine> getAllCuisines() {
        try {
            return RmiClient.getCuisineService().getAllCuisines();
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to retrieve cuisines", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets a cuisine by its ID.
     * 
     * @param id The cuisine ID
     * @return The cuisine with the specified ID or null if not found or an error occurs
     */
    public static Cuisine getCuisineById(String id) {
        try {
            return RmiClient.getCuisineService().getCuisineById(id);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to retrieve cuisine with ID: " + id, 
                e
            );
            return null;
        }
    }
    
    /**
     * Gets cuisines by name.
     * 
     * @param name The name to search for
     * @return List of cuisines matching the name or empty list if an error occurs
     */
    public static List<Cuisine> getCuisinesByName(String name) {
        try {
            return RmiClient.getCuisineService().getCuisinesByName(name);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to retrieve cuisines by name: " + name, 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets cuisines by category.
     * 
     * @param category The category to filter by
     * @return List of cuisines in the specified category or empty list if an error occurs
     */
    public static List<Cuisine> getCuisinesByCategory(Category category) {
        try {
            return RmiClient.getCuisineService().getCuisinesByCategory(category);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to retrieve cuisines by category", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets cuisines by status.
     * 
     * @param status The status to filter by
     * @return List of cuisines with the specified status or empty list if an error occurs
     */
    public static List<Cuisine> getCuisinesByStatus(CuisineStatus status) {
        try {
            return RmiClient.getCuisineService().getCuisinesByStatus(status);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to retrieve cuisines by status", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets cuisines by price range.
     * 
     * @param minPrice The minimum price
     * @param maxPrice The maximum price
     * @return List of cuisines in the specified price range or empty list if an error occurs
     */
    public static List<Cuisine> getCuisinesByPriceRange(double minPrice, double maxPrice) {
        try {
            return RmiClient.getCuisineService().getCuisinesByPriceRange(minPrice, maxPrice);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to retrieve cuisines by price range", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Adds a new cuisine.
     * 
     * @param cuisine The cuisine to add
     * @return The added cuisine with its ID set, or null if an error occurs
     */
    public static Cuisine addCuisine(Cuisine cuisine) {
        try {
            return RmiClient.getCuisineService().addCuisine(cuisine);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to add cuisine", 
                e
            );
            return null;
        }
    }
    
    /**
     * Updates an existing cuisine.
     * 
     * @param cuisine The cuisine with updated information
     * @return true if successful, false otherwise
     */
    public static boolean updateCuisine(Cuisine cuisine) {
        try {
            return RmiClient.getCuisineService().updateCuisine(cuisine);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to update cuisine", 
                e
            );
            return false;
        }
    }
    
    /**
     * Updates cuisine status.
     * 
     * @param id The cuisine ID
     * @param status The new status
     * @return true if successful, false otherwise
     */
    public static boolean updateCuisineStatus(String id, CuisineStatus status) {
        try {
            return RmiClient.getCuisineService().updateCuisineStatus(id, status);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to update cuisine status", 
                e
            );
            return false;
        }
    }
    
    /**
     * Gets all cuisine categories.
     * 
     * @return List of all categories or empty list if an error occurs
     */
    public static List<Category> getAllCategories() {
        try {
            return RmiClient.getCuisineService().getAllCategories();
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to retrieve categories", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets a category by its ID.
     * 
     * @param id The category ID
     * @return The category with the specified ID or null if not found or an error occurs
     */
    public static Category getCategoryById(String id) {
        try {
            return RmiClient.getCuisineService().getCategoryById(id);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to retrieve category with ID: " + id, 
                e
            );
            return null;
        }
    }
    
    /**
     * Adds a new category.
     * 
     * @param category The category to add
     * @return true if successful, false otherwise
     */
    public static boolean addCategory(Category category) {
        try {
            return RmiClient.getCuisineService().addCategory(category);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to add category", 
                e
            );
            return false;
        }
    }
    
    /**
     * Updates an existing category.
     * 
     * @param category The category with updated information
     * @return true if successful, false otherwise
     */
    public static boolean updateCategory(Category category) {
        try {
            return RmiClient.getCuisineService().updateCategory(category);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Cuisine Service Error", 
                "Failed to update category", 
                e
            );
            return false;
        }
    }
}