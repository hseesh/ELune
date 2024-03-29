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

    CONFIG(new Color(38, 79, 218, 102)),

    CONFIG_START(new Color(49, 119, 147, 51)),

    DATABASE(new Color(243, 125, 125)),

    DATABASE_START(new Color(243, 125, 125, 80)),

    ENTITY(new Color(163, 218, 255)),

    ENTITY_START(new Color(163, 218, 255, 80)),

    PROTOCOL(new Color(100, 65, 164)),

    PROTOCOL_START(new Color(132, 102, 187, 80)),

    PUSH(new Color(16, 51, 225, 128)),
    PUSH_START(new Color(188, 225, 135, 80)),

    REQUEST(new Color(18, 138, 168, 128)),

    REQUEST_START(new Color(195, 232, 226, 80)),

    RESPONSE(new Color(234, 65, 211, 128)),

    RESPONSE_START(new Color(234, 172, 223, 80)),

    ENUM(new Color(179, 248, 5, 128)),


    ENUM_START(new Color(202, 245, 98, 77)),

    NODE_DARK_BANK_GROUND(Color.BLACK);

    private final Color color;

    ColorBox(Color color) {
        this.color = color;
    }

    public final Color getColor() {
        return color;
    }
}