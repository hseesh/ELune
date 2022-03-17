package com.yatoufang.test.component;

import com.intellij.ui.JBColor;
import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutContext;
import com.yatoufang.test.event.Context;
import com.yatoufang.test.model.Element;
import com.yatoufang.utils.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

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
            System.out.println(" =- ");
            System.out.println(bounds);
            element.setBounds(bounds.x, bounds.y, bounds.width + 20, bounds.height);
            Context.setTextAreaBounds(element.getBounds());
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
        String[] items = {"item", "item1", "item2", "item3", "item4"};
        for (String item : items) {
            JMenuItem menuItem = new JMenuItem(item);
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

    public static void calcPrepareLine(Element source, Element target) {
        int index = 1;

    }
}
        

