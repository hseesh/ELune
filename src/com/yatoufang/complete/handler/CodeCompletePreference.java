package com.yatoufang.complete.handler;

import com.yatoufang.complete.model.context.CodeCompleteTrigger;
import com.yatoufang.complete.model.type.ConfigType;
import com.yatoufang.complete.model.type.ModelType;

/**
 * @author user
 * @since 2023/6/1
 */
public interface CodeCompletePreference {

    boolean trigger(CodeCompleteTrigger triggerContext);

    ModelType getModelType();


    ConfigType getConfigType();


    String getResult(CodeCompleteTrigger triggerContext);


}
