package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.constant.CommandHelper;
import com.yatoufang.editor.component.UndoRedoNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class NodeMouseMotionListener implements MouseMotionListener {
    private final AbstractNode abstractNode;

    public NodeMouseMotionListener(AbstractNode abstractNode) {
        this.abstractNode = abstractNode;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!abstractNode.getModel().getDragging()) {
                abstractNode.getModel().addUndoableStackFrame(abstractNode.getModel().getSelectedNodes(), UndoRedoNode.MOVED);
            }
            abstractNode.getModel().setDragging(true);
            abstractNode.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            int dx = e.getX() - abstractNode.getStartPoint().x;
            int dy = e.getY() - abstractNode.getStartPoint().y;
            abstractNode.getModel().moveSelected(CommandHelper.getCommand('y') ? 0 : dx, CommandHelper.getCommand('x') ? 0 : dy);
        }
    }
}