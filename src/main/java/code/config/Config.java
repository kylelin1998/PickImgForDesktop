package code.config;

import java.io.File;
import java.nio.file.Paths;

public class Config {

    public static class MetaData {
        public final static String CurrentVersion = "1.1.0";
        public final static String ProcessBaseName = "PickImgForDesktop";
        public final static String ProcessName = ProcessBaseName + ".exe";
        public final static String GitOwner = "kylelin1998";
        public final static String GitRepo = "PickImgForDesktop";
    }

    public static String CurrentPath = System.getProperty("user.home") + "/AppData/Local/" + MetaData.ProcessBaseName;

    public final static String ResourcePath = Paths.get("src","main","resources").toFile().getAbsolutePath();

    public final static String SchemeConfigPath = CurrentPath + "/config/scheme_config.json";

    public final static String TempPath = System.getProperty("user.dir") + "/temp";

    static {
        File file = new File(CurrentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
