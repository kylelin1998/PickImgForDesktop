package code.config;

import lombok.Getter;

@Getter
public enum ProxyTypeEnum {

    NotOpen(0, "Not Open"),
    HttpProxy(1, "Http Proxy"),

    ;

    private int type;
    private String alias;

    ProxyTypeEnum(int type, String alias) {
        this.type = type;
        this.alias = alias;
    }

    public static ProxyTypeEnum getDefault() {
        return ProxyTypeEnum.values()[0];
    }

    public static ProxyTypeEnum getProxyTypeEnum(int type) {
        for (ProxyTypeEnum value : ProxyTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return null;
    }

    public static ProxyTypeEnum getProxyTypeEnumByAlias(String alias) {
        for (ProxyTypeEnum value : ProxyTypeEnum.values()) {
            if (value.getAlias().equals(alias)) {
                return value;
            }
        }
        return null;
    }

    public static String[] getAliasArray() {
        ProxyTypeEnum[] values = ProxyTypeEnum.values();
        String[] aliasArray = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            aliasArray[i] = values[i].getAlias();
        }
        return aliasArray;
    }

}
