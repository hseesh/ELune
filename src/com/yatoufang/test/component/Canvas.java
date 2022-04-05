package com.yatoufang.test.component;

import com.intellij.ui.JBColor;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutContext;
import com.yatoufang.test.event.EditorContext;
import com.yatoufang.test.model.Element;
import com.yatoufang.utils.StringUtil;
import icons.Icon;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class Canvas {

    private static final AffineTransform AFFINE_TRANSFORM = new AffineTransform();

    private static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(AFFINE_TRANSFORM, true, true);

    public static Point calcBestPosition(String text, Font font, Rectangle2D bounds) {
        int width = (int) font.getStringBounds(text, FONT_RENDER_CONTEXT).getWidth();
        int height = (int) font.getStringBounds(text, FONT_RENDER_CONTEXT).getHeight();
        double x = (bounds.getWidth() - width) / 2;
        double y = bounds.getHeight() - (int) (height / 2);
        return new Point((int) (bounds.getX() + x), (int) (bounds.getY() + y));
    }

    public static void setTextBounds(Element element) {
        int width = (int) element.font.getStringBounds(element.text, FONT_RENDER_CONTEXT).getWidth();
        int height = (int) element.font.getStringBounds(element.text, FONT_RENDER_CONTEXT).getHeight();
        Rectangle bounds = element.getBounds();
        if (width > bounds.width * 2 / 3) {
            element.setBounds(bounds.x, bounds.y, bounds.width + 25, bounds.height);
            EditorContext.setTextAreaBounds(element.getBounds());
        }
    }

    public static void drawBankGround(Graphics brush) {
        Rectangle clipBounds = brush.getClipBounds();

        brush.setColor(new JBColor(new Color(0x617B94), new Color(0x617B94)));
        brush.drawRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);

        final double scaledGridStep = 32;

        final float minX = clipBounds.x;
        final float minY = clipBounds.y;
        final float maxX = clipBounds.x + clipBounds.width;
        final float maxY = clipBounds.y + clipBounds.height;

        brush.setColor(JBColor.BLACK);

        for (float x = 0.0f; x < maxX; x += scaledGridStep) {
            if (x < minX) {
                continue;
            }
            final int x1 = Math.round(x);
            brush.drawLine(x1, (int) minY, x1, (int) maxY);
        }

        for (float y = 0.0f; y < maxY; y += scaledGridStep) {
            if (y < minY) {
                continue;
            }
            final int y1 = Math.round(y);
            brush.drawLine((int) minX, y1, (int) maxX, y1);
        }
    }

    public static JPopupMenu createMenu() {
        JPopupMenu menu = new JPopupMenu();
        String[] items = {"Preview current", "Preview all", "item2", "item3", "item4"};
        for (String item : items) {
            JMenuItem menuItem = new JMenuItem(item, Icon.PUSH);
            menuItem.addActionListener(e -> {
                JMenuItem source = (JMenuItem) e.getSource();
                System.out.println("e = " + source.getText());
                EditorContext.updateUI();
            });
            menu.add(menuItem);
            menu.addSeparator();
        }
        return menu;
    }

    public static Element createElement(Element superNode) {
        Element node = new Element(StringUtil.EMPTY, superNode);
        AbstractLayoutParser parser = LayoutContext.getParser(superNode.layoutType);
        parser.onMeasure(superNode, node);
        return node;
    }

    public static Shape makeShape(Element element, float x, float y) {
        Rectangle bounds = element.getBounds();
        final float round = 2.0f;
        return new RoundRectangle2D.Double(bounds.x + x, bounds.y + y, bounds.getWidth(), bounds.getHeight(), round, round);
    }

    public static Shape makeShape(Element element) {
        Rectangle bounds = element.getBounds();
        final float round = 2.0f;
        return new RoundRectangle2D.Double(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight(), round, round);
    }

    public static void calcPrepareLine(Element source, Element target) {
        int index = 1;
    }

    public static Rectangle calcTopLeftPoint(Point point, Rectangle bounds) {
        int newX = point.x - bounds.width / 2;
        int newY = point.y - bounds.height / 2;
        return new Rectangle(newX, newY, bounds.width, bounds.height);
    }
}
        

