package com.yatoufang.editor.component;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.border.Border;
import java.awt.*;

/**
 * @author hse
 * @since 2022/09/1 0001
 */
public class RoundBorder implements Border {
    private final Color color;

    public RoundBorder(Color color) {// 有参数的构造方法
        this.color = color;
    }

    public RoundBorder() {
        this.color = JBColor.BLACK;
    }

    public Insets getBorderInsets(Component c) {
        return JBUI.emptyInsets();
    }

    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 30, 30);
    }
}

