package com.yatoufang.editor.component.impl;

import com.yatoufang.editor.Model;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.component.Connector;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.type.Position;
import com.yatoufang.editor.type.SourceType;
import com.yatoufang.templet.NativeWords;


import java.awt.*;

/**
 * @author hse
 * @since 2022/11/29 0029
 */
public class ConfigNode extends BaseNode {

    private final transient Connector connector;

    public ConfigNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.NORMAL_CONFIG);
        connector = new Connector(this, Position.SSW, SourceType.CAME_TYPE);
    }

    @Override
    public void refresh(String content) {
        nodeData.refresh(content);
        nodeData.setAlias(nodeData.getMetaData().getName());
        nodeData.setName(nodeData.getMetaData().getName() + NativeWords.CONFIG);
        refreshBounds();
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NORMAL_CONFIG;
    }

    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.CONFIG_START.getColor(), getWidth(), getHeight(), ColorBox.CONFIG.getColor(), true);
    }

    public Connector getConnector() {
        return connector;
    }
}
