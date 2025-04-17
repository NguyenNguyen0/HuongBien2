package huongbien.dao.remote;

import huongbien.entity.Promotion;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface IPromotionDAO extends Remote {
    List<Promotion> getAll() throws RemoteException;

    List<Promotion> getAllById(String id) throws RemoteException;

    List<Promotion> getForCustomer(int customerMembershipLevel, double orderAmount) throws RemoteException;

    List<Promotion> getByStatus(String status) throws RemoteException;

    List<Promotion> getByStatusWithPagination(int offset, int limit, String status) throws RemoteException;

    List<Promotion> getAllWithPagination(int offset, int limit) throws RemoteException;

    List<Promotion> getAllByIdWithPagination(int offset, int limit, String id) throws RemoteException;

    Promotion getById(String id) throws RemoteException;

    int countTotal() throws RemoteException;

    int countTotalByStatus(String status) throws RemoteException;

    List<String> getDistinctPromotionDiscount() throws RemoteException;

    List<String> getDistinctPromotionStatus() throws RemoteException;

    List<String> getDistinctPromotionMinimumOrderAmount() throws RemoteException;

    List<Promotion> getLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status, int pageIndex) throws RemoteException;

    int getCountLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status) throws RemoteException;

    List<Promotion> getPaymentPromotion(int memberShipLevel, double totalMoney) throws RemoteException;

    int countTotalById(String id) throws RemoteException;

    boolean add(Promotion promotion) throws RemoteException;

    boolean update(Promotion promotion) throws RemoteException;

    boolean delete(String id) throws RemoteException;
}