package com.vaf.stellar.installationSteps;


import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebViewWindowController {

    @FXML
    private WebView webView;

    public void loadUrl(String url) {
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);  // Load the provided URL in the WebView
    }
}
