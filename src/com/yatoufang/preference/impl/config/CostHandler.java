package com.yatoufang.preference.impl.config;

import com.yatoufang.entity.ConfigParam;
import com.yatoufang.preference.ConfigPreferenceHandler;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

/**
 * @author GongHuang（hse）
 * @since 2022/8/2
 */
public class CostHandler extends ConfigPreferenceHandler {

    @Override
    public void invoke(ConfigParam configParam) {
        if (check(configParam.getName())) {
            return;
        }
        String prefix = getPrefix(configParam.getName());
        String param = prefix + StringUtil.toUpper(getKey(), ARRAY);
        String expression = String.format(getExpression(), param, configParam.getName(), param, prefix + StringUtil.toUpper(getName()));
        copy(configParam);
        configParam.setReferenceExpression(expression);
    }

    @Override
    public boolean check(String variable) {
        return !variable.toLowerCase().contains(getKey());
    }

    @Override
    public ConfigParam getTemplate() {
        ConfigParam param = ConfigParam.valueOf(getKey(), "消耗[[rewardType,id,num],[rewardType,id,num]] {@code RewardType}", String.class.getName());
        ConfigParam alias = ConfigParam.valueOf(getName(), "消耗列表", "Collection<RewardObject>");
        param.setAliaParam(alias);
        alias.setDefaultValue(" = Lists.newArrayList()");
        return param;
    }

    @Override
    public String getExpression() {
        return "JSONArray %s = JSONArray.parseArray(%s);for (Object rewardItem : %s) {\n            "
            + "JSONArray rewardArray = JSONArray.parseArray(rewardItem.toString());\n            "
            + "RewardObject rewardObject = RewardObject.valueOf(rewardArray);\n           "
            + "%s.add(rewardObject);\n        }";
    }

    @Override
    public String getName() {
        return "costList";
    }

    @Override
    public String getKey() {
        return ProjectKeys.COST;
    }
}
