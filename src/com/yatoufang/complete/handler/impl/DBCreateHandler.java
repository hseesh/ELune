package com.yatoufang.complete.handler.impl;

import com.yatoufang.complete.handler.CodeCompleteHandler;
import com.yatoufang.complete.model.CodeCompleteConfig;
import com.yatoufang.complete.model.context.CodeCompleteTrigger;
import com.yatoufang.complete.model.type.ConfigType;
import com.yatoufang.complete.model.type.ModelType;
import com.yatoufang.entity.Method;
import com.yatoufang.utils.StringUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author GongHuang(hse)
 * @since 2023/6/3
 */
public class DBCreateHandler extends CodeCompleteHandler {

    public final Collection<String> ARRAY_TYPE = Arrays.asList(Collection.class.getSimpleName(), List.class.getSimpleName());

    @Override
    public boolean trigger(CodeCompleteTrigger triggerContext) {
        return triggerContext.shouldCreateDB(CONTEXT.getDataBase());
    }

    @Override
    public ModelType getModelType() {
        return ModelType.STARTING_POINT;
    }

    @Override
    public ConfigType getConfigType() {
        return ConfigType.CREATE_DB;
    }

    @Override
    public String getResult(CodeCompleteTrigger triggerContext) {
        Method dataBase = CONTEXT.getDataBase();
        if (dataBase == null) {
            return StringUtil.EMPTY;
        }
        CodeCompleteConfig config = getConfig();
        if (config == null) {
            return StringUtil.EMPTY;
        }
        Method method = triggerContext.getMethod();
        if (ARRAY_TYPE.contains(method.getRequestType())) {
            String argumentList = CONTEXT.getArgumentList(triggerContext, CONTEXT.getDataBaseList().getParams());
            CONTEXT.getDataBaseList().setArgumentList(argumentList);
            return velocityService.execute(config.getTemplate(ARRAY_TYPE.iterator().next()), CONTEXT.getDataBaseList());
        }
        String argumentList = CONTEXT.getArgumentList(triggerContext, CONTEXT.getDataBase().getParams());
        CONTEXT.getDataBase().setArgumentList(argumentList);
        return velocityService.execute(config.getTemplate(StringUtil.EMPTY), CONTEXT.getDataBase(), null);
    }
}
