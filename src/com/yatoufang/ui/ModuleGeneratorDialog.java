package com.yatoufang.ui;

import com.google.common.collect.Maps;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.FileNode;
import com.yatoufang.entity.Table;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.ExceptionUtil;
import com.yatoufang.utils.StringUtil;
import com.yatoufang.utils.SwingUtils;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/2/9
 */
public class ModuleGeneratorDialog extends DialogWrapper {

    private final Table table;
    private FileNode defaultNode;

    private Tree tree;
    private EditorTextField editor;
    private final List<JRadioButton> fields = Lists.newArrayList();


    private ActionListener actionListener;
    private final VelocityService velocityService = VelocityService.getInstance();

    public ModuleGeneratorDialog(Table table) {
        super(Application.project);
        this.table = table;
        initComponent();
        init();
        setTitle("My Module");
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
                .addComponent(subRootPanel)
                .addComponentFillVertically(createMethodPanel(), 0)
                .addComponent(executeDimension)
                .getPanel();
        rootPanel.setLeftComponent(leftRootPanel);
        rootPanel.setRightComponent(editor);
        subRootPanel.setLeftComponent(createStructureTree());
        subRootPanel.setRightComponent(createFieldsPanel());
        return rootPanel;
    }


    private JComponent createStructureTree() {
        FileNode root = initTreeData();
        DefaultTreeModel fileTreeModel = new DefaultTreeModel(root);
        tree = new Tree(fileTreeModel);
        tree.setDragEnabled(true);
        tree.setExpandableItemsEnabled(true);
        tree.setSelectionPath(new TreePath(defaultNode));
        TreeSpeedSearch treeSpeedSearch = new TreeSpeedSearch(tree);
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(tree);
        toolbarDecorator.setAddAction(anActionButton -> {
            FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> fileNode.isCatalog);
            for (FileNode selectedNode : selectedNodes) {
                System.out.println("selectedNode = " + Arrays.toString(selectedNode.getPath()));
            }
        });
        toolbarDecorator.setRemoveAction(anActionButton -> {
            FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> fileNode.isCatalog);
            for (FileNode selectedNode : selectedNodes) {
                fileTreeModel.removeNodeFromParent(selectedNode);
            }
        });
        final FileNode[] lastSelectedNode = {root};
        tree.addTreeSelectionListener(l -> {
            FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> !fileNode.isCatalog);
            for (FileNode selectedNode : selectedNodes) {
                if(!selectedNode.equals(lastSelectedNode[0])){
                    if(selectedNode.fields.size() > 0){
                        editor.setText(velocityService.execute(selectedNode.templatePath,selectedNode));
                    }else{
                        editor.setText(velocityService.execute(selectedNode.templatePath, table));
                    }
                    lastSelectedNode[0] = selectedNode;
                }
            }
        });

        DefaultTreeCellRenderer coloredTreeCellRenderer = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                FileNode node = (FileNode) value;
                if(node.isCatalog){
                    this.setIcon(AllIcons.Nodes.Folder);
                }else{
                    this.setIcon(AllIcons.Nodes.Class);
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


    private JComponent createFieldsPanel() {
        FormBuilder formBuilder = FormBuilder.createFormBuilder();
        if (table != null && table.getFields() != null) {
            for (Field field : table.getFields()) {
                JRadioButton radioButton = new JRadioButton(field.getName());
                radioButton.addActionListener(actionListener);
                formBuilder.addComponent(radioButton);
                fields.add(radioButton);
            }
        }
        return new JBScrollPane(formBuilder.getPanel());
    }


    private JComponent createMethodPanel() {
        return new JLabel();
    }

    private void initComponent() {
        table.getFields().removeIf( e -> "actorId".equals(e.getName()) || "updateTime".equals(e.getName()));
        editor = SwingUtils.createEditor(velocityService.execute(ProjectKeys.ENTITY_VO_TEMPLATE, table));
        editor.setFont(new Font(null, Font.PLAIN, 14));
        actionListener =event -> {
            Object sourceObject = event.getSource();
            if (sourceObject instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) sourceObject;
                String text = radioButton.getText();
                Field field = getFieldByName(text);
                if (field != null) {
                    FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> !fileNode.isCatalog);
                    for (FileNode selectedNode : selectedNodes) {
                        if(selectedNode.tryAddFields(field)){
                            editor.setText(velocityService.execute(selectedNode.templatePath,selectedNode));
                        }
                        break;
                    }
                }
            } else if (sourceObject instanceof JButton) {
                JButton button = (JButton) sourceObject;

            }
        };
    }

    private Field getFieldByName(String text) {
        for (Field field : table.getFields()) {
            if (field.getName().toLowerCase(Locale.ROOT).startsWith(text.toLowerCase(Locale.ROOT))){
                return field;
            }
        }
        return null;
    }


    private FileNode initTreeData() {
        FileNode root = new FileNode(table.getName(), true);
        FileNode dao = new FileNode("dao");
        FileNode daoImpl = new FileNode("impl");
        FileNode impl = new FileNode("impl");
        FileNode facade = new FileNode("facade");
        FileNode help = new FileNode("help");
        FileNode model = new FileNode("model");
        FileNode request = new FileNode("request");
        FileNode response = new FileNode("response");
        root.add(dao);
        dao.add(daoImpl);
        facade.add(impl);
        root.add(facade);
        root.add(help);
        root.add(model);
        root.add(request);
        root.add(response);

        FileNode pushHelper = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.PUSH, ProjectKeys.HELPER, ProjectKeys.JAVA), ProjectKeys.PUSH_HELP_TEMPLATE);
        FileNode entityCmdFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.CMD, ProjectKeys.JAVA), ProjectKeys.ENTITY_CMD_TEMPLATE);
        FileNode entityHandlerFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.HANDLER, ProjectKeys.JAVA), ProjectKeys.ENTITY_HANDLER_TEMPLATE);
        FileNode daoFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.DAO, ProjectKeys.JAVA),false);
        FileNode voFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.VO, ProjectKeys.JAVA), ProjectKeys.ENTITY_VO_TEMPLATE);
        FileNode entityFacadeFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.FACADE, ProjectKeys.JAVA), ProjectKeys.ENTITY_FACADE_TEMPLATE);
        FileNode entityResponseFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.RESPONSE, ProjectKeys.JAVA), ProjectKeys.ENTITY_RESPONSE_TEMPLATE);
        FileNode entityFacadeImplFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.FACADE, ProjectKeys.IMPL, ProjectKeys.JAVA), ProjectKeys.ENTITY_FACADE_IMPL_TEMPLATE);
        FileNode daoImplFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.DAO, ProjectKeys.IMPL, ProjectKeys.JAVA),false);
        FileNode entityDeleteResponseFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.DELETE, ProjectKeys.RESPONSE, ProjectKeys.JAVA), ProjectKeys.ENTITY_DELETE_RESPONSE_TEMPLATE);
        FileNode entityRewardResponseFile = new FileNode(StringUtil.toUpper(table.getName(), ProjectKeys.REWARD, ProjectKeys.RESULT, ProjectKeys.RESPONSE, ProjectKeys.JAVA), ProjectKeys.ENTITY_REWARD_RESPONSE_TEMPLATE);
        if (table.isMultiEntity()) {
            daoFile.setTemplatePath(ProjectKeys.MULTI_ENTITY_TEMPLATE);
            daoImplFile.setTemplatePath(ProjectKeys.MULTI_ENTITY_IMPL_TEMPLATE);
        } else {
            daoFile.setTemplatePath(ProjectKeys.SINGLE_ENTITY_TEMPLATE);
            daoImplFile.setTemplatePath(ProjectKeys.SINGLE_ENTITY_IMPL_TEMPLATE);
        }
        root.add(entityCmdFile);
        root.add(entityHandlerFile);
        help.add(pushHelper);
        model.add(voFile);
        facade.add(entityFacadeFile);
        impl.add(entityFacadeImplFile);
        dao.add(daoFile);
        daoImpl.add(daoImplFile);
        response.add(entityResponseFile);
        response.add(entityRewardResponseFile);
        response.add(entityDeleteResponseFile);
        defaultNode = voFile;
        return root;
    }

    private void generateCode(String rootPath, Table table) {
        table.getFields().removeIf(k -> k.getName().equals(ProjectKeys.UPDATE_TIME));
        String moduleName = table.getName();
        HashMap<String, File> fileMap = Maps.newHashMap();
        String targetPath = StringUtil.buildPath(rootPath, ProjectKeys.MODULE + "_", moduleName.toUpperCase(Locale.ROOT));
        ConsoleService consoleService = ConsoleService.getInstance();
        File entityCmdFile = new File(StringUtil.buildPath(targetPath, StringUtil.toUpper(table.getName(), ProjectKeys.CMD, ProjectKeys.JAVA)));
        File entityHandlerFile = new File(StringUtil.buildPath(targetPath, StringUtil.toUpper(table.getName(), ProjectKeys.HANDLER, ProjectKeys.JAVA)));
        File daoFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.DAO, StringUtil.toUpper(table.getName(), ProjectKeys.DAO, ProjectKeys.JAVA)));
        File voFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.MODEL, StringUtil.toUpper(table.getName(), ProjectKeys.VO, ProjectKeys.JAVA)));
        File entityFacadeFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.FACADE, StringUtil.toUpper(table.getName(), ProjectKeys.FACADE, ProjectKeys.JAVA)));
        File entityResponseFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.RESPONSE, StringUtil.toUpper(table.getName(), ProjectKeys.RESPONSE, ProjectKeys.JAVA)));
        File helperFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.HELPER, StringUtil.toUpper(table.getName(), ProjectKeys.PUSH, ProjectKeys.HELPER, ProjectKeys.JAVA)));
        File entityFacadeImplFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.FACADE, StringUtil.toUpper(table.getName(), ProjectKeys.FACADE, ProjectKeys.IMPL, ProjectKeys.JAVA)));
        File daoImplFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.DAO, ProjectKeys.IMPL, StringUtil.toUpper(table.getName(), ProjectKeys.DAO, ProjectKeys.IMPL, ProjectKeys.JAVA)));
        File entityDeleteResponseFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.RESPONSE, StringUtil.toUpper(table.getName(), ProjectKeys.DELETE, ProjectKeys.RESPONSE, ProjectKeys.JAVA)));
        File entityRewardResponseFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.RESPONSE, StringUtil.toUpper(table.getName(), ProjectKeys.REWARD, ProjectKeys.RESULT, ProjectKeys.RESPONSE, ProjectKeys.JAVA)));
        if (table.isMultiEntity()) {
            fileMap.put(ProjectKeys.MULTI_ENTITY_TEMPLATE, daoFile);
            fileMap.put(ProjectKeys.MULTI_ENTITY_IMPL_TEMPLATE, daoImplFile);
        } else {
            fileMap.put(ProjectKeys.SINGLE_ENTITY_TEMPLATE, daoFile);
            fileMap.put(ProjectKeys.SINGLE_ENTITY_IMPL_TEMPLATE, daoImplFile);
        }
        fileMap.put(ProjectKeys.ENTITY_VO_TEMPLATE, voFile);
        fileMap.put(ProjectKeys.PUSH_HELP_TEMPLATE, helperFile);
        fileMap.put(ProjectKeys.ENTITY_CMD_TEMPLATE, entityCmdFile);
        fileMap.put(ProjectKeys.ENTITY_FACADE_TEMPLATE, entityFacadeFile);
        fileMap.put(ProjectKeys.ENTITY_HANDLER_TEMPLATE, entityHandlerFile);
        fileMap.put(ProjectKeys.ENTITY_RESPONSE_TEMPLATE, entityResponseFile);
        fileMap.put(ProjectKeys.ENTITY_FACADE_IMPL_TEMPLATE, entityFacadeImplFile);
        fileMap.put(ProjectKeys.ENTITY_REWARD_RESPONSE_TEMPLATE, entityRewardResponseFile);
        fileMap.put(ProjectKeys.ENTITY_DELETE_RESPONSE_TEMPLATE, entityDeleteResponseFile);
        fileMap.forEach((fileName, file) -> {
            try {
                FileUtil.writeToFile(file, velocityService.execute(fileName, table));
                consoleService.print(file.getCanonicalPath() + " created successfully\n");
            } catch (IOException e) {
                consoleService.printError(ExceptionUtil.getExceptionInfo(e));
            }
        });
    }
}
