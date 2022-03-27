package com.yatoufang.test.model;

import com.google.common.collect.Lists;
import com.intellij.ui.JBColor;
import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.component.Crayons;
import com.yatoufang.test.controller.Drawable;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutContext;
import com.yatoufang.test.draw.LayoutType;
import com.yatoufang.test.event.Context;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.Objects;

/**
 * @author GongHuang（hse）
 * @since 2021/12/25 0025
 */
public class Element implements Drawable {
    public Element parent;
    private Rectangle bounds = new Rectangle();

    public List<Element> children = Lists.newArrayList();

    public String text;

    public Font font;

    public ElementType type;
    public LayoutType layoutType;

    protected Color fillColor;
    protected Color textColor;
    protected Color borderColor;

    public Element(String text, Element element) {
        this.text = text;
        this.parent = element;
        this.layoutType = LayoutType.RIGHT_TREE;
        this.font = new Font(null, Font.PLAIN, 25);
        borderColor = MindMapConfig.elementBorderColor;
    }

    public void add(Element element) {
        this.children.add(element);
    }

    @Override
    public void draw(Graphics2D g) {
        AbstractLayoutParser parser = LayoutContext.getParser(this.layoutType);
        Crayons.brush = g;
        for (int i = children.size() - 1; i >= 0; i--) {
            drwLinkLine(this.bounds, children.get(i).getBounds());
            children.get(i).draw(g);
        }
        parser.onDraw(g, this);
    }


    public void setBounds(int x, int y, int x1, int y1) {
        this.bounds.setFrame(x, y, x1, y1);
    }


    public void drawComponent() {
        int x = (int) this.bounds.getX() - 5;
        int y = (int) this.bounds.getY() - 5;
        int width = (int) this.bounds.getWidth() + 10;
        int height = (int) this.bounds.getHeight() + 10;
        Crayons.setStroke(2f, StrokeType.SOLID);
        Crayons.drawRect(x,y,width,height,borderColor,fillColor);
        final Shape shape = Canvas.makeShape(this);
        Crayons.draw(shape, MindMapConfig.elementBorderColor, MindMapConfig.rootBackgroundColor);
        Point point = Canvas.calcBestPosition(text, font, this.bounds);
        Crayons.drawString(text, point.x, point.y, MindMapConfig.rootTextColor);
    }


    public void drwLinkLine(Rectangle2D source, Rectangle2D destination) {
        Crayons.setStroke(4f, StrokeType.SOLID);

        final double startX;
        if (destination.getCenterX() < source.getCenterX()) {
            // left
            startX = source.getCenterX() - source.getWidth() / 4;
        } else {
            // right
            startX = source.getCenterX() + source.getWidth() / 4;
        }
        Crayons.drawCurve(startX, source.getCenterY(), destination.getCenterX(), destination.getCenterY(), MindMapConfig.linkLineColor);
    }


    public void fillText(JTextComponent compo, Rectangle rectangle) {
        this.text = compo.getText();
        this.font = compo.getFont();
        this.bounds.setBounds(rectangle);
    }


    public Element find(Point point) {
        if (this.bounds.contains(point)) {
            return this;
        } else {
            for (Element child : this.children) {
                Element element = child.find(point);
                if (element != null) {
                    return element;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return bounds.equals(element.bounds) && text.equals(element.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bounds, text);
    }


    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }
}
