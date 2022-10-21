package com.yatoufang.preference.impl.config;

import com.yatoufang.entity.ConfigParam;
import com.yatoufang.preference.ConfigPreferenceHandler;
import com.yatoufang.utils.StringUtil;

/**
 * @author GongHuang（hse）
 * @since 2022/10/12
 */
public class LimitHandler extends ConfigPreferenceHandler {

    @Override
    public void invoke(ConfigParam configParam) {
        if (check(configParam.getName())) {
            handler.invoke(configParam);
            return;
        }
        copy(configParam);
    }

    @Override
    public boolean check(String variable) {
        return !(variable.endsWith(StringUtil.toUpper(getKey())) || variable.contains(getKey()));
    }

    @Override
    public ConfigParam getTemplate() {
        return ConfigParam.valueOf(getKey(), "限制", int.class.getName());
    }

    @Override
    public String getExpression() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getKey() {
        return "limit";
    }
}
