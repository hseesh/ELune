package com.yatoufang.ui.dialog;

import com.google.common.collect.Maps;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.FileNode;
import com.yatoufang.entity.Table;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.*;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/2/9
 */
public class ProtocolBuildDialog extends DialogWrapper {

    private final String rootPath;
    private final Table table;

    private Tree tree;
    private EditorTextField editor;

    private Map<String, String> fileMap = Maps.newHashMap();

    private final VelocityService velocityService = VelocityService.getInstance();

    public ProtocolBuildDialog(Table table, String rootPath) {
        super(Application.project, true, false);
        this.table = table;
        this.table.setName(StringUtil.getUpperCaseVariable(table.getName()));
        this.rootPath = StringUtil.buildPath(rootPath, ProjectKeys.MODULE);
        initComponent();
        init();
        setTitle("My Protocol");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return createPanel();
    }

    @NotNull
    @Override
    protected JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        return new JPanel();
    }

    private JComponent createPanel() {
        JSplitPane rootPanel = new JSplitPane();
        JSplitPane subRootPanel = new JSplitPane();
        JButton execute = new JButton("Execute");
        JPanel executeDimension = new JPanel(new BorderLayout());
        executeDimension.setSize(new Dimension(600, 50));
        subRootPanel.setDividerSize(2);
        subRootPanel.setDividerLocation(250);
        rootPanel.setDividerSize(2);
        rootPanel.setDividerLocation(400);
        rootPanel.setMinimumSize(new Dimension(1100, 800));
        subRootPanel.setMinimumSize(new Dimension(400, 600));
        executeDimension.add(execute);
        JPanel leftRootPanel = FormBuilder.createFormBuilder()
                .addComponent(createStructureTree())
                .addComponentFillVertically(createMethodPanel(), 0)
                .addComponent(executeDimension)
                .getPanel();
        rootPanel.setLeftComponent(leftRootPanel);
        rootPanel.setRightComponent(editor);
        execute.addActionListener(e -> {
            saveFile();
        });
        return rootPanel;
    }

    private JComponent createStructureTree() {
        FileNode root = new FileNode(table.getName(), true);
        DefaultTreeModel fileTreeModel = new DefaultTreeModel(root);
        tree = new Tree(fileTreeModel);
        tree.setDragEnabled(true);
        tree.setExpandableItemsEnabled(true);
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(tree);
        toolbarDecorator.setAddAction(anActionButton -> {
            String fileName = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
            if (fileName == null) {
                return;
            }
            FileNode selectedNode = getSelectedNode();
            if (selectedNode == null) {
                FileNode newNode = createNode(fileName);
                root.add(newNode);
                fileTreeModel.nodesWereInserted(root, new int[]{root.getChildCount() - 1});
                tree.setSelectionPath(new TreePath(newNode.getPath()));
                return;
            }
            if (ProjectKeys.REQUEST.equals(selectedNode.name) || ProjectKeys.RESPONSE.equals(selectedNode.name)) {
                ChooseFieldsDialog chooseFieldsDialog = new ChooseFieldsDialog(table);
                ActionListener actionListener = new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */
                    @Override
                    @SuppressWarnings("all")
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() instanceof List) {
                            List<Field> fields = (List<Field>) e.getSource();
                            if (fields.size() > 0) {
                                Table table = new Table();
                                table.setName(fileName);
                                table.setFields(fields);
                                DataUtil.valueOf(table);

                                String result = velocityService.execute(ProjectKeys.ENTITY_TEMPLATE, table);
                                FileNode newNode = new FileNode(fileName);

                                newNode.content = result;
                                newNode.isCatalog = false;
                                editor.setText(result);

                                selectedNode.add(newNode);
                                fileTreeModel.nodesWereInserted(selectedNode, new int[]{selectedNode.getChildCount() - 1});
                                tree.setSelectionPath(new TreePath(newNode.getPath()));
                            }
                        }
                    }
                };
                chooseFieldsDialog.setListener(actionListener);
                chooseFieldsDialog.show();
            } else {
                FileNode newNode = createNode(fileName);
                root.add(newNode);
                fileTreeModel.nodesWereInserted(root, new int[]{root.getChildCount() - 1});
                tree.setSelectionPath(new TreePath(newNode.getPath()));
            }
        });
        toolbarDecorator.setRemoveAction(anActionButton -> {
            FileNode selectedNode = getSelectedNode();
            if (selectedNode == null) {
                return;
            }
            fileTreeModel.removeNodeFromParent(selectedNode);
            editor.setText(StringUtil.EMPTY);
        });
        toolbarDecorator.setEditAction(anActionButton -> {
            FileNode selectedNode = getSelectedNode();
            if (selectedNode == null) {
                return;
            }
            String fileName = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
            if (fileName == null || fileName.isEmpty()) {
                return;
            }
            selectedNode.name = fileName;
            fileTreeModel.reload(selectedNode);
        });

        final FileNode[] lastSelectedNode = {root};
        tree.addTreeSelectionListener(l -> {
            FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> !fileNode.isCatalog);
            for (FileNode selectedNode : selectedNodes) {
                if (!selectedNode.equals(lastSelectedNode[0])) {
                    if (selectedNode.content == null || selectedNode.content.isEmpty()) {
                        selectedNode.content = StringUtil.EMPTY;
                    }
                    editor.setText(selectedNode.content);
                    lastSelectedNode[0] = selectedNode;
                }
            }
        });

        DefaultTreeCellRenderer coloredTreeCellRenderer = new DefaultTreeCellRenderer() {
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
        };
        tree.setCellRenderer(coloredTreeCellRenderer);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        return toolbarDecorator.createPanel();
    }


    private JComponent createMethodPanel() {
        return new JLabel();
    }

    private void initComponent() {
        table.getFields().removeIf(e -> "actorId".equals(e.getName()) || "updateTime".equals(e.getName()));
        editor = SwingUtils.createEditor(StringUtil.EMPTY);
        editor.setFont(new Font(null, Font.PLAIN, 14));
        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                FileNode selectedNode = getSelectedNode();
                if (selectedNode == null) {
                    return;
                }
                selectedNode.content = editor.getText();
            }
        });
    }

    private FileNode getSelectedNode() {
        FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> fileNode.isCatalog);
        for (FileNode selectedNode : selectedNodes) {
            return selectedNode;
        }
        return null;
    }

    private void saveFile() {
        TreeModel model = tree.getModel();
        FileNode root = (FileNode) model.getRoot();
        List<FileNode> files = Lists.newArrayList();
        List<String> methods = Lists.newArrayList();

        visitNode(root, files);
        files.removeIf(e -> e.isCatalog);
        for (FileNode file : files) {
            String filePath = file.getFilePath(rootPath);
            methods.add(StringUtil.collectChineseCharacter(filePath));
            if (filePath.contains(ProjectKeys.REQUEST)) {
                String path = StringUtil.buildPath(rootPath, table.getName(), ProjectKeys.REQUEST, file.name+ ProjectKeys.JAVA);
                FileWrite.write(file.content,path, true, false);
            } else if (filePath.contains(ProjectKeys.RESPONSE)) {
                String path = StringUtil.buildPath(rootPath, table.getName(), ProjectKeys.RESPONSE, file.name+ProjectKeys.JAVA);
                FileWrite.write(file.content,path, true, false);
            }
            PsiClass aClass = BuildUtil.createClass(file.content);
            if(aClass == null){
                continue;
            }

        }
        System.out.println(methods);
        dispose();
    }

    private void visitNode(FileNode root, List<FileNode> files) {
        for (int i = 0; i < root.getChildCount(); i++) {
            FileNode leafNode = (FileNode) root.getChildAt(i);
            files.add(leafNode);
            visitNode(leafNode, files);
        }
    }

    private FileNode createNode(String node) {
        FileNode newNode = new FileNode(node);
        FileNode request = new FileNode(ProjectKeys.REQUEST);
        FileNode response = new FileNode(ProjectKeys.RESPONSE);
        request.isCatalog = true;
        response.isCatalog = true;
        newNode.isInterface = true;
        newNode.add(request);
        newNode.add(response);
        return newNode;
    }

}
