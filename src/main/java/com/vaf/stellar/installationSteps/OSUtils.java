package com.vaf.stellar.installationSteps;

public class OSUtils {
    private static final String os = System.getProperty("os.name").toLowerCase();

    public static String getVideoURL() {
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/HtXJe1rAZaI?autoplay=1&controls=1";
        } else if (os.contains("mac")) {
            return "https://www.youtube.com/embed/HtXJe1rAZaI?autoplay=1";
        } else {
            return "https://www.youtube.com/embed/HtXJe1rAZaI?autoplay=1";
        }
    }
}
