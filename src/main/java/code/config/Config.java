package code.config;

import code.util.PlatformUtil;

import java.io.File;

public class Config {

    public static class MetaData {
        public final static String CurrentVersion = "1.1.0";
        public final static String ProcessBaseName = "PickImgForDesktop";
        public final static String ProcessName = ProcessBaseName + ".exe";
        public final static String GitOwner = "kylelin1998";
        public final static String GitRepo = "PickImgForDesktop";
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
            System.out.println(file.mkdirs());;
        }
    }



}
