package com.yatoufang.ui.dialog;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/5/15 0015
 */
public class ChooseFieldsDialog extends DialogWrapper {
    private Tree fieldsTree;

    private ActionListener listener;
    private final Table table;

    protected ChooseFieldsDialog(Table table) {
        super(false);
        this.table = table;
        init();
        setTitle("My Fields");
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        return createFieldsPanel();
    }

    @NotNull
    @Override
    protected JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        JPanel jPanel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Close");
        jPanel.add(confirmButton);
        jPanel.add(cancelButton);

        confirmButton.addActionListener(e -> {
            DefaultMutableTreeNode[] selectedNodes = fieldsTree.getSelectedNodes(DefaultMutableTreeNode.class, fileNode -> !fileNode.getAllowsChildren());
            List<Field> fields = Lists.newArrayList();
            for (DefaultMutableTreeNode node : selectedNodes) {
                Field field = (Field) node.getUserObject();
                fields.add(field);
            }
            e.setSource(fields);
            if (listener != null) {
                listener.actionPerformed(e);
            }
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());
        return jPanel;
    }

    private JComponent createFieldsPanel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(table.getName());
        for (Field field : table.getFields()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(field);
            node.setAllowsChildren(false);
            root.add(node);
        }
        DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObject = node.getUserObject();
                if (userObject instanceof Field) {
                    Field field = (Field) userObject;
                    this.setIcon(AllIcons.Nodes.Field);
                    this.setText(field.getName() + "  " + field.getAlias());
                } else {
                    this.setIcon(AllIcons.Nodes.Class);
                }
                return this;
            }
        };

        DefaultTreeModel fileTreeModel = new DefaultTreeModel(root);
        fieldsTree = new Tree(fileTreeModel);
        fieldsTree.setDragEnabled(true);
        fieldsTree.setExpandableItemsEnabled(true);
        fieldsTree.setCellRenderer(cellRenderer);
        return new JBScrollPane(fieldsTree);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }
}
