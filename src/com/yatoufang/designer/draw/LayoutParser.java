package com.yatoufang.designer.draw;


import com.yatoufang.designer.model.Element;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/11
 */
public interface LayoutParser {

    /**
     *  create node
     * @param node new node
     */
    void onCreate(Element node);

    /**
     * calc max bounds for element(include all children)
     *
     * @param node node element
     */
    void onMeasure(Element node);

    /**
     *  layout node
     * @param parent parent node
     * @param node current node
     */
    void onLayout(Element parent,Element node);

    /**
     * draw node
     *
     * @param graphics Graphics2D object
     * @param element  node element
     */
    void onDraw(Graphics2D graphics, Element element);
}
