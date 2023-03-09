package com.yatoufang.ui.dialog.edit;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.constant.GlobalConstant;
import com.yatoufang.entity.FileNode;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.component.TreeCellRender;
import com.yatoufang.utils.FileWrite;
import com.yatoufang.utils.StringUtil;
import com.yatoufang.utils.SwingUtils;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hse
 * @since 2022/12/8 0008
 */
public class ProtocolPreviewDialog extends DialogWrapper {


    private ArrayList<AbstractNode> returnNodes;
    private final HashMap<String, String> map;
    private final String moduleName;
    private EditorTextField editor;

    public String filePath;
    private Tree tree;

    public ProtocolPreviewDialog(HashMap<String, String> map, String moduleName, String filePath, ArrayList<AbstractNode> returnNodes) {
        super(Application.project, true);
        this.filePath = StringUtil.buildPath(filePath, ProjectKeys.MODULE);
        this.returnNodes = returnNodes;
        this.moduleName = moduleName;
        this.map = map;
        init();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        editor = SwingUtils.createEditor(StringUtil.EMPTY);
        editor.setFont(GlobalConstant.FONT);
        JSplitPane rootPanel = new JSplitPane();
        JSplitPane subRootPanel = new JSplitPane();
        subRootPanel.setDividerSize(2);
        subRootPanel.setDividerLocation(250);
        rootPanel.setDividerSize(2);
        rootPanel.setDividerLocation(400);
        rootPanel.setMinimumSize(new Dimension(1100, 800));
        subRootPanel.setMinimumSize(new Dimension(400, 600));
        JPanel leftRootPanel = FormBuilder.createFormBuilder()
                .addComponentFillVertically(createStructureTree(), 0)
                .getPanel();
        rootPanel.setLeftComponent(leftRootPanel);
        rootPanel.setRightComponent(editor);
        return rootPanel;
    }

    @NotNull
    @Override
    protected JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        JPanel jPanel = new JPanel();
        JButton saveButton = new JButton("Execute");
        JButton cancelButton = new JButton("Close");
        jPanel.add(saveButton);
        jPanel.add(cancelButton);

        saveButton.addActionListener(e -> saveFile());
        cancelButton.addActionListener(e -> dispose());
        return jPanel;
    }

    private JComponent createStructureTree() {
        FileNode root = getFileNode();
        DefaultTreeModel fileTreeModel = new DefaultTreeModel(root);
        tree = new Tree(fileTreeModel);
        tree.setDragEnabled(true);
        tree.setExpandableItemsEnabled(true);
        tree.setCellRenderer(new TreeCellRender());
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        tree.addTreeSelectionListener(l -> {
            FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> !fileNode.isCatalog);
            for (FileNode selectedNode : selectedNodes) {
                if (selectedNode.content == null || selectedNode.content.isEmpty()) {
                    selectedNode.content = StringUtil.EMPTY;
                }
                editor.setText(selectedNode.content);
            }
        });
        return tree;
    }


    private FileNode getFileNode() {
        String name = StringUtil.toUpperCaseForFirstCharacter(moduleName);
        FileNode root = new FileNode(moduleName, true);
        FileNode cmd = new FileNode(StringUtil.toUpper(name, ProjectKeys.CMD), false, false);
        FileNode handler = new FileNode(StringUtil.toUpper(name, ProjectKeys.HANDLER), false, false);
        FileNode daoRoot = new FileNode(ProjectKeys.DAO, true, false);
        FileNode helpRoot = new FileNode(ProjectKeys.HELPER, true, false);
        FileNode implRoot = new FileNode(ProjectKeys.IMPL, true, false);
        FileNode facadeRoot = new FileNode(ProjectKeys.FACADE, true, false);
        FileNode dao = new FileNode(name + StringUtil.toUpper(ProjectKeys.DAO), false, true);
        FileNode facade = new FileNode(name + StringUtil.toUpper(ProjectKeys.FACADE), false, true);
        FileNode daoImpl = new FileNode(name + StringUtil.toUpper(ProjectKeys.DAO, ProjectKeys.IMPL), false, false);
        FileNode facadeImpl = new FileNode(name + StringUtil.toUpper(ProjectKeys.FACADE, ProjectKeys.IMPL), false, false);
        FileNode pushHelper = new FileNode(StringUtil.toUpper(moduleName, ProjectKeys.PUSH, ProjectKeys.HELPER), false, false);

        cmd.content = map.get(ProjectKeys.CMD);
        dao.content = map.get(ProjectKeys.DAO);
        facade.content = map.get(ProjectKeys.FACADE);
        handler.content = map.get(ProjectKeys.HANDLER);
        facadeImpl.content = map.get(ProjectKeys.IMPL);
        daoImpl.content = map.get(ProjectKeys.DAO + ProjectKeys.IMPL);
        root.add(daoRoot);
        daoRoot.add(dao);
        daoRoot.add(daoImpl);
        root.add(helpRoot);
        root.add(facadeRoot);
        helpRoot.add(pushHelper);
        facadeRoot.add(implRoot);
        facadeRoot.add(facade);
        implRoot.add(facadeImpl);
        root.add(cmd);
        root.add(handler);
        return root;
    }


    private void saveFile() {
        List<String> paths = Lists.newArrayList();
        List<FileNode> files = Lists.newArrayList();
        TreeModel model = tree.getModel();
        FileNode root = (FileNode) model.getRoot();
        visitNode(root, files);
        files.removeIf(e -> e.isCatalog);
        if (files.isEmpty()) {
            return;
        }
        for (FileNode file : files) {
            paths.add(file.getFilePath(this.filePath) + ProjectKeys.JAVA);
        }
        for (int i = 0; i < files.size(); i++) {
            String content = files.get(i).content;
            if (content == null || content.isEmpty()) {
                continue;
            }
            FileWrite.write(content, paths.get(i), true, false);
        }
        for (AbstractNode node : returnNodes) {
            FileWrite.write(node.getNodeData().getContent(), node.getWorkPath(), true, false);
        }
        dispose();
    }

    private void visitNode(FileNode root, List<FileNode> files) {
        for (int i = 0; i < root.getChildCount(); i++) {
            FileNode leafNode = (FileNode) root.getChildAt(i);
            files.add(leafNode);
            visitNode(leafNode, files);
        }
    }


}
