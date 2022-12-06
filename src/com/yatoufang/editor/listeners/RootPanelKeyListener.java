package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.Canvas;
import com.yatoufang.editor.constant.CommandHelper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanelKeyListener implements KeyListener {
    private final Canvas canvas;

    public RootPanelKeyListener(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CommandHelper.removeCommand(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        canvas.processEvent(e);
    }
}