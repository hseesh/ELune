package com.yatoufang.test.model;

import com.google.common.collect.Lists;
import com.intellij.ui.JBColor;
import com.intellij.util.IconUtil;
import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.component.Crayons;
import com.yatoufang.test.controller.Drawable;
import com.yatoufang.test.event.Context;
import icons.Icon;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2021/12/25 0025
 */
public class Element implements Drawable {
    public Element parent;
    public Rectangle bounds = new Rectangle();

    public List<Element> children = Lists.newArrayList();

    public String text;

    public Font font;

    public ElementType type;

    protected Color fillColor;
    protected Color textColor;
    protected Color borderColor;

    public Element(String text,Element element) {
        this.text = text;
        this.parent = element;
        this.font =new Font(null, Font.PLAIN, 25);
        borderColor = MindMapConfig.elementBorderColor;
    }

    private final Dimension2D leftBlockSize = new Dimension();
    private final Dimension2D rightBlockSize = new Dimension();

    private Shape makeShape() {
        final float round = 2.0f;
        return new RoundRectangle2D.Double(bounds.x, bounds.y, this.bounds.getWidth(), this.bounds.getHeight(), round, round);
    }

    private Shape makeShape(float x, float y) {
        final float round = 2.0f;
        return new RoundRectangle2D.Double(bounds.x + x, bounds.y + y, this.bounds.getWidth(), this.bounds.getHeight(), round, round);
    }


    public boolean movable() {
        return false;
    }


    public boolean isCollapsed() {
        return false;
    }


    public Color getBackgroundColor() {
        return MindMapConfig.rootBackgroundColor;
    }


    public Color getTextColor() {
        return MindMapConfig.rootTextColor;
    }


    public boolean hasDirection() {
        return false;
    }


    public Dimension2D calcBlockSize(Dimension2D size, final boolean childrenOnly) {
        final double insetV = MindMapConfig.scale * MindMapConfig.firstLevelVerticalInset;
        final double insetH = MindMapConfig.scale * MindMapConfig.firstLevelHorizontalInset;

        double leftWidth = 0.0d;
        double leftHeight = 0.0d;
        double rightWidth = 0.0d;
        double rightHeight = 0.0d;

        rightWidth = Math.max(rightWidth, size.getWidth());
        rightHeight += size.getHeight();

        if (!childrenOnly) {
            rightWidth += insetH;
        }

        this.leftBlockSize.setSize(leftWidth, leftHeight);
        this.rightBlockSize.setSize(rightWidth, rightHeight);

        if (childrenOnly) {
            size.setSize(leftWidth + rightWidth, Math.max(leftHeight, rightHeight));
        } else {
            size.setSize(leftWidth + rightWidth + this.bounds.getWidth(), Math.max(this.bounds.getHeight(), Math.max(leftHeight, rightHeight)));
        }

        return size;
    }


    public void add(Element element) {
        this.children.add(element);
    }

    @Override
    public void draw(Graphics2D g) {
        if (Context.current != null && Context.current.equals(this)) {
            g.setColor(JBColor.RED);
        } else {
            g.setColor(JBColor.BLUE);
        }
        Crayons.brush = g;
        for (Element child : children) {
            drwLinkLine(this.bounds, child.getBounds());
            child.draw(g);
        }
        drawComponent();
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
        final Shape shape = makeShape();
        Crayons.draw(shape, MindMapConfig.elementBorderColor, MindMapConfig.rootBackgroundColor);
        Point point = Canvas.calcBestPosition(text, font, this.bounds);
        Crayons.drawString(text, point.x, point.y, MindMapConfig.rootTextColor);
        Image image = IconUtil.toImage(Icon.ROBOT);
        Crayons.drawImage(image,300,300);
    }


    public void drwLinkLine(Rectangle2D source, Rectangle2D destination) {
        Crayons.setStroke(2f, StrokeType.SOLID);

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

    public Dimension2D getLeftBlockSize() {
        return this.leftBlockSize;
    }


    public Dimension2D getRightBlockSize() {
        return this.rightBlockSize;
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


    public Rectangle getBounds() {
        return bounds;
    }
}
