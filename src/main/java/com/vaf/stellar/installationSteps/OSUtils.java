package com.vaf.stellar.installationSteps;

public class OSUtils {
    private static final String os = System.getProperty("os.name").toLowerCase();

    public static String getJdkURL() {
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/9PKsZEDCLHc";
        } else{
            return "https://www.youtube.com/embed/9PKsZEDCLHc";
        }
    }
    public static String getIntellijVideo(){
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/JDiz99v13fo";
        } else{
            return "https://www.youtube.com/embed/JDiz99v13fo";
        }
    }
    public static String getMavenURL() {
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/g-ixjFtKPWk";
        } else{
            return "https://www.youtube.com/embed/g-ixjFtKPWk";
        }
    }
}

