package com.yatoufang.test.controller;

import com.yatoufang.test.event.EventService;
import com.yatoufang.test.event.EventType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author GongHuang（hse）
 * @since 2021/12/30
 */
public class MyMouseMotionAdapter extends MouseMotionAdapter {

    /**
     * {@inheritDoc}
     *
     * @param e
     * @since 1.6
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("e = " + e.getPoint());
        EventService.post(e,EventType.MOUSE_DRAG);
    }

}
