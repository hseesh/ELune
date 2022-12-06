package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.constant.CommandHelper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class NodeKeyListener implements KeyListener {
    private final AbstractNode abstractNode;

    public NodeKeyListener(AbstractNode AbstractNode) {
        this.abstractNode = AbstractNode;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        abstractNode.doCommand(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CommandHelper.removeCommand(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}