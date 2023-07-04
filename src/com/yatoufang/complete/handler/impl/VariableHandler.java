package com.yatoufang.complete.handler.impl;

import com.yatoufang.complete.handler.CodeCompleteHandler;
import com.yatoufang.complete.model.CodeCompleteConfig;
import com.yatoufang.complete.model.context.CodeCompleteTrigger;
import com.yatoufang.complete.model.type.ConfigType;
import com.yatoufang.complete.model.type.ModelType;
import com.yatoufang.entity.Param;
import com.yatoufang.utils.StringUtil;

import java.util.Locale;

/**
 * @author GongHuang(hse)
 * @since 2023/6/3
 */
public class VariableHandler extends CodeCompleteHandler {

    private String cacheTemplate;

    @Override
    public boolean trigger(CodeCompleteTrigger triggerContext) {
        cacheTemplate = null;
        Param prepareParam = triggerContext.getPrepareParam();
        if (prepareParam == null) {
            return false;
        }
        if (prepareParam.hasPrefix()) {
            cacheTemplate = StringUtil.EMPTY;
            return true;
        }
        CodeCompleteConfig config = getConfig();
        if (config == null) {
            return false;
        }
        cacheTemplate = config.getTemplate(prepareParam.getTypeAlias());
        return cacheTemplate == null;
    }

    @Override
    public ModelType getModelType() {
        return ModelType.VARIABLE_MODEL;
    }

    @Override
    public ConfigType getConfigType() {
        return ConfigType.VARIABLE;
    }

    @Override
    public String getResult(CodeCompleteTrigger triggerContext) {
        if (cacheTemplate == null) {
            return StringUtil.EMPTY;
        }
        Param prepareParam = triggerContext.getPrepareParam();
        CodeCompleteConfig config = getConfig();
        if (prepareParam.hasPrefix()) {
            String key = triggerContext.getPrepareKey(prepareParam.getName());
            String template = config.getTemplate(key);
            String metaType = StringUtil.getMetaType(prepareParam.getTypeAlias());
            if (metaType.toLowerCase(Locale.ROOT).equals(key)) {
                return StringUtil.EMPTY;
            }
            Param param = new Param(prepareParam.getName(), metaType, triggerContext.getMethod().getReturnType());
            param.setDefaultValue(StringUtil.toLowerCaseForFirstChar(metaType));
            return velocityService.execute(template, param, null);
        }
        String template = config.getTemplate(prepareParam.getTypeAlias());
        if (template == null) {
            return StringUtil.EMPTY;
        }
        return velocityService.execute(cacheTemplate, prepareParam, null);
    }
}
