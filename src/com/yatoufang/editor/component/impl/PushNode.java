package com.yatoufang.editor.component.impl;

import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.component.Connector;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.Model;
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
public class PushNode extends BaseNode {

    private final transient Connector inPutConnector;

    public PushNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.RUSH_NODE);
        inPutConnector = new Connector(this, Position.W, SourceType.CAME_TYPE);
        addConnector(inPutConnector);
        addListeners();
    }


    @Override
    public String getKey() {
        return ProjectKeys.RESPONSE;
    }

    @Override
    public Color getLineColor() {
        return ColorBox.LINE_PUSH.getColor();
    }

    @Override
    public void sync(String name,String alias) {
        if (name == null || name.length() == 0) {
            return;
        }
        getNodeData().setName(name + NativeWords.PUSH);
        getNodeData().setAlias(StringUtil.toUpper(alias, ProjectKeys.RESPONSE));
        refreshBounds();
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RUSH_NODE;
    }


    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.PUSH_START.getColor(), getWidth(), getHeight(), ColorBox.PUSH.getColor(), true);
    }

    public Connector getInPutConnector() {
        return inPutConnector;
    }
}
