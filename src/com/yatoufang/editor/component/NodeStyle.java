package com.yatoufang.editor.component;

import com.yatoufang.editor.type.NodeType;

import java.awt.*;

/**
 * @author hse
 * @since 2022/11/29 0029
 */
public interface NodeStyle {

    String getKey();

    Paint getPaint();

    NodeType getNodeType();

    String getTemplatePath();

}
