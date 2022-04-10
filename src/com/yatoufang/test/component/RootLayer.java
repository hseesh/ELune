package com.yatoufang.test.component;

import com.yatoufang.test.controller.Drawable;
import com.yatoufang.test.draw.LayoutType;
import com.yatoufang.test.event.EditorContext;
import com.yatoufang.test.model.Element;
import com.yatoufang.test.style.AbstractStyleParser;
import com.yatoufang.test.style.NodeType;
import com.yatoufang.test.style.StyleContext;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/30
 */
public class RootLayer extends JComponent {

    public Element topic;

    Drawable paint;

    public RootLayer() {
        setSize(800, 600);
        topic = new Element("EMPTY NODE", null);
        topic.setBounds(100, 100, 70, 40);
        topic.layoutType = LayoutType.RIGHT_TIME;
        topic.type = NodeType.NONE;
        EditorContext.textArea.setBounds(100, 100, 70, 40);
        EditorContext.textArea.setEnabled(true);
        AbstractStyleParser parser = StyleContext.getParser(NodeType.NONE);
        parser.onCreate(topic);
        paint = topic;
    }


    public void create(Drawable drawable) {
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
            Graphics2D brush = (Graphics2D) graphics;
            Canvas.drawBankGround(brush);
            brush.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paint.draw(brush);
        }
    }


}
