package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.Canvas;
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
    private final Canvas canvas;

    public RootPanelMouseMotionListener(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        canvas.setCursorPoint(e.getPoint());
        if (canvas.getModel().getStartConnector() != null) {
            canvas.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (CommandHelper.getCommand(KeyEvent.VK_SPACE) || SwingUtilities.isMiddleMouseButton(e)) {
            canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            Point origin = canvas.getClickedPoint();
            double zoomFactor = canvas.getRootPanel().getZoomPanel().getScale();
            if (origin != null) {
                JViewport viewPort = canvas.getRootPanel().getScrollPane().getViewport();
                if (viewPort != null) {
                    int deltaX = (int) ((origin.x - e.getX()) * zoomFactor);
                    int deltaY = (int) ((origin.y - e.getY()) * zoomFactor);
                    Rectangle view = viewPort.getViewRect();
                    view.x += deltaX;
                    view.y += deltaY;
                    canvas.scrollRectToVisible(view);
                }
            }
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            int x1 = (int) canvas.getClickedPoint().getX();
            int y1 = (int) canvas.getClickedPoint().getY();
            int x2 = (int) e.getPoint().getX();
            int y2 = (int) e.getPoint().getY();
            canvas.getSelection().setBounds(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
            canvas.getModel().selectBoxes(canvas.getSelection());
            canvas.repaint();
        }
    }
}