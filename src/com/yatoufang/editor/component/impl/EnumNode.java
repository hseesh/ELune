package com.yatoufang.editor.component.impl;

import com.yatoufang.editor.Model;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.templet.ProjectKeys;

import java.awt.*;
import java.util.Collection;
import java.util.List;

/**
 * @author hse
 * @since 2023/3/6
 */
public class EnumNode extends BaseNode {

    private static final long serialVersionUID  = NodeType.ENUM_CONFIG.getId();

    public static final Collection<String> AUTO_FILTER = List.of(ProjectKeys.ID, "NONE");

    public EnumNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.ENUM_CONFIG);
    }


    @Override
    public void onInitialize() {
        nodeData.getMetaData().getPramList().removeIf(e -> AUTO_FILTER.contains(e.getName()));
    }

    @Override
    public void refresh(String content) {
        nodeData.refresh(content);
        nodeData.setAlias(nodeData.getMetaData().getName());
        nodeData.setName(nodeData.getMetaData().getDescription() + Enum.class.getSimpleName());
        nodeData.getMetaData().getPramList().removeIf(e -> AUTO_FILTER.contains(e.getName()));
        refreshBounds();
    }

    @Override
    public String getKey() {
        return ProjectKeys.ENUM;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ENUM_CONFIG;
    }

    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.ENUM_START.getColor(), getWidth(), getHeight(), ColorBox.ENUM.getColor(), true);
    }

}
