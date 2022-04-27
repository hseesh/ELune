package com.yatoufang.test.draw.impl;

import com.intellij.ui.JBColor;
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

    @Override
    public void onCreate(Element node) {
        Rectangle bounds = node.getBounds();
        int offset = MindMapConfig.element_width;
        for (Element child : node.children) {
            Dimension dimension = new Dimension();
            Canvas.getNodeWidth(child, dimension);
            offset += dimension.width;
            Rectangle selfBounds = child.getBounds();
            child.setBounds(bounds.x + offset, bounds.y, selfBounds.width, selfBounds.height);
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
    public void onMeasure(Element node) {
        EditorContext.reMeasure();
    }

    /**
     * layout node
     *
     * @param parent parent node
     * @param node   current node
     */
    @Override
    public void onLayout(Element parent, Element node) {
        int offset = MindMapConfig.element_width;
        Dimension dimension = new Dimension();
        Canvas.getNodeWidth(parent, dimension);
        Rectangle bounds = parent.getBounds();
        offset += dimension.width;
        node.setBounds(bounds.x + offset, bounds.y, bounds.width, bounds.height);
    }

    /**
     * draw component
     *
     * @param graphics Graphics2D object
     * @param element  node element
     */
    @Override
    public void onDraw(Graphics2D graphics, Element element) {
        graphics.setColor(JBColor.RED);
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
