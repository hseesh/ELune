package com.yatoufang.editor.menus;

import com.intellij.icons.AllIcons;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.constant.CopyPasteHelper;
import com.yatoufang.editor.menus.listener.NewNodeActionListener;
import com.yatoufang.editor.constant.NodeHelp;
import icons.Icon;

import javax.swing.*;

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
        rename.addActionListener(e -> NodeHelp.init(node));
        remove.addActionListener(e -> node.getModel().delete(node));

        add(edit);
        add(rename);
        add(copy);
        add(remove);
    }
}