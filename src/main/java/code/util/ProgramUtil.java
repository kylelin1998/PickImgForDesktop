package code.util;

import lombok.extern.slf4j.Slf4j;
import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class ProgramUtil {

    private final static String Dir = System.getProperty("user.dir");
    private final static String StartupPath = "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup";

    private final static String RestartScpt = new BufferedReader(new InputStreamReader(ProgramUtil.class.getResourceAsStream("restart.scpt"), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

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
        clear("restart.bat", "upgrade.bat", "restart.scpt");
    }

    private static void clear(String... filename) {
        for (String s : filename) {
            File restart = new File(Dir + "/" + s);
            if (restart.exists()) {
                restart.delete();
            }
        }
    }

    public static void restart(String processName) {
        try {
            if (PlatformUtil.isWindows()) {
                String bat = StringUtils.replace(RestartBat, "${processName}", processName);
                File restart = new File(Dir + "/restart.bat");
                FileUtils.write(restart, bat, StandardCharsets.UTF_8);

                Runtime.getRuntime().exec("cmd /c start \"\" restart.bat");
            }

            if (PlatformUtil.isMacos()) {
                String bat = StringUtils.replace(RestartScpt, "${processName}", processName);
                File restart = new File(Dir + "/restart.scpt");
                FileUtils.write(restart, bat, StandardCharsets.UTF_8);

                Runtime.getRuntime().exec(new String[]{"osascript", Dir + "/restart.scpt"});
            }
        } catch (IOException e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
    }

    public static void activate(String processName) {
        if (PlatformUtil.isMacos()) {
            try {
                String command = "tell application \"%s\" to activate";
                command = String.format(command, processName);
                Runtime.getRuntime().exec(new String[]{"osascript", "-e", command});
            } catch (IOException e) {
                log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
            }
        }
    }

    public static void upgrade(String programName) {
        try {
            if (PlatformUtil.isWindows()) {
                String bat = StringUtils.replace(UpgradeBat, "${programName}", programName);
                File restart = new File(Dir + "/upgrade.bat");
                FileUtils.write(restart, bat, StandardCharsets.UTF_8);

                Runtime.getRuntime().exec("cmd /c start \"\" upgrade.bat");
                System.exit(1);
            }
        } catch (IOException e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
    }

    public static void coverStartup(String programBaseName, String programName) {
        if (!PlatformUtil.isWindows()) {
            return;
        }

        File file = new File(System.getProperty("user.home") + StartupPath + "/" + programBaseName + ".lnk");
        if (file.exists()) {
            startup(programBaseName, programName);
        }
    }

    public static void startup(String programBaseName, String programName) {
        if (!PlatformUtil.isWindows()) {
            return;
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
        if (!PlatformUtil.isWindows()) {
            return;
        }

        File file = new File(System.getProperty("user.home") + StartupPath + "/" + programBaseName + ".lnk");
        if (file.exists()) {
            file.delete();
        }
    }

}
