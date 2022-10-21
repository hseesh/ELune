package com.yatoufang.preference;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yatoufang.preference.model.AccessContentConfig;
import com.yatoufang.preference.model.AccessContentType;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.FileWrite;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/10/17
 */
public abstract class AccessPreferenceHandler implements AccessPreference {
    private static final List<AccessPreferenceHandler> HANDLERS = Lists.newArrayList();

    private static final Map<AccessContentType, AccessContentConfig> CONFIG_MAP = Maps.newHashMap();

    public AccessPreferenceHandler() {
        HANDLERS.add(this);
    }

    public static List<AccessPreferenceHandler> getHandlers() {
        return HANDLERS;
    }

    public static void clear() {
        HANDLERS.clear();
        Collection<AccessContentConfig> configs = FileWrite.loadConfig(AccessPreferenceHandler.class, AccessContentConfig.class, ProjectKeys.PATH_CONFIG_ACCESS_BUILD);
        for (AccessContentConfig config : configs) {
            CONFIG_MAP.put(AccessContentType.getType(config.getType()), config);
        }
    }

    public Collection<String> getConfig(AccessContentType type) {
        AccessContentConfig config = CONFIG_MAP.getOrDefault(type, new AccessContentConfig());
        return config.getTemplates();
    }

}
