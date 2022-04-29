package com.yatoufang.test.draw.impl;

import com.intellij.ui.JBColor;
import com.intellij.util.IconUtil;
import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.component.Crayons;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutContext;
import com.yatoufang.test.draw.LayoutType;
import com.yatoufang.test.event.EditorContext;
import com.yatoufang.test.model.Element;
import com.yatoufang.test.model.StrokeType;
import icons.Icon;

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
        Element rootElement = EditorContext.getRootElement();
        Rectangle rootBounds = rootElement.getBounds();
        int offset = MindMapConfig.distance * 2;
        for (Element child : node.children) {
            Dimension dimension = new Dimension();
            Canvas.getNodeWidth(child, dimension);
            Rectangle selfBounds = child.getBounds();
            child.setBounds(rootBounds.x + offset, rootBounds.y, selfBounds.width, selfBounds.height);
            AbstractLayoutParser parser = LayoutContext.getParser(child.layoutType);
            parser.onCreate(child);
            offset += dimension.width;
        }
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
        int offset = MindMapConfig.element_width * 2;
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
        Image image = IconUtil.toImage(Icon.EDIT);
        Crayons.drawImage(image, x - 20, y + 20);
    }
}
