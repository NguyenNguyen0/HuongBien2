package huongbien.rmi.interfaces;

import huongbien.entity.Promotion;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Remote interface for promotion services
 */
public interface PromotionService extends Remote {
    
    // Promotion retrieval
    List<Promotion> getAllPromotion() throws RemoteException;
    Promotion getPromotion(String promotionId) throws RemoteException;
    List<Promotion> getPromotionForCustomer(int customerMembershipLevel, double orderAmount) throws RemoteException;
    List<Promotion> getPromotionByStatus(String status) throws RemoteException;
    List<Promotion> getAllPromotionById(String id) throws RemoteException;
    
    // Promotion with pagination
    List<Promotion> getPromotionByStatusWithPagination(int offset, int limit, String status) throws RemoteException;
    List<Promotion> getPromotionByIdWithPagination(int offset, int limit, String id) throws RemoteException;
    
    // Promotion lookup
    List<Promotion> getLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate,
                                     double discount, double minimumOrderAmount, String status, 
                                     int pageIndex) throws RemoteException;
    
    // Promotion distinct values
    List<String> getDistinctPromotionDiscount() throws RemoteException;
    List<String> getDistinctPromotionStatus() throws RemoteException;
    List<String> getDistinctPromotionMinimumOrderAmount() throws RemoteException;
    
    // Promotion counts
    int countTotalPromotion() throws RemoteException;
    int countTotalPromotionByStatus(String status) throws RemoteException;
    int countTotalPromotionById(String id) throws RemoteException;
    int getCountLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate,
                              double discount, double minimumOrderAmount, String status) throws RemoteException;
    
    // Promotion management
    boolean addPromotion(Promotion promotion) throws RemoteException;
    boolean updatePromotion(Promotion promotion) throws RemoteException;
    List<Promotion> getPaymentPromotion(int memberShipLevel, double totalMoney) throws RemoteException;
}