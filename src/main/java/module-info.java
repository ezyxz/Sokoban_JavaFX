module XinyuanZuo_IntelliJ_11 {
    requires javafx.media;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;
    requires junit;
    opens sample.controller;
    opens sample.scene;
    opens sample.dataoperation;
    opens sample.load;
    opens sample.ui;
}