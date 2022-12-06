package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.Connector;
import com.yatoufang.editor.constant.CommandHelper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class ConnectorListener implements KeyListener {
    private final Connector connector;

    public ConnectorListener(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        connector.getAbstractNode().doCommand(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CommandHelper.removeCommand(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}