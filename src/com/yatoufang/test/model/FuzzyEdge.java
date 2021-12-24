package com.yatoufang.test.model;

import com.yatoufang.test.component.Crayons;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class FuzzyEdge {
    private Image image;
    private int x;
    private int y;
    private int width;
    private int height;


    boolean isVisible() {
        return image != null;
    }

    public boolean containsPoint(final int relativeX, final int relativeY) {
        return relativeX >= x && relativeY >= y && relativeX < x + this.width && relativeY < y + this.height;
    }

    public void draw(Crayons crayons, int offsetX, int offsetY) {
        if (isVisible()) {
            crayons.drawImage(image, x + offsetX, y + offsetY);
        }
    }
}
