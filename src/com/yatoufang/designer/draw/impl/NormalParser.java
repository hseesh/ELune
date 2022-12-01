package com.yatoufang.designer.draw.impl;

import com.yatoufang.config.MindMapConfig;
import com.yatoufang.designer.component.Crayons;
import com.yatoufang.designer.component.Canvas;
import com.yatoufang.designer.draw.AbstractLayoutParser;
import com.yatoufang.designer.draw.LayoutType;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.model.StrokeType;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/3/13 0013
 */
public class NormalParser extends AbstractLayoutParser {
    /**
     * get element layout style type
     *
     * @return {@link LayoutType}
     */
    @Override
    public LayoutType getType() {
        return LayoutType.NONE;
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
        onCreate(node);
    }

    /**
     * layout node
     *
     * @param parent parent node
     * @param node   current node
     */
    @Override
    public void onLayout(Element parent, Element node) {
        int offset = MindMapConfig.distance;
        Rectangle parentBounds = parent.getBounds();
        for (Element child : parent.children) {
            Rectangle bounds = child.getBounds();
            offset += (child.children.size() + 1) * (bounds.height + MindMapConfig.distance);
        }
        node.setBounds(parentBounds.x + MindMapConfig.distance, parentBounds.y + offset, parentBounds.width, parentBounds.height);
    }

    /**
     * draw component
     *
     * @param graphics Graphics2D object
     * @param element  node element
     */
    @Override
    public void onDraw(Graphics2D graphics, Element element) {
        Rectangle bounds = element.getBounds();
        int x = (int) bounds.getX() - 5;
        int y = (int) bounds.getY() - 5;
        int width = (int) bounds.getWidth() + 10;
        int height = (int) bounds.getHeight() + 10;
        Crayons.setStroke(2f, StrokeType.SOLID);
        Crayons.drawRect(x, y, width, height, element.getBorderColor(), element.getFillColor());
        Shape shape = Canvas.makeShape(element);
        Crayons.draw(shape, MindMapConfig.elementBorderColor, MindMapConfig.rootBackgroundColor);
        Point point = Canvas.calcBestPosition(element.text, element.font, bounds);
        Crayons.drawString(element.text, point.x, point.y, MindMapConfig.rootTextColor);
    }
}
