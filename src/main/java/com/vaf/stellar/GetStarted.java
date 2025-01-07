package com.vaf.stellar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GetStarted extends Application {

    public static Scene globalScene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GetStarted.class.getResource("/com/vaf/stellar/views/get-started.fxml"));
        GetStarted.globalScene = new Scene(fxmlLoader.load(), 945.48, 590.81);
        stage.setTitle("Stellar Installer");
        Image image = new Image(GetStarted.class.getResourceAsStream("/com/vaf/stellar/assets/Eva.png"));
        stage.getIcons().add(image);
        stage.setResizable(Boolean.FALSE);
        stage.setScene(GetStarted.globalScene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}