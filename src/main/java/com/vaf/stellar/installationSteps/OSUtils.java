package com.vaf.stellar.installationSteps;

public class OSUtils {
    private static final String os = System.getProperty("os.name").toLowerCase();

    public static String getJdkURL() {
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/9PKsZEDCLHc?&controls=1";
        } else{
            return "https://www.youtube.com/embed/4TIMpzZwMOY?&controls=1";
        }
    }
    public static String getIntellijVideo(){
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/JDiz99v13fo?&controls=1";
        } else{
            return "https://www.youtube.com/embed/nAUJzOuzIf0?&controls=1";
        }
    }
    public static String getMavenURL() {
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/g-ixjFtKPWk?&controls=1";
        } else{
            return "https://www.youtube.com/embed/3Zh_aCY_GUw?&controls=1";
        }
    }

    public static String getFinishVideoURL() {
        if (os.contains("win")) {
            return "https://www.youtube.com/embed/Ddb81dvT7Wc?1&controls=1";
        } else{
            return "https://www.youtube.com/embed/9glrISDStFQ?&controls=1";
        }
    }
    public static String getIntellijDocURL() {
        if (os.contains("win")) {
            return "https://www.jetbrains.com/idea/download/?section=windows";
        } else{
            return "https://www.jetbrains.com/idea/download/?section=mac";
        }
    }
}

