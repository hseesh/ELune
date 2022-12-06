package com.yatoufang.editor.constant;

import com.intellij.ui.JBColor;

import java.awt.Color;

/**
 * @author hse
 * @since 2022/9/4 0004
 */
@SuppressWarnings("all")
public enum ColorBox {

    BOX_RESIZING_BACKGROUND(new Color(255, 230, 178, 200)),

    LINE(new Color(96,158,199)),

    SELECTION_BACKGROUND(new Color(213, 235, 242, 127)),

    SELECTION_BORDER(new Color(116,165,193)),

    LINE_PUSH(new Color(215,237,221)),

    TRANSPARENT(new JBColor(new Color(65, 67, 68, 0), new Color(192, 243, 235, 0))),

    NODE_PENCIL(new Color(246, 246, 224,200)),

    ROOT_PANEL_BANK_GROUND(new Color(39, 39, 39)),

    CONNECTOR_DEFAULT(new Color(94, 52, 72)),
    BOX_BORDER(new Color(93,96,99)),

    CONFIG(new Color(83, 103, 82)),

    CONFIG_START(new Color(83, 103, 82, 80)),

    DATABASE(new Color(243, 125, 125)),

    DATABASE_START(new Color(243, 125, 125, 80)),

    ENTITY(new Color(163, 218, 255)),

    ENTITY_START(new Color(163, 218, 255, 80)),

    PROTOCOL(new Color(100, 65, 164)),

    PROTOCOL_START(new Color(100, 65, 164, 80)),

    PUSH(new Color(67, 43, 27)),
    PUSH_START(new Color(67, 43, 27, 80)),

    REQUEST(new Color(147, 127, 47)),

    REQUEST_START(new Color(147, 127, 47, 80)),

    RESPONSE(new Color(192, 148, 183, 255)),

    RESPONSE_START(new Color(192, 148, 183, 80)),

    NODE_DARK_BANK_GROUND(Color.BLACK);

    private final Color color;

    ColorBox(Color color) {
        this.color = color;
    }

    public final Color getColor() {
        return color;
    }
}