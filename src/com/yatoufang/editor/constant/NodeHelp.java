package com.yatoufang.editor.constant;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.service.TranslateService;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.utils.StringUtil;

import java.util.Locale;

/**
 * @author GongHuang(hse)
 * @since 2023/6/17
 */
public class NodeHelp {

    public static void init(AbstractNode node) {
        if (!(node instanceof BaseNode)) {
            return;
        }
        String name = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
        if (name != null && name.length() != 0) {
            node.getNodeData().setName(name);
            if (NodeType.AUTO_TRANSLATE.contains(node.getNodeData().getNodeType())) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    String fileName = TranslateService.translate(name);
                    if (fileName == null || fileName.length() == 0) {
                        return;
                    }
                    if (node.getNodeData().getNodeType() == NodeType.PROTOCOL_NODE) {
                        String allUpperCase = fileName.replace(StringUtil.SPACE_FLAG, StringUtil.UNDER_LINE).toUpperCase(Locale.ROOT);
                        String cameCase = StringUtil.toCameCaseFormTranslate(fileName);
                        node.getNodeData().setAlias(allUpperCase);
                        node.getNodeData().setExtra(cameCase);
                        for (AbstractNode abstractNode : node.getAllLinkNode()) {
                            abstractNode.onSynchronized(node.getNodeData().getName(), node.getNodeData().getExtra());
                        }
                    } else if (node.getNodeData().getNodeType() == NodeType.ENTITY_NODE) {
                        String[] split = fileName.split(String.valueOf(StringUtil.SPACE));
                        String cameCase = StringUtil.toUpper(split);
                        node.getNodeData().setAlias(cameCase);
                    }
                    node.refreshBounds();
                });
            }
        }
    }

}
