package com.yatoufang.preference;

import com.yatoufang.entity.ConfigParam;
import com.yatoufang.utils.StringUtil;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public abstract class ConfigPreferenceHandler implements ConfigPreference {

    protected ConfigPreferenceHandler handler;

    public String getPrefix(String variable) {
        return StringUtil.getPrefix(variable, StringUtil.toUpper(getKey()));
    }

    public void copy(ConfigParam configParam) {
        ConfigParam template = getTemplate();
        configParam.setTypeAlias(template.getTypeAlias());
        configParam.setDescription(template.getDescription());
        if (template.getAliaParam() != null) {
            ConfigParam aliaParam = template.getAliaParam();
            aliaParam.setName(getPrefix(configParam.getName()) + StringUtil.toUpper(aliaParam.getName()));
            configParam.setAliaParam(aliaParam);
        }
    }

}
