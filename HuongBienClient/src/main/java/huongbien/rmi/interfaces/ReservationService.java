package huongbien.rmi.interfaces;

import huongbien.entity.Customer;
import huongbien.entity.FoodOrder;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Remote interface for Reservation operations.
 * This interface defines the operations that can be performed remotely on Reservation entities.
 */
public interface ReservationService extends Remote {
    
    /**
     * Gets all reservations.
     * 
     * @return List of all reservations
     * @throws RemoteException if a remote communication error occurs
     */
    List<Reservation> getAllReservations() throws RemoteException;
    
    /**
     * Gets a reservation by ID.
     * 
     * @param id The reservation ID
     * @return The reservation with the specified ID or null if not found
     * @throws RemoteException if a remote communication error occurs
     */
    Reservation getReservationById(String id) throws RemoteException;
    
    /**
     * Gets reservations by customer.
     * 
     * @param customer The customer to filter by
     * @return List of reservations for the specified customer
     * @throws RemoteException if a remote communication error occurs
     */
    List<Reservation> getReservationsByCustomer(Customer customer) throws RemoteException;
    
    /**
     * Gets reservations by status.
     * 
     * @param status The status to filter by
     * @return List of reservations with the specified status
     * @throws RemoteException if a remote communication error occurs
     */
    List<Reservation> getReservationsByStatus(ReservationStatus status) throws RemoteException;
    
    /**
     * Gets pending reservations for today.
     * 
     * @return List of pending reservations for today
     * @throws RemoteException if a remote communication error occurs
     */
    List<Reservation> getPendingReservationsForToday() throws RemoteException;
    
    /**
     * Gets reservations by date.
     * 
     * @param date The date to filter by
     * @return List of reservations for the specified date
     * @throws RemoteException if a remote communication error occurs
     */
    List<Reservation> getReservationsByDate(LocalDate date) throws RemoteException;
    
    /**
     * Creates a new reservation.
     * 
     * @param customer The customer making the reservation
     * @param tables The tables to reserve
     * @param date The reservation date
     * @param time The reservation time
     * @param partySize The party size
     * @param partyType The party type (e.g., dinner, celebration)
     * @param note Any special notes for the reservation
     * @param foodOrders Any pre-ordered food
     * @param deposit The deposit amount
     * @return The created reservation
     * @throws RemoteException if a remote communication error occurs
     */
    Reservation createReservation(
        Customer customer, 
        List<Table> tables, 
        LocalDate date, 
        LocalTime time, 
        int partySize,
        String partyType,
        String note, 
        List<FoodOrder> foodOrders, 
        double deposit
    ) throws RemoteException;
    
    /**
     * Updates reservation status.
     * 
     * @param id The reservation ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean updateReservationStatus(String id, ReservationStatus status) throws RemoteException;
    
    /**
     * Cancels a reservation and processes refund.
     * 
     * @param id The reservation ID
     * @param refundAmount The amount to refund
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean cancelReservation(String id, double refundAmount) throws RemoteException;
    
    /**
     * Updates food orders for a reservation.
     * 
     * @param reservationId The reservation ID
     * @param foodOrders The updated food orders
     * @return true if successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean updateFoodOrders(String reservationId, List<FoodOrder> foodOrders) throws RemoteException;
}