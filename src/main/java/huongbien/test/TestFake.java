package huongbien.test;

import huongbien.dao.EmployeeDAO;
import huongbien.data.DataGenerator;
import huongbien.entity.Cuisine;
import huongbien.entity.Employee;
import huongbien.entity.Order;
import huongbien.entity.Reservation;

import java.time.LocalDate;

public class TestFake {
    public static void main(String[] args) {
        Reservation reservation = DataGenerator.fakeReservation(LocalDate.now());
        System.out.println(reservation);
        System.out.println(reservation.getFoodOrders());
        System.out.println(reservation.getTables());

    }
}
