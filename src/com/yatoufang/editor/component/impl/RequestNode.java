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
public class RequestNode extends BaseNode {

    private static final long serialVersionUID  = NodeType.REQUEST_NODE.getId();

    private final transient Connector outPutConnector;

    public RequestNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.REQUEST_NODE);
        outPutConnector = new Connector(this, Position.E, SourceType.OUT_PUT);
        addConnector(outPutConnector);
        addListeners();
    }

    @Override
    public String getKey() {
        return ProjectKeys.REQUEST;
    }

    @Override
    public void sync(String name,String alias) {
        if (name == null || name.length() == 0) {
            return;
        }
        getNodeData().setName(name + NativeWords.REQUEST);
        getNodeData().setAlias(StringUtil.toUpper(alias, ProjectKeys.REQUEST));
        refreshBounds();
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.REQUEST_NODE;
    }


    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.REQUEST_START.getColor(), getWidth(), getHeight(), ColorBox.REQUEST.getColor(), true);
    }

    public Connector getOutPutConnector() {
        return outPutConnector;
    }
}
