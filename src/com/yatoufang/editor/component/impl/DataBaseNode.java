package com.yatoufang.editor.component.impl;

import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.Model;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

import java.awt.*;

/**
 * @author hse
 * @since 2022/11/29 0029
 */
public class DataBaseNode extends BaseNode {

    public DataBaseNode(Model model, Point clickedPoint) {
        super(model, clickedPoint, NodeType.DATA_BASE);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.DATA_BASE;
    }

    @Override
    public void refresh(String content) {
        nodeData.refresh(content);
        refreshBounds();
        nodeData.setName(nodeData.getMetaData().getName());
        getModel().setModuleName(StringUtil.toLowerCaseForFirstChar(nodeData.getMetaData().getName()));
    }

    @Override
    public Paint getPaint() {
        return new GradientPaint(0.0f, 0.0f, ColorBox.DATABASE_START.getColor(), getWidth(), getHeight(), ColorBox.DATABASE.getColor(), true);
    }
}
