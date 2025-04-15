package huongbien.service;

import huongbien.entity.Customer;
import huongbien.entity.FoodOrder;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.rmi.RmiClient;
import huongbien.util.ExceptionHandler;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for ReservationService remote interface.
 * Provides a simplified API for UI components and handles remote exceptions.
 */
public class ReservationServiceAdapter {

    /**
     * Gets all reservations.
     * 
     * @return List of all reservations or empty list if an error occurs
     */
    public static List<Reservation> getAllReservations() {
        try {
            return RmiClient.getReservationService().getAllReservations();
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to retrieve reservations", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets a reservation by ID.
     * 
     * @param id The reservation ID
     * @return The reservation with the specified ID or null if not found or an error occurs
     */
    public static Reservation getReservationById(String id) {
        try {
            return RmiClient.getReservationService().getReservationById(id);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to retrieve reservation with ID: " + id, 
                e
            );
            return null;
        }
    }
    
    /**
     * Gets reservations by customer.
     * 
     * @param customer The customer to filter by
     * @return List of reservations for the specified customer or empty list if an error occurs
     */
    public static List<Reservation> getReservationsByCustomer(Customer customer) {
        try {
            return RmiClient.getReservationService().getReservationsByCustomer(customer);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to retrieve reservations for customer: " + customer.getName(), 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets reservations by status.
     * 
     * @param status The status to filter by
     * @return List of reservations with the specified status or empty list if an error occurs
     */
    public static List<Reservation> getReservationsByStatus(ReservationStatus status) {
        try {
            return RmiClient.getReservationService().getReservationsByStatus(status);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to retrieve reservations with status: " + status.getStatus(), 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets pending reservations for today.
     * 
     * @return List of pending reservations for today or empty list if an error occurs
     */
    public static List<Reservation> getPendingReservationsForToday() {
        try {
            return RmiClient.getReservationService().getPendingReservationsForToday();
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to retrieve today's pending reservations", 
                e
            );
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets reservations by date.
     * 
     * @param date The date to filter by
     * @return List of reservations for the specified date or empty list if an error occurs
     */
    public static List<Reservation> getReservationsByDate(LocalDate date) {
        try {
            return RmiClient.getReservationService().getReservationsByDate(date);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to retrieve reservations for date: " + date, 
                e
            );
            return new ArrayList<>();
        }
    }
    
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
     * @return The created reservation or null if an error occurs
     */
    public static Reservation createReservation(
            Customer customer, 
            List<Table> tables, 
            LocalDate date, 
            LocalTime time, 
            int partySize,
            String partyType,
            String note, 
            List<FoodOrder> foodOrders, 
            double deposit) {
        
        try {
            return RmiClient.getReservationService().createReservation(
                customer, tables, date, time, partySize, partyType, note, foodOrders, deposit);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to create reservation", 
                e
            );
            return null;
        }
    }
    
    /**
     * Updates reservation status.
     * 
     * @param id The reservation ID
     * @param status The new status
     * @return true if successful, false otherwise
     */
    public static boolean updateReservationStatus(String id, ReservationStatus status) {
        try {
            return RmiClient.getReservationService().updateReservationStatus(id, status);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to update reservation status", 
                e
            );
            return false;
        }
    }
    
    /**
     * Cancels a reservation and processes refund.
     * 
     * @param id The reservation ID
     * @param refundAmount The amount to refund
     * @return true if successful, false otherwise
     */
    public static boolean cancelReservation(String id, double refundAmount) {
        try {
            return RmiClient.getReservationService().cancelReservation(id, refundAmount);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to cancel reservation", 
                e
            );
            return false;
        }
    }
    
    /**
     * Updates food orders for a reservation.
     * 
     * @param reservationId The reservation ID
     * @param foodOrders The updated food orders
     * @return true if successful, false otherwise
     */
    public static boolean updateFoodOrders(String reservationId, List<FoodOrder> foodOrders) {
        try {
            return RmiClient.getReservationService().updateFoodOrders(reservationId, foodOrders);
        } catch (RemoteException e) {
            ExceptionHandler.handleRemoteException(
                "Reservation Service Error", 
                "Failed to update food orders", 
                e
            );
            return false;
        }
    }
}