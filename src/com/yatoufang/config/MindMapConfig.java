package com.yatoufang.config;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author GongHuang（hse）
 * @since 2021/12/26 0026
 */
public class MindMapConfig {
    public static int collapsatorSize = 16;
    public static int textMargins = 10;
    public static int otherLevelVerticalInset = 16;
    public static int otherLevelHorizontalInset = 32;
    public static int firstLevelVerticalInset = 32;
    public static int firstLevelHorizontalInset = 48;
    public static int paperMargins = 20;
    public static int selectLineGap = 5;
    public static int horizontalBlockGap = 5;
    public static int scaleModifiers = KeyEvent.CTRL_MASK;
    public static boolean drawBackground = true;
    public static  Color paperColor = new Color(0x617B94);
    public static  Color gridColor = paperColor.darker();
    public static boolean showGrid = true;
    public static int gridStep = 32;
    public static  Color rootBackgroundColor = new Color(0x031A31);
    public static  Color firstLevelBackgroundColor = new Color(0xB1BFCC);
    public static  Color otherLevelBackgroundColor = new Color(0xFDFDFD);
    public static  Color rootTextColor = Color.WHITE;
    public static  Color firstLevelTextColor = Color.BLACK;
    public static  Color otherLevelTextColor = Color.BLACK;
    public static  Color elementBorderColor = Color.BLACK;
    public static  Color linkLineColor = Color.WHITE;
    public static  Color shadowColor = new Color(0x30000000, true);
    public static  Color collapsatorBorderColor = Color.DARK_GRAY;
    public static  Color collapsatorBackgroundColor = Color.WHITE;
    public static  Color selectLineColor = Color.ORANGE;
    public static  Color jumpLinkColor = Color.CYAN;
    public static float shadowOffset = 5.0f;
    public static float elementBorderWidth = 1.0f;
    public static float collapsatorBorderWidth = 1.0f;
    public static float connectorWidth = 1.5f;
    public static float selectLineWidth = 3.0f;
    public static float jumpLinkWidth = 1.5f;
    public static boolean smartTextPaste = false;
    public static  Font font = new Font(Font.SERIF, Font.BOLD, 18);
    public static  double scale = 1.0d;
    public static boolean dropShadow = true;

}
