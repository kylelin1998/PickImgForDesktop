package code.config;

import lombok.Getter;

@Getter
public enum UpgradeSourceEnum {

    Gitte("Gitte"),
    Github("Github"),

    ;

    private String name;

    UpgradeSourceEnum(String name) {
        this.name = name;
    }

    public static UpgradeSourceEnum getDefault() {
        return UpgradeSourceEnum.values()[0];
    }

    public static UpgradeSourceEnum getUpgradeSourceEnum(String name) {
        for (UpgradeSourceEnum value : UpgradeSourceEnum.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public static String[] getNameArray() {
        UpgradeSourceEnum[] values = UpgradeSourceEnum.values();
        String[] aliasArray = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            aliasArray[i] = values[i].getName();
        }
        return aliasArray;
    }

}
