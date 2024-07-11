package com.example.onlinebanksystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("frm_Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
        ControlManager controlManager = fxmlLoader.getController();
        controlManager.displayLoginContent(controlManager);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.showAllStudent();


    }
}
