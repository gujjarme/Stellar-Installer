package com.vaf.stellar.installationSteps;

import javafx.application.Platform;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadAndInstallJar {
    // Add Progress bar (not used here but could be used for swing-based UIs)
    private static JProgressBar progressBar;

    public static void downloadAndInstallJar(String saveDir, ProgressDisplayController controller) {
        try {
            String jarUrl = "https://bitbucket.org/vertexitsystems1/vaf-sample/downloads/Stellar-1.2.0.jar";
            String projectUrl = "https://bitbucket.org/stellar2/stellar-starter-project/get/master.zip";
            // Download JAR
            String jarFilePath = saveDir + "/Stellar-1.2.0.jar";
            downloadFile(jarUrl, jarFilePath, controller);
            // Assume 50% progress after first file
            Platform.runLater(() -> controller.updateProgress(0.5));

            // Run Maven install after the JAR is downloaded
            runMavenInstall(jarFilePath);

            // Download project
            downloadFile(projectUrl, saveDir + "/stellar-master.zip", controller);
            // Finalize progress
            Platform.runLater(() -> controller.updateProgress(1.0));
            System.out.println("Downloads completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadFile(String fileURL, String saveFilePath, ProgressDisplayController controller) {
        try {
            URL url = new URL(fileURL);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            byte[] buffer = new byte[4096];
            int totalRead = 0;
            int bytesRead;
            int fileSize = connection.getContentLength();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                double progress = (double) totalRead / fileSize;
                Platform.runLater(() -> controller.updateProgress(progress));
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runMavenInstall(String jarPath) throws Exception {
        // Prepare the Maven command
        String mavenCommand = "mvn install:install-file -Dfile=" + jarPath +
                " -DgroupId=Stellar -DartifactId=io.vstellar -Dversion=1.2.0 -Dpackaging=jar";

        // Execute the command
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", mavenCommand);
        processBuilder.directory(new File(System.getProperty("user.home"))); // Run command from user home
        Process process = processBuilder.start();

        process.waitFor();
        System.out.println("Maven install completed.");
    }
}