package com.vaf.stellar.installationSteps;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

public class IntellijController {

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


    @FXML
    public void initialize() {

        infoImageView.setOnMouseClicked(event -> openWebViewWindow());
        arrowImageView.setOnMouseClicked(event -> goToPreviousScreen());
        downloadJDKLink.setOnAction(event -> openJDKDownloadPage());
    }

    private void openWebViewWindow() {
        String videoUrl = "https://www.youtube.com/embed/P_tAU3GM9XI?autoplay=1";
        String containerId = "movie_player";
        webView.setVisible(Boolean.TRUE);
        infoImageView.setVisible(Boolean.FALSE);
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        webEngine.load(videoUrl);



    }

    private void goToPreviousScreen() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/maven-installation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) arrowImageView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openJDKDownloadPage() {
        try {

            String url = "https://www.jetbrains.com/idea/";
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openStellarInstallationScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/stellar.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
