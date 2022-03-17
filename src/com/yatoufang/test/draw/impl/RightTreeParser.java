package com.yatoufang.test.draw.impl;

import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutType;
import com.yatoufang.test.model.Element;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/3/13 0013
 */
public class RightTreeParser extends AbstractLayoutParser {
    /**
     * get element layout style type
     *
     * @return {@link LayoutType}
     */
    @Override
    public LayoutType getType() {
        return LayoutType.RIGHT_TREE;
    }

    /**
     * calc max bounds for element(include all children)
     *
     * @param parent
     * @param node   node element
     */
    @Override
    public void onMeasure(Element parent, Element node) {
        int offset = MindMapConfig.distance;
        Rectangle parentBounds = parent.getBounds();
        for (Element child : parent.children) {
            Rectangle bounds = child.getBounds();
            offset += (child.children.size() + 1) * (bounds.height + MindMapConfig.distance);
        }
        node.setBounds(parentBounds.x + MindMapConfig.distance, parentBounds.y + offset, parentBounds.width, parentBounds.height);
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
