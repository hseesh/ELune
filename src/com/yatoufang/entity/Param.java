package com.yatoufang.entity;

import com.intellij.psi.PsiType;
import com.yatoufang.utils.StringUtil;

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
    private String setString;
    private String getString;
    private String annotation;


    public Param(String paramName) {
        this.required = true;
        this.description = StringUtil.EMPTY;
        this.name = paramName;
        this.alias = paramName;
        initGetSetString();
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
        this.description = StringUtil.EMPTY;
        this.name = paramName;
        this.type = paramType;
        this.typeAlias = paramType.getPresentableText();
    }

    public String getSetString() {
        return setString;
    }

    public String getGetString() {
        return getString;
    }

    private void initGetSetString() {
        this.getString = "get" + StringUtil.getUpperCaseVariable(this.getAlias());
        this.setString = "set" + StringUtil.getUpperCaseVariable(this.getAlias());
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
        this.name = name.replace("\"", StringUtil.EMPTY);
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
        this.defaultValue = StringUtil.EMPTY;
    }

    public String getValue() {
        return defaultValue;
    }

    public void setValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public String toString() {
        return "| " + name + " | " + type.getPresentableText() + " | " + required + " | " + description + " |";
    }
}
