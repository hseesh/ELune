package com.yatoufang.test.component;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class Canvas {

    public static void drawBankGround(Graphics2D brush) {
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
        String[] items = {"item","item1","item2","item3","item4"};
        for (String item : items) {
            JMenuItem menuItem = new JMenuItem(item);
            menu.add(menuItem);
            menu.addSeparator();
        }
        return menu;
    }
}
        

