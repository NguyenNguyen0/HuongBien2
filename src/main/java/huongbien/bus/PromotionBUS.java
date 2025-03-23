package com.huongbien.bus;


import com.huongbien.dao.PromotionDAO;
import com.huongbien.entity.Promotion;

import java.time.LocalDate;
import java.util.List;

public class PromotionBUS {
    private final PromotionDAO promotionDao;

    public PromotionBUS() {
        promotionDao = PromotionDAO.getInstance();
    }

    public int countTotalPromotion() {
        return promotionDao.countTotal();
    }

    public int countTotalPromotionByStatus(String status) {
        if (status.isBlank()) return 0;
        if (status.equals("Tất cả")) return countTotalPromotion();
        return promotionDao.countTotalByStatus(status);
    }

    public int countTotalPromotionById(String id) {
        if (id.isBlank()) return 0;
        return promotionDao.countTotalById(id);
    }

    public List<Promotion> getAllPromotion() {
        return promotionDao.getAll();
    }

    public Promotion getPromotion(String promotionId) {
        if (promotionId.isBlank()) return null;
        return promotionDao.getById(promotionId);
    }

    public List<Promotion> getPromotionForCustomer(int customerMembershipLevel, double orderAmount) {
        if (orderAmount < 0) return null;
        if (customerMembershipLevel < 0 || customerMembershipLevel > 3) return null;
        return promotionDao.getForCustomer(customerMembershipLevel, orderAmount);
    }

    public List<Promotion> getPromotionByStatus(String status) {
        if (status.isBlank()) return null;
        return promotionDao.getByStatus(status);
    }

    public List<Promotion> getAllPromotionById(String id) {
        if (id.isBlank()) return null;
        return promotionDao.getAllById(id);
    }
    public List<String> getDistinctPromotionDiscount(){ return promotionDao.getDistinctPromotionDiscount(); }

    public List<String> getDistinctPromotionStatus(){ return promotionDao.getDistinctPromotionStatus(); }

    public List<String> getDistinctPromotionMinimumOrderAmount(){ return promotionDao.getDistinctPromotionMinimumOrderAmount(); }

    public List<Promotion> getLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status, int pageIndex){
        return promotionDao.getLookUpPromotion(promotionName, startDate, endDate,discount , minimumOrderAmount, status, pageIndex);
    }

    public int getCountLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status){
        return promotionDao.getCountLookUpPromotion(promotionName, startDate, endDate,discount , minimumOrderAmount, status);
    }

    public List<Promotion> getPromotionByStatusWithPagination(int offset, int limit, String status) {
        if (offset < 0 || limit < 0) return null;
        if (status.equals("Tất cả")) return promotionDao.getAllWithPagination(offset, limit);
        if (!status.equals("Còn hiệu lực") && !status.equals("Hết hiệu lực")) return null;
        return promotionDao.getByStatusWithPagination(offset, limit, status);
    }

    public List<Promotion> getPromotionByIdWithPagination(int offset, int limit, String id) {
        if (offset < 0 || limit < 0) return null;
        if (id.isBlank()) return null;
        return promotionDao.getAllByIdWithPagination(offset, limit, id);
    }

    public boolean addPromotion(Promotion promotion) {
        if (promotion == null) return false;
        return promotionDao.add(promotion);
    }

    public boolean updatePromotion(Promotion promotion) {
        if (promotion == null) return false;
        return promotionDao.updateInfo(promotion);
    }

    public List<Promotion> getPaymentPromotion(int memberShipLevel, double totalMoney){ return promotionDao.getPaymentPromotion(memberShipLevel, totalMoney); }
}
