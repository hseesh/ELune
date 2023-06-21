package com.yatoufang.complete.services;

import com.yatoufang.complete.handler.CodeCompleteHandler;
import com.yatoufang.complete.handler.impl.DBCreateHandler;
import com.yatoufang.complete.handler.impl.FillArgumentHandler;
import com.yatoufang.complete.handler.impl.ParamCheckHandler;
import com.yatoufang.complete.handler.impl.VariableHandler;
import com.yatoufang.complete.model.context.CodeCompleteTrigger;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author GongHuang(hse)
 * @since 2023/6/1
 */
public class CodeCompleteService {

    private static CodeCompleteService instance;

    private final List<CodeCompleteHandler> handlers = Lists.newArrayList();

    public static CodeCompleteService getInstance() {
        if (instance == null) {
            instance = new CodeCompleteService();
        }
        return instance;
    }

    private CodeCompleteService() {
        handlers.add(new FillArgumentHandler());
        handlers.add(new ParamCheckHandler());
        handlers.add(new DBCreateHandler());
        handlers.add(new VariableHandler());
        CodeCompleteHandler.initialize();
    }


    public String action(CodeCompleteTrigger triggerContext){
        for (CodeCompleteHandler handler : handlers) {
            if (handler.trigger(triggerContext)) {
                return handler.getResult(triggerContext);
            }
        }
        return StringUtil.EMPTY;
    }
}
