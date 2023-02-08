package code.ui;

import code.config.*;
import code.util.DownloadHelper;
import code.util.GiteeUtil;
import code.util.GithubUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class UpgradeWindow {

    protected static JFrame frame = null;
    private static DownloadHelper downloadHelper;
    private static Thread downloadThread;

    private static int getVersionInt(String version) {
        String v = version.replaceAll("\\.", "");
        v = v.replaceAll("v", "");
        v = v.replaceAll("version", "");
        return Integer.valueOf(v).intValue();
    }

    public static void checkUpdate() {
        EventQueue.invokeLater(() -> {
            String download = "";
            String targetVersion = "";
            String body = "";

            SchemeConfig.SchemeEntity config = SchemeConfig.readConfig();
            UpgradeSourceEnum upgradeSourceEnum = UpgradeSourceEnum.getUpgradeSourceEnum(config.getUpgrade().getCurrent());
            switch (upgradeSourceEnum) {
                case Github:
                    GithubUtil.LatestReleaseResponse githubResponse =
                            GithubUtil.getLatestRelease(RequestProxyConfig.getProxyConfig(config), Config.MetaData.GitOwner, Config.MetaData.GitRepo);
                    if (!githubResponse.isOk()) {
                        MessageUI.warning(I18nEnum.CheckUpdateFail.getText());
                        return;
                    }
                    targetVersion = githubResponse.getTagName();
                    body = githubResponse.getBody();
                    for (GithubUtil.LatestReleaseAsset asset : githubResponse.getAssets()) {
                        if (Config.MetaData.ProcessName.equals(asset.getName())) {
                            download = asset.getBrowserDownloadUrl();
                        }
                    }
                    break;
                case Gitte:
                    GiteeUtil.LatestReleaseResponse giteeResponse =
                            GiteeUtil.getLatestRelease(RequestProxyConfig.getProxyConfig(config), Config.MetaData.GitOwner, Config.MetaData.GitRepo);
                    if (!giteeResponse.isOk()) {
                        MessageUI.warning(I18nEnum.CheckUpdateFail.getText());
                        return;
                    }
                    targetVersion = giteeResponse.getTagName();
                    body = giteeResponse.getBody();
                    for (GiteeUtil.LatestReleaseAsset asset : giteeResponse.getAssets()) {
                        if (Config.MetaData.ProcessName.equals(asset.getName())) {
                            download = asset.getBrowserDownloadUrl();
                        }
                    }
                    break;
            }

            if (getVersionInt(targetVersion) > getVersionInt(Config.MetaData.CurrentVersion)) {
                if (JOptionPane.showConfirmDialog(null,
                        I18nEnum.CheckUpdateFound.getText(body, targetVersion), I18nEnum.Title.getText(),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    render(config, targetVersion, download);
                }
            } else {
                MessageUI.warning(I18nEnum.CheckUpdateNewest.getText());
            }
        });
    }

    public static void render(SchemeConfig.SchemeEntity config, String targetVersion, String download) {
        if (null != frame) {
            frame.setVisible(true);
            return;
        }

        frame = new JFrame(I18nEnum.UpdateTitle.getText());

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ProgramUI.restart();
            }
        });

        ImageIcon img = new ImageIcon(SettingsWindow.class.getResource("icon.png"));
        frame.setSize(350, 200);
        frame.setIconImage(img.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        renderAll(config, targetVersion, download);

        frame.setVisible(true);
    }

    private static void renderAll(SchemeConfig.SchemeEntity config, String targetVersion, String download) {
        Box vBox = Box.createVerticalBox();
        frame.setLayout(new BoxLayout(vBox, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(targetVersion + " - " + I18nEnum.UpdateDownload.getText());
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(label);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(horizontalBox);

        Box vBox2 = Box.createHorizontalBox();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        vBox2.add(Box.createHorizontalStrut(20));
        vBox2.add(progressBar);
        vBox2.add(Box.createHorizontalStrut(20));

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(vBox2);
        vBox.add(Box.createVerticalStrut(30));

        frame.setContentPane(vBox);

        // ...
        downloadThread = new Thread(() -> {
            downloadHelper = new DownloadHelper();
            RequestProxyConfig.ProxyConfig proxyConfig = RequestProxyConfig.getProxyConfig(config);
            try {
                File file = new File(Config.TempPath + "/" + Config.MetaData.ProcessName);
                boolean exists = file.getParentFile().exists();
                if (!exists) {
                    file.getParentFile().mkdirs();
                }
                downloadHelper.download(proxyConfig, download, file, (long size, long total) -> {
                    EventQueue.invokeLater(() -> {
                        progressBar.setMaximum((int) total);
                        progressBar.setValue((int) size);
                        progressBar.updateUI();
                    });
                });

                EventQueue.invokeLater(() -> {
                    ProgramUI.upgrade();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        downloadThread.start();
    }

}
