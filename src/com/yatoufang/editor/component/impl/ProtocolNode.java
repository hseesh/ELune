package com.yatoufang.editor.component.impl;

import com.yatoufang.editor.Model;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.component.Connector;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.type.Position;
import com.yatoufang.editor.type.SourceType;
import com.yatoufang.templet.ProjectKeys;

import java.awt.*;

/**
 * @author hse
 * @since 2022/11/29 0029
 */
public class ProtocolNode extends BaseNode {

    private static final long serialVersionUID  = NodeType.PROTOCOL_NODE.getId();

    private transient Connector inPutConnector;
    private final transient Connector[] outPutConnector = new Connector[2];

    public ProtocolNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.PROTOCOL_NODE);
        initConnectors();
    }

    @Override
    public String getKey() {
        return ProjectKeys.FACADE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.PROTOCOL_NODE;
    }

    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.PROTOCOL_START.getColor(), getWidth(), getHeight(), ColorBox.PROTOCOL.getColor(), true);
    }

    public void initConnectors() {
        inPutConnector = new Connector(this, Position.W, SourceType.IN_PUT);
        outPutConnector[0] = new Connector(this, Position.E, SourceType.OUT_PUT);
        outPutConnector[1] = new Connector(this, Position.ESE, SourceType.OUT_PUT);
        addConnector(inPutConnector);
        addConnector(outPutConnector[0]);
        addConnector(outPutConnector[1]);
        addListeners();
    }


    public Connector getInPutConnector() {
        return inPutConnector;
    }


    public Connector[] getOutPutConnector() {
        return outPutConnector;
    }
}
