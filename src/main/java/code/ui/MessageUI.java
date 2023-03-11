package code.ui;

import code.config.Config;
import code.config.I18nEnum;
import code.util.ProgramUtil;

import javax.swing.*;
import java.awt.*;

public class MessageUI {

    public static void warning(String message) {
        warning(null, message);
    }
    public static void warning(Component parentComponent, String message) {
        ProgramUtil.activate(Config.MetaData.ProcessName);
        JOptionPane.showMessageDialog(null, message, I18nEnum.Title.getText(), JOptionPane.WARNING_MESSAGE);
    }
    public static void info(String message) {
        info(null, message);
    }
    public static void info(Component parentComponent, String message) {
        ProgramUtil.activate(Config.MetaData.ProcessName);
        JOptionPane.showMessageDialog(parentComponent, message, I18nEnum.Title.getText(), JOptionPane.INFORMATION_MESSAGE);
    }

}
