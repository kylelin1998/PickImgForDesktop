package code.ui;

import code.config.Config;
import code.config.I18nEnum;
import code.ui.component.JHyperLabel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class AboutWindow {

    protected static JFrame frame;

    public static JFrame render() {
        frame = new JFrame(I18nEnum.Title.getText());

        ImageIcon img = new ImageIcon(SchemeWindow.class.getResource("icon.png"));
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        renderAll();

        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);

        return frame;
    }

    private static void renderAll() {
        Box vBox = Box.createVerticalBox();

        vBox.add(Box.createVerticalStrut(20));

        Box vBox2 = Box.createVerticalBox();
        renderForm(vBox2);

        vBox.add(vBox2);
        vBox.add(Box.createVerticalStrut(30));

        frame.setContentPane(vBox);
    }

    private static void renderForm(JComponent panel) {
        try {
            createFormTextField(panel, true, I18nEnum.GithubProjectUrl.getText() + ": ", Config.AboutData.GithubProjectUrl);
            createFormTextField(panel, true, I18nEnum.WebSite.getText() + ": ", Config.AboutData.WebSite);
            createFormTextField(panel, true, I18nEnum.Channel.getText() + ": ", Config.AboutData.Channel);
            createFormTextField(panel, false, I18nEnum.Email.getText() + ": ", Config.AboutData.Email);
            createFormTextField(panel, false, I18nEnum.Author.getText() + ": ", Config.AboutData.Author);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createFormTextField(JComponent panel, boolean isHyperLabel, String label, String text) {
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalStrut(30));

        JLabel jLabel = new JLabel(label);

        horizontalBox.add(jLabel);
        JLabel hyperLabel = isHyperLabel ? JHyperLabel.create(text) : new JLabel(text);
        horizontalBox.add(hyperLabel);
        horizontalBox.add(Box.createHorizontalStrut(30));

        panel.add(Box.createVerticalStrut(15));
        panel.add(horizontalBox);
    }

}
