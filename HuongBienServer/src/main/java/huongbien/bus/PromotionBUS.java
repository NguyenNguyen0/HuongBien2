package huongbien.bus;

import huongbien.dao.impl.PromotionDAO;
import huongbien.entity.Promotion;
import huongbien.jpa.PersistenceUnit;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public class PromotionBUS {
    private final PromotionDAO promotionDao;

    public PromotionBUS() throws RemoteException {
        promotionDao = new PromotionDAO(PersistenceUnit.MARIADB_JPA);
    }

    public int countTotalPromotion() throws RemoteException {
        return promotionDao.countTotal();
    }

    public int countTotalPromotionByStatus(String status) throws RemoteException {
        if (status.isBlank()) return 0;
        if (status.equals("Tất cả")) return countTotalPromotion();
        return promotionDao.countTotalByStatus(status);
    }

    public int countTotalPromotionById(String id) throws RemoteException {
        if (id.isBlank()) return 0;
        return promotionDao.countTotalById(id);
    }

    public List<Promotion> getAllPromotion() throws RemoteException {
        return promotionDao.getAll();
    }

    public Promotion getPromotion(String promotionId) throws RemoteException {
        if (promotionId.isBlank()) return null;
        return promotionDao.getById(promotionId);
    }

    public List<Promotion> getPromotionForCustomer(int customerMembershipLevel, double orderAmount) throws RemoteException {
        if (orderAmount < 0) return null;
        if (customerMembershipLevel < 0 || customerMembershipLevel > 3) return null;
        return promotionDao.getForCustomer(customerMembershipLevel, orderAmount);
    }

    public List<Promotion> getPromotionByStatus(String status) throws RemoteException {
        if (status.isBlank()) return null;
        return promotionDao.getByStatus(status);
    }

    public List<Promotion> getAllPromotionById(String id) throws RemoteException {
        if (id.isBlank()) return null;
        return promotionDao.getAllById(id);
    }

    public List<String> getDistinctPromotionDiscount() throws RemoteException {
        return promotionDao.getDistinctPromotionDiscount();
    }

    public List<String> getDistinctPromotionStatus() throws RemoteException {
        return promotionDao.getDistinctPromotionStatus();
    }

    public List<String> getDistinctPromotionMinimumOrderAmount() throws RemoteException {
        return promotionDao.getDistinctPromotionMinimumOrderAmount();
    }

    public List<Promotion> getLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status, int pageIndex) throws RemoteException {
        return promotionDao.getLookUpPromotion(promotionName, startDate, endDate, discount, minimumOrderAmount, status, pageIndex);
    }

    public int getCountLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status) throws RemoteException {
        return promotionDao.getCountLookUpPromotion(promotionName, startDate, endDate, discount, minimumOrderAmount, status);
    }

    public List<Promotion> getPromotionByStatusWithPagination(int offset, int limit, String status) throws RemoteException {
        if (offset < 0 || limit < 0) return null;
        if (status.equals("Tất cả")) return promotionDao.getAllWithPagination(offset, limit);
        if (!status.equals("Còn hiệu lực") && !status.equals("Hết hiệu lực")) return null;
        return promotionDao.getByStatusWithPagination(offset, limit, status);
    }

    public List<Promotion> getPromotionByIdWithPagination(int offset, int limit, String id) throws RemoteException {
        if (offset < 0 || limit < 0) return null;
        if (id.isBlank()) return null;
        return promotionDao.getAllByIdWithPagination(offset, limit, id);
    }

    public boolean addPromotion(Promotion promotion) throws RemoteException {
        if (promotion == null) return false;
        return promotionDao.add(promotion);
    }

    public boolean updatePromotion(Promotion promotion) throws RemoteException {
        if (promotion == null) return false;
        return promotionDao.update(promotion);
    }

    public List<Promotion> getPaymentPromotion(int memberShipLevel, double totalMoney) throws RemoteException {
        return promotionDao.getPaymentPromotion(memberShipLevel, totalMoney);
    }
}