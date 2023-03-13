package code.ui;

import code.config.*;
import code.util.PlatformUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class SettingsWindow {
    protected static JFrame frame = null;

    public static void render(boolean visible) {
        if (null != frame) {
            frame.setVisible(true);
            return;
        }

        frame = new JFrame(I18nEnum.Title.getText() + " - " + Config.MetaData.CurrentVersion);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frame.setVisible(false);
            }
        });

        frame.setSize(500, 650);
        ImageIcon img = new ImageIcon(SettingsWindow.class.getResource("icon.png"));
        frame.setIconImage(img.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        renderAll();

        frame.setAlwaysOnTop(true);
        frame.setVisible(visible);
    }

    private static void renderAll() {
        Box vBox = Box.createVerticalBox();

        JButton saveButton = new JButton(I18nEnum.SaveButton.getText());

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(saveButton);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(horizontalBox);

        Box vBox2 = Box.createVerticalBox();
        renderForm(vBox2, saveButton);

        vBox.add(vBox2);
        vBox.add(Box.createVerticalStrut(30));

        frame.setContentPane(vBox);
    }

    private static void renderForm(JComponent panel, JButton saveButton) {
        SchemeConfig.SchemeEntity schemeEntityDisplay = SchemeConfig.readConfig();

        String[] switchArray = { I18nEnum.On.getText(), I18nEnum.Off.getText() };

        JComboBox languageFormComboField = createFormComboField(panel, I18nEnum.Language.getText() + ": ", I18nLocaleEnum.getAliasArray(), I18nConfig.getCurrentLocale().ordinal());
        languageFormComboField.addActionListener((ActionEvent e) -> {
            EventQueue.invokeLater(() -> {
                JComboBox cb = (JComboBox) e.getSource();
                String petName = (String) cb.getSelectedItem();
                I18nLocaleEnum localeEnum = I18nLocaleEnum.getI18nLocaleEnumByAlias(petName);
                schemeEntityDisplay.setLang(localeEnum.getAlias());
                SchemeConfig.saveConfig(schemeEntityDisplay);
                if (JOptionPane.showConfirmDialog(frame,
                        I18nEnum.RestartAtOnce.getText(), I18nEnum.Title.getText(),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    ProgramUI.restart();
                }
            });
        });

        JComboBox startupFormComboField = createFormComboField(panel, I18nEnum.Startup.getText() + ": ", switchArray, schemeEntityDisplay.getStartup() ? 0 : 1);
        if (!PlatformUtil.isWindows()) {
            startupFormComboField.disable();
        }

        JComboBox currentSchemeFormComboField = createFormComboField(panel, I18nEnum.CurrentScheme.getText() + ": ", SchemeItemEnum.getNameArray(), SchemeItemEnum.getSchemeItemEnum(schemeEntityDisplay.getCurrentScheme()).ordinal());

        JComboBox currentUpgradeSourceFormComboField = createFormComboField(panel, I18nEnum.CurrentUpgradeSource.getText() + ": ", UpgradeSourceEnum.getNameArray(), UpgradeSourceEnum.getUpgradeSourceEnum(schemeEntityDisplay.getUpgrade().getCurrent()).ordinal());

        JComboBox proxyFormComboField = createFormComboField(panel, I18nEnum.Proxy.getText() + ": ", ProxyTypeEnum.getAliasArray(), ProxyTypeEnum.getProxyTypeEnum(schemeEntityDisplay.getProxy().getType()).ordinal());
        JTextField proxyHostNameFormTextField = createFormTextField(panel, I18nEnum.ProxyHostName.getText() + ": ", schemeEntityDisplay.getProxy().getHostName());
        JTextField proxyPortFormTextField = createFormTextField(panel, I18nEnum.ProxyPort.getText() + ": ", String.valueOf(schemeEntityDisplay.getProxy().getPort()));

        JTextField fileNameRuleFormTextField = createFormTextField(panel, I18nEnum.FileNameRule.getText() + ": ", schemeEntityDisplay.getFileNameRule().getRule());

        JComboBox copyComboField = createFormComboField(panel, I18nEnum.CopyCurrentScheme.getText() + ": ", CopyLinkEnum.getAliasArray(), CopyLinkEnum.getCopyLinkEnum(schemeEntityDisplay.getCopy().getCurrent()).ordinal());
        JComboBox isPromptForChooseComboField = createFormComboField(panel, I18nEnum.CopyPromptForChoose.getText() + ": ", switchArray, schemeEntityDisplay.getCopy().getIsPromptForChoose() ? 0 : 1);

        JComboBox promptBeforeUploadComboField = createFormComboField(panel, I18nEnum.PromptBeforeUpload.getText() + ": ", switchArray, schemeEntityDisplay.getPromptBeforeUpload() ? 0 : 1);

        LinkedHashMap<String[], Integer> shortcutKeyMap = new LinkedHashMap<>();
        Integer[] modifiersArray = { 16, 17, 18 };
        LinkedHashMap<Integer, String> modifiersMap = getKeycodeMap(modifiersArray);
        String[] modifiers = getKeyTextMap(modifiersMap);

        Integer[] keycodeIntArray = new Integer[91 - 65];
        for (int i = 65; i <= 90; i++) {
            keycodeIntArray[i - 65] = i;
        }
        LinkedHashMap<Integer, String> keycodeMap = getKeycodeMap(keycodeIntArray);
        String[] keycodeArray = getKeyTextMap(keycodeMap);

        shortcutKeyMap.put(modifiers, getIndexByKey(schemeEntityDisplay.getShortcutKey().getModifiers()[0], modifiersMap));
        shortcutKeyMap.put(modifiers.clone(), getIndexByKey(schemeEntityDisplay.getShortcutKey().getModifiers()[1], modifiersMap));
        shortcutKeyMap.put(keycodeArray, getIndexByKey(schemeEntityDisplay.getShortcutKey().getKeycode(), keycodeMap));
        List<JComboBox> shortcutKeyFormComboFieldList = createFormComboFieldArray(panel, I18nEnum.GlobalShortcutKey.getText() + ": ", shortcutKeyMap);

        saveButton.addActionListener((ActionEvent e) -> {
            SchemeConfig.SchemeEntity schemeEntityReal = SchemeConfig.readConfig();

            schemeEntityReal.setStartup(startupFormComboField.getSelectedIndex() == 0 ? true : false);
            schemeEntityReal.setCurrentScheme(SchemeItemEnum.getSchemeItemEnum((String) currentSchemeFormComboField.getSelectedItem()).getName());

            SchemeConfig.UpgradeEntity upgrade = schemeEntityReal.getUpgrade();
            upgrade.setCurrent(UpgradeSourceEnum.getUpgradeSourceEnum((String) currentUpgradeSourceFormComboField.getSelectedItem()).getName());

            SchemeConfig.ProxyEntity proxy = schemeEntityReal.getProxy();
            proxy.setType(ProxyTypeEnum.values()[proxyFormComboField.getSelectedIndex()].getType());
            proxy.setHostName(proxyHostNameFormTextField.getText());
            proxy.setPort(Integer.valueOf(proxyPortFormTextField.getText()));

            SchemeConfig.FileNameRuleEntity fileNameRule = schemeEntityReal.getFileNameRule();
            fileNameRule.setRule(fileNameRuleFormTextField.getText());

            SchemeConfig.CopyEntity copy = schemeEntityReal.getCopy();
            copy.setCurrent(CopyLinkEnum.getCopyLinkEnumByAlias((String) copyComboField.getSelectedItem()).getLinkType());
            copy.setIsPromptForChoose(isPromptForChooseComboField.getSelectedIndex() == 0 ? true : false);

            schemeEntityReal.setPromptBeforeUpload(promptBeforeUploadComboField.getSelectedIndex() == 0 ? true : false);

            SchemeConfig.ShortcutKeyEntity shortcutKey = schemeEntityReal.getShortcutKey();
            shortcutKey.setModifiers(new Integer[] { modifiersArray[shortcutKeyFormComboFieldList.get(0).getSelectedIndex()], modifiersArray[shortcutKeyFormComboFieldList.get(1).getSelectedIndex()] });
            shortcutKey.setKeycode(keycodeIntArray[shortcutKeyFormComboFieldList.get(2).getSelectedIndex()]);

            SchemeConfig.saveConfig(schemeEntityReal);
            ShortcutKey.register(schemeEntityReal);
            if (schemeEntityReal.getStartup()) {
                ProgramUI.startup();
            } else {
                ProgramUI.cancelStartup();
            }

//            MessageUI.info(frame, Config.SchemeConfigPath + " " + I18nEnum.SaveSuccess.getText());

            frame.dispose();
        });
    }

    private static LinkedHashMap<Integer, String> getKeycodeMap(Integer... keycode) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        for (Integer integer : keycode) {
            map.put(integer, KeyEvent.getKeyText(integer));
        }
        return map;
    }
    private static String[] getKeyTextMap(LinkedHashMap<Integer, String> keycodeMap) {
        String[] keyTextMap = new String[keycodeMap.size()];
        return keycodeMap.values().toArray(keyTextMap);
    }
    private static int getIndexByKey(int key, LinkedHashMap<Integer, String> keycodeMap) {
        List keys = new ArrayList(keycodeMap.keySet());
        for (int i = 0; i < keys.size(); i++) {
            Object o = keys.get(i);
            if (o.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private static JComboBox createFormComboField(JComponent panel, String label, String[] aliasArray, int selectedIndex) {
        Box horizontalBox = Box.createHorizontalBox();

        JLabel jlabel = new JLabel(label);
        horizontalBox.add(Box.createHorizontalStrut(30));
        horizontalBox.add(jlabel);

        JComboBox aliasArrayComboBox = new JComboBox(aliasArray);
        aliasArrayComboBox.setSelectedIndex(selectedIndex);
        horizontalBox.add(aliasArrayComboBox);
        horizontalBox.add(Box.createHorizontalStrut(30));

        panel.add(Box.createVerticalStrut(15));
        panel.add(horizontalBox);

        return aliasArrayComboBox;
    }

    private static JTextField createFormTextField(JComponent panel, String label, String text) {
        Box horizontalBox = Box.createHorizontalBox();

        JLabel jLabel = new JLabel(label);
        horizontalBox.add(Box.createHorizontalStrut(30));
        horizontalBox.add(jLabel);

        JTextField jText = new JTextField(20);
        jText.setText(text);
        horizontalBox.add(jText);
        horizontalBox.add(Box.createHorizontalStrut(30));

        panel.add(Box.createVerticalStrut(15));
        panel.add(horizontalBox);

        return jText;
    }

    private static List<JComboBox> createFormComboFieldArray(JComponent panel, String label, LinkedHashMap<String[], Integer> comboMap) {
        Box horizontalBox = Box.createHorizontalBox();
        JLabel jlabel = new JLabel(label);
        horizontalBox.add(Box.createHorizontalStrut(30));
        horizontalBox.add(jlabel);

        ArrayList<JComboBox> jComboBoxArrayList = new ArrayList<>();
        comboMap.forEach((String[] arr, Integer index) -> {
            horizontalBox.add(Box.createHorizontalStrut(5));
            JComboBox aliasArrayComboBox = new JComboBox(arr);
            aliasArrayComboBox.setSelectedIndex(index);
            horizontalBox.add(aliasArrayComboBox);
            horizontalBox.add(Box.createHorizontalStrut(30));

            jComboBoxArrayList.add(aliasArrayComboBox);
        });

        panel.add(Box.createVerticalStrut(15));
        panel.add(horizontalBox);

        return jComboBoxArrayList;
    }

}
