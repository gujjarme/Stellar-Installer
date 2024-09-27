package com.vaf.stellar.installationSteps;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ProgressDisplayController {
    @FXML
    private ProgressBar downloadProgressBar;
    @FXML
    private Label percentageLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button continueButton;
    @FXML
    private ImageView arrowImageView;// Add a reference to the status label
    @FXML
    public void initialize() {

        arrowImageView.setOnMouseClicked(event -> goToPreviousScreen());
    }
    public void beginDownload(String saveDir) {
        DownloadAndInstallJar.downloadAndInstallJar(saveDir, this);
    }
    public void updateProgress(double progress) {
        Platform.runLater(() -> {
            downloadProgressBar.setProgress(progress);
            percentageLabel.setText(String.format("%.0f%%", progress * 100));

            // Change the status label when progress reaches 100%
            if (progress >= 1.0) {
                statusLabel.setText("Installation done");
                continueButton.setDisable(false);// Change the label text to "Installation done"
            }
        });
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
}