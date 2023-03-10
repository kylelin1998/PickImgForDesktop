package code.util;

import lombok.extern.slf4j.Slf4j;
import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ProgramUtil {

    private final static String Dir = System.getProperty("user.dir");
    private final static String StartupPath = "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup";

    private final static String RestartBat =
            "@echo off\n" +
            "taskkill -f /im ${processName}\n" +
            "start /d \"%~dp0\" ${processName}\n" +
            "exit";
    private final static String UpgradeBat =
                    "@echo off\n" +
                    "taskkill -f /im ${programName}\n" +
                    "del %~dp0\\${programName}\n" +
                    "move %~dp0\\temp\\${programName} %~dp0\n" +
                    "start /d \"%~dp0\" ${programName}\n" +
                    "exit";

    public static void delete() {
        File restart = new File(Dir + "/restart.bat");
        if (restart.exists()) {
            restart.delete();
        }
        File upgrade = new File(Dir + "/upgrade.bat");
        if (upgrade.exists()) {
            upgrade.delete();
        }
    }

    public static void restart(String processName) {
        try {
            String bat = StringUtils.replace(RestartBat, "${processName}", processName);
            File restart = new File(Dir + "/restart.bat");
            FileUtils.write(restart, bat, StandardCharsets.UTF_8);

            Runtime.getRuntime().exec("cmd /c start \"\" restart.bat");
            System.exit(1);
        } catch (IOException e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
    }

    public static void upgrade(String programName) {
        try {
            String bat = StringUtils.replace(UpgradeBat, "${programName}", programName);
            File restart = new File(Dir + "/upgrade.bat");
            FileUtils.write(restart, bat, StandardCharsets.UTF_8);

            Runtime.getRuntime().exec("cmd /c start \"\" upgrade.bat");
            System.exit(1);
        } catch (IOException e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
    }

    public static void startup(String programBaseName, String programName) {
        PlatformUtil.Platform platform = PlatformUtil.getPlatform();
        switch (platform) {
            case Mac:
                return;
            default:
                break;
        }

        File file = new File(System.getProperty("user.home") + StartupPath + "/" + programBaseName + ".lnk");
        if (file.exists()) {
            file.delete();
        }

        try {
            ShellLink.createLink(programName, file.getAbsolutePath());
        } catch (IOException e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
    }

    public static void cancelStartup(String programBaseName) {
        File file = new File(System.getProperty("user.home") + StartupPath + "/" + programBaseName + ".lnk");
        if (file.exists()) {
            file.delete();
        }
    }

}
