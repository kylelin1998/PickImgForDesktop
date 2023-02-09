package code.config;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum I18nLocaleEnum {
    ZH_CN(Locale.SIMPLIFIED_CHINESE, "简体中文"),
    EN(Locale.US, "English"),

    ;

    private Locale locale;
    private String alias;

    I18nLocaleEnum(Locale locale, String alias) {
        this.locale = locale;
        this.alias = alias;
    }

    public static I18nLocaleEnum getI18nLocaleEnumByAlias(String alias) {
        for (I18nLocaleEnum value : I18nLocaleEnum.values()) {
            if (value.getAlias().equals(alias)) {
                return value;
            }
        }
        return null;
    }
    public static String[] getAliasArray() {
        I18nLocaleEnum[] values = I18nLocaleEnum.values();
        String[] aliasArray = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            aliasArray[i] = values[i].getAlias();
        }
        return aliasArray;
    }

}
