package huongbien.controller;

import huongbien.entity.Cuisine;
import huongbien.entity.Customer;
import huongbien.util.JPAUtil;
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
//        Sử dụng lớp JPAUtil để lấy instance EntityManager
        EntityManager entityManager = JPAUtil.getEntityManager();

        try {
            Cuisine cuisine = entityManager.find(Cuisine.class, "CU001");
            welcomeText.setText("Customer has been saved!: " + cuisine);
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            welcomeText.setText("Customer cannot saved!");
        } finally {
            JPAUtil.close();
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}