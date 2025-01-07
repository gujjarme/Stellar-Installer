package com.vaf.stellar.installationSteps;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class StellarDownloadInfoController {

    @FXML
    private Button continueButton;
    @FXML
    private Button resumeButton;

    @FXML
    private ImageView arrowImageView;


    @FXML
    public void initialize() {

        arrowImageView.setOnMouseClicked(event -> goToPreviousScreen());
    }



    private void goToPreviousScreen() {
        try {
            // Load the get-started.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/stellar.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) arrowImageView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void FinishScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/finish.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void resumeDownload(){

    }

}
