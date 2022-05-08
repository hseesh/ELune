package com.yatoufang.test.model;

import com.google.common.collect.Lists;
import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.controller.Drawable;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutContext;
import com.yatoufang.test.draw.LayoutType;
import com.yatoufang.test.style.NodeType;

import javax.swing.text.JTextComponent;
import java.awt.*;
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

    public NodeType type;
    public LayoutType layoutType;

    protected Color fillColor;
    public Color borderColor;
    public Color linkLineColor;

    public float scaleCoefficient;
    public String icon;


    public Element(String text, Element element) {
        this.text = text;
        this.parent = element;
        if(this.parent != null){
            this.parent.add(this);
        }
        this.layoutType = LayoutType.RIGHT_TREE;
        this.font = new Font(null, Font.PLAIN, 25);
        this.bounds.setBounds(0,0,70,40);
        borderColor = MindMapConfig.elementBorderColor;
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
    public void draw(Graphics2D g) {
        AbstractLayoutParser parser = LayoutContext.getParser(this.layoutType);
        parser.parser(g, this);
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

    public void add(Element element) {
        this.children.add(element);
    }

    public void delete(Element element) {
        if (element.parent == null) {
            return;
        }
        element.parent.children.remove(element);
    }


    public void fillText(JTextComponent compo, Rectangle rectangle) {
        this.text = compo.getText();
        this.font = compo.getFont();
        this.bounds.setBounds(rectangle);
    }


    public void setBounds(int x, int y, int x1, int y1) {
        this.bounds.setFrame(x, y, x1, y1);
    }

    public void setLocation(int x, int y){
        this.bounds.setLocation(x,y);
    }


    public void addOffset(int x1, int x, int y){
        if(this.bounds.x <= x1){
            return;
        }
        Point location = this.bounds.getLocation();
        this.bounds.setLocation(location.x + x,location.y + y);
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

    public int getSelfWidth() {
        return getBounds().width  + MindMapConfig.fix_width;
    }

    public int getWidthBetweenParent(){
        return getBounds().width  + MindMapConfig.element_width;
    }

    public int getSelfHeight() {
        return getBounds().height + MindMapConfig.element_height;
    }
}
