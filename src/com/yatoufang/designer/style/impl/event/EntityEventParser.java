package com.yatoufang.designer.style.impl.event;

import com.yatoufang.designer.event.EditorContext;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.style.AbstractNodeEventParser;
import com.yatoufang.designer.style.NodeType;
import com.yatoufang.templet.Annotations;
import com.yatoufang.ui.dialog.EntityTemplateDialog;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.SwingUtils;

import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/5/15 0015
 */
public class EntityEventParser extends AbstractNodeEventParser {
    @Override
    public NodeType getType() {
        return NodeType.ENTITY_NODE;
    }

    @Override
    public void preview() {
        onPreview(EditorContext.current);
    }

    @Override
    public void onPreview(Element node) {
        Map<String, String> configContentMap = EditorContext.designer.getEntityContentMap();
        String content = configContentMap.get(node.text);
        if (content.isEmpty()) {
            return;
        }
        String info = PSIUtil.getFilePrimaryInfo(content, Annotations.FILED_IGNORE);
        SwingUtils.createPreviewWindow(info);
    }

    @Override
    public void onExecute() {
        new EntityTemplateDialog(EditorContext.filePath, "", EditorContext.designer.getEntityContentMap()).show();
    }

    @Override
    public boolean isSuperNode() {
        return true;
    }
}
