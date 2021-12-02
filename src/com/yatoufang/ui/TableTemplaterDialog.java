package com.yatoufang.ui;


import com.google.common.collect.Maps;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.EditorTextField;
import com.yatoufang.core.ConsoleService;
import com.yatoufang.core.VelocityService;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKey;
import com.yatoufang.utils.ExceptionUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hse
 * @date 2021/6/5 0005
 */
public class TableTemplaterDialog {

    private JSplitPane rootPane;

    private ComboBox<String> ketType;
    private JTextField tableName;

    private EditorTextField editor;


    private VelocityService velocityService;

    private Map<String, Field> fieldMap = Maps.newHashMap();

    private Table table;


    public TableTemplaterDialog(FileType fileType) {
        initData();
        drawPanel(fileType);
        velocityService = VelocityService.getInstance();
    }

    private void drawPanel(FileType fileType) {

        rootPane = new JSplitPane();

        editor = new EditorTextField(Application.project, JavaClassFileType.INSTANCE);
        editor.setFont(new Font(null, Font.PLAIN, 15));

        String[] listData = new String[]{"Multi Primary Key", "Single Primary Key"};
        ketType = new ComboBox<>(listData);

        tableName = new JTextField();
        tableName.setSize(new Dimension(50, 100));

        JButton execute = new JButton("Execute");

        Box content = Box.createVerticalBox();

        Box entityTitlePanel = Box.createHorizontalBox();

        JPanel entityPanel = new JPanel();
        JPanel leftRootPanel = new JPanel(new BorderLayout());
        JPanel rightRootPanel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel();


        entityPanel.setPreferredSize(new Dimension(300, ((table.getFields().size() / 4) + 1) * 50));

        ArrayList<JCheckBox> formObjectsGroup = Lists.newArrayList();

        ActionListener actionListener = event -> {
            Object sourceObject = event.getSource();
            if (sourceObject instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) sourceObject;
                String text = checkBox.getText();
                table.addFields(fieldMap.get(text));
            }else if(sourceObject instanceof JButton){
                saveFile();
            }
            calc();
        };

        ketType.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                table.setMultiEntity(ketType.getSelectedIndex() == 0);
            }
        });

        tableName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = tableName.getText();
                if (text.isEmpty()) {
                    return;
                }
                table.setName(text);
            }
        });


        drawContent(entityPanel, formObjectsGroup, table.getFields(), actionListener);

        execute.addActionListener(actionListener);

        entityTitlePanel.add(tableName);
        entityTitlePanel.add(ketType);

        content.add(entityTitlePanel);
        content.add(entityPanel);
        content.add(fieldsPanel);

        leftRootPanel.add(execute, BorderLayout.SOUTH);
        leftRootPanel.add(content);

        rightRootPanel.add(editor);

        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(300);

        rootPane.setLeftComponent(leftRootPanel);
        rootPane.setRightComponent(rightRootPanel);

    }


    private void drawContent(JComponent panel, ButtonGroup buttonGroup, List<String> tags, ActionListener listener) {
        for (int i = 0; i < tags.size(); i++) {
            JRadioButton radioButton = new JRadioButton(tags.get(i));
            if (i == 0) {
                radioButton.setSelected(true);
            }
            radioButton.addActionListener(listener);
            buttonGroup.add(radioButton);
            panel.add(radioButton);

        }
    }

    private void drawContent(JComponent panel, ArrayList<JCheckBox> checkBoxList, List<Field> params, ActionListener listener) {
        for (Param param : params) {
            JCheckBox checkBox = new JCheckBox(getParamInfo(param));
            checkBoxList.add(checkBox);
            checkBox.addActionListener(listener);
            panel.add(checkBox);
        }
    }

    private String getParamInfo(Param param) {
        if (param.getDescription() != null && param.getDescription().length() != 0) {
            return param.getName() + "(" + param.getDescription() + ")";
        } else {
            return param.getName();
        }
    }


    private void calc() {
        String result = "";
        if(table.isMultiEntity()){
             result = velocityService.execute(ProjectKey.MULTI_TEMPLATE, table);
        }else{
            result = velocityService.execute(ProjectKey.SINGLE_TEMPLATE, table);
        }
        editor.setText(result);
    }

    private void saveFile() {
        ConsoleService consoleService = ConsoleService.getInstance();
        File tableFile = new File(StringUtil.buildPath("targetPath", ProjectKey.RESPONSE, StringUtil.toUpper(table.getName(), ProjectKey.REWARD, ProjectKey.RESULT, ProjectKey.RESPONSE, ProjectKey.JAVA)));
        try {
            FileUtil.writeToFile(tableFile, editor.getText());
            consoleService.print(tableFile.getCanonicalPath() + " created successfully\n");
        } catch (IOException e) {
            consoleService.printError(ExceptionUtil.getExceptionInfo(e));
        }
    }


    public JComponent getRootPanel() {
        return rootPane;
    }


    private void initData() {
        table = new Table("tableName", "");
        ArrayList<Field> fields = Lists.newArrayList();

        Field configId = new Field("configId");
        Field level = new Field("level");
        Field advanceLevel = new Field("advanceLevel");
        Field startLevel = new Field("startLevel");
        Field aptitude = new Field("aptitude");
        Field advanceExp = new Field("advanceExp");
        Field exp = new Field("exp");
        exp.setAlias("int");
        level.setAlias("int");
        configId.setAlias("int");
        aptitude.setAlias("int");
        startLevel.setAlias("int");
        advanceExp.setAlias("int");
        advanceLevel.setAlias("int");

        fieldMap.put(exp.getName(), exp);
        fieldMap.put(level.getName(), level);
        fieldMap.put(configId.getName(), configId);
        fieldMap.put(aptitude.getName(), aptitude);
        fieldMap.put(startLevel.getName(), startLevel);
        fieldMap.put(advanceExp.getName(), advanceExp);
        fieldMap.put(advanceLevel.getName(), advanceLevel);

    }

}
