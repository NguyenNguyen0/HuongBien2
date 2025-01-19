package huongbien.dao;

import huongbien.entity.Promotion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromotionDAOTest {
    private static final PromotionDAO promotionDAO = new PromotionDAO();

    @Test
    void add() {
        Promotion promotion = new Promotion();
        promotion.setId("KM231023005");
        promotion.setName("New Year Sale");
        promotion.setStartDate(LocalDate.now());
        promotion.setEndDate(LocalDate.now().plusDays(30));
        promotion.setDiscount(20.0);
        promotion.setDescription("New Year Sale Promotion");

        assertTrue(promotionDAO.add(promotion));
    }

    @Test
    void update() {
        Promotion promotion = promotionDAO.getById("KM231023003");
        assertNotNull(promotion);

        promotion.setName("Updated New Year Sale");
        promotion.setDiscount(25.0);
        promotion.setDescription("Updated New Year Sale Promotion");

        assertTrue(promotionDAO.update(promotion));
    }

    @Test
    void getAll() {
        assertNotNull(promotionDAO.getAll());
    }

    @Test
    void getAllById() {
        assertNotNull(promotionDAO.getAllById("KM231023002"));
    }

    @Test
    void getForCustomer() {
        List<Promotion> promotions = promotionDAO.getForCustomer(2, 100.0);
        assertTrue(promotions.size() > 0);
    }

//    @Test
//    void getByStatus() {
//        assertNotNull(promotionDAO.getByAvailable(true));
//    }

//    @Test
//    void getByStatusWithPagination() {
//
//    }

//    @Test
//    void getAllWithPagination() {
//        assertNotNull(promotionDAO.getAllWithPagination(0, 10));
//    }
//
//    @Test
//    void getAllByIdWithPagination() {
//        assertNotNull(promotionDAO.getAllByIdWithPagination(0, 10, "KM231023002"));
//    }

    @Test
    void getById() {
        Promotion promotion = promotionDAO.getById("KM231023002");
        assertNotNull(promotion);
    }

    @Test
    void countTotal() {
        int total = promotionDAO.countTotal();
        assertTrue(total >= 0);
    }

//    @Test
//    void countTotalByStatus() {
//    }

    @Test
    void countTotalById() {
        int total = promotionDAO.countTotalById("KM231023002");
        assertTrue(total >= 0);
    }

    @Test
    void getDistinctPromotionDiscount() {
        List<String> discounts = promotionDAO.getDistinctPromotionDiscount();
        assertNotNull(discounts);
        assertTrue(discounts.size() > 0);
    }

//    @Test
//    void getDistinctPromotionStatus() {
//    }

    @Test
    void getDistinctPromotionMinimumOrderAmount() {
        List<String> amounts = promotionDAO.getDistinctPromotionMinimumOrderAmount();
        assertNotNull(amounts);
        assertTrue(amounts.size() > 0);
    }

//TODO: Dinh status nen chua lam
//    @Test
//    void getLookUpPromotion() {
//        List<Promotion> promotions = promotionDAO.getLookUpPromotion("Ưu đãi cho thành viên bạc", LocalDate.now().minusDays(10), LocalDate.now().plusDays(10), 10.0, 50.0, true, 0);
//        assertNotNull(promotions);
//        assertTrue(promotions.size() > 0);
//    }

    @Test
    void getCountLookUpPromotion() {
        int count = promotionDAO.getCountLookUpPromotion("Ưu đãi cho thành viên bạc", LocalDate.now().minusDays(10), LocalDate.now().plusDays(10), 10.0, 50.0, "Còn hiệu lực");
        assertTrue(count >= 0);
    }

    @Test
    void getPaymentPromotion() {
        int memberShipLevel = 2;
        double totalMoney = 100.0;
        List<Promotion> promotions = promotionDAO.getPaymentPromotion(memberShipLevel, totalMoney);
        assertTrue(promotions.size() > 0);
    }
}