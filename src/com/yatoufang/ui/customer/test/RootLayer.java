package com.yatoufang.ui.customer.test;

import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.controller.Drawable;
import com.yatoufang.test.event.Context;
import com.yatoufang.test.model.Element;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/30
 */
public class RootLayer extends JLayeredPane {

    public Element topic;

    Drawable paint;

    public RootLayer() {
        System.out.println("init ");
        setSize(800, 600);
        topic = new Element("EMPTY NODE",null);
        topic.setBounds(100,100,70,40);
        Context.textArea.setBounds(100,100,70,40);
        Context.textArea.setEnabled(true);
        paint = topic;
    }


    public void create(Drawable drawable){
        this.paint = drawable;
        repaint();
    }
    /**
     * Paints each of the components in this container.
     *
     * @param graphics the graphics context.
     * @see Component#paint
     * @see Component#paintAll
     */
    @Override
    public void paintComponent(Graphics graphics) {
        if (paint != null) {
            Graphics2D brush=(Graphics2D)graphics;
            Canvas.drawBankGround(brush);
            brush.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paint.draw(brush);
        }
    }
}
