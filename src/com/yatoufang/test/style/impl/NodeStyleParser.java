package com.yatoufang.test.style.impl;

import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.draw.LayoutType;
import com.yatoufang.test.model.ColorBox;
import com.yatoufang.test.model.Element;
import com.yatoufang.test.model.entity.NodeConfig;
import com.yatoufang.test.style.AbstractStyleParser;
import com.yatoufang.test.style.NodeType;
import com.yatoufang.test.style.StyleContext;

/**
 * @author GongHuang（hse）
 * @since 2022/4/9 0009
 */
public class NodeStyleParser extends AbstractStyleParser {

    @Override
    public void onCreate(Element element) {
        NodeConfig config = StyleContext.getConfig(element.type.getID());
        parser(config, element);
    }

    @Override
    public NodeType getType() {
        return NodeType.NONE;
    }

    private void parser(NodeConfig config, Element element) {
        if (config == null) {
            return;
        }
        Element node = Canvas.createElement(element);
        if (config.getName() != null && !config.getName().isEmpty()) {
            node.text = config.getName();
        }
        if (config.getLinkLineColor() != null && !config.getLinkLineColor().isEmpty()) {
            node.linkLineColor = ColorBox.getColor(config.getLinkLineColor());
        }
        if (config.getBorderColor() != null && !config.getBorderColor().isEmpty()) {
            node.borderColor = ColorBox.getColor(config.getBorderColor());
        }
        node.type = NodeType.getType(config.getType());
        node.layoutType = LayoutType.getType(config.getLayoutStyle());
        node.icon = config.getIcon();
        node.scaleCoefficient = config.getScaleCoefficient();
        element.add(node);
        for (Integer child : config.getChildren()) {
            parser(StyleContext.getConfig(child), node);
        }
    }
}
