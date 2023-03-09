package com.yatoufang.editor.component.impl;

import com.yatoufang.editor.Model;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.component.Connector;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.type.Position;
import com.yatoufang.editor.type.SourceType;
import com.yatoufang.templet.NativeWords;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

import java.awt.*;

/**
 * @author hse
 * @since 2022/11/29 0029
 */
public class ResponseNode extends BaseNode {

    private static final long serialVersionUID  = NodeType.RESPONSE_NODE.getId();

    private final transient Connector inPutConnector;

    public ResponseNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.RESPONSE_NODE);
        inPutConnector = new Connector(this, Position.W, SourceType.CAME_TYPE);
        addConnector(inPutConnector);
        addListeners();
    }

    @Override
    public String getKey() {
        return ProjectKeys.RESPONSE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RESPONSE_NODE;
    }

    @Override
    public void sync(String name, String alias) {
        if (name == null || name.length() == 0) {
            return;
        }
        getNodeData().setName(name + NativeWords.RESPONSE);
        getNodeData().setAlias(StringUtil.toUpper(alias, getKey()));
        refreshBounds();
    }

    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.RESPONSE_START.getColor(), getWidth(), getHeight(), ColorBox.RESPONSE.getColor(), true);
    }

    public Connector getInPutConnector() {
        return inPutConnector;
    }
}
