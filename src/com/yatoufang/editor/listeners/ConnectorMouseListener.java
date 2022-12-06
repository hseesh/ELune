package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.Connector;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ConnectorMouseListener implements MouseListener {
    private final Connector connector;

    public ConnectorMouseListener(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        connector.getAbstractNode().getModel().manageConnection(connector);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        connector.getAbstractNode().getModel().checkSourceType(connector);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        connector.setShowErrorFlag(false);
        connector.setMouseEntered(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        connector.requestFocus();
        connector.setStartPoint(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        connector.getAbstractNode().setResizing(false);
        connector.getAbstractNode().resize();
    }
}