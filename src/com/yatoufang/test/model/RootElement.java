package com.yatoufang.test.model;

import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.component.Crayons;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/25 0025
 */
public class RootElement extends AbstractElement {
    public RootElement(String text) {
        super(text);
    }

    private final Dimension2D leftBlockSize = new Dimension();
    private final Dimension2D rightBlockSize = new Dimension();

    private Shape makeShape(final double x, final double y) {
        final float round = 1.0f;
        return new RoundRectangle2D.Double(x, y, this.bounds.getWidth(), this.bounds.getHeight(), round, round);
    }


    @Override
    public void drawComponent(Crayons crayons, boolean drawCollaborator) {
        crayons.setStroke(1f, StrokeType.SOLID);

        final Shape shape = makeShape(1f, 1f);

        if (MindMapConfig.dropShadow) {
            crayons.draw(makeShape(MindMapConfig.shadowOffset, MindMapConfig.shadowOffset), null, MindMapConfig.shadowColor);
        }

        crayons.draw(shape, MindMapConfig.elementBorderColor, MindMapConfig.rootBackgroundColor);

        textArea.paint(crayons, MindMapConfig.rootTextColor);

    }

    @Override
    public void drwLinkLine(Crayons crayons, Rectangle2D source, Rectangle2D destination) {
        crayons.setStroke(1f, StrokeType.SOLID);

        final double startX;
        if (destination.getCenterX() < source.getCenterX()) {
            // left
            startX = source.getCenterX() - source.getWidth() / 4;
        } else {
            // right
            startX = source.getCenterX() + source.getWidth() / 4;
        }

        crayons.drawCurve(startX, source.getCenterY(), destination.getCenterX(), destination.getCenterY(), MindMapConfig.linkLineColor);
    }

    @Override
    public boolean movable() {
        return false;
    }

    @Override
    public boolean isCollapsed() {
        return false;
    }

    @Override
    public Color getBackgroundColor() {
        return MindMapConfig.rootBackgroundColor;
    }

    @Override
    public Color getTextColor() {
        return  MindMapConfig.rootTextColor;
    }

    @Override
    public boolean hasDirection() {
        return false;
    }

    @Override
    public Dimension2D calcBlockSize(Dimension2D size, final boolean childrenOnly) {
        final double insetV = MindMapConfig.scale * MindMapConfig.firstLevelVerticalInset;
        final double insetH = MindMapConfig.scale * MindMapConfig.firstLevelHorizontalInset;

        double leftWidth = 0.0d;
        double leftHeight = 0.0d;
        double rightWidth = 0.0d;
        double rightHeight = 0.0d;

        boolean nonfirstOnRight = false;

        for (final Topic topic : this.topic.getChildren()) {
            AbstractElement element = topic.getElement();
            element.calcBlockSize(size, false);
            rightWidth = Math.max(rightWidth, size.getWidth());
            rightHeight += size.getHeight();
            if (nonfirstOnRight) {
                rightHeight += insetV;
            } else {
                nonfirstOnRight = true;
            }
        }

        if (!childrenOnly) {
            rightWidth += nonfirstOnRight ? insetH : 0.0d;
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

    @Override
    public void setBounds(int x, int y, int x1, int y1) {
        this.bounds.setRect(x,y,x1,y1);
    }


    public Dimension2D getLeftBlockSize() {
        return this.leftBlockSize;
    }


    public Dimension2D getRightBlockSize() {
        return this.rightBlockSize;
    }

}
