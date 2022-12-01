package com.yatoufang.designer.style.impl.event;

import com.yatoufang.designer.event.EditorContext;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.style.AbstractNodeEventParser;
import com.yatoufang.designer.style.AbstractStyleParser;
import com.yatoufang.designer.style.NodeType;
import com.yatoufang.designer.style.StyleContext;

/**
 * @author GongHuang（hse）
 * @since 2022/5/15 0015
 */
public class DefaultEventParser extends AbstractNodeEventParser {

    @Override
    public NodeType getType() {
        return NodeType.NONE;
    }

    @Override
    public void preview() {
        Element current = EditorContext.current;
        if (!EditorContext.textArea.hasFocus()) {
            return;
        }
        Element parent = current.parent;
        AbstractStyleParser parser = StyleContext.getParser(parent.type);
        if (!(parser instanceof AbstractNodeEventParser)) {
            return;
        }
        AbstractNodeEventParser eventParser = (AbstractNodeEventParser) parser;
        if(!eventParser.isSuperNode()){
            return;
        }
        eventParser.onPreview(current);
    }

    @Override
    public void onPreview(Element node) {

    }

    @Override
    public void onExecute() {

    }

    @Override
    public boolean isSuperNode() {
        return false;
    }
}
