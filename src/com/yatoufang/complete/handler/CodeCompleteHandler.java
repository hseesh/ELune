package com.yatoufang.complete.handler;

import com.google.common.collect.Maps;
import com.yatoufang.complete.model.CodeCompleteConfig;
import com.yatoufang.complete.model.context.CodeCompleteContext;
import com.yatoufang.complete.model.type.ConfigType;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.FileWrite;

import java.util.Collection;
import java.util.Map;

/**
 * @author GongHuang(hse)
 * @since 2023/6/1
 */
public abstract class CodeCompleteHandler implements CodeCompletePreference {

    public static final CodeCompleteContext CONTEXT = new CodeCompleteContext();
    private static final Map<ConfigType, CodeCompleteConfig> CONFIG_MAP = Maps.newHashMap();

    protected static final VelocityService velocityService = VelocityService.getInstance();

    public static void initialize(){
        Collection<CodeCompleteConfig> loadConfig = FileWrite.loadConfig(CodeCompleteHandler.class, CodeCompleteConfig.class, ProjectKeys.PATH_AUTO_COMPLETE);
        for (CodeCompleteConfig config : loadConfig) {
            CONFIG_MAP.put(ConfigType.getType(config.getId()), config);
        }
    }

    protected CodeCompleteConfig getConfig() {
        return CONFIG_MAP.get(getConfigType());
    }

}
