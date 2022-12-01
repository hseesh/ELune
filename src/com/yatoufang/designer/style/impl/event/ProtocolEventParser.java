package com.yatoufang.designer.style.impl.event;

import com.yatoufang.designer.event.EditorContext;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.style.AbstractNodeEventParser;
import com.yatoufang.designer.style.NodeType;
import com.yatoufang.ui.dialog.ProtocolBuildDialog;

/**
 * @author GongHuang（hse）
 * @since 2022/5/1 0001
 */
public class ProtocolEventParser extends AbstractNodeEventParser {
    @Override
    public NodeType getType() {
        return NodeType.PROTOCOL_NODE;
    }

    @Override
    public void preview() {
        onExecute();
    }

    @Override
    public void onPreview(Element node) {
    }

    @Override
    public void onExecute() {
        new ProtocolBuildDialog(EditorContext.designer,EditorContext.filePath).show();
    }

    @Override
    public boolean isSuperNode() {
        return true;
    }
}
