package code.util;

public class PlatformUtil {

    private final static String OS = System.getProperty("os.name");
    private static Platform PlatformCache = Platform.Unknown;

    static {
        String os = OS.toLowerCase();
        if (os.contains("windows")) {
            PlatformCache = Platform.Windows;
        } else if (os.contains("mac")) {
            PlatformCache = Platform.Mac;
        } else {
            PlatformCache = Platform.Other;
        }
    }

    public enum Platform {
        Windows,
        Mac,
        Other,
        Unknown,
        ;
    }

    public static Platform getPlatform() {
        return PlatformCache;
    }

    public static boolean isWindows() {
        switch (PlatformCache) {
            case Windows:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMacos() {
        switch (PlatformCache) {
            case Mac:
                return true;
            default:
                return false;
        }
    }

}
