package huongbien.dao;

import huongbien.data.DataGenerator;
import huongbien.entity.Reservation;
import huongbien.jpa.PersistenceUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationDAOTest {
    private static final ReservationDAO reservationDAO = new ReservationDAO(PersistenceUnit.MARIADB_JPA);

    @Test
    public void add() {
        assertTrue(reservationDAO.add(DataGenerator.fakeReservation(LocalDate.of(2024, 1, 20))));
    }

    @Test
    public void update() {
        Reservation reservation = DataGenerator.fakeReservation(LocalDate.of(2024, 1, 20));
        reservation.setId("R001");
        reservation.setNote("Updated Notes");
        assertTrue(reservationDAO.update(reservation));
    }

    @Test
    public void getAllByCustomerId() {
        List<Reservation> reservations = reservationDAO.getAllByCustomerId("KH240119704");
        assertFalse(reservations.isEmpty());
    }

    @Test
    public void getAllByEmployeeId() {
        List<Reservation> reservations = reservationDAO.getAllByEmployeeId("NV240110625");
        assertFalse(reservations.isEmpty());
    }

    @Test
    public void getAll() {
        assertNotNull(reservationDAO.getAll());
    }

    @Test
    public void getStatusReservationByDate() {
        List<Reservation> reservations = reservationDAO.getStatusReservationByDate(LocalDate.now(), "Đã xác nhận", "C001");
        assertFalse(reservations.isEmpty());
    }

    @Test
    public void getCountStatusReservationByDate() {
        int count = reservationDAO.getCountStatusReservationByDate(LocalDate.of(2024, 1, 22), "Đã hoàn thành", "KH240119704");
        assertTrue(count > 0);
    }

    @Test
    public void getListWaitedToday() {
        List<Reservation> reservations = reservationDAO.getListWaitedToday();
        assertFalse(reservations.isEmpty());
    }

    @Test
    public void getById() {
        assertNotNull(reservationDAO.getById("DB240119084140781"));
    }

    @Test
    public void getLookUpReservation() {
        List<Reservation> reservations = reservationDAO.getLookUpReservation("123", "C001", LocalDate.now(), LocalDate.now(), 1);
        assertFalse(reservations.isEmpty());
    }

    @Test
    public void getCountLookUpReservation() {
        int count = reservationDAO.getCountLookUpReservation("DB240119084140781", "KH920421186", LocalDate.of(2024, 1, 19), LocalDate.of(2024, 1, 20));
        assertTrue(count > 0);
    }

    @Test
    public void countTotal() {
        assertTrue(reservationDAO.countTotal() > 0);
    }
}