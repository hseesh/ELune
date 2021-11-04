package com.yatoufang.entity;

import com.intellij.psi.PsiType;

/**
 * @author hse
 * @Date: 2021/1/13
 */
public class Param {


    private String name;
    private String alias;
    private PsiType type;
    private String typeAlias;
    private boolean required;
    private String defaultValue;
    private String description;


    public Param(String paramName) {
        this.required = true;
        this.description = "";
        this.name = paramName;
        this.alias = paramName;
    }

    public Param(String name, String typeAlias, boolean required, String defaultValue, String description) {
        this.name = name;
        this.typeAlias = typeAlias;
        this.required = required;
        this.defaultValue = defaultValue;
        this.description = description;
    }

    public Param(String name, String alias, String typeAlias) {
        this.name = name;
        this.alias = alias;
        this.typeAlias = typeAlias;
    }

    public Param(String paramName, PsiType paramType) {
        this.description = "";
        this.name = paramName;
        this.type = paramType;
        this.typeAlias = paramType.getPresentableText();
    }

    public String getTypeAlias() {
        return typeAlias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PsiType getType() {
        return type;
    }

    public void setType(PsiType paramType) {
        this.type = paramType;
        this.typeAlias = paramType.getCanonicalText();
    }


    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return defaultValue;
    }

    public void setValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getALL() {
        return "| " + name + " | " + type.getPresentableText() + " | " + required + " | " + description + " |";
    }

    public void setTypeAlias(String typeAlias) {
        this.typeAlias = typeAlias;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "Param{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", type=" + type +
                ", typeAlias='" + typeAlias + '\'' +
                ", required=" + required +
                ", defaultValue='" + defaultValue + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
