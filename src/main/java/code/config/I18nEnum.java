package code.config;

public enum I18nEnum {

    Title("title"),
    Error("error"),
    ConfigError("configError"),
    On("on"),
    Off("off"),
    Yes("yes"),
    No("no"),
    MakeChoice("makeChoice"),
    SystemTrayWarning("systemTrayWarning"),
    ShortcutKeyWarning("shortcutKeyWarning"),

    TrayAbout("trayAbout"),
    TraySettings("traySettings"),
    TrayRestart("trayRestart"),
    TrayExit("trayExit"),

    PromptBeforeUpload("promptBeforeUpload"),
    PromptBeforeUploadInfo("promptBeforeUploadInfo"),

    ClipboardNotFoundImage("clipboardNotFoundImage"),
    SetUpWarning("setUpWarning"),
    Uploading("uploading"),
    UploadWarning("uploadWarning"),
    UploadFail("uploadFail"),
    UploadSuccess("uploadSuccess"),


    SaveButton("saveButton"),
    SaveSuccess("saveSuccess"),

    Language("language"),
    CurrentScheme("currentScheme"),
    CurrentUpgradeSource("currentUpgradeSource"),
    Proxy("proxy"),
    ProxyHostName("proxyHostName"),
    ProxyPort("proxyPort"),
    FileNameRule("fileNameRule"),
    CopyCurrentScheme("copyCurrentScheme"),
    CopyPromptForChoose("copyPromptForChoose"),
    GlobalShortcutKey("globalShortcutKey"),

    CheckUpdate("checkUpdate"),
    CheckUpdateFail("checkUpdateFail"),
    CheckUpdateNewest("checkUpdateNewest"),
    CheckUpdateFound("checkUpdateFound"),

    UpdateTitle("updateTitle"),
    UpdateFail("updateFail"),
    UpdateDownload("updateDownload"),

    RestartAtOnce("restartAtOnce"),
    Startup("startup"),
    EmptyInputPrompt("emptyInputPrompt"),

    ;

    private String key;

    I18nEnum(String key) {
        this.key = key;
    }

    public String getText() {
        String string = I18nConfig.getResourceBundle().getString(key);
        return string;
    }
    public String getText(String... replace) {
        return String.format(I18nConfig.getResourceBundle().getString(key), replace);
    }

}
