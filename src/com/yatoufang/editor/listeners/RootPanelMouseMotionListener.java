package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.RootCanvas;
import com.yatoufang.editor.constant.CommandHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanelMouseMotionListener implements MouseMotionListener {
    private final RootCanvas rootCanvas;

    public RootPanelMouseMotionListener(RootCanvas rootCanvas) {
        this.rootCanvas = rootCanvas;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        rootCanvas.setCursorPoint(e.getPoint());
        if (rootCanvas.getModel().getStartConnector() != null) {
            rootCanvas.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (CommandHelper.getCommand(KeyEvent.VK_SPACE) || SwingUtilities.isMiddleMouseButton(e)) {
            rootCanvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            Point origin = rootCanvas.getClickedPoint();
            double zoomFactor = rootCanvas.getRootPanel().getZoomPanel().getScale();
            if (origin != null) {
                JViewport viewPort = rootCanvas.getRootPanel().getScrollPane().getViewport();
                if (viewPort != null) {
                    int deltaX = (int) ((origin.x - e.getX()) * zoomFactor);
                    int deltaY = (int) ((origin.y - e.getY()) * zoomFactor);
                    Rectangle view = viewPort.getViewRect();
                    view.x += deltaX;
                    view.y += deltaY;
                    rootCanvas.scrollRectToVisible(view);
                }
            }
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            int x1 = (int) rootCanvas.getClickedPoint().getX();
            int y1 = (int) rootCanvas.getClickedPoint().getY();
            int x2 = (int) e.getPoint().getX();
            int y2 = (int) e.getPoint().getY();
            rootCanvas.getSelection().setBounds(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
            rootCanvas.getModel().selectBoxes(rootCanvas.getSelection());
            rootCanvas.repaint();
        }
    }
}