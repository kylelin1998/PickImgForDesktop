package code.config;

import code.util.PlatformUtil;

import java.io.File;

public class Config {

    public static class MetaData {
        public final static String CurrentVersion = "1.1.0";
        public final static String ProcessBaseName = "PickImgForDesktop";
        public final static String ProcessName = PlatformUtil.isWindows() ? ProcessBaseName + ".exe" : ProcessBaseName;
        public final static String GitOwner = "kylelin1998";
        public final static String GitRepo = "PickImgForDesktop";
    }

    public static class AboutData {
        public final static String GithubProjectUrl = "https://github.com/kylelin1998/PickImgForDesktop";
        public final static String WebSite = "https://kylelin1998.com";
        public final static String Channel = "https://t.me/KyleLin1998Channel";
        public final static String Email = "email@kylelin1998.com";
        public final static String Author = "kylelin1998";
    }

    public static String CurrentPath;

    public final static String SchemeConfigPath;

    public final static String TempPath;

    static {
        PlatformUtil.Platform platform = PlatformUtil.getPlatform();
        switch (platform) {
            case Windows:
                CurrentPath = System.getProperty("user.home") + "/AppData/Local/" + MetaData.ProcessBaseName;
                break;
            case Mac:
                CurrentPath = System.getProperty("user.home") + "/AppData/" + MetaData.ProcessBaseName;
                break;
            default:
                CurrentPath = System.getProperty("user.home") + "/" + MetaData.ProcessBaseName;
                break;
        }
        SchemeConfigPath = CurrentPath + "/config/scheme_config.json";
        TempPath = System.getProperty("user.dir") + "/temp";

        File file = new File(CurrentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
