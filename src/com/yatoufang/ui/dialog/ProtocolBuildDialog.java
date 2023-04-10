package com.yatoufang.ui.dialog;

import com.google.common.collect.Maps;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.entity.*;
import com.yatoufang.service.TranslateService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.designer.event.EditorContext;
import com.yatoufang.designer.model.entity.Designer;
import com.yatoufang.utils.*;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.*;

/**
 * @author GongHuang（hse）
 * @since 2022/2/9
 */
public class ProtocolBuildDialog extends DialogWrapper {

    private final String rootPath;
    private final Table table;
    private Node node;

    private Tree tree;
    private EditorTextField editor;

    private boolean designerModel;

    private Map<String, List<Field>> fileMap = Maps.newHashMap();

    private final VelocityService velocityService = VelocityService.getInstance();

    public ProtocolBuildDialog(Table table, String rootPath) {
        super(Application.project, true, false);
        this.table = table;
        this.rootPath = StringUtil.buildPath(rootPath, ProjectKeys.MODULE);
        this.table.setName(StringUtil.toUpperCaseForFirstCharacter(table.getName()));
        initComponent();
        init();
        setTitle("My Protocol");
    }


    public ProtocolBuildDialog(Designer designer, String filePath) {
        super(Application.project, true, false);
        this.table = designer.getTable();
        this.node = designer.getNode();
        designerModel = true;
        this.rootPath = StringUtil.buildPath(filePath, ProjectKeys.MODULE);
        initComponent();
        init();
        setTitle("My Protocol");
        if (designer.getEntityContentMap() != null) {
            designer.getEntityContentMap().forEach((name, content) -> {
                if (content == null || content.isEmpty()) {
                    return;
                }
                PsiClass aClass = BuildUtil.createClass(content);
                if (aClass == null) {
                    return;
                }
                List<Field> classFields = PSIUtil.getClassField(aClass);
                fileMap.put(name, classFields);
            });
        }
        if (designer.getConfigContentMap() != null) {
            designer.getConfigContentMap().forEach((name, content) -> {
                if (content == null || content.isEmpty()) {
                    return;
                }
                PsiClass aClass = BuildUtil.createClass(content);
                if (aClass == null) {
                    return;
                }
                List<Field> classFields = PSIUtil.getClassField(aClass);
                fileMap.put(name, classFields);
            });
        }
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
            .addComponentFillVertically(createStructureTree(),0)
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
        FileNode root;
        if (!designerModel) {
            root = new FileNode(table.getName(), true);
        } else {
            if (this.node == null) {
                root = getFileNode();
            } else {
                root = initNode();
            }
        }
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
                ChooseFieldsDialog chooseFieldsDialog = new ChooseFieldsDialog(table, fileMap);
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
                                String result = StringUtil.EMPTY;
                                FileNode nodeWithoutCatalog = getSelectedNodeWithoutCatalog();
                                if (nodeWithoutCatalog != null) {
                                    TreeNode parent = nodeWithoutCatalog.getParent();
                                    if (parent != null) {
                                        FileNode fileNode = (FileNode) parent.getParent();
                                        String character = StringUtil.collectChineseCharacter(fileNode.name);
                                        if (character == null || character.isEmpty()) {
                                            table.setComment(character);
                                        }
                                    }
                                }
                                if (selectedNode.name.contains(ProjectKeys.REQUEST)) {
                                    result = velocityService.execute(ProjectKeys.REQUEST_TEMPLATE, table);
                                } else if (selectedNode.name.contains(ProjectKeys.RESPONSE)) {
                                    result = velocityService.execute(ProjectKeys.ENTITY_TEMPLATE, table);
                                }

                                FileNode newNode = new FileNode(fileName);

                                newNode.content = result;
                                newNode.isCatalog = false;
                                editor.setText(result);

                                selectedNode.add(newNode);
                                fileTreeModel.nodesWereInserted(selectedNode, new int[]{selectedNode.getChildCount() - 1});
                                tree.setSelectionPath(new TreePath(newNode.getPath()));
                                refresh(root);
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
        editor.setFont(new Font(null, Font.PLAIN, 12));
        return toolbarDecorator.createPanel();
    }

    @NotNull
    private FileNode getFileNode() {
        String name = StringUtil.toUpperCaseForFirstCharacter(table.getName());
        FileNode root = new FileNode(table.getName(), true);
        FileNode cmd = new FileNode(name + "Cmd", false);
        FileNode handler = new FileNode(name + "Handler", false);
        FileNode helpRoot = new FileNode(ProjectKeys.HELPER, true);
        FileNode implRoot = new FileNode(ProjectKeys.IMPL, true);
        FileNode facadeRoot = new FileNode(ProjectKeys.FACADE, true);
        FileNode facade = new FileNode(name + StringUtil.toUpper(ProjectKeys.FACADE));
        FileNode facadeImpl = new FileNode(name + StringUtil.toUpper(ProjectKeys.FACADE, ProjectKeys.IMPL));
        FileNode pushHelper = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.PUSH, ProjectKeys.HELPER));
        cmd.isInterface = true;
        cmd.isCatalog = false;
        facade.isCatalog = false;
        facadeImpl.isCatalog = false;
        pushHelper.isCatalog = false;
        facade.isInterface = true;
        root.add(cmd);
        root.add(handler);
        root.add(helpRoot);
        root.add(facadeRoot);
        helpRoot.add(pushHelper);
        facadeRoot.add(implRoot);
        facadeRoot.add(facade);
        implRoot.add(facadeImpl);
        pushHelper.content = velocityService.execute(ProjectKeys.PUSH_HELP_TEMPLATE, table);
        return root;
    }

    private void initComponent() {
        if (table != null && table.getFields() != null) {
            table.getFields().removeIf(e -> "actorId".equals(e.getName()) || "updateTime".equals(e.getName()));
        }
        editor = SwingUtils.createEditor(StringUtil.EMPTY);
        editor.setFont(new Font(null, Font.PLAIN, 14));
        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                FileNode selectedNode = getSelectedNodeWithoutCatalog();
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

    private FileNode getSelectedNodeWithoutCatalog() {
        FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> !fileNode.isCatalog);
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
                String path = StringUtil.buildPath(rootPath, table.getName(), ProjectKeys.REQUEST, file.name + ProjectKeys.JAVA);
                FileWrite.write(file.content, path, true, false);
            } else if (filePath.contains(ProjectKeys.RESPONSE)) {
                String path = StringUtil.buildPath(rootPath, table.getName(), ProjectKeys.RESPONSE, file.name + ProjectKeys.JAVA);
                FileWrite.write(file.content, path, true, false);
            } else {
                String path = file.getFilePath(rootPath) + ProjectKeys.JAVA;
                FileWrite.write(file.content, path, true, false);
            }
        }
        doCancelAction();
        dispose();
    }

    private void refresh(FileNode root) {
        List<String> methods = Lists.newArrayList();
        for (int i = 0; i < root.getChildCount(); i++) {
            FileNode leafNode = (FileNode) root.getChildAt(i);
            String name = StringUtil.collectChineseCharacter(leafNode.name);
            if (name.isEmpty()) {
                continue;
            }
            methods.add(name);
        }
        ApplicationManager.getApplication().invokeLater(() -> {
            StringBuilder builder = new StringBuilder();
            HashMap<String, Integer> map = Maps.newHashMap();
            for (int i = 0; i < methods.size(); i++) {
                builder.append(methods.get(i));
                if (i != methods.size() - 1) {
                    builder.append(StringUtil.EQUAL);
                }
                map.put(methods.get(i), i);
            }
            String action = TranslateService.translate(builder.toString());
            if (action == null) {
                return;
            }
            String[] results = action.split(String.valueOf(StringUtil.EQUAL));
            ArrayList<String> upCaseList = Lists.newArrayList();
            ArrayList<String> cameCaseList = Lists.newArrayList();
            for (String result : results) {
                result = result.trim();
                String allUpperCase = result.replace(" ", StringUtil.UNDER_LINE).toUpperCase(Locale.ROOT);
                String[] s = result.split(" ");
                StringBuilder camelCase = new StringBuilder(StringUtil.toLowerCaseForFirstChar(s[0]));
                if (s.length > 1) {
                    for (int i = 1; i < s.length; i++) {
                        camelCase.append(StringUtil.getUpperCaseVariable(s[i]));
                    }
                }
                upCaseList.add(allUpperCase);
                cameCaseList.add(camelCase.toString());
            }
            int cmdIndex = 1;
            FileNode cmdNode = null;
            FileNode facadeNode = null;
            FileNode handlerNode = null;
            FileNode facadeImplNode = null;
            ArrayList<TcpMethod> list = Lists.newArrayList();
            for (int i = 0; i < root.getChildCount(); i++) {
                FileNode leafNode = (FileNode) root.getChildAt(i);
                Integer index = map.get(leafNode.name);
                if (index == null) {
                    if (leafNode.name.contains("Cmd")) {
                        cmdNode = leafNode;
                    } else if (leafNode.name.contains("Handler")) {
                        handlerNode = leafNode;
                    } else if (leafNode.name.contains(ProjectKeys.FACADE)) {
                        for (int j = 0; j < leafNode.getChildCount(); j++) {
                            FileNode child = (FileNode) leafNode.getChildAt(j);
                            if (child.name.endsWith(StringUtil.toUpper(ProjectKeys.FACADE))) {
                                facadeNode = child;
                            } else if (child.name.contains(ProjectKeys.IMPL)) {
                                for (int k = 0; k < child.getChildCount(); k++) {
                                    FileNode implChild = (FileNode) child.getChildAt(k);
                                    if (implChild.name.endsWith(StringUtil.toUpper(ProjectKeys.FACADE, ProjectKeys.IMPL))) {
                                        facadeImplNode = child;
                                    }
                                }
                            }
                        }
                    }
                    continue;
                }
                TcpMethod method = new TcpMethod(cameCaseList.get(index), leafNode.name);
                method.setAlias(upCaseList.get(index));

                method.setCmdCode(String.valueOf(cmdIndex++));
                list.add(method);
                for (int j = 0; j < leafNode.getChildCount(); j++) {
                    FileNode parent = (FileNode) leafNode.getChildAt(j);
                    for (int k = 0; k < parent.getChildCount(); k++) {
                        FileNode child = (FileNode) parent.getChildAt(0);
                        if (child != null) {
                            if (parent.name.contains(ProjectKeys.REQUEST)) {
                                if (child.content != null && !child.content.isEmpty()) {
                                    PsiClass aClass = BuildUtil.createClass(child.content);
                                    if (aClass != null) {
                                        List<Param> classFields = PSIUtil.getClassFields(aClass);
                                        method.addAll(classFields);
                                    }
                                }
                                method.setRequest(child.name);
                            } else if (parent.name.contains(ProjectKeys.RESPONSE)) {
                                method.setResponse(child.name);
                            } else {
                                method.setPush(child.name);
                            }
                        }
                    }
                }
                String valueOf = DataUtil.valueOf(method.getParams());
                method.setValueOf(valueOf);
            }
            Protocol protocol = Protocol.valueOf(table.getName(), table.getAlias(), table.getComment(), list);
            if (cmdNode != null) {
                cmdNode.content = velocityService.execute(ProjectKeys.CMD_TEMPLATE, protocol);
            }
            if (facadeNode != null) {
                facadeNode.content = velocityService.execute(ProjectKeys.FACADE_TEMPLATE, protocol);
            }
            if (handlerNode != null) {
                handlerNode.content = velocityService.execute(ProjectKeys.HANDLER_TEMPLATE, protocol);
            }
            if (facadeImplNode != null) {
                facadeImplNode.content = velocityService.execute(ProjectKeys.FACADE_IMPL_TEMPLATE, protocol);
            }
        });
    }

    public static void main(String[] args) {
        List<String> methods = Lists.newArrayList();
        methods.add("获取信息");
        methods.add("升级");
        methods.add("高级升级");
        methods.add("领取等级奖励");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < methods.size(); i++) {
            builder.append(methods.get(i));
            if (i != methods.size() - 1) {
                builder.append(StringUtil.EQUAL);
            }
        }
        String action = TranslateService.translate(builder.toString());
        if (action == null) {
            return;
        }
        String[] results = action.split(String.valueOf(StringUtil.EQUAL));
        for (String result : results) {
            result = result.trim();
            String allUpperCase = result.replace(" ", StringUtil.UNDER_LINE).toUpperCase(Locale.ROOT);
            String[] s = result.split(" ");
            StringBuilder camelCase = new StringBuilder(StringUtil.toLowerCaseForFirstChar(s[0]));
            if (s.length > 1) {
                for (int i = 1; i < s.length; i++) {
                    camelCase.append(StringUtil.getUpperCaseVariable(s[i]));
                }
            }
            System.out.println(allUpperCase);
            System.out.println(camelCase);
        }
        System.out.println(Arrays.toString(results));
    }

    private void visitNode(FileNode root, List<FileNode> files) {
        for (int i = 0; i < root.getChildCount(); i++) {
            FileNode leafNode = (FileNode) root.getChildAt(i);
            files.add(leafNode);
            visitNode(leafNode, files);
        }
    }

    private void visitNode(FileNode root, Node parent) {
        for (int i = 0; i < root.getChildCount(); i++) {
            FileNode leafNode = (FileNode) root.getChildAt(i);
            Node node = Node.valueOf(leafNode);
            parent.addChild(node);
            visitNode(leafNode, node);
        }
    }

    private void initNode(FileNode root, Node parent) {
        for (Node child : parent.children) {
            FileNode fileNode = FileNode.valueOf(child);
            root.add(fileNode);
            initNode(fileNode, child);
        }
    }

    protected FileNode initNode() {
        FileNode parent = FileNode.valueOf(this.node);
        initNode(parent, this.node);
        return parent;
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

    @Override
    public void doCancelAction() {
        super.doCancelAction();
        if (!designerModel) {
            return;
        }
        TreeModel model = tree.getModel();
        FileNode root = (FileNode) model.getRoot();
        Node node = Node.valueOf(root);
        visitNode(root, node);
        if (node.children.size() < 1) {
            return;
        }
        List<String> methods = Lists.newArrayList();
        for (int i = 0; i < root.getChildCount(); i++) {
            FileNode leafNode = (FileNode) root.getChildAt(i);
            String name = StringUtil.collectChineseCharacter(leafNode.name);
            if (name.isEmpty()) {
                continue;
            }
            methods.add(leafNode.name);
        }
        EditorContext.setDesigner(node, methods);
    }
}
