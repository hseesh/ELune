package com.yatoufang.editor.type;

import java.awt.*;

/**
 * @author hse
 * @since 2022/09/1 0001
 */
public enum Position {
    N(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)),
    NNE(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)),
    NE(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)),
    ENE(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)),
    E(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)),
    ESE(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)),
    SE(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)),
    SSE(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)),
    S(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)),
    SSW(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)),
    SW(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)),
    WSW(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)),
    W(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)),
    WNW(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)),
    NW(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)),
    NNW(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));

    private final Cursor cursor;

    Position(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }
}