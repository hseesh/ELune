package com.yatoufang.test.draw.impl;

import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutType;
import com.yatoufang.test.model.Element;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/11
 */
public class RightTimeParser extends AbstractLayoutParser {


    /**
     * get element layout style type
     *
     * @return {@link LayoutType}
     */
    @Override
    public LayoutType getType() {
        return LayoutType.RIGHT_TIME;
    }

    /**
     * calc max bounds for element(include all children)
     *
     * @param node node element
     * @return Point.x -- max width Point.y -- max height
     */
    @Override
    public void onMeasure(Element parent, Element node) {
        int offset = MindMapConfig.distance;
        for (Element child : parent.children) {
            Rectangle bounds = child.getBounds();
            offset += bounds.width + MindMapConfig.distance;
        }
        node.setBounds(parent.bounds.x + offset, parent.bounds.y, parent.bounds.width, parent.bounds.height);
    }

    /**
     * calc proper position for element
     *
     * @param element node element
     */
    @Override
    public void onLayout(Element element) {
        System.out.println("onLayout");
    }

    /**
     * draw component
     *
     * @param graphics Graphics2D object
     * @param element  node element
     */
    @Override
    public void onDraw(Graphics2D graphics, Element element) {
        System.out.println("onDraw");
    }
}
