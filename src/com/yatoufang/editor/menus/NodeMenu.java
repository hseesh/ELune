package com.yatoufang.editor.menus;


import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.constant.CopyPasteHelper;
import com.yatoufang.editor.menus.listener.NewNodeActionListener;
import com.yatoufang.service.TranslateService;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.utils.StringUtil;
import icons.Icon;

import javax.swing.*;
import java.util.Locale;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class NodeMenu extends JPopupMenu {
    public NodeMenu(AbstractNode node) {
        JMenuItem edit = new JMenuItem("Edit", Icon.EDITOR);
        JMenuItem copy = new JMenuItem("Copy", Icon.ADD_CHILD);
        JMenuItem rename = new JMenuItem("Rename", Icon.VIEW);
        JMenuItem remove = new JMenuItem("Delete", AllIcons.Actions.DeleteTag);

        NewNodeActionListener actionListener = new NewNodeActionListener(node);
        edit.addActionListener(actionListener);
        copy.addActionListener(e -> {
            node.getModel().setSelected(node, true);
            CopyPasteHelper.copy(node.getModel());
            if (!node.getModel().isSingleSelected()) {
                node.getModel().setSelected(node, false);
            }
        });
        rename.addActionListener(e -> {
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
                            asyncCalcAlia(node);
                        } else if (node.getNodeData().getNodeType() == NodeType.ENTITY_NODE) {
                            String[] split = fileName.split(String.valueOf(StringUtil.SPACE));
                            String cameCase = StringUtil.toUpper(split);
                            node.getNodeData().setAlias(cameCase);
                        }
                        node.refreshBounds();
                    });
                }
            }
        });
        remove.addActionListener(e -> node.getModel().delete(node));

        add(edit);
        add(rename);
        add(copy);
        add(remove);
    }


    private void asyncCalcAlia(AbstractNode node) {
        for (AbstractNode abstractNode : node.getAllLinkNode()) {
            abstractNode.onSynchronized(node.getNodeData().getName(), node.getNodeData().getExtra());
        }
    }
}