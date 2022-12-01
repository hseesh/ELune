package com.yatoufang.designer.component;

import java.awt.geom.Rectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class FuzzyArea {

    private final Rectangle2D bounds = new Rectangle2D.Double();

    private boolean contentPresented;

    public Rectangle2D getBounds() {
        return bounds;
    }

    public boolean isContentPresented() {
        return contentPresented;
    }

    public void setContentPresented(boolean contentPresented) {
        this.contentPresented = contentPresented;
    }
}
