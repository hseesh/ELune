package com.yatoufang.entity;


import com.yatoufang.utils.StringUtil;

import java.util.Objects;

/**
 * @author GongHuang（hse）
 * @since 2022/1/19
 */
public class ConfigParam extends Param{

    private ConfigParam aliaParam;
    private String referenceExpression;

    public ConfigParam() {
        super(StringUtil.EMPTY);
    }

    public ConfigParam(String paramName) {
        super(paramName);
        super.setDefaultValue(StringUtil.EMPTY);
    }

    public ConfigParam(String name, String type) {
        super(name);
        super.setTypeAlias(type);
        super.setDefaultValue(StringUtil.EMPTY);
    }

    public static ConfigParam valueOf(String name, String description, String typeAlias ) {
        ConfigParam model = new ConfigParam();
        model.setDescription(description);
        model.setTypeAlias(typeAlias);
        model.setName(name);
        return model;
    }

    public ConfigParam getAliaParam() {
        return aliaParam;
    }

    public void setAliaParam(ConfigParam aliaParam) {
        this.aliaParam = aliaParam;
    }

    public String getReferenceExpression() {
        return referenceExpression;
    }

    public void setReferenceExpression(String referenceExpression) {
        this.referenceExpression = referenceExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ConfigParam param = (ConfigParam) o;
        return Objects.equals(getName(), param.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        String string = super.toString();
        if (referenceExpression != null) {
            string += referenceExpression;
        }
        return  string;
    }
}
