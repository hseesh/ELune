package com.yatoufang.designer.style.impl;
import com.yatoufang.designer.component.Canvas;
import com.yatoufang.designer.draw.AbstractLayoutParser;
import com.yatoufang.designer.draw.LayoutContext;
import com.yatoufang.designer.draw.LayoutType;
import com.yatoufang.designer.model.ColorBox;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.model.entity.NodeConfig;
import com.yatoufang.designer.style.AbstractStyleParser;
import com.yatoufang.designer.style.NodeType;
import com.yatoufang.designer.style.StyleContext;

/**
 * @author GongHuang（hse）
 * @since 2022/4/9 0009
 */
public class NodeLayoutStyleParser extends AbstractStyleParser {

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
