package com.vaf.stellar.installationSteps;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
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

public class MavenController {

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


    @FXML
    public void initialize() {
        // Handle ImageView click to open the WebView window
        infoImageView.setOnMouseClicked(event -> openWebViewWindow());
        arrowImageView.setOnMouseClicked(event -> goToPreviousScreen());
        // Handle Hyperlink click to open the JDK download page
        downloadJDKLink.setOnAction(event -> openJDKDownloadPage());
        //isPlaying = Boolean.FALSE;
        openWebViewWindow();
    }

    private void openWebViewWindow() {

        this.isPlaying=Boolean.TRUE;
        String videoUrl = OSUtils.getMavenURL();
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
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Opening a browser is not supported on this system.");
            }
        }).start(); // Start a new thread to handle the Desktop API call
    }
    private void goToPreviousScreen() {
        try {
            // Load the get-started.fxml file
            if (isPlaying){
                webView.getEngine().executeScript("document.querySelector('video').pause();");
            }
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
            // Define the Maven download URL
            String url = "https://maven.apache.org/download.cgi";

            // Open the URL in the default system browser
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            // Handle any exceptions that occur
            ErrorUtils.showInfoPopup("Could not open default browser.");
            e.printStackTrace();
        }
    }
    @FXML
    private void openIntellijInstallationScreen() {

        try {
            String os = System.getProperty("os.name").toLowerCase();
            if(os.toLowerCase().contains("win")){
                if(!isMavenInstalled()){
                    ErrorUtils.showInfoPopup("Please set up Maven in your system to proceed.");
                    return;
                }
            }
            if (isPlaying){
                webView.getEngine().executeScript("document.querySelector('video').pause();");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/intellij-installation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean isMavenInstalled() {
        try {
            // Use ProcessBuilder to execute the 'mvn -version' command
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "mvn -version");
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
                // Check if the output contains "Maven"
                return output.toString().toLowerCase().contains("maven");
            } else {
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }


}
