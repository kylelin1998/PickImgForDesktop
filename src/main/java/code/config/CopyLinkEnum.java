package code.config;

import lombok.Getter;

@Getter
public enum CopyLinkEnum {

    Markdown("markdown", "MarkdownLink"),
    Ordinary("ordinary", "Url"),

    ;

    private String linkType;
    private String alias;

    CopyLinkEnum(String linkType, String alias) {
        this.linkType = linkType;
        this.alias = alias;
    }

    public static CopyLinkEnum getDefault() {
        return CopyLinkEnum.values()[0];
    }

    public static CopyLinkEnum getCopyLinkEnum(String linkType) {
        for (CopyLinkEnum value : CopyLinkEnum.values()) {
            if (value.getLinkType().equals(linkType)) {
                return value;
            }
        }
        return null;
    }

    public static CopyLinkEnum getCopyLinkEnumByAlias(String alias) {
        for (CopyLinkEnum value : CopyLinkEnum.values()) {
            if (value.getAlias().equals(alias)) {
                return value;
            }
        }
        return null;
    }

    public static String[] getAliasArray() {
        CopyLinkEnum[] values = CopyLinkEnum.values();
        String[] aliasArray = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            aliasArray[i] = values[i].getAlias();
        }
        return aliasArray;
    }

}
