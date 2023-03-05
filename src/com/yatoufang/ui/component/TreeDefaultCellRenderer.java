package com.yatoufang.ui.component;

import com.intellij.icons.AllIcons;
import com.yatoufang.entity.Param;
import com.yatoufang.utils.StringUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author hse
 * @since 2023/1/24 0024
 */
public class TreeDefaultCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();
        if (userObject instanceof Param) {
            Param field = (Param) userObject;
            this.setIcon(AllIcons.Nodes.Field);
            this.setText(field.getName() + StringUtil.SPACE + field.getTypeAlias() + StringUtil.SPACE + field.getDescription());
        } else {
            this.setIcon(AllIcons.Nodes.Class);
        }
        return this;
    }
}
