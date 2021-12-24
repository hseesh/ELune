package com.yatoufang.test.adapter;

import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.component.MindMapEditor;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class MindMapController extends MouseAdapter {

    private final MindMapEditor mapEditor;
    private final JPopupMenu menu;

    public MindMapController(MindMapEditor mapEditor) {
        this.mapEditor = mapEditor;
        menu = Canvas.createMenu();
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

    }

    /**
     * {@inheritDoc}
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.isPopupTrigger()){
            menu.show(mapEditor.getComponent(), e.getX(), e.getY());
        }
        menu.setEnabled(false);
        mapEditor.getPreferredFocusedComponent().repaint();
    }


    /**
     * {@inheritDoc}
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
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
