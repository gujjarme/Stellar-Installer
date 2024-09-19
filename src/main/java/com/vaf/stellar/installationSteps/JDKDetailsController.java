package com.vaf.stellar.installationSteps;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public void initialize() {
        // Handle ImageView click to open the WebView window
        infoImageView.setOnMouseClicked(event -> openWebViewWindow());

        // Handle Hyperlink click to open the JDK download page
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

        webEngine.documentProperty().addListener((observable, oldDoc, newDoc) -> {
            if (newDoc != null) {
                webEngine.executeScript(
                        "document.addEventListener('DOMContentLoaded', function() {" +
                                "   var targetElement = document.getElementById('" + containerId + "');" +
                                "   if (targetElement) {" +
                                "       // Remove all other elements from the body and set styles" +
                                "       var body = document.body;" +
                                "       while (body.firstChild) {" +
                                "           body.removeChild(body.firstChild);" +
                                "       }" +
                                "       body.appendChild(targetElement); " +
                                "       body.style.display = 'flex'; " +
                                "       body.style.justifyContent = 'center'; " +
                                "       body.style.alignItems = 'center'; " +
                                "       body.style.height = '100vh'; " +
                                "       body.style.margin = '0'; " +
                                "       body.style.overflow = 'hidden'; " +
                                "   }" +
                                "});"
                );


            }
        });
//        webEngine.documentProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                webEngine.executeScript(
//                        "document.getElementById('movie_player').style.margin = 'auto';" +
//                                "document.getElementById('movie_player').style.position = 'absolute';" +
//                                "document.getElementById('movie_player').style.left = '50%';" +
//                                "document.getElementById('movie_player').style.top = '50%';" +
//                                "document.getElementById('movie_player').style.transform = 'translate(-50%, -50%)';"
//                );
//            }
//        });

        //WebViewWindowController controller = loader.getController();
        // controller.loadUrl("https://www.youtube.com/watch?v=buECPGQlvV0");

        // Create and configure the WebView window
//            Stage stage = new Stage();
//            stage.setTitle("WebView Window");
//            stage.setScene(new Scene(root, 800, 600)); // Set window size
//            stage.show();

    }

    private void openJDKDownloadPage() {
        try {
            // Define the JDK download URL
            String url = "https://www.oracle.com/java/technologies/javase-jdk11-downloads.html";

            // Open the URL in the default system browser
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            // Handle any exceptions that occur
            e.printStackTrace();
        }
    }
}
