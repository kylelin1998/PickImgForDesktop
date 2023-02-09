package code.config;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nConfig {

    private static ResourceBundle bundle;
    private static I18nLocaleEnum currentLocale;

    public static void init(String lang) {
        currentLocale = I18nLocaleEnum.getI18nLocaleEnumByAlias(lang);
        Locale.setDefault(currentLocale.getLocale());
        bundle = ResourceBundle.getBundle("i18n/i18n", currentLocale.getLocale());
    }

    public static I18nLocaleEnum getCurrentLocale() {
        if (null != currentLocale) {
            return currentLocale;
        }
        return I18nLocaleEnum.values()[0];
    }

    public static ResourceBundle getResourceBundle() {
        return bundle;
    }

}
