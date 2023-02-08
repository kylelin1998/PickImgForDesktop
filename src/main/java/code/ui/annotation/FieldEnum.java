package code.ui.annotation;

import lombok.Getter;

@Getter
public enum FieldEnum {

    Text("text"),
    Textarea("textarea"),

    ;

    private String name;

    FieldEnum(String name) {
        this.name = name;
    }

}
