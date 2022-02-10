package com.yatoufang.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.FileNode;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Application;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/2/9
 */
public class ModuleGeneratorDialog extends DialogWrapper {

    private Table table;
    private List<JRadioButton> fields = Lists.newArrayList();

    private ActionListener actionListener = event -> {
        Object sourceObject = event.getSource();
        if (sourceObject instanceof JRadioButton) {
            JRadioButton radioButton = (JRadioButton) sourceObject;
            String text = radioButton.getText();

        } else if (sourceObject instanceof JButton) {
            JButton button = (JButton) sourceObject;

        }
    };

    public ModuleGeneratorDialog() {
        super(Application.project);
        table = new Table("dragon","String");
        ArrayList<Field> fields = Lists.newArrayList();
        table.setFields(fields);
        table.addFields(new Field("attribute"));
        table.addFields(new Field("actorId"));
        table.addFields(new Field("times"));
        init();
        setTitle("Export File");
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
        subRootPanel.setLeftComponent(createStructureTree());
        subRootPanel.setRightComponent(createFieldsPanel());
        subRootPanel.setDividerSize(2);
        subRootPanel.setDividerLocation(250);
        subRootPanel.setMinimumSize(new Dimension(400,600));
        rootPanel.setDividerSize(2);
        rootPanel.setDividerLocation(400);
        JButton execute = new JButton("Execute");
        JPanel executeDimension = new JPanel(new BorderLayout());
        executeDimension.setSize(new Dimension(600, 50));
        executeDimension.add(execute);
        JPanel leftRootPanel = FormBuilder.createFormBuilder()
                .addComponent(subRootPanel)
                .addComponentFillVertically(createMethodPanel(),0)
                .addComponent(executeDimension)
                .getPanel();
        rootPanel.setLeftComponent(leftRootPanel);
        return rootPanel;
    }


    private JComponent createStructureTree(){
        FileNode root = initTreeData();
        DefaultTreeModel fileTreeModle = new DefaultTreeModel(root);
        Tree tree = new Tree(fileTreeModle);
        tree.setDragEnabled(true);
        tree.setExpandableItemsEnabled(true);
        TreeSpeedSearch treeSpeedSearch = new TreeSpeedSearch(tree);
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(tree);
        toolbarDecorator.setAddAction(anActionButton -> {
            FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> !fileNode.isCatalog);
        });
        toolbarDecorator.setRemoveAction(anActionButton -> {
            FileNode[] selectedNodes = tree.getSelectedNodes(FileNode.class, fileNode -> !fileNode.isCatalog);
            for (FileNode selectedNode : selectedNodes) {
                if(selectedNode.isCatalog){
                    continue;
                }
                root.remove(selectedNode);
            }
        });
        return toolbarDecorator.createPanel();
    }



    private JComponent createFieldsPanel(){
        FormBuilder formBuilder = FormBuilder.createFormBuilder();
        if(table != null && table.getFields() != null){
            for (Field field : table.getFields()) {
                JRadioButton radioButton = new JRadioButton(field.getName());
                radioButton.addActionListener(actionListener);
                formBuilder.addComponent(radioButton);
                fields.add(radioButton);
            }
        }
        return formBuilder.getPanel();
    }


    private JComponent createMethodPanel(){
        return  new JLabel();
    }


    private FileNode initTreeData() {
        FileNode root = new FileNode(table.getName(),true);
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
        return root;
    }
}
