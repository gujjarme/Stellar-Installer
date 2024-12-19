package com.vaf.stellar.installationSteps;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class StellarController {

    @FXML
    private Button continueButton;

    @FXML
    private Button startDownloadButton;

    @FXML
    private ImageView arrowImageView;


    @FXML
    public void initialize() {

        arrowImageView.setOnMouseClicked(event -> goToPreviousScreen());
        startDownloadButton.setOnMouseClicked(mouseEvent -> onStartDownloadClick());
    }



    private void goToPreviousScreen() {
        try {
            // Load the get-started.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/intellij-installation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) arrowImageView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onStartDownloadClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select the Download Location");
        File selectedDirectory = directoryChooser.showDialog(startDownloadButton.getScene().getWindow());

        if (selectedDirectory != null) {
            startDownload(selectedDirectory.getAbsolutePath());
        }
    }

    private void startDownload(String saveDir) {
        // Load the progress scene and pass the save directory to it
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/progressDisplay.fxml"));
            Scene scene = new Scene(loader.load());
            ProgressDisplayController controller = loader.getController();

            Stage stage = (Stage) startDownloadButton.getScene().getWindow();
            stage.setScene(scene);
            controller.beginDownload(saveDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
