package com.vaf.stellar.installationSteps;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JDKDetailsController {

    @FXML
    private ImageView infoImageView;

    @FXML
    private Hyperlink downloadJDKLink;

    @FXML
    private WebView webView;

    @FXML
    private AnchorPane webViewContainer;

    @FXML
    private Button continueButton;

    @FXML
    private ImageView arrowImageView;

    private boolean isVideoPlaying = false; // Flag to track if the video is playing

    @FXML
    public void initialize() {
        // Existing initialization code
        infoImageView.setOnMouseClicked(event -> openWebViewWindow());
        arrowImageView.setOnMouseClicked(event -> goToPreviousScreen()); // This will now stop the video if playing
        downloadJDKLink.setOnAction(event -> openJDKDownloadPage());
        continueButton.setOnAction(event -> proceedToMavenInstallation());
    }

    private void openWebViewWindow() {
        String videoUrl = "https://www.youtube.com/embed/P_tAU3GM9XI?autoplay=1&controls=1";
        webView.setVisible(true);
        infoImageView.setVisible(false);
        WebEngine webEngine = webView.getEngine();
        webEngine.load(videoUrl);
        isVideoPlaying = true; // Set flag to true when the video starts
    }

    private void stopVideo() {
        if (isVideoPlaying) { // Check if video is playing before attempting to pause
            WebEngine webEngine = webView.getEngine();
            webEngine.executeScript("document.querySelector('video').pause();"); // Pause the video
            isVideoPlaying = false; // Update flag to false as video is paused
        }
    }

    private void proceedToMavenInstallation() {
        stopVideo(); // Check and stop the video if it's playing
        openMavenInstallationScreen(); // Proceed to the next screen
    }

    private void goToPreviousScreen() {
        stopVideo(); // Check and stop the video if it's playing
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/get-started.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) arrowImageView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openJDKDownloadPage() {
        try {
            String url = "https://www.oracle.com/java/technologies/javase-jdk11-downloads.html";
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openMavenInstallationScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/maven-installation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
