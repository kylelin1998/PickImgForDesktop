package code.ui;

import code.config.*;
import code.util.*;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Base64;
import java.util.List;

@Slf4j
public class ShortcutKey {

    private static Provider provider;
    private static KeyStroke keyStrokeOnce;

    public static void init(SchemeConfig.SchemeEntity schemeEntity) {
        provider = Provider.getCurrentProvider(true);
        provider.reset();

        SchemeConfig.ShortcutKeyEntity shortcutKey = schemeEntity.getShortcutKey();
        if (shortcutKey.getEnable()) {
            register(schemeEntity);
        }
    }

    public static void register(SchemeConfig.SchemeEntity schemeEntity) {
        if (null != keyStrokeOnce) {
            provider.unregister(keyStrokeOnce);
        }
        try {
            SchemeConfig.ShortcutKeyEntity shortcutKey = schemeEntity.getShortcutKey();
            String format = String.format("%s %s %s", KeyEvent.getKeyText(shortcutKey.getModifiers()[0]).toLowerCase(), KeyEvent.getKeyText(shortcutKey.getModifiers()[1]).toLowerCase(), KeyEvent.getKeyText(shortcutKey.getKeycode()));
            KeyStroke keyStroke = KeyStroke.getKeyStroke(format);
            keyStrokeOnce = keyStroke;
            provider.register(keyStroke, (HotKey arg0) -> {
                new Thread(() -> {
                    ClipboardUtil.Image image = ClipboardUtil.getImage();
                    if (null == image) {
                        SystemTrayUI.warning(I18nEnum.ClipboardNotFoundImage.getText());
                        return;
                    }
                    SchemeConfig.SchemeEntity config = SchemeConfig.readConfig();
                    SchemeConfig.SchemeItemEntity schemeItemEntity = getSchemeItemEntity(config.getItemList(), config.getCurrentScheme());
                    if (null == schemeItemEntity) {
                        SystemTrayUI.warning(I18nEnum.SetUpWarning.getText(config.getCurrentScheme()));
                        return;
                    }

                    if (config.getPromptBeforeUpload()) {
                        ImageIcon imageIcon = new ImageIcon(image.getContent());
                        Image newImg = imageIcon.getImage().getScaledInstance(160, 160,  java.awt.Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(newImg);
                        if (JOptionPane.showConfirmDialog(null,
                                I18nEnum.PromptBeforeUploadInfo.getText(), I18nEnum.Title.getText(),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, imageIcon) == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }

                    SchemeItemEnum schemeItemEnum = SchemeItemEnum.getSchemeItemEnum(config.getCurrentScheme());
                    switch (schemeItemEnum) {
                        case Github:
                            githubHandle(config, schemeItemEntity, image);
                            break;
                        case Imgur:
                            imgurHandle(config, schemeItemEntity, image);
                            break;
                        case AlibabaCloudOSS:
                            alibabaCloudOSSHandle(config, schemeItemEntity, image);
                            break;
                        default:
                            SystemTrayUI.warning(I18nEnum.ShortcutKeyWarning.getText());
                    }
                }).start();
            });
        } catch (Exception e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
            SystemTrayUI.warning(I18nEnum.ShortcutKeyWarning.getText());
        }
    }

    private static SchemeConfig.SchemeItemEntity getSchemeItemEntity(List<SchemeConfig.SchemeItemEntity> itemList, String name) {
        for (SchemeConfig.SchemeItemEntity schemeItemEntity : itemList) {
            if (schemeItemEntity.getName().equals(name)) {
                return schemeItemEntity;
            }
        }
        return null;
    }

    private static void githubHandle(SchemeConfig.SchemeEntity schemeEntity, SchemeConfig.SchemeItemEntity schemeItemEntity, ClipboardUtil.Image image) {
        SchemeConfig.SchemeItemGithubSettingsEntity github = schemeItemEntity.getGithub();
        if (null == github) {
            SystemTrayUI.warning(I18nEnum.SetUpWarning.getText(schemeEntity.getCurrentScheme()));
            return;
        }

        String path = github.getPath();
        if (!path.startsWith("/")) {
            github.setPath("/" + path);
        } else if ("/".equals(path)) {
            github.setPath("");
        }

        String fileName = FileNameUtil.getCustomFileName(image.getContent(), schemeEntity.getFileNameRule().getRule(), image.getName());
        SystemTrayUI.info(I18nEnum.Uploading.getText(fileName));

        GithubUtil.UploadParameters uploadParameters = new GithubUtil.UploadParameters();
        uploadParameters.setMessage(String.format("Upload image for '%s'", fileName) + "by " + I18nEnum.Title.getText());
        uploadParameters.setRepo(github.getRepo());
        uploadParameters.setBranch(github.getBranch());
        uploadParameters.setOwner(github.getOwner());
        uploadParameters.setPath(github.getPath() + "/" + fileName);
        uploadParameters.setContent(Base64.getEncoder().encodeToString(image.getContent()));

        GithubUtil.UploadResponse response = GithubUtil.upload(RequestProxyConfig.getProxyConfig(schemeEntity), github.getToken(), uploadParameters);
        if (response.isOk()) {
            String url = "";
            String customDomain = github.getCustomDomain();
            if (StringUtils.isBlank(customDomain)) {
                url = "%s" + "/" + github.getOwner() + "/" + github.getRepo() + "/" + github.getBranch() + github.getPath() + "/" + fileName;
                url = String.format(url, "https://raw.githubusercontent.com");
            } else {
                url = "%s" + "/" + fileName;
                url = String.format(url, StringUtils.endsWith(customDomain, "/") ? StringUtils.removeEnd(customDomain, "/") : customDomain);
            }
            copy(schemeEntity, fileName, url);
        } else {
            SystemTrayUI.warning(I18nEnum.UploadFail.getText(fileName, "response status code: " + response.getStatusCode()));
        }
    }

    private static void imgurHandle(SchemeConfig.SchemeEntity schemeEntity, SchemeConfig.SchemeItemEntity schemeItemEntity, ClipboardUtil.Image image) {
        SchemeConfig.SchemeItemImgurSettingsEntity imgur  = schemeItemEntity.getImgur();
        if (null == imgur) {
            SystemTrayUI.warning(I18nEnum.SetUpWarning.getText(schemeEntity.getCurrentScheme()));
            return;
        }

        String fileName = FileNameUtil.getCustomFileName(image.getContent(), schemeEntity.getFileNameRule().getRule(), image.getName());
        SystemTrayUI.info(I18nEnum.Uploading.getText(fileName));

        ImgurUtil.UploadResponse response = ImgurUtil.upload(RequestProxyConfig.getProxyConfig(schemeEntity), imgur.getClientId(), fileName, Base64.getEncoder().encodeToString(image.getContent()));
        if (response.isOk()) {
            String link = response.getData().getLink();
            copy(schemeEntity, fileName, link);
        } else {
            SystemTrayUI.warning(I18nEnum.UploadFail.getText(fileName, "response status code: " + response.getStatus()));
        }
    }

    private static void alibabaCloudOSSHandle(SchemeConfig.SchemeEntity schemeEntity, SchemeConfig.SchemeItemEntity schemeItemEntity, ClipboardUtil.Image image) {
        SchemeConfig.SchemeItemAlibabaCloudOSSSettingsEntity oss = schemeItemEntity.getAlibabaCloudOSS();
        if (null == oss) {
            SystemTrayUI.warning(I18nEnum.SetUpWarning.getText(schemeEntity.getCurrentScheme()));
            return;
        }

        String fileName = FileNameUtil.getCustomFileName(image.getContent(), schemeEntity.getFileNameRule().getRule(), image.getName());
        SystemTrayUI.info(I18nEnum.Uploading.getText(fileName));

        AlibabaCloudOSSUtil.UploadParameters uploadParameters = new AlibabaCloudOSSUtil.UploadParameters();
        uploadParameters.setAccessKeyId(oss.getAccessKeyId());
        uploadParameters.setAccessKeySecret(oss.getAccessKeySecret());
        uploadParameters.setBucketName(oss.getBucketName());
        uploadParameters.setContent(image.getContent());

        String endpoint = oss.getEndpoint();
        uploadParameters.setEndpoint(StringUtils.endsWith(endpoint, "/") ? StringUtils.removeEnd(endpoint, "/") : endpoint);
        String objectName = oss.getObjectName();
        if (StringUtils.startsWith(objectName, "/")) {
            objectName = StringUtils.removeStart(objectName, "/");
        }
        if (StringUtils.endsWith(objectName, "/")) {
            objectName = StringUtils.removeEnd(objectName, "/");
        }
        uploadParameters.setObjectName(objectName + "/" + fileName);

        AlibabaCloudOSSUtil.UploadResponse response = AlibabaCloudOSSUtil.upload(RequestProxyConfig.getProxyConfig(schemeEntity), uploadParameters);
        if (response.isOk()) {
            String url = "";
            String customDomain = oss.getCustomDomain();
            if (StringUtils.endsWith(customDomain, "/")) {
                customDomain = StringUtils.removeEnd(customDomain, "/");
            }

            if (StringUtils.isBlank(customDomain)) {
                url = "https://" + uploadParameters.getBucketName() + "." + uploadParameters.getEndpoint() + "/" + uploadParameters.getObjectName();
            } else {
                url = customDomain + "/" + uploadParameters.getObjectName();
            }
            copy(schemeEntity, fileName, url);
        } else {
            SystemTrayUI.warning(I18nEnum.UploadFail.getText(fileName, "error msg: " + response.getErrorMessage()));
        }
    }

    private static void copy(SchemeConfig.SchemeEntity schemeEntity, String fileName, String url) {
        SchemeConfig.CopyEntity copyEntity = schemeEntity.getCopy();
        CopyLinkEnum copyLinkEnum = CopyLinkEnum.getCopyLinkEnum(copyEntity.getCurrent());
        if (copyEntity.getIsPromptForChoose()) {
            String [] options = CopyLinkEnum.getAliasArray();
            int n =  JOptionPane.showOptionDialog(null, I18nEnum.MakeChoice.getText(), I18nEnum.Title.getText(), JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            copyLinkEnum = CopyLinkEnum.getCopyLinkEnumByAlias(options[n]);
        }

        switch (copyLinkEnum) {
            case Ordinary:
                ClipboardUtil.setText(url);
                break;
            case Markdown:
                ClipboardUtil.setText(String.format("![%s](%s)", fileName, url));
                break;
        }
        SystemTrayUI.info(I18nEnum.UploadSuccess.getText(fileName));
    }

}
