@SuppressWarnings("JavaModuleNaming")
module java.huongbien2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jakarta.persistence;

    exports huongbien2.ui;
    opens huongbien2.ui to javafx.fxml;
    exports huongbien2.controller;
    opens huongbien2.controller to javafx.fxml;
}