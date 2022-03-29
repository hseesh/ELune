package com.yatoufang.test.style;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/3/29 0029
 */
public class StyleContext {

    private static final Map<NodeType,AbstractStyleParser> PARSERS = Maps.newHashMap();

    public static void register(NodeType nodeType,AbstractStyleParser styleParser){
        PARSERS.put(nodeType, styleParser);
    }

    public static AbstractStyleParser getParser(NodeType nodeType){
        return PARSERS.get(nodeType);
    }
}
