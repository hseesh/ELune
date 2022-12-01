package com.yatoufang.designer.draw;

import com.google.common.collect.Maps;
import com.yatoufang.designer.draw.impl.LeftTree;
import com.yatoufang.designer.draw.impl.NormalParser;
import com.yatoufang.designer.draw.impl.RightTimeParser;
import com.yatoufang.designer.draw.impl.RightTreeParser;

import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/1/11
 */
public class LayoutContext {

    private final static Map<LayoutType,AbstractLayoutParser> PARSERS = Maps.newHashMap();

    static {
        new RightTimeParser();
        new RightTreeParser();
        new LeftTree();
        new NormalParser();
    }

    public static void register(LayoutType layoutType, AbstractLayoutParser parser){
        PARSERS.put(layoutType,parser);
    }

    public static AbstractLayoutParser getParser(LayoutType layoutType){
        return PARSERS.get(layoutType);
    }

    public static void main(String[] args) {
        System.out.println(PARSERS.size());
        PARSERS.get(LayoutType.RIGHT_TIME).parser(null,null);
    }

}
