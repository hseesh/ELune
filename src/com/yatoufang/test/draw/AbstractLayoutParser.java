package com.yatoufang.test.draw;


import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.component.Crayons;
import com.yatoufang.test.model.Element;
import com.yatoufang.test.model.StrokeType;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/11
 */
public abstract class AbstractLayoutParser implements LayoutParser{

    protected  AbstractLayoutParser(){
        LayoutContext.register(getType(),this);
    }
    /**
     *  get element layout style type
     * @return {@link LayoutType}
     */
    public abstract LayoutType getType();


    @Override
    public void onMeasure(Element node) {
        Canvas.setElementBounds(node);
    }

    public void parser(Graphics2D graphics, Element element){
        if(element.parent != null){
            onMeasure(element);
            onLayout(element.parent, element);
        }

        for (int i = element.children.size() - 1; i >= 0; i--) {
            Element child = element.children.get(i);
            drwLinkLine(element.getBounds(), child.getBounds());
            child.draw(graphics);
        }
        onDraw(graphics, element);
    }


    public void drwLinkLine(Rectangle source, Rectangle destination) {
        Crayons.setStroke(4f, StrokeType.SOLID);

        final double startX;
        if (destination.getCenterX() < source.getCenterX()) {
            // left
            startX = source.getCenterX() - source.getWidth() / 4;
        } else {
            // right
            startX = source.getCenterX() + source.getWidth() / 4;
        }
        Point jumpPoint = new Point((int) source.getCenterX(), (int) destination.getCenterY());
        Crayons.drawLine((int) source.getCenterX(), (int) source.getCenterY(), jumpPoint.x, jumpPoint.y, MindMapConfig.linkLineColor);
        Crayons.drawLine( jumpPoint.x, jumpPoint.y,(int) destination.getCenterX(), (int) destination.getCenterY(), MindMapConfig.linkLineColor);
        // Crayons.drawCurve(startX, source.getCenterY(), destination.getCenterX(), destination.getCenterY(), MindMapConfig.linkLineColor);
    }
}
