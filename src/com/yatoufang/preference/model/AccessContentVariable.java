package com.yatoufang.preference.model;

/**
 * @author GongHuang（hse）
 * @since 2022/10/18
 */
public class AccessContentVariable {

    private String name;

    private String alias;

    private String type;

    private String key;

    private String value;

    private String description;

    public static AccessContentVariable valueOf(String name) {
        AccessContentVariable model = new AccessContentVariable();
        model.name = name;
        return model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
