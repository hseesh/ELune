package com.yatoufang.test.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.controller.Drawable;
import com.yatoufang.test.component.Crayons;
import com.yatoufang.test.component.FuzzyArea;
import com.yatoufang.test.component.TextArea;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public abstract class AbstractElement implements Drawable {

    public List<AbstractElement> children = Lists.newArrayList();

    protected Color fillColor;
    protected Color textColor;
    protected Color borderColor;

    protected TextArea textArea;

    private FuzzyArea fuzzyArea;

    protected Rectangle bounds = new Rectangle();
    protected Dimension2D blockSize = new Dimension();

    public AbstractElement(String text) {
        textColor = MindMapConfig.rootTextColor;
        borderColor = MindMapConfig.rootBackgroundColor;

        textArea = new TextArea(text);

        double width = 1.0d;
        width += textArea.getBounds().getWidth();


        bounds.setRect(0d, 0d, width, 1);
    }

    public String getText() {
        return this.textArea.getText();
    }

    public void setText(String text) {
        this.textArea.setText(text);
        this.textArea.updateText(text);
    }

    public abstract void setBounds(int x, int y, int x1, int y1);

    public abstract void drawComponent();

    public abstract void drwLinkLine(Rectangle2D source, Rectangle2D destination);

    public abstract boolean movable();

    public abstract boolean isCollapsed();

    public abstract Color getBackgroundColor();


    public abstract Color getTextColor();

    public abstract boolean hasDirection();

    public abstract Dimension2D calcBlockSize(Dimension2D size, boolean childrenOnly);

    public abstract void add(RootElement rootElement);


    public JTextComponent fillText(JTextComponent compo) {
        textArea.fillText(compo);
        return compo;
    }

    public double calcPointDistance(Point point) {
        double d1 = point.distance(this.bounds.getX(), this.bounds.getY());
        double d2 = point.distance(this.bounds.getMaxX(), this.bounds.getY());
        double d3 = point.distance(this.bounds.getX(), this.bounds.getMaxY());
        double d4 = point.distance(this.bounds.getMaxX(), this.bounds.getMaxY());
        return (d1 + d2 + d3 + d4) / (this.bounds.contains(point) ? 8.0d : 4.0d);
    }

    public TextAlign getTextAlign() {
        return this.textArea.getTextAlign();
    }


    public Dimension2D getBlockSize() {
        return this.blockSize;
    }

    public void moveTo(double x, double y) {
        this.bounds.setFrame(x, y, this.bounds.getWidth(), this.bounds.getHeight());
    }


    public void pain(boolean drawCollaborator) {

    }

    public void drawLinkLine() {
        Rectangle2D source = this.bounds;

    }

    public AbstractElement find(Point point) {
        if (this.bounds.contains(point)) {
            return this;
        } else {
            for (AbstractElement child : this.children) {
                AbstractElement element = child.find(point);
                if(element != null){
                    return element;
                }
            }
        }
        return null;
    }


    protected double calcBlockY() {
        return this.bounds.getY() - (this.blockSize.getHeight() - this.bounds.getHeight()) / 2;
    }

    protected double calcBlockX() {
        return this.bounds.getX();
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
