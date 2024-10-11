package com.vaf.stellar.installationSteps;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

//    private boolean isVideoPlaying = false; // Flag to track if the video is playing

    @FXML
    public void initialize() {
        infoImageView.setOnMouseClicked(event -> openWebViewWindow());
        arrowImageView.setOnMouseClicked(event -> goToPreviousScreen());
        downloadJDKLink.setOnAction(event -> openJDKDownloadPage());
        continueButton.setOnAction(event -> proceedToMavenInstallation());
        openWebViewWindow();
    }

    private void openWebViewWindow() {
        this.isPlaying = Boolean.TRUE;
        String videoUrl = OSUtils.getJdkURL();
        webView.setVisible(true);
        infoImageView.setVisible(false);
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(videoUrl);
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains("youtube.com")) {
                System.out.println("YouTube logo clicked! Preventing navigation.");
                webEngine.load(videoUrl); // Cancel navigation back to the original page
                openInSystemBrowserAsync(videoUrl);
            }
        });
    }

    private void openInSystemBrowserAsync(String url) {
        new Thread(() -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(URI.create(url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Opening a browser is not supported on this system.");
            }
        }).start(); // Start a new thread to handle the Desktop API call
    }

    private void stopVideo() {
        if (isPlaying) { // Check if video is playing before attempting to pause
            WebEngine webEngine = webView.getEngine();
            webEngine.executeScript("document.querySelector('video').pause();"); // Pause the video
            isPlaying = false; // Update flag to false as video is paused
        }
    }

    private void proceedToMavenInstallation() {
        if (!isJDKInstalled()) {
            showErrorPopup("Please set up JDK in your system to proceed.");
            currentScreen();
            stopVideo();
        } else {
            stopVideo();
            openMavenInstallationScreen();
        }
    }

    private void goToPreviousScreen() {
        stopVideo(); // Check and stop the video if it's playing
        try {
            if (isPlaying) {
                webView.getEngine().executeScript("document.querySelector('video').pause();");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/get-started.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) arrowImageView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showErrorPopup("Something went wrong.");
            e.printStackTrace();
        }
    }

    private void currentScreen() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/jdk-details.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) arrowImageView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openJDKDownloadPage() {
        try {
            String url = "https://www.oracle.com/pk/java/technologies/downloads/";
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void showErrorPopup(String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(errorMessage);
           // alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }

    @FXML
    private void openMavenInstallationScreen() {
        try {
            if (isPlaying) {
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


    private boolean isJDKInstalled() {
        try {
            // Determine the operating system
            String osName = System.getProperty("os.name").toLowerCase();

            // Use the appropriate command based on the OS
            ProcessBuilder processBuilder;
            if (osName.contains("win")) {
                // Windows environment
                processBuilder = new ProcessBuilder("cmd.exe", "/c", "java -version");
            } else {
                // macOS or Linux environment
                processBuilder = new ProcessBuilder("java", "-version");
            }

            processBuilder.redirectErrorStream(true);  // Merge error stream with the output stream
            Process process = processBuilder.start();

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // Check if the output contains "javac" or "Java" to verify JDK presence
                return output.toString().toLowerCase().contains("version");
            } else {
                return false;
            }
        } catch (IOException | InterruptedException e) {
            // Show error or handle exception
            e.printStackTrace();
            return false;
        }
    }

}


