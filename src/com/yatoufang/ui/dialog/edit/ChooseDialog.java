package com.yatoufang.ui.dialog.edit;

import com.google.common.collect.Maps;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.treeStructure.Tree;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Param;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.component.TreeDefaultCellRenderer;
import com.yatoufang.utils.FileWrite;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/5/15 0015
 */
public class ChooseDialog extends DialogWrapper {

    private ActionListener listener;
    private final Map<String, Collection<Param>> fileMap;
    private final Map<String, Tree> treeMap = Maps.newHashMap();


    protected ChooseDialog(Map<String, Collection<Param>> fileMap) {
        super(false);
        this.fileMap = fileMap;
        init();
        setTitle("My Fields");
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        JBTabbedPane contentPanel = new JBTabbedPane();
        Collection<Field> defaultFields = FileWrite.loadConfig(this.getClass(), Field.class, "/config/default_fields.json");
        String defaultName = ProjectKeys.DEFAULT_NODE;
        Collection<Param> params = fileMap.get(defaultName);
        params.addAll(defaultFields);

        TreeDefaultCellRenderer renderer = new TreeDefaultCellRenderer();
        fileMap.forEach((name, list) -> {
            DefaultMutableTreeNode parent = new DefaultMutableTreeNode(name);
            for (Param param : list) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(param);
                child.setAllowsChildren(false);
                parent.add(child);
            }
            if (list.isEmpty()) {
                return;
            }
            DefaultTreeModel treeModel = new DefaultTreeModel(parent);
            Tree tree = new Tree(treeModel);
            contentPanel.add(name, tree);
            treeMap.put(name, tree);
            tree.setCellRenderer(renderer);
            for (int i = 0; i < tree.getRowCount(); i++) {
                tree.expandRow(i);
            }
        });
        rootPanel.add(contentPanel);
        return new JBScrollPane(rootPanel);
    }

    @NotNull
    @Override
    protected JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        JPanel bottomPanel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Close");
        bottomPanel.add(confirmButton);
        bottomPanel.add(cancelButton);

        confirmButton.addActionListener(e -> {
            List<Param> fields = Lists.newArrayList();
            treeMap.forEach((name, tree) -> {
                DefaultMutableTreeNode[] nodes = tree.getSelectedNodes(DefaultMutableTreeNode.class, fileNode -> !fileNode.getAllowsChildren());
                for (DefaultMutableTreeNode node : nodes) {
                    Param field = (Param) node.getUserObject();
                    fields.add(field);
                }
            });
            e.setSource(fields);
            if (listener != null) {
                listener.actionPerformed(e);
            }
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());
        return bottomPanel;
    }


    public void setListener(ActionListener listener) {
        this.listener = listener;
    }
}
