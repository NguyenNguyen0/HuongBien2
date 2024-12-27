package huongbien2.controller;

import huongbien2.entity.Customer;
import huongbien2.util.JPAUtil;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer customer = new Customer();
        customer.setId(Customer.randomId());
        customer.setName("John Joestar");
        customer.setAddress("123 Street, City, Country");
        customer.setGender(1);
        customer.setPhoneNumber("1234567890");
        customer.setEmail("join@lmail.com");
        customer.setBirthday(java.time.LocalDate.of(2000, 1, 1));
        customer.setRegistrationDate(java.time.LocalDate.now());
        customer.setAccumulatedPoints(1000);
        customer.setMembershipLevel(1);

//        Sử dụng lớp JPAUtil để lấy instance EntityManager
        EntityManager entityManager = JPAUtil.getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(customer);
            entityManager.getTransaction().commit();
            welcomeText.setText("Customer has been saved!: " + customer);
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            welcomeText.setText("Customer cannot saved!");
        } finally {
            entityManager.close();
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}