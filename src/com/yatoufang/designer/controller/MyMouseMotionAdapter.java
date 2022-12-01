package com.yatoufang.designer.controller;


import com.yatoufang.designer.event.EventService;
import com.yatoufang.designer.event.EventType;

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
        EventService.post(e, EventType.MOUSE_DRAG);
    }

}
