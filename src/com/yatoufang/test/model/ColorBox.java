package com.yatoufang.test.model;

import com.google.common.collect.Maps;
import com.intellij.ui.JBColor;

import java.awt.*;
import java.util.HashMap;

/**
 * @author GongHuang（hse）
 * @since 2022/4/9 0009
 */
public class ColorBox {
    private static final HashMap<String, Color> colors = Maps.newHashMap();

    static {
        colors.put("black", JBColor.BLACK);
        colors.put("blue", JBColor.BLUE);
        colors.put("cyan", JBColor.CYAN);
        colors.put("gray", JBColor.GRAY);
        colors.put("white", JBColor.WHITE);
        colors.put("green", JBColor.GREEN);
        colors.put("yellow", JBColor.YELLOW);
        colors.put("orange", JBColor.ORANGE);
        colors.put("pink", JBColor.PINK);
        colors.put("darkGray", JBColor.DARK_GRAY);
        colors.put("lightGray", JBColor.LIGHT_GRAY);
    }

    public static Color getColor(String name) {
        return colors.getOrDefault(name, JBColor.GRAY);
    }
}
