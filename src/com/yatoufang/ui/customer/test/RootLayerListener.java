package com.yatoufang.ui.customer.test;

import com.yatoufang.test.event.EventService;
import com.yatoufang.test.event.EventType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author GongHuang（hse）
 * @since 2021/12/30
 */
public class RootLayerListener extends MouseAdapter {

    /**
     * {@inheritDoc}
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        EventService.post(e, EventType.MOUSE_CLICK);
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     * @since 1.6
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        EventService.post(e,EventType.MOUSE_DRAG);
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     * @since 1.6
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        EventService.post(e,EventType.MOUSE_MOVE);
    }

}
