package code.ui;

import code.config.*;
import code.util.PlatformUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SettingsWindowFx extends Application {

    private Scene scene;
    private Stage stage;

    public static void render() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(SettingsWindowFx.class.getResource("settings_window.fxml"));
        scene = new Scene(fxmlLoader.load());

        stage.getIcons().add(new Image("/code/ui/icon.png"));
        stage.setTitle(I18nEnum.Title.getText() + " - " + Config.MetaData.CurrentVersion);
        stage.setScene(scene);

        stage.setResizable(false);

        stage.show();
    }

//    private void renderAll(VBox vBox) {
//        Button saveButton = new Button(I18nEnum.SaveButton.getText());
//
//        vBox.getChildren().add(saveButton);
//        vBox.getChildren().add(new VBox(10));
//
//        renderForm(vBox, saveButton);
//    }
//
//    private void renderForm(VBox vBox, Button saveButton) {
//        SchemeConfig.SchemeEntity schemeEntityDisplay = SchemeConfig.readConfig();
//
//        String[] switchArray = { I18nEnum.On.getText(), I18nEnum.Off.getText() };
//
//        ComboBox languageFormComboField = createFormComboField(vBox, I18nEnum.Language.getText() + ": ", I18nLocaleEnum.getAliasArray(), I18nConfig.getCurrentLocale().ordinal());
//        languageFormComboField.setOnMouseClicked(e -> {
//            Platform.runLater(() -> {
//                ComboBox cb = (ComboBox) e.getSource();
////                String petName = (String) cb.getSelectedItem();
////                I18nLocaleEnum localeEnum = I18nLocaleEnum.getI18nLocaleEnumByAlias(petName);
////                schemeEntityDisplay.setLang(localeEnum.getAlias());
////                SchemeConfig.saveConfig(schemeEntityDisplay);
////                if (JOptionPane.showConfirmDialog(frame,
////                        I18nEnum.RestartAtOnce.getText(), I18nEnum.Title.getText(),
////                        JOptionPane.YES_NO_OPTION,
////                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
////                    ProgramUI.restart();
////                }
//                System.out.println("222222222");
//            });
//        });
//
//        ComboBox startupFormComboField = createFormComboField(vBox, I18nEnum.Startup.getText() + ": ", switchArray, schemeEntityDisplay.getStartup() ? 0 : 1);
//        if (!PlatformUtil.isWindows()) {
//            startupFormComboField.hide();
//        }
//
//        ComboBox currentSchemeFormComboField = createFormComboField(vBox, I18nEnum.CurrentScheme.getText() + ": ", SchemeItemEnum.getNameArray(), SchemeItemEnum.getSchemeItemEnum(schemeEntityDisplay.getCurrentScheme()).ordinal());
//
//        ComboBox currentUpgradeSourceFormComboField = createFormComboField(vBox, I18nEnum.CurrentUpgradeSource.getText() + ": ", UpgradeSourceEnum.getNameArray(), UpgradeSourceEnum.getUpgradeSourceEnum(schemeEntityDisplay.getUpgrade().getCurrent()).ordinal());
//
//        ComboBox proxyFormComboField = createFormComboField(vBox, I18nEnum.Proxy.getText() + ": ", ProxyTypeEnum.getAliasArray(), ProxyTypeEnum.getProxyTypeEnum(schemeEntityDisplay.getProxy().getType()).ordinal());
//        TextField proxyHostNameFormTextField = createFormTextField(vBox, I18nEnum.ProxyHostName.getText() + ": ", schemeEntityDisplay.getProxy().getHostName());
//        TextField proxyPortFormTextField = createFormTextField(vBox, I18nEnum.ProxyPort.getText() + ": ", String.valueOf(schemeEntityDisplay.getProxy().getPort()));
//
//        TextField fileNameRuleFormTextField = createFormTextField(vBox, I18nEnum.FileNameRule.getText() + ": ", schemeEntityDisplay.getFileNameRule().getRule());
//
//        ComboBox copyComboField = createFormComboField(vBox, I18nEnum.CopyCurrentScheme.getText() + ": ", CopyLinkEnum.getAliasArray(), CopyLinkEnum.getCopyLinkEnum(schemeEntityDisplay.getCopy().getCurrent()).ordinal());
//        ComboBox isPromptForChooseComboField = createFormComboField(vBox, I18nEnum.CopyPromptForChoose.getText() + ": ", switchArray, schemeEntityDisplay.getCopy().getIsPromptForChoose() ? 0 : 1);
//
//        ComboBox promptBeforeUploadComboField = createFormComboField(vBox, I18nEnum.PromptBeforeUpload.getText() + ": ", switchArray, schemeEntityDisplay.getPromptBeforeUpload() ? 0 : 1);
//
//        LinkedHashMap<String[], Integer> shortcutKeyMap = new LinkedHashMap<>();
//        Integer[] modifiersArray = { 16, 17, 18 };
//        LinkedHashMap<Integer, String> modifiersMap = getKeycodeMap(modifiersArray);
//        String[] modifiers = getKeyTextMap(modifiersMap);
//
//        Integer[] keycodeIntArray = new Integer[91 - 65];
//        for (int i = 65; i <= 90; i++) {
//            keycodeIntArray[i - 65] = i;
//        }
//        LinkedHashMap<Integer, String> keycodeMap = getKeycodeMap(keycodeIntArray);
//        String[] keycodeArray = getKeyTextMap(keycodeMap);
//
//        shortcutKeyMap.put(modifiers, getIndexByKey(schemeEntityDisplay.getShortcutKey().getModifiers()[0], modifiersMap));
//        shortcutKeyMap.put(modifiers.clone(), getIndexByKey(schemeEntityDisplay.getShortcutKey().getModifiers()[1], modifiersMap));
//        shortcutKeyMap.put(keycodeArray, getIndexByKey(schemeEntityDisplay.getShortcutKey().getKeycode(), keycodeMap));
//        java.util.List<ComboBox> shortcutKeyFormComboFieldList = createFormComboFieldArray(vBox, I18nEnum.GlobalShortcutKey.getText() + ": ", shortcutKeyMap);
//
//        saveButton.setOnMouseClicked(e -> {
//            SchemeConfig.SchemeEntity schemeEntityReal = SchemeConfig.readConfig();
//
////            schemeEntityReal.setStartup(startupFormComboField.getSelectedIndex() == 0 ? true : false);
////            schemeEntityReal.setCurrentScheme(SchemeItemEnum.getSchemeItemEnum((String) currentSchemeFormComboField.getSelectedItem()).getName());
//
//            SchemeConfig.UpgradeEntity upgrade = schemeEntityReal.getUpgrade();
////            upgrade.setCurrent(UpgradeSourceEnum.getUpgradeSourceEnum((String) currentUpgradeSourceFormComboField.getSelectedItem()).getName());
//
//            SchemeConfig.ProxyEntity proxy = schemeEntityReal.getProxy();
////            proxy.setType(ProxyTypeEnum.values()[proxyFormComboField.getSelectedIndex()].getType());
//            proxy.setHostName(proxyHostNameFormTextField.getText());
//            proxy.setPort(Integer.valueOf(proxyPortFormTextField.getText()));
//
//            SchemeConfig.FileNameRuleEntity fileNameRule = schemeEntityReal.getFileNameRule();
//            fileNameRule.setRule(fileNameRuleFormTextField.getText());
//
//            SchemeConfig.CopyEntity copy = schemeEntityReal.getCopy();
////            copy.setCurrent(CopyLinkEnum.getCopyLinkEnumByAlias((String) copyComboField.getSelectedItem()).getLinkType());
////            copy.setIsPromptForChoose(isPromptForChooseComboField.getSelectedIndex() == 0 ? true : false);
//
////            schemeEntityReal.setPromptBeforeUpload(promptBeforeUploadComboField.getSelectedIndex() == 0 ? true : false);
//
//            SchemeConfig.ShortcutKeyEntity shortcutKey = schemeEntityReal.getShortcutKey();
////            shortcutKey.setModifiers(new Integer[] { modifiersArray[shortcutKeyFormComboFieldList.get(0).getSelectedIndex()], modifiersArray[shortcutKeyFormComboFieldList.get(1).getSelectedIndex()] });
////            shortcutKey.setKeycode(keycodeIntArray[shortcutKeyFormComboFieldList.get(2).getSelectedIndex()]);
//
//            SchemeConfig.saveConfig(schemeEntityReal);
//            ShortcutKey.register(schemeEntityReal);
//            if (schemeEntityReal.getStartup()) {
//                ProgramUI.startup();
//            } else {
//                ProgramUI.cancelStartup();
//            }
//
////            MessageUI.info(frame, Config.SchemeConfigPath + " " + I18nEnum.SaveSuccess.getText());
//
//            stage.hide();
//        });
//    }
//
//    private LinkedHashMap<Integer, String> getKeycodeMap(Integer... keycode) {
//        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
//        for (Integer integer : keycode) {
//            map.put(integer, KeyEvent.getKeyText(integer));
//        }
//        return map;
//    }
//    private String[] getKeyTextMap(LinkedHashMap<Integer, String> keycodeMap) {
//        String[] keyTextMap = new String[keycodeMap.size()];
//        return keycodeMap.values().toArray(keyTextMap);
//    }
//    private int getIndexByKey(int key, LinkedHashMap<Integer, String> keycodeMap) {
//        java.util.List keys = new ArrayList(keycodeMap.keySet());
//        for (int i = 0; i < keys.size(); i++) {
//            Object o = keys.get(i);
//            if (o.equals(key)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private ComboBox createFormComboField(VBox vBox, String label, String[] aliasArray, int selectedIndex) {
//        HBox hBox = new HBox();
//
//        Label jlabel = new Label(label);
//        hBox.getChildren().add(jlabel);
//
//        ComboBox aliasArrayComboBox = new ComboBox();
////        aliasArrayComboBox.setSelectedIndex(selectedIndex);
//        hBox.getChildren().add(aliasArrayComboBox);
//
//        vBox.getChildren().add(hBox);
//
//        return aliasArrayComboBox;
//    }
//
//    private TextField createFormTextField(VBox vBox, String label, String text) {
//        HBox hBox = new HBox();
//
//        Label jLabel = new Label(label);
//        hBox.getChildren().add(jLabel);
//
//        TextField jText = new TextField();
//        jText.setText(text);
//        hBox.getChildren().add(jText);
//
//        vBox.getChildren().add(hBox);
//
//        return jText;
//    }
//
//    private List<ComboBox> createFormComboFieldArray(VBox vBox, String label, LinkedHashMap<String[], Integer> comboMap) {
//        HBox hBox = new HBox();
//
//        Label jlabel = new Label(label);
//        hBox.getChildren().add(jlabel);
//
//        ArrayList<ComboBox> jComboBoxArrayList = new ArrayList<>();
//        comboMap.forEach((String[] arr, Integer index) -> {
//            ComboBox aliasArrayComboBox = new ComboBox();
////            aliasArrayComboBox.setSelectedIndex(index);
//            hBox.getChildren().add(aliasArrayComboBox);
//
//            jComboBoxArrayList.add(aliasArrayComboBox);
//        });
//
//        vBox.getChildren().add(hBox);
//
//        return jComboBoxArrayList;
//    }

}
