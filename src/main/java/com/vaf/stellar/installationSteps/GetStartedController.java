package com.vaf.stellar.installationSteps;

import com.vaf.stellar.GetStarted;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GetStartedController {

    @FXML
    private Button getStartedButton;

    @FXML
    protected void onGetStartedButtonClick() {
        try {
            // Load the new FXML (JDKDetails)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vaf/stellar/views/jdk-details.fxml"));
            GetStarted.globalScene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) getStartedButton.getScene().getWindow();

            // Set the new scene
            stage.setScene(GetStarted.globalScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
