package com.yatoufang.ui.customer.test;

import com.yatoufang.test.model.RootElement;
import com.yatoufang.utils.StringUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author GongHuang（hse）
 * @since 2021/12/30
 */
public class RootLayerListener extends MouseAdapter {

    private final RootLayer rootLayer;
    private final JTextArea textArea;

    public RootLayerListener(RootLayer rootLayer, JTextArea textArea) {
        this.rootLayer = rootLayer;
        this.textArea = textArea;
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        RootElement rootElement = new RootElement(StringUtil.EMPTY);
        switch (e.getButton()) {
            case 3:
                rootElement.setBounds(e.getX(), e.getY(), 70, 40);
                textArea.setBounds(e.getX(), e.getY(), 70, 40);
                textArea.setVisible(true);
                textArea.setEnabled(true);
                textArea.requestFocus();
                break;
            case 2:
                break;
            default:
                textArea.setVisible(false);
                textArea.setEnabled(false);
                break;
        }
       // rootLayer.repaint();
        rootElement.drawComponent(rootLayer.brush, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     * @since 1.6
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     * @since 1.6
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }
}
