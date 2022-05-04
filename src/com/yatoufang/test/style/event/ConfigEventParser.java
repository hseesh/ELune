package com.yatoufang.test.style.event;

import com.yatoufang.test.event.EditorContext;
import com.yatoufang.test.style.AbstractNodeEventParser;
import com.yatoufang.test.style.NodeType;
import com.yatoufang.ui.ConfigTemplateDialog;

/**
 * @author GongHuang（hse）
 * @since 2022/5/1 0001
 */
public class ConfigEventParser extends AbstractNodeEventParser {
    @Override
    public NodeType getType() {
        return NodeType.CONFIG_NODE;
    }

    @Override
    public void preview() {
        onExecute();
    }

    @Override
    public void onExecute() {
        new ConfigTemplateDialog(EditorContext.filePath,"").show();
    }

    @Override
    public boolean isSuperNode() {
        return true;
    }
}
