package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.Canvas;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanelMouseWheelListener implements MouseWheelListener {
    private final Canvas canvas;

    public RootPanelMouseWheelListener(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.isControlDown()) {
            if(e.getWheelRotation() < 0) {
                canvas.getRootPanel().getZoomPanel().zoomIn();
            }else if(e.getWheelRotation() > 0) {
                canvas.getRootPanel().getZoomPanel().zoomOut();
            }
            canvas.getRootPanel().getSideBar().updateZoomLabel(canvas);
        }
    }
}