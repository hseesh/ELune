package com.yatoufang.editor.listeners;

import com.yatoufang.editor.component.RootCanvas;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanelMouseWheelListener implements MouseWheelListener {
    private final RootCanvas rootCanvas;

    public RootPanelMouseWheelListener(RootCanvas rootCanvas) {
        this.rootCanvas = rootCanvas;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.isControlDown()) {
            if(e.getWheelRotation() < 0) {
                rootCanvas.getRootPanel().getZoomPanel().zoomIn();
            }else if(e.getWheelRotation() > 0) {
                rootCanvas.getRootPanel().getZoomPanel().zoomOut();
            }
            rootCanvas.getRootPanel().getSideBar().updateZoomLabel(rootCanvas);
        }
    }
}