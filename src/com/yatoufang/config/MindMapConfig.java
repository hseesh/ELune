package com.yatoufang.config;

import com.intellij.ui.JBColor;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author GongHuang（hse）
 * @since 2021/12/26 0026
 */
public class MindMapConfig {
    public static int textMargins = 10;
    public static int otherLevelVerticalInset = 16;
    public static int otherLevelHorizontalInset = 32;
    public static int firstLevelVerticalInset = 32;
    public static int firstLevelHorizontalInset = 48;
    public static int paperMargins = 20;
    public static int selectLineGap = 5;
    public static int horizontalBlockGap = 5;
    public static boolean drawBackground = true;
    public static  Color paperColor = new JBColor(new Color(0x617B94), JBColor.BLACK);
    public static  Color gridColor = paperColor.darker();
    public static boolean showGrid = true;
    public static int gridStep = 32;
    public static  Color rootBackgroundColor = new JBColor(new Color(0x031A31), JBColor.BLACK);
    public static  Color firstLevelBackgroundColor = new JBColor(new Color(0xB1BFCC), JBColor.BLACK);
    public static  Color otherLevelBackgroundColor = new JBColor(new Color(0xFDFDFD), JBColor.BLACK);
    public static  Color rootTextColor = JBColor.WHITE;
    public static  Color firstLevelTextColor = JBColor.BLACK;
    public static  Color otherLevelTextColor = JBColor.BLACK;
    public static  Color elementBorderColor = JBColor.BLACK;
    public static  Color linkLineColor = JBColor.PINK;
    public static  Color shadowColor = new JBColor(new Color(0x30000000, true), JBColor.BLACK);
    public static  Color selectLineColor = JBColor.ORANGE;
    public static  Color jumpLinkColor = JBColor.CYAN;
    public static float shadowOffset = 5.0f;
    public static float elementBorderWidth = 1.0f;
    public static float connectorWidth = 1.5f;
    public static float selectLineWidth = 3.0f;
    public static float jumpLinkWidth = 1.5f;
    public static boolean smartTextPaste = false;
    public static  Font font = new Font(Font.SERIF, Font.BOLD, 18);
    public static  double scale = 1.0d;
    public static boolean dropShadow = true;

    public static int distance = 120;

    public static int element_width = 120;

    public static int element_height = 40;



}
