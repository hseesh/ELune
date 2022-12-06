package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.component.Connector;
import com.yatoufang.editor.component.UndoRedoNode;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class ConnectorMouseMotionListener implements MouseMotionListener {
    private final Connector connector;

    public ConnectorMouseMotionListener(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.isControlDown()) {
            if (!connector.getAbstractNode().isResizing()) {
                connector.getAbstractNode().getModel().addUndoableStackFrame(new HashSet<>() {{
                    add(connector.getAbstractNode());
                }}, UndoRedoNode.RESIZED);
            }
            resizing(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void resizing(MouseEvent e) {
        AbstractNode abstractNode = connector.getAbstractNode();
        int connectorX = connector.getX();
        int connectorY = connector.getY();
        int width = abstractNode.getWidth();
        int height = abstractNode.getHeight();
        int vertical = e.getY() - connector.getStartPoint().y;
        int horizontal = e.getX() - connector.getStartPoint().x;
        final int minSize = 60;
        switch (connector.getPosition()) {
            case ENE:
            case E:
            case ESE:
                if (width + horizontal > minSize) {
                    connector.setLocation(connectorX + horizontal, connectorY);
                    abstractNode.setBounds(abstractNode.getX(), abstractNode.getY(), abstractNode.getWidth() + horizontal, abstractNode.getHeight());
                }
                break;
            case NNW:
            case N:
            case NNE:
                if (height - vertical > minSize) {
                    connector.setLocation(connectorX, connectorY + vertical);
                    abstractNode.setBounds(abstractNode.getX(), abstractNode.getY() + vertical, abstractNode.getWidth(), abstractNode.getHeight() - vertical);
                }
                break;
            case NE:
                if (width + horizontal > minSize && height - vertical > minSize) {
                    connector.setLocation(connectorX + horizontal, connectorY + vertical);
                    abstractNode.setBounds(abstractNode.getX(), abstractNode.getY() + vertical, abstractNode.getWidth() + horizontal, abstractNode.getHeight() - vertical);
                }
                break;
            case NW:
                if (width - horizontal > minSize && height - vertical > minSize) {
                    connector.setLocation(connectorX + horizontal, connectorY + vertical);
                    abstractNode.setBounds(abstractNode.getX() + horizontal, abstractNode.getY() + vertical, abstractNode.getWidth() - horizontal, abstractNode.getHeight() - vertical);
                }
                break;
            case SSW:
            case S:
            case SSE:
                if (height + vertical > minSize) {
                    connector.setLocation(connectorX, connectorY + vertical);
                    abstractNode.setBounds(abstractNode.getX(), abstractNode.getY(), abstractNode.getWidth(), abstractNode.getHeight() + vertical);
                }
                break;
            case SE:
                if (width + horizontal > minSize && height + vertical > minSize) {
                    connector.setLocation(connectorX + horizontal, connectorY + vertical);
                    abstractNode.setBounds(abstractNode.getX(), abstractNode.getY(), abstractNode.getWidth() + horizontal, abstractNode.getHeight() + vertical);
                }
                break;
            case SW:
                if (width - horizontal > minSize && height + vertical > minSize) {
                    connector.setLocation(connectorX + horizontal, connectorY + vertical);
                    abstractNode.setBounds(abstractNode.getX() + horizontal, abstractNode.getY(), abstractNode.getWidth() - horizontal, abstractNode.getHeight() + vertical);
                }
                break;
            case WNW:
            case W:
            case WSW:
                if (width - horizontal > minSize) {
                    connector.setLocation(connectorX + horizontal, connectorY);
                    abstractNode.setBounds(abstractNode.getX() + horizontal, abstractNode.getY(), abstractNode.getWidth() - horizontal, abstractNode.getHeight());
                }
                break;
            default:
        }
        abstractNode.setResizing(true);
        abstractNode.resize();
    }
}