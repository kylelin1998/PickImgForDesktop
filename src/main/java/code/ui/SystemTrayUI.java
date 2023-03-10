package code.ui;

import code.config.I18nEnum;
import code.config.SchemeItemEnum;
import code.util.ExceptionUtil;
import code.util.PlatformUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SystemTrayUI {

    private static TrayIcon trayIcon = null;
    private static List<JMenuItem> schemeMenuItemList = new ArrayList<>();

    public static void render() {
        if (!SystemTray.isSupported()) {
            MessageUI.warning(I18nEnum.SystemTrayWarning.getText());
            System.exit(1);
        }
        if (null != trayIcon) {
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();

        URL url = SystemTrayUI.class.getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().createImage(url);

        trayIcon = new TrayIcon(image, I18nEnum.Title.getText());
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(I18nEnum.Title.getText());

        JMenuItem aboutItem = new JMenuItem(I18nEnum.TrayAbout.getText());
        aboutItem.setName(I18nEnum.TrayAbout.getText());
        aboutItem.addActionListener((ActionEvent e) -> {
            try {
                Desktop.getDesktop().browse(new URI("https://kylelin1998.com/"));
                Desktop.getDesktop().browse(new URI("https://github.com/kylelin1998/PickImgForDesktop"));
                Desktop.getDesktop().browse(new URI("https://gitee.com/kylelin1998/PickImgForDesktop"));
            } catch (IOException | URISyntaxException ex) {
                log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(ex));
                MessageUI.warning(I18nEnum.Error.getText());
            }
        });

        JMenuItem settings = new JMenuItem(I18nEnum.TraySettings.getText());
        settings.addActionListener((ActionEvent e) -> {
            SettingsWindow.render();
        });
        SchemeItemEnum[] schemeItemEnums = SchemeItemEnum.values();
        for (SchemeItemEnum schemeItemEnum : schemeItemEnums) {
            JMenuItem menuItem = new JMenuItem(schemeItemEnum.getName());
            menuItem.addActionListener((ActionEvent e) -> {
                SchemeWindow.render(schemeItemEnum);
            });
            schemeMenuItemList.add(menuItem);
        }
        JMenuItem checkUpdateItem = new JMenuItem(I18nEnum.CheckUpdate.getText());
        checkUpdateItem.addActionListener((ActionEvent e) -> {
            UpgradeWindow.checkUpdate();
        });
        JMenuItem restartItem = new JMenuItem(I18nEnum.TrayRestart.getText());
        restartItem.addActionListener((ActionEvent e) -> {
            ProgramUI.restart();
        });
        JMenuItem exitItem = new JMenuItem(I18nEnum.TrayExit.getText());
        exitItem.addActionListener((ActionEvent e) -> {
            System.exit(1);
        });

        JDialog jDialog = new JDialog();
        jDialog.setUndecorated(true);
        jDialog.setSize(1, 1);

        JPopupMenu jPopupMenu = new JPopupMenu() {
            @Override
            public void firePopupMenuWillBecomeInvisible() {
                jDialog.setVisible(false);
            }
        };

        jPopupMenu.add(aboutItem);
        jPopupMenu.addSeparator();
        jPopupMenu.add(settings);
        for (JMenuItem jMenuItem : schemeMenuItemList) {
            jPopupMenu.add(jMenuItem);
        }
        jPopupMenu.addSeparator();
        jPopupMenu.add(checkUpdateItem);
        jPopupMenu.add(restartItem);
        jPopupMenu.add(exitItem);

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    SettingsWindow.render();
                } else if (e.getButton() == 3) {
                    jDialog.setLocation(e.getX() + 5, e.getY() - 5 - jPopupMenu.getHeight());

                    jDialog.setVisible(true);
                    jPopupMenu.show(jDialog, 0, 0);
                }
            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            MessageUI.warning(I18nEnum.SystemTrayWarning.getText());
            System.exit(1);
        }
    }

    public static void info(String msg) {
        EventQueue.invokeLater(() -> {
            PlatformUtil.Platform platform = PlatformUtil.getPlatform();
            switch (platform) {
                case Windows:
                    trayIcon.displayMessage(I18nEnum.Title.getText(), msg, TrayIcon.MessageType.INFO);
                    break;
                case Mac:
                    notificationForMacos("INFO - " + I18nEnum.Title.getText(), msg);
                    break;
                default:
                    MessageUI.warning("Error...");
                    break;
            }
        });
    }
    public static void warning(String msg) {
        EventQueue.invokeLater(() -> {
            PlatformUtil.Platform platform = PlatformUtil.getPlatform();
            switch (platform) {
                case Windows:
                    trayIcon.displayMessage(I18nEnum.Title.getText(), msg, TrayIcon.MessageType.WARNING);
                    break;
                case Mac:
                    notificationForMacos("WARNING - " + I18nEnum.Title.getText(), msg);
                    break;
                default:
                    MessageUI.warning("Error...");
                    break;
            }
        });
    }

    private static void notificationForMacos(String title, String text) {
        String command = "display notification \"%s\" with title \"%s\"";
        command = String.format(command, text, title);

        try {
            Runtime.getRuntime().exec(new String[]{"osascript", "-e", command});
        } catch (IOException e) {
            MessageUI.warning("Error...");
        }
    }

}
