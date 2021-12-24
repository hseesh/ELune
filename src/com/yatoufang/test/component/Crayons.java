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

    private final Graphics2D brush;
    private StrokeType strokeType = StrokeType.SOLID;
    private float strokeWidth = 1.0f;

    public Crayons(Graphics2D graphics2D) {
        this.brush = graphics2D;
    }


    public void drawRect(final int x, final int y, final int width, final int height, Color border, Color fill) {
        if (fill != null) {
            this.brush.setColor(fill);
            this.brush.fillRect(x, y, width, height);
        }

        if (border != null) {
            this.brush.setColor(border);
            this.brush.drawRect(x, y, width, height);
        }
    }


    public Graphics2D getWrappedGraphics() {
        return this.brush;
    }


    public void setClip(final int x, final int y, final int w, final int h) {
        this.brush.setClip(x, y, w, h);
    }


    public void dispose() {
        this.brush.dispose();
    }

    public void translate(final double x, final double y) {
        this.brush.translate(x, y);
    }

    public Rectangle getClipBounds() {
        return this.brush.getClipBounds();
    }

    public void setStroke(float width, StrokeType type) {
        if (type != this.strokeType || Float.compare(this.strokeWidth, width) != 0) {
            this.strokeType = type;
            this.strokeWidth = width;

            final Stroke stroke;

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
            this.brush.setStroke(stroke);
        }
    }


    public void drawLine(int startX, int startY, int endX, int endY, Color color) {
        if (color != null) {
            this.brush.setColor(color);
            this.brush.drawLine(startX, startY, endX, endY);
        }
    }

    public void draw(Shape shape, Color border, Color fill) {
        if (fill != null) {
            this.brush.setColor(fill);
            this.brush.fill(shape);
        }

        if (border != null) {
            this.brush.setColor(border);
            this.brush.draw(shape);
        }
    }

    public void drawCurve(double startX, double startY, double endX, double endY, Color color) {
        final Path2D path = new Path2D.Double();
        path.moveTo(startX, startY);
        path.curveTo(startX, endY, startX, endY, endX, endY);
        if (color != null) {
            this.brush.setColor(color);
        }
        this.brush.draw(path);
    }

    public void drawOval(int x, int y, int w, int h, Color border, Color fill) {
        if (fill != null) {
            this.brush.setColor(fill);
            this.brush.fillOval(x, y, w, h);
        }

        if (border != null) {
            this.brush.setColor(border);
            this.brush.drawOval(x, y, w, h);
        }
    }


    public void drawImage(Image image, final int x, final int y) {
        if (image != null) {
            this.brush.drawImage(image, x, y, null);
        }
    }


    public float getFontMaxAscent() {
        return this.brush.getFontMetrics().getMaxAscent();
    }


    public Rectangle2D getStringBounds(String str) {
        return this.brush.getFont().getStringBounds(str, this.brush.getFontRenderContext());
    }

    public void setFont(Font font) {
        this.brush.setFont(font);
    }


    public void drawString(String text, final int x, final int y, Color color) {
        if (color != null && this.brush.getFont().getSize2D() > 1.0f) {
            this.brush.setColor(color);
            this.brush.drawString(text, x, y);
        }
    }
}
