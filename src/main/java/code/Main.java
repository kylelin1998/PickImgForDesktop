package code;

import code.config.I18nEnum;
import code.config.SchemeConfig;
import code.ui.*;
import code.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class Main {
    public static void main(String[] args) {
        SchemeConfig.SchemeEntity schemeEntity = null;
        try {
            schemeEntity = SchemeConfig.readConfig();
        } catch (Exception e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
            MessageUI.warning(I18nEnum.ConfigError.getText());
        }
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
            MessageUI.warning(I18nEnum.Error.getText());
        }

        ProgramUI.init();

        SettingsWindow.render(false);
        SystemTrayUI.render();

        ShortcutKey.init(schemeEntity);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ShortcutKey.close();
        }));
    }
}
