package com.yatoufang.test.draw.impl;

import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.component.Crayons;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutContext;
import com.yatoufang.test.draw.LayoutType;
import com.yatoufang.test.event.EditorContext;
import com.yatoufang.test.model.Element;
import com.yatoufang.test.model.StrokeType;

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

    @Override
    public void onCreate(Element node) {
        Rectangle bounds = node.getBounds();
        int offset = MindMapConfig.VERTICAL_OFFSET;
        for (Element child : node.children) {
            Dimension dimension = new Dimension();
            Canvas.getNodeHeight(child, dimension);
            Rectangle selfBounds = child.getBounds();
            child.setBounds(bounds.x + MindMapConfig.element_height, bounds.y + offset, selfBounds.width, selfBounds.height);
            offset += dimension.height + MindMapConfig.VERTICAL_OFFSET;
            AbstractLayoutParser parser = LayoutContext.getParser(child.layoutType);
            parser.onCreate(child);
        }
    }

    /**
     * calc max bounds for element(include all children)
     *
     * @param node node element
     */
    @Override
    public void  onMeasure(Element node) {
        Rectangle bounds = node.getBounds();
        EditorContext.addHorizontalOffset(bounds.x,node.getSelfWidth());
    }

    /**
     * layout node
     *
     * @param parent parent node
     * @param node   current node
     */
    @Override
    public void onLayout(Element parent, Element node) {
        Rectangle parentBounds = parent.getBounds();
        Dimension dimension = new Dimension(MindMapConfig.distance,MindMapConfig.distance);
        Canvas.getNodeHeight(parent,dimension);
        node.setBounds(parentBounds.x + MindMapConfig.distance, parentBounds.y + dimension.height, parentBounds.width, parentBounds.height);
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
