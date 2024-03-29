package com.yatoufang.designer.draw;

import com.yatoufang.config.MindMapConfig;
import com.yatoufang.designer.component.Crayons;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.model.StrokeType;
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


    public void parser(Graphics2D graphics, Element element){
        for (int i = element.children.size() - 1; i >= 0; i--) {
            drwLinkLine(element.getBounds(), element.children.get(i).getBounds());
            element.children.get(i).draw(graphics);
        }
        onDraw(graphics, element);
    }

    public void drwLinkLine(Rectangle source, Rectangle destination) {
        Crayons.setStroke(4f, StrokeType.SOLID);
        Point jumpPoint = new Point((int) source.getCenterX(), (int) destination.getCenterY());
        Crayons.drawLine((int) source.getCenterX(), (int) source.getCenterY(), jumpPoint.x, jumpPoint.y, MindMapConfig.linkLineColor);
        Crayons.drawLine( jumpPoint.x, jumpPoint.y,(int) destination.getCenterX(), (int) destination.getCenterY(), MindMapConfig.linkLineColor);
    }
}
