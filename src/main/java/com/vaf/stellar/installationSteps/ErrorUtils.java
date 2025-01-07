package com.vaf.stellar.installationSteps;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class ErrorUtils {

    public static void showInfoPopup(String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(errorMessage);
            //alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }
    public static void showInfoPopup(String errorMessage, String option1Text, Runnable option1Action, String option2Text, Runnable option2Action) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(errorMessage);

            // Create buttons for the CTAs
            ButtonType option1Button = new ButtonType(option1Text);
            ButtonType option2Button = new ButtonType(option2Text);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            // Add buttons to the alert
            alert.getButtonTypes().setAll(option1Button, option2Button, cancelButton);

            // Show the alert and wait for user response
            alert.showAndWait().ifPresent(response -> {
                if (response == option1Button) {
                    option1Action.run();
                } else if (response == option2Button) {
                    option2Action.run();
                }
                // No action for the cancel button
            });
        });
    }


    public static void showErrorPopup(String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(errorMessage);
            //alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }
}
