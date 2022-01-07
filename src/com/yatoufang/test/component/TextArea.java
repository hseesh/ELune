package com.yatoufang.test.component;

import com.yatoufang.test.model.TextAlign;
import com.yatoufang.test.model.Line;
import com.yatoufang.utils.EditorUtil;
import com.yatoufang.utils.StringUtil;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class TextArea {

    private static final Rectangle2D ZERO = new Rectangle2D.Double();
    private Rectangle2D bounds = new Rectangle2D.Double();
    private String text;
    private Line[] lines;
    private Font font;
    private double maxLineAscent;
    private TextAlign textAlign;

    public TextArea(String text) {
        this.text = text;
        font = new Font(null, Font.PLAIN,14);
    }

    public TextArea(String text, TextAlign textAlign) {
        this.text = text;
        this.textAlign = textAlign;

    }

    public Rectangle2D getBounds() {
        return bounds;
    }


    public TextAlign getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign) {
        textAlign = textAlign == null ? TextAlign.CENTER : textAlign;
        invalidate();
    }

    public void invalidate() {
        lines = null;
    }

    public void setOffset(final double x, final double y) {
        bounds.setFrame(x, y, bounds.getWidth(), bounds.getHeight());
    }

    public void updateSize(Crayons crayons) {
        Font font = new Font(Font.SERIF, Font.BOLD, 18);
        font = font.deriveFont(AffineTransform.getScaleInstance(1, 1));
        Crayons.setFont(font);

        maxLineAscent = crayons.getFontMaxAscent();

        double maxWidth = 0.0d;
        double maxHeight = 0.0d;

        final String[] brokenText = EditorUtil.breakToLines(text);

        lines = new Line[brokenText.length];

        int index = 0;
        for (final String s : brokenText) {
            final Rectangle2D lineBounds = crayons.getStringBounds(s);
            maxWidth = Math.max(lineBounds.getWidth(), maxWidth);
            maxHeight += lineBounds.getHeight();
            lines[index++] = new Line(s, lineBounds);
        }
        bounds.setRect(0.0d, 0.0d, maxWidth, maxHeight);
    }


    public void paint(Color color) {
        Crayons.setFont(this.font);
        Point point = Canvas.calcBestPosition(text, font, bounds);
        Crayons.drawString(text, point.x, point.y, color);
    }

    public String getText() {
        return text;
    }

    public void updateText(String text) {
        this.text = text == null ? StringUtil.EMPTY : text;
        invalidate();
    }

    public void fillText(JTextComponent component) {
        this.bounds = component.getBounds();
        this.text = component.getText();
        this.font = component.getFont();
    }

    public void setText(String text) {
        this.text = text;
    }
}
