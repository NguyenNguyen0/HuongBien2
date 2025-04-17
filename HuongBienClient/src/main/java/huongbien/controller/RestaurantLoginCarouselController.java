package huongbien.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class RestaurantLoginCarouselController implements Initializable {
    @FXML
    private AnchorPane slide01;
    @FXML
    private AnchorPane slide02;
    @FXML
    private AnchorPane slide03;
    @FXML
    private AnchorPane slide04;
    private int currentSlide = 0;

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slide01.setTranslateY(0);
        slide02.setTranslateY(1000);
        slide03.setTranslateY(2000);
        slide04.setTranslateY(3000);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3.5), event -> autoSlide())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void autoSlide() {
        if (currentSlide == 0) {
            translateAnimation(0.5, slide01, -1000);
            translateAnimation(0.5, slide02, -1000);
            translateAnimation(0.5, slide03, -1000);
            translateAnimation(0.5, slide04, -1000);
            currentSlide = 1;
        } else if (currentSlide == 1) {
            translateAnimation(0.5, slide01, -1000);
            translateAnimation(0.5, slide02, -1000);
            translateAnimation(0.5, slide03, -1000);
            translateAnimation(0.5, slide04, -1000);
            currentSlide = 2;
        } else if (currentSlide == 2) {
            translateAnimation(0.5, slide01, -1000);
            translateAnimation(0.5, slide02, -1000);
            translateAnimation(0.5, slide03, -1000);
            translateAnimation(0.5, slide04, -1000);
            currentSlide = 3;
        } else if (currentSlide == 3) {
            slide01.setTranslateY(0);
            slide02.setTranslateY(1000);
            slide03.setTranslateY(2000);
            slide04.setTranslateY(3000);
            currentSlide = 0;
        }
    }

    public void translateAnimation(double duration, Node node, double height) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByY(height);
        translateTransition.play();
    }
}
