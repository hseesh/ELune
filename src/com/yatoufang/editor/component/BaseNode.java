package com.yatoufang.editor.component;


import com.intellij.ui.JBColor;
import com.yatoufang.editor.model.NodeData;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.Model;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.constant.GlobalConstant;
import com.yatoufang.entity.Param;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * @author hse
 * @since 2022/9/5 0005
 */
public class BaseNode extends AbstractNode implements NodeStyle {

    private static final long serialVersionUID = 123456L;


    public BaseNode(Model model, Point clickedPoint, NodeType nodeType) {
        super(new NodeData(nodeType), model);
        setBorder(new RoundBorder());
        setBackground(ColorBox.TRANSPARENT.getColor());
        int initWidth = GlobalConstant.NODE_WIDTH;
        int initHeight = GlobalConstant.NODE_HEIGHT;
        setBounds(clickedPoint.x - (initWidth / 2), clickedPoint.y - (initHeight / 2), initWidth, initHeight);
        addConnectors();
        addListeners();
    }

    private BaseNode(Model model, Rectangle bounds, NodeType nodeType) {
        this(model, bounds.getLocation(), nodeType);
        setBounds(bounds);
        setConnectorLocation();
    }

    @Override
    public Color getLineColor() {
        return ColorBox.LINE.getColor();
    }

    @Override
    public void sync(String name, String alias) {

    }

    @Override
    public BaseNode copy() {
        BaseNode baseNode = new BaseNode(getModel(), getBounds(), getNodeType());
        baseNode.setNodeData(getNodeData());
        return baseNode;
    }

    @Override
    public String getWorkPath() {
        NodeData data = getNodeData();
        String basePath = getModel().getBasePath();
        return StringUtil.buildPath(basePath, data.getWorkingSpace(), getModel().getModuleName(), getKey(), data.getName() + ProjectKeys.JAVA);
    }

    @Override
    public void doCommand(KeyEvent e) {
        getModel().getCanvas().processEvent(e);
    }

    @Override
    public Paint getPaint() {
        return null;
    }


    @Override
    public NodeType getNodeType() {
        return NodeType.NONE;
    }

    @Override
    public String getTemplatePath() {
        return null;
    }

    @Override
    public String getKey() {
        return ProjectKeys.PROJECT;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int offsetY = 50;

        g.setColor(ColorBox.BOX_BORDER.getColor());
        g.drawLine(0, offsetY, getWidth(), offsetY);

        Paint p = getPaint();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(p);
        g2d.fillRoundRect(0, 0, getWidth(), offsetY, 30, 30);

        int offset = GlobalConstant.SELECTED_OBJECT_BORDER_OFFSET;
        g2d.setColor(JBColor.BLUE);
        g2d.setStroke(GlobalConstant.STROKE_THREE);
        g2d.drawLine(-offset, 0, -offset, -offset);
        g2d.drawLine(-offset, -offset, 0, -offset);

        g2d.drawLine(getWidth(), getHeight() - offset, getWidth() + offset, -offset);
        g2d.drawLine(getWidth() + offset, getHeight() - offset, getWidth() + offset, offset);

        g2d.drawLine(offset + getWidth(), getHeight() - offset, offset + getWidth(), getHeight() + offset);
        g2d.drawLine(offset + getWidth(), getHeight() + offset, getWidth() - offset, getHeight() + offset);

        g2d.drawLine(offset, getHeight() + offset, -offset, getHeight() + offset);
        g2d.drawLine(-offset, getHeight() + offset, -offset, getHeight() - offset);

        g.setColor(ColorBox.NODE_DARK_BANK_GROUND.getColor());
        g.fillRoundRect(0, offsetY, getWidth(), getHeight() - offsetY, 30, 30);
        g.drawRect(0, offsetY, 30, 30);

        offsetY = 20;
        g.setColor(JBColor.BLACK);
        g.drawString("<<" + nodeData.getName() + ">>", (getWidth() / 2) - (g.getFontMetrics().stringWidth("<<" + nodeData.getName() + ">>") / 2), offsetY);
        if (!ProjectKeys.GAME_SERVER.equals(nodeData.getWorkingSpace())) {
            g.setFont(GlobalConstant.FONT_MIN);
            g.drawChars(nodeData.getWorkingSpace().toCharArray(), 0, 1, 10, offsetY);
        }
        g.setFont(g.getFont().deriveFont((Font.BOLD), 14));
        offsetY = 40;
        g.drawString(nodeData.getAlias(), 10, offsetY);

        g.setColor(ColorBox.BOX_BORDER.getColor());
        g.drawLine(0, 50, getWidth(), 50);
        offsetY += 10;

        g2d.setStroke(GlobalConstant.STROKE_THREE);
        g2d.setColor(ColorBox.NODE_PENCIL.getColor());
        for (Param param : nodeData.getMetaData().pramList) {
            g2d.drawString(param.getPrimaryInfo(), 15, offsetY + 30);
            offsetY += 25;
        }
        if (isResizing()) {
            g.setColor(ColorBox.BOX_RESIZING_BACKGROUND.getColor());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(JBColor.BLACK);
            g.setFont(g.getFont().deriveFont((Font.BOLD), 14));
            String dim = getWidth() + " * " + getHeight();
            g.drawString(dim, (getWidth() / 2) - (g.getFontMetrics().stringWidth(dim) / 2), getHeight() / 2 + 20);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseNode)) return false;
        BaseNode baseNode = (BaseNode) o;
        return nodeData.equals(baseNode.nodeData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeData);
    }

}