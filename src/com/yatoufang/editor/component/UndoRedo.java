package com.yatoufang.editor.component;

import java.awt.*;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public interface UndoRedo {
    void undo(UndoRedoNode node);
    void redo(UndoRedoNode node);
    Rectangle getBounds();
}