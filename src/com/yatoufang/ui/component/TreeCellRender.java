package com.yatoufang.ui.component;

import com.intellij.icons.AllIcons;
import com.yatoufang.entity.FileNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author hse
 * @since 2022/12/8 0008
 */
public class TreeCellRender extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        FileNode node = (FileNode) value;
        if (node.isCatalog) {
            this.setIcon(AllIcons.Nodes.Folder);
        } else {
            if (node.isInterface) {
                this.setIcon(AllIcons.Nodes.Interface);
            } else {
                this.setIcon(AllIcons.Nodes.Class);
            }
        }
        return this;
    }
}
