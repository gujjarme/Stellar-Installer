package com.vaf.stellar.installationSteps;

import javafx.application.Platform;
import javafx.concurrent.Task;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadAndInstallJar {
    private static JProgressBar progressBar;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void downloadAndInstallJar(String saveDir, ProgressDisplayController controller) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    String jarUrl = "https://bitbucket.org/stellar2/stellar-starter-project/downloads/Stellar-1.2.0.jar";
                    String projectUrl = "https://bitbucket.org/stellar2/stellar-starter-project/get/master.zip";

                    // Download JAR (0% to 30%)
                    String jarFilePath = saveDir + "/Stellar-1.2.0.jar";
                    if (!downloadFile(jarUrl, jarFilePath, 0.0, 0.3, controller)) {
                        showErrorAndCancel("Failed to download JAR file.", controller);
                        return null;
                    }

                    // Run Maven install (30% to 60%)
                    if (!runMavenInstall(jarFilePath, 0.3, 0.6, controller)) {
                        showErrorAndCancel("Maven installation failed.", controller);
                        return null;
                    }

                    // Download project zip (60% to 80%)
                    String zipFilePath = saveDir + "/stellar-master.zip";
                    if (!downloadFile(projectUrl, zipFilePath, 0.6, 0.8, controller)) {
                        showErrorAndCancel("Failed to download project zip file.", controller);
                        return null;
                    }

                    // Extract zip file and rename the directory (80% to 100%)
                    if (!extractAndRenameZip(zipFilePath, saveDir, "stellar-starter-project", 0.8, 1.0, controller)) {
                        showErrorAndCancel("Failed to extract project zip file.", controller);
                        return null;
                    }

                    // Finalize progress
                    updateProgress(1.0, 1.0);
                    Platform.runLater(() -> controller.updateProgress(1.0));
                } catch (Exception e) {
                    showErrorAndCancel("An unexpected error occurred.", controller);
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Bind the task's progress to the controller's progress update
        task.progressProperty().addListener((obs, oldProgress, newProgress) ->
                Platform.runLater(() -> controller.updateProgress(newProgress.doubleValue()))
        );

        executor.execute(task);
    }

    private static boolean downloadFile(String fileURL, String saveFilePath, double startProgress, double endProgress, ProgressDisplayController controller) {
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
                double progress = startProgress + (double) totalRead / fileSize * (endProgress - startProgress);

                // Smooth progress updates
                Platform.runLater(() -> controller.updateProgress(progress));
                Thread.sleep(20); // Adjust the delay for smoother progress effect
            }
            outputStream.close();
            inputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



//    private static void runMavenInstall(String jarPath, double startProgress, double endProgress, ProgressDisplayController controller) throws Exception {
//        String os = System.getProperty("os.name").toLowerCase();
//        String mavenCommand = "mvn install:install-file -Dfile=" + jarPath +
//                " -DgroupId=Stellar -DartifactId=io.vstellar -Dversion=1.2.0 -Dpackaging=jar";
//
//        ProcessBuilder processBuilder;
//        if (os.contains("win")) {
//            processBuilder = new ProcessBuilder("cmd.exe", "/c", mavenCommand);
//        } else {
//            processBuilder = new ProcessBuilder("/bin/sh", "-c", mavenCommand);
//        }
//
//        processBuilder.directory(new File(System.getProperty("user.home")));
//        Process process = processBuilder.start();
//
//        // Simulate progress for Maven installation
//        for (int i = 0; i <= 100; i += 10) {
//            double progress = startProgress + (i / 100.0) * (endProgress - startProgress);
//            Platform.runLater(() -> controller.updateProgress(progress));
//            Thread.sleep(100); // Adjust the delay as needed for smooth progress
//        }
//
//        process.waitFor();
//    }
private static boolean runMavenInstall(String jarPath, double startProgress, double endProgress, ProgressDisplayController controller) throws IOException, InterruptedException {
    String mavenCommand = "mvn install:install-file -Dfile=" + jarPath +
            " -DgroupId=Stellar -DartifactId=io.vstellar -Dversion=1.2.0 -Dpackaging=jar";

    // Create the ProcessBuilder directly with Maven command split into arguments
    ProcessBuilder processBuilder = new ProcessBuilder("mvn", "install:install-file",
            "-Dfile=" + jarPath,
            "-DgroupId=Stellar",
            "-DartifactId=io.vstellar",
            "-Dversion=1.2.0",
            "-Dpackaging=jar");

    processBuilder.directory(new File(System.getProperty("user.home")));  // Set the working directory to user's home

    Process process = processBuilder.start();  // Start the process

    // Simulate progress for Maven installation
    for (int i = 0; i <= 100; i += 10) {
        double progress = startProgress + (i / 100.0) * (endProgress - startProgress);
        Platform.runLater(() -> controller.updateProgress(progress));  // Update progress in UI
        Thread.sleep(100);  // Adjust the delay as needed for smooth progress
    }

    process.waitFor();  // Wait for the process to complete

   return process.exitValue() == 0;  // Return true if Maven install was successful
}

    private static boolean extractAndRenameZip(String zipFilePath, String extractDir, String newDirName, double startProgress, double endProgress, ProgressDisplayController controller) {
        try {
            File destDir = new File(extractDir);
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zipEntry = zis.getNextEntry();
            File extractedDir = null;

            File existingProjectDir = new File(destDir, newDirName);
            if (existingProjectDir.exists()) {
                deleteDirectory(existingProjectDir);
            }

            int totalEntries = 0;
            while (zipEntry != null) {
                totalEntries++;
                zipEntry = zis.getNextEntry();
            }
            zis.close();

            zis = new ZipInputStream(new FileInputStream(zipFilePath));
            zipEntry = zis.getNextEntry();
            int processedEntries = 0;

            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (extractedDir == null) {
                        extractedDir = newFile;
                    }
                    if (!newFile.exists()) {
                        newFile.mkdirs();
                    }
                } else {
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }

                processedEntries++;
                double progress = startProgress + (processedEntries / (double) totalEntries) * (endProgress - startProgress);
                Platform.runLater(() -> controller.updateProgress(progress));
                Thread.sleep(20); // Adjust the delay for smooth effect

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

            if (extractedDir != null) {
                File renamedDir = new File(destDir, newDirName);
                extractedDir.renameTo(renamedDir);
            }

            File zipFile = new File(zipFilePath);
            zipFile.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void showErrorAndCancel(String errorMessage, ProgressDisplayController controller) {
        Platform.runLater(() -> {
            controller.showErrorPopup(errorMessage); // Assuming `showErrorPopup` is a method in `ProgressDisplayController`
            controller.updateProgress(0); // Reset progress to 0
        });
    }

    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }
}