package huongbien.service;

import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.rmi.RmiClient;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Client-side adapter for the ReservationService.
 * Provides error handling and simplifies the use of the remote ReservationService.
 */
public class ReservationServiceAdapter {
    
    /**
     * Get a reservation by its ID.
     * 
     * @param reservationId Reservation ID to retrieve.
     * @return The reservation if found, null otherwise.
     */
    public static Reservation getReservationById(String reservationId) {
        try {
            return RmiClient.getReservationService().getReservationById(reservationId);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving reservation with ID: " + reservationId, e);
            return null;
        }
    }
    
    /**
     * Get all reservations for a customer.
     * 
     * @param customerId Customer ID.
     * @return List of reservations, or an empty list if an error occurs.
     */
    public static List<Reservation> getReservationsByCustomerId(String customerId) {
        try {
            return RmiClient.getReservationService().getReservationsByCustomerId(customerId);
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving reservations for customer ID: " + customerId, e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get all reservations that are waiting today.
     * 
     * @return List of today's waiting reservations, or an empty list if an error occurs.
     */
    public static List<Reservation> getListWaitedToday() {
        try {
            return RmiClient.getReservationService().getListWaitedToday();
        } catch (RemoteException e) {
            handleRemoteException("Error retrieving today's waiting reservations", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Perform a reservation lookup with filters.
     * 
     * @param reservationId Reservation ID filter.
     * @param reservationCusId Customer ID filter.
     * @param reservationDate Reservation date filter.
     * @param receiveDate Reception date filter.
     * @param pageIndex Page index for pagination.
     * @return List of matching reservations, or an empty list if an error occurs.
     */
    public static List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, 
                                                      LocalDate reservationDate, LocalDate receiveDate, 
                                                      int pageIndex) {
        try {
            return RmiClient.getReservationService().getLookUpReservation(reservationId, reservationCusId, 
                                                                        reservationDate, receiveDate, pageIndex);
        } catch (RemoteException e) {
            handleRemoteException("Error performing reservation lookup", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Count total reservations.
     * 
     * @return Total number of reservations, or 0 if an error occurs.
     */
    public static int countTotalReservations() {
        try {
            return RmiClient.getReservationService().countTotalReservations();
        } catch (RemoteException e) {
            handleRemoteException("Error counting total reservations", e);
            return 0;
        }
    }
    
    /**
     * Count reservations that match the given filters.
     * 
     * @param reservationId Reservation ID filter.
     * @param reservationCusId Customer ID filter.
     * @param reservationDate Reservation date filter.
     * @param receiveDate Reception date filter.
     * @return Count of matching reservations, or 0 if an error occurs.
     */
    public static int getCountLookUpReservation(String reservationId, String reservationCusId, 
                                             LocalDate reservationDate, LocalDate receiveDate) {
        try {
            return RmiClient.getReservationService().getCountLookUpReservation(reservationId, reservationCusId, 
                                                                             reservationDate, receiveDate);
        } catch (RemoteException e) {
            handleRemoteException("Error counting reservations for lookup", e);
            return 0;
        }
    }
    
    /**
     * Add a new reservation.
     * 
     * @param reservation Reservation to add.
     * @return true if successful, false otherwise.
     */
    public static boolean addReservation(Reservation reservation) {
        try {
            return RmiClient.getReservationService().addReservation(reservation);
        } catch (RemoteException e) {
            handleRemoteException("Error adding reservation", e);
            return false;
        }
    }
    
    /**
     * Update a reservation's information.
     * 
     * @param reservation Reservation with updated information.
     * @return true if successful, false otherwise.
     */
    public static boolean updateReservation(Reservation reservation) {
        try {
            return RmiClient.getReservationService().updateReservation(reservation);
        } catch (RemoteException e) {
            handleRemoteException("Error updating reservation", e);
            return false;
        }
    }
    
    /**
     * Update a reservation's status.
     * 
     * @param reservationId Reservation ID.
     * @param status New status.
     */
    public static void updateStatus(String reservationId, ReservationStatus status) {
        try {
            RmiClient.getReservationService().updateStatus(reservationId, status);
        } catch (RemoteException e) {
            handleRemoteException("Error updating reservation status", e);
        }
    }
    
    /**
     * Get table status for today based on reservations.
     * 
     * @param reservationList List of reservations to check.
     * @return List of tables with their status, or an empty list if an error occurs.
     */
    public static List<Table> getListTableStatusToday(List<Reservation> reservationList) {
        try {
            return RmiClient.getReservationService().getListTableStatusToday(reservationList);
        } catch (RemoteException | SQLException e) {
            handleException("Error retrieving table statuses", e);
            return Collections.emptyList();
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
    
    /**
     * Handle any exception by logging and possibly displaying an error message.
     * 
     * @param message Error message to log.
     * @param e The exception that occurred.
     */
    private static void handleException(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        
        // TODO: Add proper logging and/or user notification
    }
}