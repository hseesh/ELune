package com.yatoufang.test.component;

import com.yatoufang.test.model.StrokeType;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class Crayons {

    public static Graphics2D brush;
    private static StrokeType strokeType = StrokeType.SOLID;
    private static float strokeWidth = 1.0f;


    public static void drawRect(int x, int y, int width, int height, Color border, Color fill) {
        if (fill != null) {
            brush.setColor(fill);
            brush.fillRect(x, y, width, height);
        }

        if (border != null) {
            brush.setColor(border);
            brush.drawRect(x, y, width, height);
        }
    }


    public Graphics2D getWrappedGraphics() {
        return brush;
    }


    public static void setClip(int x, int y, int w, int h) {
        brush.setClip(x, y, w, h);
    }


    public static void dispose() {
        brush.dispose();
    }

    public static void translate(double x, double y) {
        brush.translate(x, y);
    }

    public Rectangle getClipBounds() {
        return brush.getClipBounds();
    }

    public static void setStroke(float width, StrokeType type) {
        if (type != strokeType || Float.compare(strokeWidth, width) != 0) {
            strokeType = type;
            strokeWidth = width;

            Stroke stroke;

            switch (type) {
                case SOLID:
                    stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
                    break;
                case DASHES:
                    stroke = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{width * 3.0f, width}, 0.0f);
                    break;
                case DOTS:
                    stroke = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{width, width * 2.0f}, 0.0f);
                    break;
                default:
                    throw new Error("Unexpected stroke type : " + type);
            }
            brush.setStroke(stroke);
        }
    }


    public static void drawLine(int startX, int startY, int endX, int endY, Color color) {
        if (color != null) {
            brush.setColor(color);
            brush.drawLine(startX, startY, endX, endY);
        }
    }

    public static void draw(Shape shape, Color border, Color fill) {
        if (fill != null) {
            brush.setColor(fill);
            brush.fill(shape);
        }

        if (border != null) {
            brush.setColor(border);
            brush.draw(shape);
        }
    }

    public static void drawCurve(double startX, double startY, double endX, double endY, Color color) {
        Path2D path = new Path2D.Double();
        path.moveTo(startX, startY);
        path.curveTo(startX, endY, startX, endY, endX, endY);
        if (color != null) {
            brush.setColor(color);
        }
        brush.draw(path);
    }

    public static void drawOval(int x, int y, int w, int h, Color border, Color fill) {
        if (fill != null) {
            brush.setColor(fill);
            brush.fillOval(x, y, w, h);
        }

        if (border != null) {
            brush.setColor(border);
            brush.drawOval(x, y, w, h);
        }
    }


    public static void drawImage(Image image, int x, int y) {
        if (image != null) {
            brush.drawImage(image, x, y, null);
        }
    }


    public float getFontMaxAscent() {
        return brush.getFontMetrics().getMaxAscent();
    }


    public Rectangle2D getStringBounds(String str) {
        return brush.getFont().getStringBounds(str, brush.getFontRenderContext());
    }

    public static void setFont(Font font) {
        brush.setFont(font);
    }


    public static void drawString(String text, int x, int y, Color color) {
        brush.setColor(color);
        brush.drawString(text, x, y);
    }

    public static void drawString(String text, double x, double y) {
        brush.drawString(text, (int) x, (int) y);
    }
}
