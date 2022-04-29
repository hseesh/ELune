package com.yatoufang.test.style.impl;

import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.draw.AbstractLayoutParser;
import com.yatoufang.test.draw.LayoutContext;
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
    public void create(Element element) {
        NodeConfig config = StyleContext.getConfig(NodeType.INIT_NODE.getID());
        for (Integer child : config.getChildren()) {
            parser(StyleContext.getConfig(child), element);
        }
    }

    /**
     * crate a element
     *
     * @param element element node
     */
    @Override
    public void onCreate(Element element) {
        AbstractLayoutParser parser = LayoutContext.getParser(element.layoutType);
        parser.onCreate(element);
    }

    @Override
    public NodeType getType() {
        return NodeType.TOP_ROOT;
    }

    private void parser(NodeConfig config, Element element) {
        if (config == null) {
            return;
        }
        Element node = Canvas.createElement(element,config.getName(), LayoutType.getType(config.getLayoutStyle()));
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
        for (Integer child : config.getChildren()) {
            parser(StyleContext.getConfig(child), node);
        }
    }

}
