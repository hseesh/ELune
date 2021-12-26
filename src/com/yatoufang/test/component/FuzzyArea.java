package com.yatoufang.test.component;

import com.yatoufang.test.model.Topic;

import java.awt.geom.Rectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class FuzzyArea {

    private final Rectangle2D bounds = new Rectangle2D.Double();
    private Topic model;
    private boolean contentPresented;

    public Rectangle2D getBounds() {
        return bounds;
    }

    public Topic getModel() {
        return model;
    }

    public void setModel(Topic model) {
        this.model = model;
    }

    public boolean isContentPresented() {
        return contentPresented;
    }

    public void setContentPresented(boolean contentPresented) {
        this.contentPresented = contentPresented;
    }
}
