package huongbien.controller;

import huongbien.entity.Category;
import huongbien.entity.Cuisine;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
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
//        Mặc định lấy EntityManager của PersistenceUnit.MARIADB_JPA còn không thì truyền vào tham số PersistenceUnit
        EntityManager entityManager = JPAUtil.getEntityManager(PersistenceUnit.MARIADB_JPA_CREATE);

        try {

            entityManager.getTransaction().begin();
            Category category = new Category();
            category.setId("C001");
            category.setName("Loại món 1");
            entityManager.persist(category);
            entityManager.getTransaction().commit();
            welcomeText.setText(entityManager.find(Category.class, "C001").getName());
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}