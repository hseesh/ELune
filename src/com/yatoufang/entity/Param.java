package com.yatoufang.entity;

import com.intellij.psi.PsiType;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

import java.util.Objects;

/**
 * @author hse
 * @Date: 2021/1/13
 */
public class Param {

    private String name;
    private transient String alias;
    private transient PsiType type;
    private String typeAlias;
    private transient boolean required;
    private String defaultValue;
    private String description;
    private transient String setString;
    private transient String getString;
    private transient String annotation;

    public Param(String paramName) {
        this.required = true;
        this.description = StringUtil.EMPTY;
        this.name = paramName.trim();
        this.alias = paramName.trim();
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

    public void initGetSetString() {
        this.getString = "get" + StringUtil.getUpperCaseVariable(this.alias);
        this.setString = "set" + StringUtil.getUpperCaseVariable(this.alias);
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
        this.defaultValue = StringUtil.EMPTY;
        if (description == null || description.isEmpty()) {
            return;
        }
        this.description = description;
        this.description = this.description.replaceAll(String.valueOf(StringUtil.NEW_LINE),"<br>");
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

    public String getPrimaryInfo() {
        return typeAlias + StringUtil.SPACE + StringUtil.SPACE + name + StringUtil.SPACE + StringUtil.SPACE + description;
    }

    public void setSetString(String setString) {
        this.setString = setString;
    }

    public void setGetString(String getString) {
        this.getString = getString;
    }

    public boolean hasPrefix() {
        if (typeAlias == null) {
            return false;
        }
        return typeAlias.contains(ProjectKeys.RESULT_OF);
    }

    public String getPrefix(){
        return StringUtil.getPrefix(typeAlias, String.valueOf(StringUtil.LESS_THEN));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Param param = (Param) o;
        return Objects.equals(name, param.name) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "| " + name + " | " + (type == null ? StringUtil.EMPTY : type.getPresentableText()) + " | " + required + " | " + description + " |";
    }
}
