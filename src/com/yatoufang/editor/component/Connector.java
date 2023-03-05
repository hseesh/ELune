package com.yatoufang.editor.component;


import com.intellij.ui.JBColor;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.constant.GlobalConstant;
import com.yatoufang.editor.listeners.ConnectorListener;
import com.yatoufang.editor.listeners.ConnectorMouseListener;
import com.yatoufang.editor.listeners.ConnectorMouseMotionListener;
import com.yatoufang.editor.type.Position;
import com.yatoufang.editor.type.SourceType;
import com.yatoufang.utils.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author hse
 * @since 2022/9/5 0005
 */
public class Connector extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Position position;
    private final Cursor resizeCursor;
    private final SourceType sourceType;
    private final AbstractNode abstractNode;
    private transient boolean mouseEntered = false;
    private transient boolean showErrorFlag = false;
    private Point startPoint = new Point(0, 0);

    public Connector(AbstractNode abstractNode, Position position, SourceType sourceType) {
        this.abstractNode = abstractNode;
        this.resizeCursor = position.getCursor();
        this.position = position;
        this.sourceType = sourceType == null ? SourceType.NONE : sourceType;

        setBackground(ColorBox.TRANSPARENT.getColor());

        final int initWidth = 17;
        final int initHeight = 17;

        setSize(initWidth, initHeight);
        setVisible(abstractNode.getModel().isConnectorsVisible());
        setLocation();
        setFocusable(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int offset = 2;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(GlobalConstant.STROKE_TWO);
        g2.setColor(ColorBox.CONNECTOR_DEFAULT.getColor());
        if (getSourceType() == SourceType.IN_PUT || getSourceType() == SourceType.OUT_PUT) {
            if (mouseEntered) {
                g2.setColor(JBColor.GREEN);
            } else {
                g2.setColor(JBColor.BLUE);
            }
        }
        if (showErrorFlag) {
            g2.setColor(JBColor.RED);
            g2.fillOval(0, 0, 14, 14);
        } else {
            g2.drawOval(0, 0, 14, 14);
        }
        g2.fillPolygon(new int[]{12, 19, 12}, new int[]{offset, offset + 5, offset + 10}, 3);
    }

    void addListeners() {
        addMouseMotionListener(new ConnectorMouseMotionListener(this));
        addMouseListener(new ConnectorMouseListener(this));
        addKeyListener(new ConnectorListener(this));
    }

    public AbstractNode getAbstractNode() {
        return abstractNode;
    }

    public Position getPosition() {
        return position;
    }

    public boolean is(Position position) {
        return this.position == position;
    }

    public boolean isOwnedBy(AbstractNode co) {
        return abstractNode.equals(co);
    }

    public void setCursor(MouseEvent e) {
        setCursor(e.isControlDown() ? resizeCursor : Cursor.getDefaultCursor());
    }

    public int getCenterX() {
        return getX() + getWidth() / 2;
    }

    public int getCenterY() {
        return getY() + getHeight() / 2;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setMouseEntered(boolean mouseEntered) {
        this.mouseEntered = mouseEntered;
    }

    public void setShowErrorFlag(boolean showErrorFlag) {
        this.showErrorFlag = showErrorFlag;
    }

    void setLocation() {
        final int intersectionFix = 1;
        switch (position) {
            case N:
                setLocation(abstractNode.getX() + (abstractNode.getWidth() / 2) - (getWidth() / 2), abstractNode.getY() - (getHeight() / 2) - intersectionFix);
                break;
            case NNE:
                setLocation(abstractNode.getX() + (abstractNode.getWidth() / 4) * 3 - (getWidth() / 2), abstractNode.getY() - (getHeight() / 2) - intersectionFix);
                break;
            case NE:
                setLocation(abstractNode.getX() + abstractNode.getWidth() - (getWidth() / 2) + intersectionFix, abstractNode.getY() - (getHeight() / 2) - intersectionFix);
                break;
            case ENE:
                setLocation(abstractNode.getX() + abstractNode.getWidth() - (getWidth() / 2) + intersectionFix, abstractNode.getY() + (abstractNode.getHeight() / 4) - (getWidth() / 2));
                break;
            case E:
                setLocation(abstractNode.getX() + abstractNode.getWidth() - (getWidth() / 2) + intersectionFix, abstractNode.getY() + (abstractNode.getHeight() / 2) - (getWidth() / 2));
                break;
            case ESE:
                setLocation(abstractNode.getX() + abstractNode.getWidth() - (getWidth() / 2) + intersectionFix, abstractNode.getY() + (abstractNode.getHeight() / 4) * 3 - (getWidth() / 2));
                break;
            case SE:
                setLocation(abstractNode.getX() + abstractNode.getWidth() - (getWidth() / 2) + intersectionFix, abstractNode.getY() + abstractNode.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case SSE:
                setLocation(abstractNode.getX() + (abstractNode.getWidth() / 4) * 3 - (getWidth() / 2), abstractNode.getY() + abstractNode.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case S:
                setLocation(abstractNode.getX() + (abstractNode.getWidth() / 2) - (getWidth() / 2), abstractNode.getY() + abstractNode.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case SSW:
                setLocation(abstractNode.getX() + (abstractNode.getWidth() / 4) - (getWidth() / 2), abstractNode.getY() + abstractNode.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case SW:
                setLocation(abstractNode.getX() - (getWidth() / 2), abstractNode.getY() + abstractNode.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case WSW:
                setLocation(abstractNode.getX() - (getWidth() / 2) - intersectionFix, abstractNode.getY() + (abstractNode.getHeight() / 4) * 3 - (getWidth() / 2));
                break;
            case W:
                setLocation(abstractNode.getX() - (getWidth() / 2) - intersectionFix, abstractNode.getY() + (abstractNode.getHeight() / 2) - (getWidth() / 2));
                break;
            case WNW:
                setLocation(abstractNode.getX() - (getWidth() / 2) - intersectionFix, abstractNode.getY() + (abstractNode.getHeight() / 4) - (getWidth() / 2));
                break;
            case NW:
                setLocation(abstractNode.getX() - (getWidth() / 2) - intersectionFix, abstractNode.getY() - (getHeight() / 2) - intersectionFix);
                break;
            case NNW:
                setLocation(abstractNode.getX() + (abstractNode.getWidth() / 4) - (getWidth() / 2), abstractNode.getY() - (getHeight() / 2) - intersectionFix);
                break;
        }
    }

    public boolean isCorner() {
        return position == Position.NE || position == Position.NW || position == Position.SE || position == Position.SW;
    }

    public static int[] getOffsets(Connector c) {
        return getOffsets(c, 50, 50);
    }

    public static int[] getOffsets(Connector c, int horizontal, int vertical) {
        switch (c.position) {
            case E:
            case ENE:
            case ESE:
                return new int[]{horizontal, 0};
            case N:
            case NNE:
            case NNW:
                return new int[]{0, -vertical};
            case S:
            case SSE:
            case SSW:
                return new int[]{0, vertical};
            case W:
            case WNW:
            case WSW:
                return new int[]{-horizontal, 0};
            case SE:
                return new int[]{horizontal, vertical};
            case SW:
                return new int[]{-horizontal, vertical};
            case NE:
                return new int[]{horizontal, -vertical};
            case NW:
                return new int[]{-horizontal, -vertical};
            default:
                return new int[]{0, 0};
        }
    }

    @Override
    public String toString() {
        return abstractNode.getNodeData().getName() + StringUtil.SPACE + sourceType;
    }
}