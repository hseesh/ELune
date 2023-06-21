package com.yatoufang.complete.handler.impl;

import com.yatoufang.complete.handler.CodeCompleteHandler;
import com.yatoufang.complete.model.context.CodeCompleteTrigger;
import com.yatoufang.complete.model.type.ConfigType;
import com.yatoufang.complete.model.type.ModelType;
import com.yatoufang.entity.Param;

import java.util.List;

/**
 * @author GongHuang(hse)
 * @since 2023/6/3
 */
public class FillArgumentHandler extends CodeCompleteHandler {

    @Override
    public boolean trigger(CodeCompleteTrigger triggerContext) {
        return triggerContext.shouldFillArgument();
    }

    @Override
    public ModelType getModelType() {
        return ModelType.FILL_ARGUMENT;
    }

    @Override
    public ConfigType getConfigType() {
        return ConfigType.NONE;
    }

    @Override
    public String getResult(CodeCompleteTrigger triggerContext) {
        List<Param> fillParam = triggerContext.getFillParam();
        return CodeCompleteHandler.CONTEXT.getArgumentList(triggerContext, fillParam);
    }
}
