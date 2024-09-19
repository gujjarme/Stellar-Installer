module com.vaf.stellarinstaller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    // Allow JavaFX to reflect on these packages
    opens com.vaf.stellar to javafx.fxml;
    opens com.vaf.stellar.installationSteps to javafx.fxml;

    // Export packages so JavaFX runtime can access them
    exports com.vaf.stellar;
    exports com.vaf.stellar.installationSteps to javafx.graphics;
}
