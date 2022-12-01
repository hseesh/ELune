package com.yatoufang.designer.model;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public enum TextAlign {

    LEFT,
    RIGHT,
    CENTER;

    public static TextAlign getName(String text) {
        if (text == null) {
            return CENTER;
        }
        for (final TextAlign align : values()) {
            if (align.name().equalsIgnoreCase(text)) {
                return align;
            }
        }
        return CENTER;
    }
}
