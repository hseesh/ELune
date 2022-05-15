package com.yatoufang.ui.dialog;

import com.google.common.collect.Maps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.entity.Config;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.test.event.EditorContext;
import com.yatoufang.utils.FileWrite;
import com.yatoufang.utils.StringUtil;
import com.yatoufang.utils.SwingUtils;
import icons.Icon;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/5/15 0015
 */
public class EntityTemplateDialog extends DialogWrapper {

    private EditorTextField editor;

   private final  ArrayList<String> files = Lists.newArrayList();

    private final String rootPath;
    private String workSpace;

    private Table table;

    private boolean designerModel;

    private Map<String, String> fileMap = Maps.newHashMap();

    private final VelocityService velocityService;


    public EntityTemplateDialog(String rootPath, String workSpace) {
        super(Application.project, true);
        this.rootPath = rootPath;
        this.workSpace = workSpace;
        velocityService = VelocityService.getInstance();
        initData();
        init();
    }

    public EntityTemplateDialog(String rootPath, String workSpace, Map<String, String> fileMap) {
        super(Application.project, true);
        this.fileMap = fileMap;
        this.rootPath = rootPath;
        this.workSpace = workSpace;
        this.designerModel = true;
        this.table = EditorContext.designer.getTable();
        velocityService = VelocityService.getInstance();
        String defaultContent = fileMap.values().stream().findFirst().orElse(StringUtil.EMPTY);
        editor = SwingUtils.createEditor(defaultContent);
        editor.setFont(new Font(null, Font.PLAIN, 14));
        files.addAll(fileMap.keySet());
        init();
    }


    private void saveFile() {
        if (fileMap.size() > 0) {
            fileMap.forEach((fileName, fileContent) -> {
                String filePath = StringUtil.buildPath(rootPath, ProjectKeys.MODEL, table.getName(), fileName + ProjectKeys.JAVA);
                FileWrite.write(fileContent, filePath, true, false);
            });
        }
    }


    private void initData() {
        String text = velocityService.execute(ProjectKeys.ENTITY_TEMPLATE, new Config());
        editor = SwingUtils.createEditor(text);
        editor.setFont(new Font(null, Font.PLAIN, 14));
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        JSplitPane rootPane = new JSplitPane();

        rootPane.setMinimumSize(new Dimension(1100, 800));


        JButton execute = new JButton("Execute");

        JPanel rightRootPanel = new JPanel(new BorderLayout());

        CollectionListModel<String> listModel = new CollectionListModel<>(files);
        JBList<String> fileList = new JBList<>(listModel);


        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fileList);

        decorator.setRemoveAction(anActionButton -> {
            String selectedValue = fileList.getSelectedValue();
            fileMap.remove(selectedValue);
        });

        decorator.setEditAction(anActionButton -> {
            String name = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
            if (name != null && name.length() != 0) {
                fileMap.replace(fileList.getSelectedValue(), name);
                listModel.setElementAt(name, fileList.getSelectedIndex());
            }
        });

        decorator.addExtraAction(new AnActionButton("Edit Object Fields", "Edit", Icon.EDIT) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
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
                                table.setName(fileList.getSelectedValue());
                                table.setFields(fields);
                                editor.setText(velocityService.execute(ProjectKeys.ENTITY_TEMPLATE, table));
                            }
                        }
                    }
                };
                chooseFieldsDialog.setListener(actionListener);
                chooseFieldsDialog.show();
            }
        });

        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                fileMap.replace(fileList.getSelectedValue(), editor.getText());
            }
        });

        fileList.addListSelectionListener(e -> {
            String selectedValue = fileList.getSelectedValue();
            String temp = fileMap.get(selectedValue);
            editor.setText(temp);
        });
        execute.addActionListener(e -> {
            saveFile();
        });
        JPanel panel = decorator.createPanel();
        panel.setMinimumSize(new Dimension(300, 400));
        JPanel executeDimension = new JPanel(new BorderLayout());
        executeDimension.setSize(new Dimension(300, 50));
        executeDimension.add(execute);

        JPanel leftRootPanel = FormBuilder.createFormBuilder()
                .addComponentFillVertically(panel, 0)
                .addComponent(executeDimension)
                .getPanel();

        rightRootPanel.add(editor);

        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(300);
        rootPane.setLeftComponent(leftRootPanel);
        rootPane.setRightComponent(rightRootPanel);

        return rootPane;
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
        if (designerModel) {
            EditorContext.setDesigner(fileMap);
        }
    }

    @Override
    protected @NotNull
    JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        return new JPanel();
    }
}
