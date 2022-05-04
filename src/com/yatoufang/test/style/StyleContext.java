package com.yatoufang.test.style;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.util.io.FileUtil;
import com.yatoufang.test.model.entity.NodeConfig;
import com.yatoufang.test.style.event.ConfigEventParser;
import com.yatoufang.test.style.event.TableEventParser;
import com.yatoufang.test.style.impl.NodeLayoutStyleParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/3/29 0029
 */
public class StyleContext {
    private final static Map<Integer,NodeConfig> CONFIG_TYPE_MAP = Maps.newHashMap();

    private static final Map<NodeType, AbstractStyleParser> PARSERS = Maps.newHashMap();

    public static void register(NodeType nodeType, AbstractStyleParser styleParser) {
        PARSERS.put(nodeType, styleParser);
    }

    public static AbstractStyleParser getParser(NodeType nodeType) {
        return PARSERS.get(nodeType);
    }

    public static NodeConfig getConfig(int type) {
        return CONFIG_TYPE_MAP.get(type);
    }

    static {
        try {
            InputStream resourceAsStream = StyleContext.class.getResourceAsStream("/config/node_config.json");
            if (resourceAsStream != null) {
                String config = FileUtil.loadTextAndClose(resourceAsStream);
                if (!config.isEmpty()) {
                    Type type = new TypeToken<List<NodeConfig>>() {
                    }.getType();
                    List<NodeConfig>  nodeConfigs = new Gson().fromJson(config, type);
                    for (NodeConfig nodeConfig : nodeConfigs) {
                        CONFIG_TYPE_MAP.put(nodeConfig.getType(), nodeConfig);
                    }
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        new NodeLayoutStyleParser();
        new ConfigEventParser();
        new TableEventParser();
    }


}
