package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.RootCanvas;
import com.yatoufang.editor.constant.CommandHelper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanelKeyListener implements KeyListener {
    private final RootCanvas rootCanvas;

    public RootPanelKeyListener(RootCanvas rootCanvas) {
        this.rootCanvas = rootCanvas;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CommandHelper.removeCommand(e);
        rootCanvas.processReleaseEvent(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        rootCanvas.processEvent(e);
    }
}