package com.vaf.stellar.installationSteps;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class DownloadAndInstallJar {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10,TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();


    public static void downloadAndInstallJar(String saveDir, ProgressDisplayController controller) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    String mavenUrl = "https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip";
                    String jarUrl = "https://bitbucket.org/stellar2/stellar-starter-project/downloads/Stellar-1.2.0.jar";
                    String projectUrl = "https://bitbucket.org/stellar2/stellar-starter-project/get/master.zip";

                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        //Download Maven
                        downloadFile(mavenUrl, saveDir + File.separator + "apache-maven-3.9.9.zip", 0.0, 0.1, controller);
                        extractAndRenameZip(saveDir + File.separator + "apache-maven-3.9.9.zip", saveDir, "apache-maven-3.9.9", 0.1, 0.2, controller);
                        changeMavenPermissions(saveDir + File.separator + "apache-maven-3.9.9/bin/mvn");
                    }

                    // Download JAR (0% to 30%)
                    String jarFilePath = saveDir + File.separator + "Stellar-1.2.0.jar";
                    downloadFile(jarUrl, jarFilePath, 0.2, 0.4, controller);


                    if(!runMavenInstall(jarFilePath, 0.4, 0.6, controller,saveDir+File.separator+"apache-maven-3.9.9/bin/mvn")){
                        ErrorUtils.showInfoPopup("Something went wrong while Installing the Jar.");
                    }

                    // Download project zip (60% to 80%)
                    String zipFilePath = saveDir + File.separator + "stellar-master.zip";
                    downloadFile(projectUrl, zipFilePath, 0.6, 0.8, controller);

                    // Extract zip file and rename the directory (80% to 100%)
                    extractAndRenameZip(zipFilePath, saveDir, "stellar-starter-project", 0.8, 1.0, controller);

                    // Finalize progress
                    updateProgress(1.0, 1.0);
                    Platform.runLater(() -> controller.updateProgress(1.0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.progressProperty().addListener((obs, oldProgress, newProgress) ->
                Platform.runLater(() -> controller.updateProgress(newProgress.doubleValue()))
        );

        executor.execute(task);
    }
    public static void downloadFile(String fileURL, String saveFilePath, double startProgress, double endProgress, ProgressDisplayController controller) {
        Request request = new Request.Builder()
                .url(fileURL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to download file: HTTP " + response.code());
            }

            InputStream inputStream = response.body().byteStream();
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            byte[] buffer = new byte[4096];
            int totalRead = 0;
            int bytesRead;
            int fileSize = (int) response.body().contentLength();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                double progress = startProgress + (double) totalRead / fileSize * (endProgress - startProgress);

                Platform.runLater(() -> controller.updateProgress(progress));

            }

            outputStream.close();
            inputStream.close();
        }catch (SocketTimeoutException socketTimeoutException){
            ErrorUtils.showInfoPopup("Network Error");
            controller.enableResumeButton();
            throw new RuntimeException();

        }
        catch (Exception e) {
//            Platform.runLater(() -> {
//
//
//            });
            ErrorUtils.showInfoPopup("Something went wrong.");
            e.printStackTrace();
        }
    }
    public static boolean changeMavenPermissions(String mavenPath) {
        // Command to change the permissions
        String[] command = { "chmod", "+x", mavenPath };

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {
            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to complete
            int exitCode = process.waitFor();

            // Check the exit code
            if (exitCode == 0) {
                System.out.println("Permissions changed successfully for: " + mavenPath);
                return true; // Success
            } else {
                System.err.println("Failed to change permissions. Exit code: " + exitCode);
                // Log the error output=
                return false; // Failure
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred while changing permissions: " + e.getMessage());
            return false; // Exception occurred
        }
    }
//    private static void downloadFile(String fileURL, String saveFilePath, double startProgress, double endProgress, ProgressDisplayController controller) throws InterruptedException {
//        try {
//            URL url = new URL(fileURL);
//            URLConnection connection = url.openConnection();
//            InputStream inputStream = connection.getInputStream();
//            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
//
//            byte[] buffer = new byte[4096];
//            int totalRead = 0;
//            int bytesRead;
//            int fileSize = connection.getContentLength();
//
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//                totalRead += bytesRead;
//                double progress = startProgress + (double) totalRead / fileSize * (endProgress - startProgress);
//
//                // Smooth progress updates
//                Platform.runLater(() -> controller.updateProgress(progress));
//                //Thread.sleep(20); // Adjust the delay for smoother progress effect
//            }
//            outputStream.close();
//            inputStream.close();
//        } catch (Exception e) {
//            ErrorUtils.showInfoPopup("Something went wrong.");
//            controller.enableResumeButton();
//            e.printStackTrace();
//        }
//    }

    private static boolean runMavenInstall(String jarPath, double startProgress, double endProgress, ProgressDisplayController controller,String mavenPath) throws IOException, InterruptedException {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            // Create the ProcessBuilder directly with Maven command split into arguments
            ProcessBuilder processBuilder = new ProcessBuilder(mavenPath, "install:install-file",
                    "-Dfile=" + jarPath,
                    "-DgroupId=Stellar",
                    "-DartifactId=io.vstellar",
                    "-Dversion=1.2.0",
                    "-Dpackaging=jar");

            //
            // Set the working directory to user's home

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

        else{
            String os = System.getProperty("os.name").toLowerCase();
            String mavenKeyword = os.contains("win") ? "mvn.cmd" : "mvn";


            // Create the ProcessBuilder directly with Maven command split into arguments
            ProcessBuilder processBuilder = new ProcessBuilder(mavenKeyword, "install:install-file",
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
            return process.exitValue() == 0;
        }
    }


    private static void extractAndRenameZip(String zipFilePath, String extractDir, String newDirName, double startProgress, double endProgress, ProgressDisplayController controller) {
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
        } catch (Exception e) {
            ErrorUtils.showInfoPopup("Something went wrong while Extracting.");
            e.printStackTrace();
        }
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