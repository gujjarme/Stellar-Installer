package com.vaf.stellar.installationSteps;

public class OSUtils {
    private static final String os = System.getProperty("os.name").toLowerCase();

    public static String getVideoURL() {
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/P_tAU3GM9XI?autoplay=1&controls=1";
        } else if (os.contains("mac")) {
            return "https://www.youtube.com/embed/mac-video-url?autoplay=1";
        } else {
            return "https://www.youtube.com/embed/default-video-url?autoplay=1";
        }
    }
}