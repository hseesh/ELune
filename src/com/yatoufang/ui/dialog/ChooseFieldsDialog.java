package com.yatoufang.ui.dialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.ui.treeStructure.Tree;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import com.yatoufang.test.style.StyleContext;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/5/15 0015
 */
public class ChooseFieldsDialog extends DialogWrapper {
    private Tree fieldsTree;
    private Tree defaultFieldsTree;

    private ActionListener listener;
    private final Table table;

    protected ChooseFieldsDialog(Table table) {
        super(false);
        this.table = table;
        init();
        setTitle("My Fields");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
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
            DefaultMutableTreeNode[] selectDefaultNodes = defaultFieldsTree.getSelectedNodes(DefaultMutableTreeNode.class, fileNode -> !fileNode.getAllowsChildren());
            List<Field> fields = Lists.newArrayList();
            for (DefaultMutableTreeNode node : selectedNodes) {
                Field field = (Field) node.getUserObject();
                fields.add(field);
            }
            for (DefaultMutableTreeNode node : selectDefaultNodes) {
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
        List<Field> defaultFields = new ArrayList<>();
        try {
            InputStream resourceAsStream = StyleContext.class.getResourceAsStream("/config/default_fields.json");
            if (resourceAsStream != null) {
                String config = FileUtil.loadTextAndClose(resourceAsStream);
                if (!config.isEmpty()) {
                    Type type = new TypeToken<List<Field>>() {
                    }.getType();
                    defaultFields = new Gson().fromJson(config, type);
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        DefaultMutableTreeNode defaultNode = new DefaultMutableTreeNode("defaultNode");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(table.getName());
        for (Field param : defaultFields) {
            param.setAlias(param.getTypeAlias());
            param.initGetSetString();
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(param);
            node.setAllowsChildren(false);
            defaultNode.add(node);
        }
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
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(defaultNode);
        fieldsTree = new Tree(fileTreeModel);
        fieldsTree.setDragEnabled(true);
        fieldsTree.setExpandableItemsEnabled(true);
        fieldsTree.setCellRenderer(cellRenderer);
        defaultFieldsTree = new Tree(defaultTreeModel);
        defaultFieldsTree.setDragEnabled(true);
        defaultFieldsTree.setExpandableItemsEnabled(true);
        defaultFieldsTree.setCellRenderer(cellRenderer);
        VerticalBox box = new VerticalBox();
        box.add(fieldsTree);
        box.add(defaultFieldsTree);
        return new JBScrollPane(box);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }
}
