package com.example.lab7;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 770, 400);
        stage.setTitle("Lab 7");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            Controller.StopThreads();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}