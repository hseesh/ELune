package com.yatoufang.preference.impl.config;

import com.yatoufang.entity.ConfigParam;
import com.yatoufang.preference.ConfigPreferenceHandler;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

/**
 * @author GongHuang（hse）
 * @since 2022/8/2
 */
public class AttributeTHandler extends ConfigPreferenceHandler {

    @Override
    public void invoke(ConfigParam configParam) {
        if (check(configParam.getName())) {
            handler.invoke(configParam);
            return;
        }
        copy(configParam);
        String prefix = getPrefix(configParam.getName());
        String param = prefix + StringUtil.toUpper(getKey());
        String expression = String.format(getExpression(), param, configParam.getName(), param, prefix + StringUtil.toUpper(getName()));
        configParam.setReferenceExpression(expression);
    }

    @Override
    public boolean check(String variable) {
        return !variable.endsWith(StringUtil.toUpper(getKey()));
    }

    @Override
    public ConfigParam getTemplate() {
        ConfigParam param = ConfigParam.valueOf(getKey(), "加成属性（万分比）([[type,value],[type,value]]){@code SpriteAttributeType}", String.class.getName());
        ConfigParam alias = ConfigParam.valueOf(getName(), "加成属性（万分比）", "Map<SpriteAttributeType, Integer>");
        param.setAliaParam(alias);
        alias.setDefaultValue(" = Maps.newHashMap()");
        return param;
    }

    @Override
    public String getExpression() {
        return "JSONArray %s = JSONArray.parseArray(%s);for (Object baseAttribute : %s) {\n            "
            + "JSONArray attributeArray = JSONArray.parseArray(baseAttribute.toString());\n            "
            + "%s.put(SpriteAttributeType.getType(attributeArray.getIntValue(0)), attributeArray.getIntValue(1));\n        }";
    }

    @Override
    public String getName() {
        return "attributeTTPercentMap";
    }

    @Override
    public String getKey() {
        return ProjectKeys.ATTRIBUTE_PERCENT;
    }
}
