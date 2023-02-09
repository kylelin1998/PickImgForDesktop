package code.config;

import lombok.Getter;

@Getter
public enum SchemeItemEnum {

    Github("Github"),
    Imgur("Imgur"),

    AlibabaCloudOSS("AlibabaCloudOSS"),

    ;

    private String name;

    SchemeItemEnum(String name) {
        this.name = name;
    }

    public static SchemeItemEnum getDefault() {
        return SchemeItemEnum.values()[0];
    }

    public static SchemeItemEnum getSchemeItemEnum(String name) {
        for (SchemeItemEnum value : SchemeItemEnum.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }
    public static String[] getNameArray() {
        SchemeItemEnum[] values = SchemeItemEnum.values();
        String[] aliasArray = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            aliasArray[i] = values[i].getName();
        }
        return aliasArray;
    }

}
