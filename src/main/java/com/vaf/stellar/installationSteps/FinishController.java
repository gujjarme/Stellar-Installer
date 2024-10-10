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

public class FinishController {

    @FXML
    private ImageView infoImageView;


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
        continueButton.setOnAction(event -> closeWindow());
        isPlaying= Boolean.FALSE;



    }

    private void openWebViewWindow() {
        String videoUrl = OSUtils.getFinishVideoURL();
        webView.setVisible(Boolean.TRUE);
        infoImageView.setVisible(Boolean.FALSE);
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        this.isPlaying=Boolean.TRUE;
        webEngine.load(videoUrl);
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains("youtube.com")) {
                System.out.println("YouTube logo clicked! Preventing navigation.");
                webView.getEngine().executeScript("document.querySelector('video').pause();");
                webEngine.load(videoUrl); // Cancel navigation back to the original pagen
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
                    ErrorUtils.showInfoPopup("Couldn't launch default browser.");
                    e.printStackTrace();
                }
            } else {

            }
        }).start(); // Start a new thread to handle the Desktop API call
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

    private void closeWindow() {
// Close the current window
        Stage stage = (Stage) continueButton.getScene().getWindow();
        stage.close();
    }

}
