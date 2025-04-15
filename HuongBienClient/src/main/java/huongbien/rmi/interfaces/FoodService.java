package huongbien.rmi.interfaces;

import huongbien.entity.Food;
import huongbien.entity.FoodCategory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface for managing food-related operations.
 */
public interface FoodService extends Remote {
    /**
     * Retrieves all food items.
     * 
     * @return List of all food items
     * @throws RemoteException If a remote communication error occurs
     */
    List<Food> getAllFoods() throws RemoteException;
    
    /**
     * Retrieves a food item by its ID.
     * 
     * @param foodId The ID of the food item
     * @return The food item, or null if not found
     * @throws RemoteException If a remote communication error occurs
     */
    Food getFoodById(int foodId) throws RemoteException;
    
    /**
     * Saves a new or updated food item.
     * 
     * @param food The food item to save
     * @return The saved food item with updated ID if it was a new item
     * @throws RemoteException If a remote communication error occurs
     */
    Food saveFood(Food food) throws RemoteException;
    
    /**
     * Deletes a food item by its ID.
     * 
     * @param foodId The ID of the food item to delete
     * @return true if deleted, false if not found
     * @throws RemoteException If a remote communication error occurs
     */
    boolean deleteFood(int foodId) throws RemoteException;
    
    /**
     * Retrieves all food categories.
     * 
     * @return List of all food categories
     * @throws RemoteException If a remote communication error occurs
     */
    List<FoodCategory> getAllCategories() throws RemoteException;
    
    /**
     * Retrieves all food items in a specific category.
     * 
     * @param categoryId The category ID
     * @return List of food items in the category
     * @throws RemoteException If a remote communication error occurs
     */
    List<Food> getFoodsByCategory(int categoryId) throws RemoteException;
}