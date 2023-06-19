package com.yatoufang.editor.listeners;


import com.yatoufang.editor.component.RootCanvas;
import com.yatoufang.editor.component.LinkLine;
import com.yatoufang.editor.component.impl.ProtocolNode;
import com.yatoufang.editor.constant.NodeHelp;

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
    private final RootCanvas rootCanvas;

    public RootPanelMouseListener(RootCanvas rootCanvas) {
        this.rootCanvas = rootCanvas;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        rootCanvas.setCursor(Cursor.getDefaultCursor());
        rootCanvas.getSelection().setBounds(0, 0, 0, 0);
        rootCanvas.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        rootCanvas.requestFocus();
        rootCanvas.setClickedPoint(e.getPoint());
        if (SwingUtilities.isRightMouseButton(e)) {
            LinkLine chartLine = clickedOnLine(e);
            if (chartLine != null) {
                chartLine.showContextMenu(e);
            } else {
                if (rootCanvas.getModel().shouldClearLinkLine()) {
                    rootCanvas.getModel().clearMakeLine();
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
            rootCanvas.getModel().deSelectAll();
        }
        if (SwingUtilities.isMiddleMouseButton(e) && e.isControlDown()) {
            rootCanvas.getRootPanel().getZoomPanel().setScale(1.0);
            rootCanvas.getRootPanel().getSideBar().updateZoomLabel(rootCanvas);
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            rootCanvas.getModel().toggleConnectors();
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (e.getClickCount() == 2) {
                ProtocolNode protocolNode = new ProtocolNode(rootCanvas.getModel(), e.getPoint());
                NodeHelp.init(protocolNode);
                rootCanvas.getModel().add(protocolNode);
            } else {
                rootCanvas.getModel().tryCreate(e.getPoint());
            }
        }
    }

    private void showContextMenu(MouseEvent e) {
        double zoomFactor = rootCanvas.getRootPanel().getZoomPanel().getScale();
        int x = (int) (e.getX() * zoomFactor);
        int y = (int) (e.getY() * zoomFactor);
        rootCanvas.getContextMenu().show(e.getComponent(), x, y);
    }

    private LinkLine clickedOnLine(MouseEvent e) {
        Point p = e.getPoint();
        for (LinkLine line : rootCanvas.getModel().getLines()) {
            Path2D path = line.getPath();
            if (path.contains(p)) {
                return line;
            }
        }
        return null;
    }
}