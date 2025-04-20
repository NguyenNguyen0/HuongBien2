package huongbien.dao.impl;

import huongbien.dao.remote.IReservationDAO;
import huongbien.dao.remote.ITableDAO;
import huongbien.entity.Reservation;
import huongbien.entity.ReservationStatus;
import huongbien.entity.Table;
import huongbien.entity.FoodOrder;
import huongbien.entity.Cuisine;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO extends GenericDAO<Reservation> implements IReservationDAO {
    public ReservationDAO() throws RemoteException {
        super();
    }

    public ReservationDAO(PersistenceUnit persistenceUnit) throws RemoteException {
        super(persistenceUnit);
    }

    @Override
    public List<Reservation> getAllByCustomerId(String customerId) throws RemoteException {
        return findMany("SELECT r FROM Reservation r WHERE r.customer.id = ?1", Reservation.class, customerId);
    }

    @Override
    public List<Reservation> getAllByEmployeeId(String employeeId) throws RemoteException {
        return findMany("SELECT r FROM Reservation r WHERE r.employee.id = ?1", Reservation.class, employeeId);
    }

    @Override
    public List<Reservation> getAll() throws RemoteException {
        return findMany("SELECT r FROM Reservation r", Reservation.class);
    }

    @Override
    public List<Reservation> getStatusReservationByDate(LocalDate date, String status, String cusId) throws RemoteException {
        try {
            ReservationStatus enumStatus = ReservationStatus.fromStatus(status);
            String jpql = "SELECT r FROM Reservation r WHERE r.status = :status " +
                    "AND r.receiveDate = :date AND r.id LIKE :customerId";

            EntityManager em = JPAUtil.getEntityManager();
            Query query = em.createQuery(jpql, Reservation.class);
            query.setParameter("status", enumStatus);
            query.setParameter("date", date);
            query.setParameter("customerId", "%" + cusId + "%");

            return query.getResultList();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public int getCountStatusReservationByDate(LocalDate date, String status, String cusId) throws RemoteException {
        String jpql = "SELECT COUNT(r) FROM Reservation r WHERE r.status = :status " +
                "AND r.receiveDate = :date AND r.id LIKE :customerId";

        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Thay đổi từ valueOf sang fromStatus để xử lý đúng giá trị tiếng Việt
            ReservationStatus enumStatus = ReservationStatus.fromStatus(status);
            if (enumStatus == null) {
                return 0; // Trả về 0 nếu không tìm thấy enum tương ứng
            }

            Query query = em.createQuery(jpql);
            query.setParameter("status", enumStatus);
            query.setParameter("date", date);
            query.setParameter("customerId", "%" + cusId + "%");

            Object result = query.getSingleResult();
            return result instanceof Number ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Reservation> getListWaitedToday() throws RemoteException {
        return findMany("SELECT r FROM Reservation r WHERE r.status = 'Chưa xác nhận' AND r.receiveDate = CURRENT_DATE", Reservation.class);
    }

    @Override
    public Reservation getById(String id) throws RemoteException {
        return findOne("SELECT r FROM Reservation r WHERE r.id = ?1", Reservation.class, id);
    }

    @Override
    public List<Reservation> getLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate, int pageIndex) throws RemoteException {
        String jpql = "SELECT r FROM Reservation r WHERE r.id LIKE CONCAT('%', ?1, '%') " +
                "AND r.customerId LIKE CONCAT('%', ?2, '%') " +
                "AND r.status = 'Chưa nhận' " +
                (reservationDate != null ? "AND r.reservationDate = ?3 " : "") +
                (receiveDate != null ? "AND r.receiveDate = ?4 " : "") +
                "ORDER BY r.receiveDate DESC";
        return findMany(jpql, Reservation.class, reservationId, reservationCusId, reservationDate, receiveDate);
    }

    @Override
    public int getCountLookUpReservation(String reservationId, String reservationCusId, LocalDate reservationDate, LocalDate receiveDate) throws RemoteException {
        String jpql = "SELECT COUNT(r) FROM Reservation r WHERE r.id LIKE CONCAT('%', ?1, '%') " +
                "AND r.customerId LIKE CONCAT('%', ?2, '%') " +
                "AND r.status = 'Chưa nhận' " +
                (reservationDate != null ? "AND r.reservationDate = ?3 " : "") +
                (receiveDate != null ? "AND r.receiveDate = ?4 " : "");
        return count(jpql, reservationId, reservationCusId, reservationDate, receiveDate);
    }

    @Override
    public int countTotal() throws RemoteException {
        return count("SELECT COUNT(r) FROM Reservation r");
    }

    @Override
    public List<Table> getListTableStatusToday(List<Reservation> reservationList) throws RemoteException {
        ITableDAO tableDAO = new TableDAO();
        return tableDAO.getListTableStatusToday(reservationList);
    }

    @Override
    public boolean add(Reservation reservation) throws RemoteException {
        if (reservation == null) {
            return false;
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            
            // Đảm bảo các Table đã tồn tại và ở trạng thái managed
            if (reservation.getTables() != null) {
                List<Table> managedTables = new ArrayList<>();
                for (Table table : reservation.getTables()) {
                    // Tìm bàn từ database và sử dụng reference thay vì entity mới
                    Table managedTable = em.find(Table.class, table.getId());
                    if (managedTable != null) {
                        managedTables.add(managedTable);
                    }
                }
                // Gán lại danh sách bàn đã managed
                reservation.setTables(managedTables);
            }
            
            // Lưu Reservation
            em.persist(reservation);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reservation reservation) throws RemoteException {
        if (reservation == null) {
            return false;
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            
            // Đảm bảo reservation hiện tại là managed
            Reservation managedReservation = em.find(Reservation.class, reservation.getId());
            if (managedReservation == null) {
                transaction.rollback();
                return false;
            }
            
            // Cập nhật các thuộc tính cơ bản
            managedReservation.setPartyType(reservation.getPartyType());
            managedReservation.setPartySize(reservation.getPartySize());
            managedReservation.setReceiveDate(reservation.getReceiveDate());
            managedReservation.setReceiveTime(reservation.getReceiveTime());
            managedReservation.setDeposit(reservation.getDeposit());
            managedReservation.setNote(reservation.getNote());
            managedReservation.setEmployee(reservation.getEmployee());
            managedReservation.setCustomer(reservation.getCustomer());
            
            // Cập nhật danh sách bàn một cách an toàn
            if (reservation.getTables() != null) {
                // Xóa tất cả các mối quan hệ bàn hiện tại
                managedReservation.getTables().clear();
                
                // Thêm các bàn mới vào (sử dụng references)
                for (Table table : reservation.getTables()) {
                    Table managedTable = em.find(Table.class, table.getId());
                    if (managedTable != null) {
                        managedReservation.getTables().add(managedTable);
                    }
                }
            }
            
            // Cập nhật danh sách food orders
            if (reservation.getFoodOrders() != null) {
                // Xóa các food orders cũ
                for (FoodOrder oldOrder : managedReservation.getFoodOrders()) {
                    em.remove(oldOrder);
                }
                managedReservation.getFoodOrders().clear();
                
                // Thêm food orders mới
                for (FoodOrder foodOrder : reservation.getFoodOrders()) {
                    // Đảm bảo Cuisine là managed
                    Cuisine managedCuisine = em.find(Cuisine.class, foodOrder.getCuisine().getId());
                    if (managedCuisine != null) {
                        foodOrder.setCuisine(managedCuisine);
                        foodOrder.setReservation(managedReservation);
                        em.persist(foodOrder);
                        managedReservation.getFoodOrders().add(foodOrder);
                    }
                }
            }
            
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        Reservation reservation = getById(id);
        if (reservation == null) {
            return false;
        }
        return super.delete(reservation);
    }

    @Override
    public void updateStatus(String reservationId, ReservationStatus status) throws RemoteException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            
            // Đảm bảo reservation hiện tại là managed
            Reservation managedReservation = em.find(Reservation.class, reservationId);
            if (managedReservation == null) {
                transaction.rollback();
                return;
            }
            
            // Cập nhật trạng thái trực tiếp trong transaction
            managedReservation.setStatus(status);
            
            // Commit transaction
            transaction.commit();
            System.out.println("Đã cập nhật trạng thái thành " + status.getStatus() + " cho đơn hàng: " + reservationId);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Lỗi khi cập nhật trạng thái cho đơn hàng " + reservationId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateRefundDeposit(String id, double refundDeposit) throws RemoteException {
        Reservation reservation = getById(id);
        if (reservation == null) {
            return;
        }
        reservation.setRefundDeposit(refundDeposit);
        update(reservation);
    }
}
