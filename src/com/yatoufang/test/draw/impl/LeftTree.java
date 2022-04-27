package com.yatoufang.test.draw.impl;

import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutType;
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

    @Override
    public void onCreate(Element node) {

    }

    /**
     * calc max bounds for element(include all children)
     *
     * @param node node element
     */
    @Override
    public void onMeasure(Element node) {

    }

    /**
     * layout node
     *
     * @param parent parent node
     * @param node   current node
     */
    @Override
    public void onLayout(Element parent, Element node) {

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
