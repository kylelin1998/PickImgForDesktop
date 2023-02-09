package code.config;

import code.ui.annotation.FieldAnnotation;
import code.ui.annotation.FieldEnum;
import code.util.ExceptionUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SchemeConfig {

    private static AtomicInteger readCount = new AtomicInteger(0);

    static {
        SchemeEntity schemeEntity = readConfig();
        I18nConfig.init(schemeEntity.getLang());
    }

    @Data
    public static class SchemeEntity {
        private Boolean startup;
        private String version;
        private String lang;
        private String currentScheme;
        private Boolean promptBeforeUpload;
        private ProxyEntity proxy;
        private FileNameRuleEntity fileNameRule;
        private CopyEntity copy;
        private UpgradeEntity upgrade;

        private ShortcutKeyEntity shortcutKey;
        private List<SchemeItemEntity> itemList;
    }
    @Data
    public static class UpgradeEntity {
        private String current;
    }
    @Data
    public static class CopyEntity {
        private String current;
        private Boolean isPromptForChoose;
    }
    @Data
    public static class ProxyEntity {
        private Integer type; // 0:not open, 1:http proxy
        private String hostName;
        private Integer port;
    }
    @Data
    public static class FileNameRuleEntity {
        private String rule;
    }
    @Data
    public static class SchemeItemEntity {
        private String name;
        private SchemeItemGithubSettingsEntity github;
        private SchemeItemImgurSettingsEntity imgur;
        private SchemeItemAlibabaCloudOSSSettingsEntity alibabaCloudOSS;
    }
    @Data
    public static class SchemeItemGithubSettingsEntity {
        @FieldAnnotation(displayText = "Owner", fieldType = FieldEnum.Text, required = true)
        public String owner;
        @FieldAnnotation(displayText = "Repo", fieldType = FieldEnum.Text, required = true)
        public String repo;
        @FieldAnnotation(displayText = "Branch", fieldType = FieldEnum.Text, required = true)
        public String branch;
        @FieldAnnotation(displayText = "Path", fieldType = FieldEnum.Text, required = true)
        public String path;
        @FieldAnnotation(displayText = "Token", fieldType = FieldEnum.Text, required = true)
        public String token;
        @FieldAnnotation(displayText = "Custom Domain", fieldType = FieldEnum.Text, required = false)
        public String customDomain;
    }
    @Data
    public static class SchemeItemImgurSettingsEntity {
        @FieldAnnotation(displayText = "Client ID", fieldType = FieldEnum.Text, required = true)
        public String clientId;
    }

    @Data
    public static class SchemeItemAlibabaCloudOSSSettingsEntity {
        @FieldAnnotation(displayText = "Access Key Id", fieldType = FieldEnum.Text, required = true)
        public String accessKeyId;
        @FieldAnnotation(displayText = "Access Key Secret", fieldType = FieldEnum.Text, required = true)
        public String accessKeySecret;
        @FieldAnnotation(displayText = "Endpoint", fieldType = FieldEnum.Text, required = true)
        public String endpoint;
        @FieldAnnotation(displayText = "Bucket Name", fieldType = FieldEnum.Text, required = true)
        public String bucketName;
        @FieldAnnotation(displayText = "Object Name", fieldType = FieldEnum.Text, required = true)
        public String objectName;
        @FieldAnnotation(displayText = "Custom Domain", fieldType = FieldEnum.Text, required = false)
        public String customDomain;
    }
    @Data
    public static class ShortcutKeyEntity {
        private Boolean enable;
        private Integer keycode;
        private Integer[] modifiers;
    }

    private static SchemeEntity getEntity() {
        ShortcutKeyEntity shortcutKeyEntity = new ShortcutKeyEntity();
        shortcutKeyEntity.setEnable(true);
        shortcutKeyEntity.setKeycode(85);
        shortcutKeyEntity.setModifiers(new Integer[] {17, 18});

        CopyEntity copyEntity = new CopyEntity();
        copyEntity.setCurrent(CopyLinkEnum.getDefault().getLinkType());
        copyEntity.setIsPromptForChoose(false);

        FileNameRuleEntity fileNameRuleEntity = new FileNameRuleEntity();
        fileNameRuleEntity.setRule("${datetime}.${extension}");

        ProxyEntity proxyEntity = new ProxyEntity();
        proxyEntity.setType(ProxyTypeEnum.getDefault().getType());
        proxyEntity.setHostName("127.0.0.1");
        proxyEntity.setPort(7890);

        UpgradeEntity upgradeEntity = new UpgradeEntity();
        upgradeEntity.setCurrent(UpgradeSourceEnum.getDefault().getName());

        List<SchemeItemEntity> schemeItemEntityArrayList = new ArrayList<>();
        for (SchemeItemEnum value : SchemeItemEnum.values()) {
            SchemeItemEntity schemeItemEntity = new SchemeItemEntity();
            schemeItemEntity.setName(value.getName());
            schemeItemEntityArrayList.add(schemeItemEntity);
        }

        SchemeEntity schemeEntity = new SchemeEntity();
        schemeEntity.setStartup(false);
        schemeEntity.setPromptBeforeUpload(true);
        schemeEntity.setLang(I18nConfig.getCurrentLocale().getAlias());
        schemeEntity.setVersion("1.0.0");
        schemeEntity.setCurrentScheme(SchemeItemEnum.getDefault().getName());
        schemeEntity.setShortcutKey(shortcutKeyEntity);
        schemeEntity.setItemList(schemeItemEntityArrayList);
        schemeEntity.setProxy(proxyEntity);
        schemeEntity.setCopy(copyEntity);
        schemeEntity.setFileNameRule(fileNameRuleEntity);
        schemeEntity.setUpgrade(upgradeEntity);

        return schemeEntity;
    }

    public synchronized static SchemeEntity readConfig() {
        try {
            File file = new File(Config.SchemeConfigPath);
            boolean exists = file.exists();
            if (!exists) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                SchemeEntity entity = getEntity();
                FileUtils.write(file, JSON.toJSONString(entity, JSONWriter.Feature.PrettyFormat), StandardCharsets.UTF_8);
                return entity;
            } else {
                String text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                SchemeEntity schemeEntity = JSON.parseObject(text, SchemeEntity.class);
                if (readCount.get() < 2) {
                    List<SchemeItemEntity> schemeItemEntityArrayList = schemeEntity.getItemList();
                    for (SchemeItemEnum value : SchemeItemEnum.values()) {
                        boolean isExist = false;
                        for (SchemeItemEntity schemeItemEntity : schemeItemEntityArrayList) {
                            if (schemeItemEntity.getName().equals(value.getName())) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            SchemeItemEntity schemeItemEntity = new SchemeItemEntity();
                            schemeItemEntity.setName(value.getName());
                            schemeItemEntityArrayList.add(schemeItemEntity);
                        }
                    }
                    saveConfig(schemeEntity);
                }
                return schemeEntity;
            }

        } catch (IOException e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        } finally {
            readCount.addAndGet(1);
        }
        return null;
    }

    public synchronized static boolean saveConfig(SchemeEntity entity) {
        try {
            File file = new File(Config.SchemeConfigPath);
            FileUtils.write(file, JSON.toJSONString(entity, JSONWriter.Feature.PrettyFormat), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
        return false;
    }

}
