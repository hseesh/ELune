package com.yatoufang.test.model;

import com.yatoufang.test.component.FuzzyArea;
import com.yatoufang.test.component.TextArea;
import org.eclipse.jdt.internal.compiler.ast.TextBlock;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public abstract  class AbstractElement {

    protected Color fillColor;
    protected Color textColor;
    protected Color borderColor;

    protected Topic model;

    protected TextArea textArea;

    private FuzzyArea fuzzyArea;

    protected final Rectangle2D bounds = new Rectangle2D.Double();
    protected final Dimension2D blockSize = new Dimension();

    public String getText() {
        return this.model.getText();
    }

    public void setText( String text) {
        this.model.setText(text);
        this.textArea.updateText(text);
    }


    public double calcPointDistance(Point point) {
        final double d1 = point.distance(this.bounds.getX(), this.bounds.getY());
        final double d2 = point.distance(this.bounds.getMaxX(), this.bounds.getY());
        final double d3 = point.distance(this.bounds.getX(), this.bounds.getMaxY());
        final double d4 = point.distance(this.bounds.getMaxX(), this.bounds.getMaxY());
        return (d1 + d2 + d3 + d4) / (this.bounds.contains(point) ? 8.0d : 4.0d);
    }

}
