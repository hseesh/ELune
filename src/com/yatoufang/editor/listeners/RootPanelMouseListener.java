package com.yatoufang.editor.listeners;


import com.yatoufang.editor.component.Canvas;
import com.yatoufang.editor.component.LinkLine;
import com.yatoufang.editor.component.impl.ProtocolNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanelMouseListener implements MouseListener {
    private final Canvas canvas;

    public RootPanelMouseListener(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        canvas.setCursor(Cursor.getDefaultCursor());
        canvas.getSelection().setBounds(0, 0, 0, 0);
        canvas.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvas.requestFocus();
        canvas.setClickedPoint(e.getPoint());
        if (SwingUtilities.isRightMouseButton(e)) {
            LinkLine chartLine = clickedOnLine(e);
            if (chartLine != null) {
                chartLine.showContextMenu(e);
            } else {
                if (canvas.getModel().shouldClearLinkLine()) {
                    canvas.getModel().clearMakeLine();
                }
                showContextMenu(e);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!e.isControlDown()) {
            canvas.getModel().deSelectAll();
        }
        if (SwingUtilities.isMiddleMouseButton(e) && e.isControlDown()) {
            canvas.getRootPanel().getZoomPanel().setScale(1.0);
            canvas.getRootPanel().getSideBar().updateZoomLabel(canvas);
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            canvas.getModel().toggleConnectors();
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (e.getClickCount() == 2) {
                canvas.getModel().add(new ProtocolNode(canvas.getModel(), e.getPoint()));
            } else {
                canvas.getModel().tryCreate(e.getPoint());
            }
        }
    }

    private void showContextMenu(MouseEvent e) {
        double zoomFactor = canvas.getRootPanel().getZoomPanel().getScale();
        int x = (int) (e.getX() * zoomFactor);
        int y = (int) (e.getY() * zoomFactor);
        canvas.getContextMenu().show(e.getComponent(), x, y);
    }

    private LinkLine clickedOnLine(MouseEvent e) {
        Point p = e.getPoint();
        for (LinkLine line : canvas.getModel().getLines()) {
            Path2D path = line.getPath();
            if (path.contains(p)) {
                return line;
            }
        }
        return null;
    }
}