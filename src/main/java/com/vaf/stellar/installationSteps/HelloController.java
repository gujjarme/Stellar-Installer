package com.vaf.stellar.installationSteps;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private Button getStartedButton;

    @FXML
    protected void onGetStartedButtonClick() {
        try {
            // Load the new FXML (JDKDetails)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/jdk-details.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) getStartedButton.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
