package com.yatoufang.editor.component.impl;

import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.Model;
import com.yatoufang.templet.ProjectKeys;

import java.awt.*;

/**
 * @author hse
 * @since 2022/11/29 0029
 */
public class EntityNode extends BaseNode {

    private static final long serialVersionUID  = NodeType.ENTITY_NODE.getId();

    public EntityNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.ENTITY_NODE);
    }

    @Override
    public String getKey() {
        return ProjectKeys.MODEL;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ENTITY_NODE;
    }

    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.ENTITY_START.getColor(), getWidth(), getHeight(), ColorBox.ENTITY.getColor(), true);
    }
}
