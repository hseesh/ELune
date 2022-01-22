package com.yatoufang.test.draw;

import com.yatoufang.test.model.Element;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/11
 */
public class LeftTree extends AbstractLayoutParser {
    /**
     * get element layout style type
     *
     * @return {@link LayoutType}
     */
    @Override
    public LayoutType getType() {
        return LayoutType.LEFT_TREE;
    }

    /**
     * calc max bounds for element(include all children)
     *
     * @param element node element
     * @return Point.x -- max width Point.y -- max height
     */
    @Override
    public Rectangle onMeasure(Element element) {
        return null;
    }

    /**
     * calc proper position for element
     *
     * @param element node element
     */
    @Override
    public void onLayout(Element element) {

    }

    /**
     * draw component
     *
     * @param graphics Graphics2D object
     * @param element  node element
     */
    @Override
    public void onDraw(Graphics2D graphics, Element element) {

    }
}
