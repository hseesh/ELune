package com.yatoufang.test.model;

import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.component.Crayons;
import com.yatoufang.test.component.FuzzyArea;
import com.yatoufang.test.component.TextArea;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public abstract class AbstractElement {

    protected Color fillColor;
    protected Color textColor;
    protected Color borderColor;

    protected Topic topic;

    protected TextArea textArea;

    private FuzzyArea fuzzyArea;

    protected Rectangle2D bounds = new Rectangle2D.Double();
    protected Dimension2D blockSize = new Dimension();

    public AbstractElement(String text) {
        textColor = MindMapConfig.rootTextColor;
        borderColor = MindMapConfig.rootBackgroundColor;

        textArea = new TextArea(text);

        double width = 1.0d;
        width += textArea.getBounds().getWidth();


        bounds.setRect(0d, 0d, width,1);
    }

    public String getText() {
        return this.topic.getText();
    }

    public void setText(String text) {
        this.topic.setText(text);
        this.textArea.updateText(text);
    }

    public abstract void drawComponent(Crayons crayons, boolean drawCollaborator);

    public abstract void drwLinkLine(Crayons crayons, Rectangle2D source, Rectangle2D destination);

    public abstract boolean movable();

    public abstract boolean isCollapsed();

    public abstract Color getBackgroundColor();


    public abstract Color getTextColor();

    public abstract boolean hasDirection();

    public abstract Dimension2D calcBlockSize(Dimension2D size, boolean childrenOnly);


    public JTextComponent fillByTextAndFont(JTextComponent compo) {
        textArea.fillByTextAndFont(compo);
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

    public void moveWholeTreeBranchCoordinates(double deltaX, double deltaY) {
        moveTo(this.bounds.getX() + deltaX, this.bounds.getY() + deltaY);
        for (Topic topic : this.topic.getChildren()) {
            AbstractElement element = topic.getElement();
            if (element != null) {
                element.moveWholeTreeBranchCoordinates(deltaX, deltaY);
            }
        }
    }


    public void pain(Crayons crayons, boolean drawCollaborator) {


    }

    public void drawLinkLine(Crayons crayons) {
        Rectangle2D source = this.bounds;
        for (Topic topic : this.topic.getChildren()) {
            drwLinkLine(crayons, source, topic.getElement().getBounds());
        }
    }

    private AbstractElement findNearestTopic(AbstractElement elementToIgnore, double maxDistance, Point point) {
        AbstractElement result = null;
        if (elementToIgnore != this) {
            double dist = calcPointDistance(point);
            if (dist < maxDistance) {
                maxDistance = dist;
                result = this;
            }
        }
        if (!this.isCollapsed()) {
            for (Topic topic : this.topic.getChildren()) {
                AbstractElement element = topic.getElement();
                if (element != null) {
                    AbstractElement nearestChild = element.findNearestTopic(elementToIgnore, maxDistance, point);
                    if (nearestChild != null) {
                        maxDistance = nearestChild.calcPointDistance(point);
                        result = nearestChild;
                    }
                }
            }
        }
        return result;
    }

    public AbstractElement findTopicBlockForPoint(Point point) {
        AbstractElement result = null;
        if (point != null) {
            double px = point.getX();
            double py = point.getY();
            if (px >= calcBlockX() && py >= calcBlockY() && px < this.bounds.getX() + this.blockSize.getWidth() && py < this.bounds.getY() + this.blockSize.getHeight()) {
                if (this.isCollapsed()) {
                    result = this;
                } else {
                    AbstractElement foundChild = null;
                    for (Topic topic : this.topic.getChildren()) {
                        if (topic.getElement() != null) {
                            foundChild = topic.getElement().findTopicBlockForPoint(point);
                            if (foundChild != null) {
                                break;
                            }
                        }
                    }
                    result = foundChild == null ? this : foundChild;
                }
            }
        }
        return result;
    }


    public AbstractElement find(Point point) {
        if (point != null) {
            if (this.bounds.contains(point)) {
                return this;
            } else {
                for (Topic topic : this.topic.getChildren()) {
                    if (topic.getElement() == null) {
                        break;
                    }
                    topic.getElement().find(point);
                }
            }
        }
        return null;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }


    public boolean hasChildren() {
        return topic.getChildren().size() == 0;
    }


    public Topic getTopic() {
        return topic;
    }

    protected double calcBlockY() {
        return this.bounds.getY() - (this.blockSize.getHeight() - this.bounds.getHeight()) / 2;
    }

    protected double calcBlockX() {
        return this.bounds.getX();
    }

}
