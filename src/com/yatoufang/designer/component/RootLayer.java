package com.yatoufang.designer.component;
import com.yatoufang.designer.controller.Drawable;
import com.yatoufang.designer.draw.AbstractLayoutParser;
import com.yatoufang.designer.draw.LayoutContext;
import com.yatoufang.designer.draw.LayoutType;
import com.yatoufang.designer.event.EditorContext;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.style.AbstractStyleParser;
import com.yatoufang.designer.style.NodeType;
import com.yatoufang.designer.style.StyleContext;

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
        System.out.println("init RootLayer");
        setSize(800, 600);
        topic = new Element("EMPTY NODE", null);
        topic.setBounds(100, 100, 70, 40);
        topic.layoutType = LayoutType.RIGHT_TIME;
        topic.type = NodeType.TOP_ROOT;
        EditorContext.textArea.setBounds(100, 100, 70, 40);
        EditorContext.textArea.setEnabled(true);
        paint = topic;
    }

    public void init() {
        System.out.println("init panel");
        if (topic.children.size() > 0) {
            return;
        }
        AbstractStyleParser parser = StyleContext.getParser(topic.type);
        if (parser != null) {
            parser.create(topic);
            parser.onCreate(topic);
        }
    }

    public void reMeasure() {
        AbstractLayoutParser parser = LayoutContext.getParser(topic.layoutType);
        parser.onCreate(topic);
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
            Crayons.brush = brush;
            Canvas.drawBankGround(brush);
            brush.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paint.draw(brush);
        }
    }

}
