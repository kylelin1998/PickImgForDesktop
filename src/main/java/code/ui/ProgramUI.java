package code.ui;

import code.config.Config;
import code.util.ExceptionUtil;
import code.util.ProgramUtil;
import lombok.extern.slf4j.Slf4j;
import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ProgramUI {

    public static void init() {
        ProgramUtil.delete();

    }

    public static void restart() {
        ProgramUtil.restart(Config.MetaData.ProcessName);
    }

    public static void startup() {
        ProgramUtil.startup(Config.MetaData.ProcessBaseName, Config.MetaData.ProcessName);
    }

    public static void cancelStartup() {
        ProgramUtil.cancelStartup(Config.MetaData.ProcessBaseName);
    }

    public static void upgrade() {
        ProgramUtil.upgrade(Config.MetaData.ProcessName);
    }

}
