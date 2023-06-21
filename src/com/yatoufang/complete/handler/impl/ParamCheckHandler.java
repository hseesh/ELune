package com.yatoufang.complete.handler.impl;

import com.intellij.psi.PsiParameter;
import com.yatoufang.complete.handler.CodeCompleteHandler;
import com.yatoufang.complete.model.CodeCompleteConfig;
import com.yatoufang.complete.model.context.CodeCompleteTrigger;
import com.yatoufang.complete.model.type.ConfigType;
import com.yatoufang.complete.model.type.ModelType;
import com.yatoufang.entity.Param;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

/**
 * @author GongHuang(hse)
 * @since 2023/6/3
 */
public class ParamCheckHandler extends CodeCompleteHandler {
    @Override
    public boolean trigger(CodeCompleteTrigger triggerContext) {
        return triggerContext.shouldCheckParam();
    }

    @Override
    public ModelType getModelType() {
        return ModelType.STARTING_POINT;
    }

    @Override
    public ConfigType getConfigType() {
        return ConfigType.PARAM_CHECK;
    }

    @Override
    public String getResult(CodeCompleteTrigger triggerContext) {
        CodeCompleteConfig config = getConfig();
        for (PsiParameter argument : triggerContext.getArguments()) {
            if (argument.getName().equals(ProjectKeys.ACTOR_ID)) {
                continue;
            }
            String type = argument.getType().getPresentableText();
            String template = config.getTemplate(type);
            if (template == null) {
                continue;
            }
            return velocityService.execute(template, new Param(argument.getName(), triggerContext.getMethod().getReturnType(), type), null);
        }
        return StringUtil.EMPTY;
    }
}
