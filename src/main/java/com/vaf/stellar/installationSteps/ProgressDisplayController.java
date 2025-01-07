package com.vaf.stellar.installationSteps;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import static com.vaf.stellar.installationSteps.DownloadAndInstallJar.downloadFile;

public class ProgressDisplayController {
    @FXML
    private ProgressBar downloadProgressBar;
    @FXML
    private Label percentageLabel;

    @FXML
    private Button continueButton;
    @FXML
    private Button resumeButton;


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
                //statusLabelGroup.setVisible(true);
                percentageLabel.setText("Download Complete");
                //continueButton.setDisable(Boolean.FALSE);
            }
        });
    }

    public void enableResumeButton(String fileURL, String saveFilePath, double startProgress, double endProgress, ProgressDisplayController controller){
        try{
            resumeButton.setVisible(Boolean.TRUE);
            resumeButton.setDisable(Boolean.FALSE);
            while(!resumeButton.isDisabled()){
                Thread.sleep(1);
            }
            downloadFile( fileURL,  saveFilePath,  startProgress,  endProgress,  controller);
        }catch (Exception e){

            e.printStackTrace();
        }
    }
    private void goToPreviousScreen() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/stellar.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) arrowImageView.getScene().getWindow();
            stage.setScene(scene);
        }catch (IOException e) {
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
    public void showErrorPopup(String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An Error Occurred");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }

    @FXML
    public void onClickResume(){
        if(this.resumeButton.isVisible()){
            this.resumeButton.setDisable(Boolean.TRUE);
        }
    }
}
