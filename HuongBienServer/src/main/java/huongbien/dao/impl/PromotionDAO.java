package huongbien.dao.impl;

import huongbien.dao.remote.IPromotionDAO;
import huongbien.entity.MembershipLevel;
import huongbien.entity.Promotion;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PromotionDAO extends GenericDAO<Promotion> implements IPromotionDAO {
    public PromotionDAO() throws RemoteException {
        super();
    }

    public PromotionDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Promotion> getAll() throws RemoteException {
        return findMany("SELECT p FROM Promotion p", Promotion.class);
    }

    @Override
    public List<Promotion> getAllById(String id) throws RemoteException {
        return findMany("SELECT p FROM Promotion p WHERE p.id LIKE ?1", Promotion.class, id + "%");
    }

    @Override
    public List<Promotion> getForCustomer(int customerMembershipLevel, double orderAmount) throws RemoteException {
        return findMany("SELECT p FROM Promotion p WHERE p.membershipLevel <= ?1 AND p.minimumOrderAmount <= ?2 AND p.status = 'Còn hiệu lực' ORDER BY p.discount DESC",
                Promotion.class, customerMembershipLevel, orderAmount);
    }

    @Override
    public List<Promotion> getByStatus(String status) throws RemoteException {
        return findMany("SELECT p FROM Promotion p WHERE p.status = ?1", Promotion.class, status);
    }

    @Override
    public List<Promotion> getByStatusWithPagination(int offset, int limit, String status) throws RemoteException {
        Map<String, Object> params = Map.of("isAvailable", "Còn hiệu lực".equals(status));
        return findManyWithPagination("SELECT p FROM Promotion p WHERE p.isAvailable = :isAvailable ORDER BY p.endDate DESC, p.membershipLevel DESC",
                Promotion.class, params, offset, limit);
    }

    @Override
    public List<Promotion> getAllWithPagination(int offset, int limit) throws RemoteException {
        return findManyWithPagination("SELECT p FROM Promotion p ORDER BY p.endDate DESC, p.membershipLevel DESC",
                Promotion.class, null, offset, limit);
    }

    @Override
    public List<Promotion> getAllByIdWithPagination(int offset, int limit, String id) throws RemoteException {
        Map<String, Object> params = Map.of("id", id + "%");
        return findManyWithPagination(
                "SELECT p FROM Promotion p WHERE p.id LIKE :id ORDER BY p.endDate DESC, p.membershipLevel DESC",
                Promotion.class, params, offset, limit);
    }

    @Override
    public Promotion getById(String id) throws RemoteException {
        return findOne("SELECT p FROM Promotion p WHERE p.id = ?1", Promotion.class, id);
    }

    @Override
    public int countTotal() throws RemoteException {
        return count("SELECT COUNT(p) FROM Promotion p");
    }

    @Override
    public int countTotalByStatus(String status) throws RemoteException {
        try {
            String jpql = "SELECT COUNT(p) FROM Promotion p WHERE p.isAvailable = :isAvailable";

            EntityManager em = JPAUtil.getEntityManager();
            Query query = em.createQuery(jpql);

            query.setParameter("isAvailable", "Còn hiệu lực".equals(status));

            Object result = query.getSingleResult();
            return result instanceof Number ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<String> getDistinctPromotionDiscount() throws RemoteException {
        return executeQuery("SELECT DISTINCT p.discount FROM Promotion p", String.class);
    }

    @Override
    public List<String> getDistinctPromotionStatus() throws RemoteException {
        return executeQuery("SELECT DISTINCT CASE WHEN p.isAvailable = true THEN 'Còn hiệu lực' ELSE 'Hết hiệu lực' END FROM Promotion p", String.class);
    }

    @Override
    public List<String> getDistinctPromotionMinimumOrderAmount() throws RemoteException {
        return executeQuery("SELECT DISTINCT p.minimumOrderAmount FROM Promotion p", String.class);
    }

    @Override
    public List<Promotion> getLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status, int pageIndex) throws RemoteException {
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

    @Override
    public int getCountLookUpPromotion(String promotionName, LocalDate startDate, LocalDate endDate, double discount, double minimumOrderAmount, String status) throws RemoteException {
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

    @Override
    public List<Promotion> getPaymentPromotion(int memberShipLevel, double totalMoney) throws RemoteException {
        MembershipLevel level = MembershipLevel.fromValue(memberShipLevel);
        return findMany("SELECT p FROM Promotion p WHERE p.membershipLevel <= ?1 AND p.isAvailable = true AND p.minimumOrderAmount <= ?2",
                Promotion.class, level, totalMoney);
    }

    @Override
    public int countTotalById(String id) throws RemoteException {
        return count("SELECT COUNT(p) FROM Promotion p WHERE p.id LIKE ?1", id + "%");
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        if (id == null || id.isBlank()) return false;
        Promotion promotion = getById(id);
        if (promotion != null) {
            delete(promotion);
            return true;
        }
        return false;
    }
}