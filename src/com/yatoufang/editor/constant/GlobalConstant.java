
package com.yatoufang.editor.constant;

import java.awt.*;

/**
 * @author hse
 * @since 2022/10/8 0008
 */
public class GlobalConstant {


    public static final int WIDTH = 5000;
    public static final int HEIGHT = 5000;

    public static final float[] DOTTED_EXAMPLE = {2f, 0f, 2f};

    public static final BasicStroke STROKE_MIN = new BasicStroke(0.2f);
    public static final BasicStroke STROKE_ONE = new BasicStroke(1.0f);
    public static final BasicStroke STROKE_TWO = new BasicStroke(2.0f);

    public static final BasicStroke STROKE_TWO_DOTTED = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 2f, DOTTED_EXAMPLE, 2f);
    public static final BasicStroke STROKE_THREE = new BasicStroke(3.0f);

    public static final Font FONT = new Font(null, Font.BOLD, 14);

    public static final Font FONT_MIN = new Font(null, Font.BOLD, 10);
    public static final int SELECTED_OBJECT_BORDER_OFFSET = 40;

    public static final int NODE_WIDTH = 220;

    public static final int NODE_HEIGHT = 120;


}
