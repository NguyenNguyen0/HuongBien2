module java.huongbien {
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
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires static lombok;

    exports huongbien.ui;
    opens huongbien.ui to javafx.fxml;
    exports huongbien.controller;
    opens huongbien.controller to javafx.fxml;
}