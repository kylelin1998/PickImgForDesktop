package code.ui;

import code.config.*;
import code.util.PlatformUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.*;

public class SettingsWindowController implements Initializable {

    @FXML
    private Button save;

    @FXML
    private Label langLabel;
    @FXML
    private ComboBox langComboBox;

    @FXML
    private Label startupLabel;
    @FXML
    private ComboBox startupComboBox;

    @FXML
    private Label schemeLabel;
    @FXML
    private ComboBox schemeComboBox;

    @FXML
    private Label updateLabel;
    @FXML
    private ComboBox updateComboBox;

    @FXML
    private Label proxyLabel;
    @FXML
    private ComboBox proxyComboBox;

    @FXML
    private Label proxyHostLabel;
    @FXML
    private TextField proxyHostText;

    @FXML
    private Label proxyPortLabel;
    @FXML
    private TextField proxyPortText;

    @FXML
    private Label filenameLabel;
    @FXML
    private TextField filenameText;

    @FXML
    private Label copySchemeLabel;
    @FXML
    private ComboBox copySchemeComboBox;

    @FXML
    private Label copyPromptLabel;
    @FXML
    private ComboBox copyPromptComboBox;

    @FXML
    private Label promptBeforeUploadLabel;
    @FXML
    private ComboBox promptBeforeUploadComboBox;

    @FXML
    private Label shortcutKeyLabel;
    @FXML
    private ComboBox shortcutKeyComboBox1;
    @FXML
    private ComboBox shortcutKeyComboBox2;
    @FXML
    private ComboBox shortcutKeyComboBox3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUI();
        setComponentValue();

        save.setOnMouseClicked((e) -> {
            Platform.runLater(() -> {
                save();
            });
        });
    }

    public void setUI() {
        langLabel.setText(I18nEnum.Language.getText() + ": ");
        startupLabel.setText(I18nEnum.Startup.getText() + ": ");
        schemeLabel.setText(I18nEnum.CurrentScheme.getText() + ": ");
        updateLabel.setText(I18nEnum.CurrentUpgradeSource.getText() + ": ");
        proxyLabel.setText(I18nEnum.Proxy.getText() + ": ");
        proxyHostLabel.setText(I18nEnum.ProxyHostName.getText() + ": ");
        proxyPortLabel.setText(I18nEnum.ProxyPort.getText() + ": ");
        filenameLabel.setText(I18nEnum.FileNameRule.getText() + ": ");
        copySchemeLabel.setText(I18nEnum.CopyCurrentScheme.getText() + ": ");
        copyPromptLabel.setText(I18nEnum.CopyPromptForChoose.getText() + ": ");
        promptBeforeUploadLabel.setText(I18nEnum.PromptBeforeUpload.getText() + ": ");
        shortcutKeyLabel.setText(I18nEnum.GlobalShortcutKey.getText() + ": ");
    }

    public void setComponentValue() {
        SchemeConfig.SchemeEntity schemeEntity = SchemeConfig.readConfig();

        String[] switchArray = { I18nEnum.On.getText(), I18nEnum.Off.getText() };
        ObservableList<String> switchList = FXCollections.observableArrayList(switchArray);

        langComboBox.setItems(FXCollections.observableArrayList(I18nLocaleEnum.getAliasArray()));
        langComboBox.getSelectionModel().select(I18nConfig.getCurrentLocale().ordinal());
        langComboBox.setOnAction((e) -> {
            Platform.runLater(() -> {
                String value = (String) langComboBox.getValue();
                I18nLocaleEnum localeEnum = I18nLocaleEnum.getI18nLocaleEnumByAlias(value);
                schemeEntity.setLang(localeEnum.getAlias());
                SchemeConfig.saveConfig(schemeEntity);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(I18nEnum.Title.getText());
                alert.setHeaderText(I18nEnum.RestartAtOnce.getText());

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    ProgramUI.restart();
                }
            });
        });

        if (PlatformUtil.isWindows()) {
            startupComboBox.setItems(switchList);
            startupComboBox.getSelectionModel().select(schemeEntity.getStartup() ? 0 : 1);
        } else {
            startupComboBox.setDisable(true);
        }

        schemeComboBox.setItems(FXCollections.observableArrayList(SchemeItemEnum.getNameArray()));
        schemeComboBox.getSelectionModel().select(SchemeItemEnum.getSchemeItemEnum(schemeEntity.getCurrentScheme()).ordinal());

        updateComboBox.setItems(FXCollections.observableArrayList(UpgradeSourceEnum.getNameArray()));
        updateComboBox.getSelectionModel().select(UpgradeSourceEnum.getUpgradeSourceEnum(schemeEntity.getUpgrade().getCurrent()).ordinal());

        proxyComboBox.setItems(FXCollections.observableArrayList(ProxyTypeEnum.getAliasArray()));
        proxyComboBox.getSelectionModel().select(ProxyTypeEnum.getProxyTypeEnum(schemeEntity.getProxy().getType()).ordinal());

        proxyHostText.setText(schemeEntity.getProxy().getHostName());
        proxyPortText.setText(String.valueOf(schemeEntity.getProxy().getPort()));

        filenameText.setText(String.valueOf(schemeEntity.getFileNameRule().getRule()));

        copySchemeComboBox.setItems(FXCollections.observableArrayList(CopyLinkEnum.getAliasArray()));
        copySchemeComboBox.getSelectionModel().select(CopyLinkEnum.getCopyLinkEnum(schemeEntity.getCopy().getCurrent()).ordinal());
        copyPromptComboBox.setItems(switchList);
        copyPromptComboBox.getSelectionModel().select(schemeEntity.getCopy().getIsPromptForChoose() ? 0 : 1);

        promptBeforeUploadComboBox.setItems(switchList);
        promptBeforeUploadComboBox.getSelectionModel().select(schemeEntity.getPromptBeforeUpload() ? 0 : 1);

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

        shortcutKeyComboBox1.setItems(FXCollections.observableArrayList(modifiers));
        shortcutKeyComboBox1.getSelectionModel().select(getIndexByKey(schemeEntity.getShortcutKey().getModifiers()[0], modifiersMap));
        shortcutKeyComboBox2.setItems(FXCollections.observableArrayList(modifiers));
        shortcutKeyComboBox2.getSelectionModel().select(getIndexByKey(schemeEntity.getShortcutKey().getModifiers()[1], modifiersMap));
        shortcutKeyComboBox3.setItems(FXCollections.observableArrayList(keycodeArray));
        shortcutKeyComboBox3.getSelectionModel().select(getIndexByKey(schemeEntity.getShortcutKey().getKeycode(), keycodeMap));

    }
    private LinkedHashMap<Integer, String> getKeycodeMap(Integer... keycode) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        for (Integer integer : keycode) {
            map.put(integer, KeyEvent.getKeyText(integer));
        }
        return map;
    }
    private String[] getKeyTextMap(LinkedHashMap<Integer, String> keycodeMap) {
        String[] keyTextMap = new String[keycodeMap.size()];
        return keycodeMap.values().toArray(keyTextMap);
    }
    private int getIndexByKey(int key, LinkedHashMap<Integer, String> keycodeMap) {
        List keys = new ArrayList(keycodeMap.keySet());
        for (int i = 0; i < keys.size(); i++) {
            Object o = keys.get(i);
            if (o.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public void save() {
//        SchemeConfig.SchemeEntity schemeEntityReal = SchemeConfig.readConfig();
//
//        schemeEntityReal.setStartup(startupFormComboField.getSelectedIndex() == 0 ? true : false);
//        schemeEntityReal.setCurrentScheme(SchemeItemEnum.getSchemeItemEnum((String) currentSchemeFormComboField.getSelectedItem()).getName());
//
//        SchemeConfig.UpgradeEntity upgrade = schemeEntityReal.getUpgrade();
//        upgrade.setCurrent(UpgradeSourceEnum.getUpgradeSourceEnum((String) currentUpgradeSourceFormComboField.getSelectedItem()).getName());
//
//        SchemeConfig.ProxyEntity proxy = schemeEntityReal.getProxy();
//        proxy.setType(ProxyTypeEnum.values()[proxyFormComboField.getSelectedIndex()].getType());
//        proxy.setHostName(proxyHostNameFormTextField.getText());
//        proxy.setPort(Integer.valueOf(proxyPortFormTextField.getText()));
//
//        SchemeConfig.FileNameRuleEntity fileNameRule = schemeEntityReal.getFileNameRule();
//        fileNameRule.setRule(fileNameRuleFormTextField.getText());
//
//        SchemeConfig.CopyEntity copy = schemeEntityReal.getCopy();
//        copy.setCurrent(CopyLinkEnum.getCopyLinkEnumByAlias((String) copyComboField.getSelectedItem()).getLinkType());
//        copy.setIsPromptForChoose(isPromptForChooseComboField.getSelectedIndex() == 0 ? true : false);
//
//        schemeEntityReal.setPromptBeforeUpload(promptBeforeUploadComboField.getSelectedIndex() == 0 ? true : false);
//
//        SchemeConfig.ShortcutKeyEntity shortcutKey = schemeEntityReal.getShortcutKey();
//        shortcutKey.setModifiers(new Integer[] { modifiersArray[shortcutKeyFormComboFieldList.get(0).getSelectedIndex()], modifiersArray[shortcutKeyFormComboFieldList.get(1).getSelectedIndex()] });
//        shortcutKey.setKeycode(keycodeIntArray[shortcutKeyFormComboFieldList.get(2).getSelectedIndex()]);
//
//        SchemeConfig.saveConfig(schemeEntityReal);
//        ShortcutKey.register(schemeEntityReal);
//        if (schemeEntityReal.getStartup()) {
//            ProgramUI.startup();
//        } else {
//            ProgramUI.cancelStartup();
//        }
//
//        MessageUI.info(frame, Config.SchemeConfigPath + " " + I18nEnum.SaveSuccess.getText());
    }

}
