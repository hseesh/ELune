package com.yatoufang.preference.impl.config;

import com.yatoufang.entity.ConfigParam;
import com.yatoufang.preference.ConfigPreferenceHandler;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

/**
 * @author GongHuang（hse）
 * @since 2022/8/2
 */
public class RewardsHandler extends ConfigPreferenceHandler {

    @Override
    public void invoke(ConfigParam configParam) {
        if (check(configParam.getName())) {
            handler.invoke(configParam);
            return;
        }
        copy(configParam);
        String prefix = getPrefix(configParam.getName());
        String param = prefix + StringUtil.toUpper(getKey(), ARRAY);
        String expression = String.format(getExpression(), param, configParam.getName(), param, prefix + StringUtil.toUpper(getName()));
        configParam.setReferenceExpression(expression);
    }

    @Override
    public boolean check(String variable) {
        return !variable.contains(StringUtil.toUpper(getKey()));
    }

    @Override
    public ConfigParam getTemplate() {
        ConfigParam param = ConfigParam.valueOf(getKey(), "奖励[[rewardType,id,num],[rewardType,id,num]] {@code RewardType}", String.class.getName());
        ConfigParam alias = ConfigParam.valueOf(getName(), "奖励列表", "Collection<RewardObject>");
        param.setAliaParam(alias);
        alias.setDefaultValue(" = Lists.newArrayList()");
        return param;
    }

    @Override
    public String getExpression() {
        return "JSONArray %s = JSONArray.parseArray(%s);for (Object reward : %s) {\n            JSONArray itemArray = JSONArray.parseArray(reward.toString());\n            "
            + "RewardObject rewardObject = RewardObject.valueOf(itemArray);\n           %s.add(rewardObject);\n        }";
    }

    @Override
    public String getName() {
        return "rewardList";
    }

    @Override
    public String getKey() {
        return ProjectKeys.REWARD;
    }
}
