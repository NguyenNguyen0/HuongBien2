package huongbien.dao;

import huongbien.entity.MembershipLevel;
import huongbien.entity.Promotion;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class PromotionDAO extends GenericDAO<Promotion> {
    public PromotionDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Promotion> getAll() {
        return findMany("SELECT p FROM Promotion p", Promotion.class);
    }

    public List<Promotion> getAllById(String id) {
        return findMany("SELECT p FROM Promotion p WHERE p.id LIKE ?1", Promotion.class, id + "%");
    }

    public List<Promotion> getForCustomer(int customerMembershipLevel, double orderAmount) {
        return findMany("SELECT p FROM Promotion p WHERE p.membershipLevel <= ?1 AND p.minimumOrderAmount <= ?2 AND p.status = 'Còn hiệu lực' ORDER BY p.discount DESC",
                Promotion.class, customerMembershipLevel, orderAmount);
    }

    public List<Promotion> getByStatus(String status) {
        return findMany("SELECT p FROM Promotion p WHERE p.status = ?1", Promotion.class, status);
    }

    public List<Promotion> getByStatusWithPagination(int offset, int limit, String status) {
        Map<String, Object> params = Map.of("isAvailable", "Còn hiệu lực".equals(status));
        return findManyWithPagination("SELECT p FROM Promotion p WHERE p.isAvailable = :isAvailable ORDER BY p.endDate DESC, p.membershipLevel DESC",
                Promotion.class, params, offset, limit);
    }

    public List<Promotion> getAllWithPagination(int offset, int limit) {
        return findManyWithPagination("SELECT p FROM Promotion p ORDER BY p.endDate DESC, p.membershipLevel DESC",
                Promotion.class, null, offset, limit);
    }

    public List<Promotion> getAllByIdWithPagination(int offset, int limit, String id) {
        Map<String, Object> params = Map.of("id", id + "%");
        return findManyWithPagination(
                "SELECT p FROM Promotion p WHERE p.id LIKE :id ORDER BY p.endDate DESC, p.membershipLevel DESC",
                Promotion.class, params, offset, limit);
    }

    public Promotion getById(String id) {
        return findOne("SELECT p FROM Promotion p WHERE p.id = ?1", Promotion.class, id);
    }

    public int countTotal() {
        return count("SELECT COUNT(p) FROM Promotion p");
    }

//    public int countTotalByStatus(String status) {
//        return count("SELECT COUNT(p) FROM Promotion p WHERE p.status = ?1", status);
//    }

    public int countTotalByStatus(String status) {
        try {
            String jpql = "SELECT COUNT(p) FROM Promotion p WHERE p.isAvailable = :isAvailable";

            EntityManager em = JPAUtil.getEntityManager();
            Query query = em.createQuery(jpql);

            query.setParameter("isAvailable", "Còn hiệu lực".equals(status) ? true : false);

            Object result = query.getSingleResult();
            return result instanceof Number ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<String> getDistinctPromotionDiscount() {
        return executeQuery("SELECT DISTINCT p.discount FROM Promotion p", String.class);
    }

    public List<String> getDistinctPromotionStatus() {
        // Since the entity seems to use isAvailable boolean instead of status String
        return executeQuery("SELECT DISTINCT CASE WHEN p.isAvailable = true THEN 'Còn hiệu lực' ELSE 'Hết hiệu lực' END FROM Promotion p", String.class);
    }

    public List<String> getDistinctPromotionMinimumOrderAmount() {
        return executeQuery("SELECT DISTINCT p.minimumOrderAmount FROM Promotion p", String.class);
    }

    public List<Promotion> getLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status, int pageIndex) {
        StringBuilder query = new StringBuilder("SELECT p FROM Promotion p WHERE p.name LIKE ?1 AND p.status LIKE ?2");

        if (startDate != null) {
            query.append(" AND p.startDate = ?3");
        }
        if (endDate != null) {
            query.append(" AND p.endDate = ?4");
        }
        if (discount != 0) {
            query.append(" AND p.discount = ?5");
        }
        if (minimumOrderAmount != 0) {
            query.append(" AND p.minimumOrderAmount >= ?6");
        }

        query.append(" ORDER BY p.discount");

        return findMany(query.toString(), Promotion.class, "%" + promotionName + "%", "%" + status + "%", startDate, endDate, discount, minimumOrderAmount, pageIndex);
    }

    public int getCountLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status) {
        StringBuilder query = new StringBuilder("SELECT COUNT(p) FROM Promotion p WHERE p.name LIKE ?1 AND p.status LIKE ?2");

        if (startDate != null) {
            query.append(" AND p.startDate = ?3");
        }
        if (endDate != null) {
            query.append(" AND p.endDate = ?4");
        }
        if (discount != 0) {
            query.append(" AND p.discount = ?5");
        }
        if (minimumOrderAmount != 0) {
            query.append(" AND p.minimumOrderAmount >= ?6");
        }

        return count(query.toString(), "%" + promotionName + "%", "%" + status + "%", startDate, endDate, discount, minimumOrderAmount);
    }

    public List<Promotion> getPaymentPromotion(int memberShipLevel, double totalMoney) {
        MembershipLevel level = MembershipLevel.fromValue(memberShipLevel); // Assuming this method exists
        return findMany("SELECT p FROM Promotion p WHERE p.membershipLevel <= ?1 AND p.isAvailable = true AND p.minimumOrderAmount <= ?2",
                Promotion.class, level, totalMoney);
    }

    public int countTotalById(String id) {
        return count("SELECT COUNT(p) FROM Promotion p WHERE p.id LIKE ?1", id + "%");
    }
}
