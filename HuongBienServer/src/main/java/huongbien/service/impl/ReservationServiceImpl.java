package huongbien.service.impl;

import huongbien.bus.ReservationBUS;
import huongbien.entity.Customer;
import huongbien.entity.FoodOrder;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.rmi.interfaces.ReservationService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Implementation of the ReservationService RMI interface.
 * Delegates calls to the ReservationBUS business logic class.
 */
public class ReservationServiceImpl extends UnicastRemoteObject implements ReservationService {
    
    private final ReservationBUS reservationBUS;
    
    public ReservationServiceImpl() throws RemoteException {
        super();
        this.reservationBUS = new ReservationBUS();
    }
    
    @Override
    public List<Reservation> getAllReservations() throws RemoteException {
        System.out.println("RMI: getAllReservations() called");
        return reservationBUS.getAll();
    }
    
    @Override
    public Reservation getReservationById(String id) throws RemoteException {
        System.out.println("RMI: getReservationById() called with ID: " + id);
        try {
            return reservationBUS.getById(id);
        } catch (Exception e) {
            System.err.println("Error in getReservationById(): " + e.getMessage());
            throw new RemoteException("Failed to get reservation by ID", e);
        }
    }
    
    @Override
    public List<Reservation> getReservationsByCustomer(Customer customer) throws RemoteException {
        System.out.println("RMI: getReservationsByCustomer() called for customer: " + customer.getName());
        return reservationBUS.getByCustomer(customer);
    }
    
    @Override
    public List<Reservation> getReservationsByStatus(ReservationStatus status) throws RemoteException {
        System.out.println("RMI: getReservationsByStatus() called with status: " + status.getStatus());
        return reservationBUS.getByStatus(status);
    }
    
    @Override
    public List<Reservation> getPendingReservationsForToday() throws RemoteException {
        System.out.println("RMI: getPendingReservationsForToday() called");
        return reservationBUS.getPendingReservationsForToday();
    }
    
    @Override
    public List<Reservation> getReservationsByDate(LocalDate date) throws RemoteException {
        System.out.println("RMI: getReservationsByDate() called with date: " + date);
        return reservationBUS.getByDate(date);
    }
    
    @Override
    public Reservation createReservation(
            Customer customer, 
            List<Table> tables, 
            LocalDate date, 
            LocalTime time, 
            int partySize,
            String partyType,
            String note, 
            List<FoodOrder> foodOrders, 
            double deposit) throws RemoteException {
        
        System.out.println("RMI: createReservation() called for customer: " + customer.getName());
        try {
            return reservationBUS.create(customer, tables, date, time, partySize, 
                                        partyType, note, foodOrders, deposit);
        } catch (Exception e) {
            System.err.println("Error in createReservation(): " + e.getMessage());
            throw new RemoteException("Failed to create reservation", e);
        }
    }
    
    @Override
    public boolean updateReservationStatus(String id, ReservationStatus status) throws RemoteException {
        System.out.println("RMI: updateReservationStatus() called for ID: " + id + " with status: " + status);
        try {
            return reservationBUS.updateStatus(id, status);
        } catch (Exception e) {
            System.err.println("Error in updateReservationStatus(): " + e.getMessage());
            throw new RemoteException("Failed to update reservation status", e);
        }
    }
    
    @Override
    public boolean cancelReservation(String id, double refundAmount) throws RemoteException {
        System.out.println("RMI: cancelReservation() called for ID: " + id + " with refund: " + refundAmount);
        try {
            return reservationBUS.cancel(id, refundAmount);
        } catch (Exception e) {
            System.err.println("Error in cancelReservation(): " + e.getMessage());
            throw new RemoteException("Failed to cancel reservation", e);
        }
    }
    
    @Override
    public boolean updateFoodOrders(String reservationId, List<FoodOrder> foodOrders) throws RemoteException {
        System.out.println("RMI: updateFoodOrders() called for reservation ID: " + reservationId);
        try {
            return reservationBUS.updateFoodOrders(reservationId, foodOrders);
        } catch (Exception e) {
            System.err.println("Error in updateFoodOrders(): " + e.getMessage());
            throw new RemoteException("Failed to update food orders", e);
        }
    }
}