package com.vaf.stellar.installationSteps;

import javafx.application.Platform;
import javafx.scene.control.Alert;

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
