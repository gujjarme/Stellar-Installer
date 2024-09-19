package com.vaf.stellar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GetStarted extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GetStarted.class.getResource("/com/vaf/stellar/views/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 945.48, 590.81);
        stage.setTitle("Stellar Installer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}