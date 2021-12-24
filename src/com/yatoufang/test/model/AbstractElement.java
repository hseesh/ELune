package com.yatoufang.test.model;

import com.yatoufang.test.component.FuzzyArea;
import org.eclipse.jdt.internal.compiler.ast.TextBlock;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class AbstractElement {

    protected Color fillColor;
    protected Color textColor;
    protected Color borderColor;

    protected Topic model;

    protected TextBlock textBlock;

    private FuzzyArea fuzzyArea;

    protected final Rectangle2D bounds = new Rectangle2D.Double();
    protected final Dimension2D blockSize = new Dimension();


}
