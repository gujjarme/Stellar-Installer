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
    private Boolean isPlaying;

    private boolean isVideoPlaying = false; // Flag to track if the video is playing

    @FXML
    public void initialize() {
        infoImageView.setOnMouseClicked(event -> openWebViewWindow());
        arrowImageView.setOnMouseClicked(event -> goToPreviousScreen()); // This will now stop the video if playing
        downloadJDKLink.setOnAction(event -> openJDKDownloadPage());
        continueButton.setOnAction(event -> proceedToMavenInstallation());
    }

    private void openWebViewWindow() {
        String videoUrl = "https://www.youtube.com/embed/P_tAU3GM9XI?autoplay=1&controls=1";
        String videoUrlPaused = "https://www.youtube.com/embed/P_tAU3GM9XI?&controls=1";
        String containerId = "movie_player";
        webView.setVisible(Boolean.TRUE);
        infoImageView.setVisible(Boolean.FALSE);
        WebEngine webEngine = webView.getEngine();
        this.isPlaying=Boolean.TRUE;
        webEngine.load(videoUrl);
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains("youtube.com")) {
                System.out.println("YouTube logo clicked! Preventing navigation.");
                //webView.getEngine().executeScript("document.querySelector('video').pause();");
                webEngine.load(videoUrlPaused); // Cancel navigation back to the original pagen
                openInSystemBrowserAsync(videoUrl);

            }
        });

    }
    private void openInSystemBrowserAsync(String url) {
        new Thread(() -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Opening a browser is not supported on this system.");
            }
        }).start(); // Start a new thread to handle the Desktop API call
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
            if (isPlaying){
                webView.getEngine().executeScript("document.querySelector('video').pause();");
            }
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
           // webView.setVisible(Boolean.FALSE);
            if (isPlaying){
                webView.getEngine().executeScript("document.querySelector('video').pause();");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/maven-installation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void injectJavaScriptToBlockLogoClick(WebEngine webEngine) {
        webEngine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                webEngine.executeScript("document.querySelectorAll('a').forEach(link => { " +
                        "if (link.href.includes('youtube.com')) { " +
                        "link.addEventListener('click', function(e) { e.preventDefault(); });" +
                        "}});");
            }
        });
    }
}
