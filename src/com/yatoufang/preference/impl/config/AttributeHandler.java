package com.yatoufang.preference.impl.config;

import com.yatoufang.entity.ConfigParam;
import com.yatoufang.preference.ConfigPreferenceHandler;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

/**
 * @author GongHuang（hse）
 * @since 2022/8/2
 */
public class AttributeHandler extends ConfigPreferenceHandler {

    @Override
    public void invoke(ConfigParam configParam) {
        if (check(configParam.getName())) {
            handler.invoke(configParam);
            return;
        }
        String prefix = getPrefix(configParam.getName());
        String param = prefix + StringUtil.toUpper(getKey());
        String expression = String.format(getExpression(), param, configParam.getName(), param, prefix + StringUtil.toUpper(getName()));
        copy(configParam);
        configParam.setReferenceExpression(expression);
    }

    @Override
    public boolean check(String variable) {
        return !variable.endsWith(StringUtil.toUpper(getKey()));
    }

    @Override
    public ConfigParam getTemplate() {
        ConfigParam param = ConfigParam.valueOf(getKey(), "基础属性Map ([[type,value],[type,value]]){@code SpriteAttributeType}", String.class.getSimpleName());
        ConfigParam alias = ConfigParam.valueOf(getName(), "基础属性Map ", "Map<SpriteAttributeType, Long>");
        param.setAliaParam(alias);
        alias.setDefaultValue(" = Maps.newHashMap()");
        return param;
    }

    @Override
    public String getExpression() {
        return "JSONArray %s = JSONArray.parseArray(%s);for (Object baseAttribute : %s) {\n            "
            + "JSONArray attributeArray = JSONArray.parseArray(baseAttribute.toString());\n            "
            + "%s.put(SpriteAttributeType.getType(attributeArray.getIntValue(0)), attributeArray.getLong(1));\n        }";
    }

    @Override
    public String getName() {
        return "attributeMap";
    }

    @Override
    public String getKey() {
        return ProjectKeys.ATTRIBUTE;
    }


}
