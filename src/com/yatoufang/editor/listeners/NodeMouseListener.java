package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.AbstractNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class NodeMouseListener implements MouseListener {
    private final AbstractNode abstractNode;

    public NodeMouseListener(AbstractNode AbstractNode) {
        this.abstractNode = AbstractNode;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.isControlDown()) {
            abstractNode.getModel().setSelected(abstractNode, !abstractNode.getModel().isSelected(abstractNode));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            showContextMenu(e);
        }
        if (!e.isControlDown()) {
            abstractNode.getModel().setSelected(abstractNode, true);
        }
        abstractNode.requestFocus();
        abstractNode.setStartPoint(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        abstractNode.setCursor(Cursor.getDefaultCursor());
        if (abstractNode.getModel().isSingleSelected() && !e.isControlDown()) {
            abstractNode.getModel().setSelected(abstractNode, false);
        }
        abstractNode.getModel().setDragging(false);
        abstractNode.getModel().getCanvas().repaint();
    }

    private void showContextMenu(MouseEvent e) {
        double zoomFactor = abstractNode.getModel().getCanvas().getRootPanel().getZoomPanel().getScale();
        int x = (int) ((abstractNode.getX() + e.getX()) * zoomFactor);
        int y = (int) ((abstractNode.getY() + e.getY()) * zoomFactor);
        abstractNode.getContextMenu().show(abstractNode.getModel().getCanvas(), x, y);
    }
}