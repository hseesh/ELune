package com.yatoufang.test.draw;

import com.yatoufang.test.model.Element;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/11
 */
public interface LayoutParser {

    /**
     * calc max bounds for element(include all children)
     *
     * @param element node element
     * @return Point.x -- max width Point.y -- max height
     */
    Rectangle onMeasure(Element element);

    /**
     * calc proper position for element
     *
     * @param element node element
     */
    void onLayout(Element element);

    /**
     * draw component
     *
     * @param graphics Graphics2D object
     * @param element  node element
     */
    void onDraw(Graphics2D graphics, Element element);
}
