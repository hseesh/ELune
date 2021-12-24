package com.yatoufang.test.model;

import java.awt.geom.Rectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class Line {
    public final Rectangle2D bounds;
    public final String line;

    public Line(String line, Rectangle2D bounds) {
        this.bounds = bounds;
        this.line = line;
    }
}
