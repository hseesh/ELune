package com.yatoufang.ui.dialog;


import com.google.common.collect.Maps;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.Table;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.ExceptionUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author hse
 * @date 2021/6/5 0005
 */
public class TableTemplateDialog {

    private JSplitPane rootPane;

    private ComboBox<String> ketType;
    private JTextField tableName;

    private EditorTextField editor;

    private final VelocityService velocityService;

    private final Map<String, Field> fieldMap = Maps.newHashMap();

    private Table table;


    public TableTemplateDialog(String rootPath, String workSpace) {
        initData(workSpace,null);
        drawPanel(rootPath);
        velocityService = VelocityService.getInstance();
    }

    public TableTemplateDialog(String rootPath, String workSpace,String content) {
        initData(workSpace,content);
        drawPanel(rootPath);
        velocityService = VelocityService.getInstance();
    }

    private void drawPanel(String rootPath) {

        rootPane = new JSplitPane();
        editor.setFont(new Font(null, Font.PLAIN, 14));

        String[] listData = new String[]{"Multi Primary Key", "Single Primary Key"};
        ketType = new ComboBox<>(listData);

        tableName = new JTextField();

        JButton execute = new JButton("Execute");

        Box titlePanel = Box.createHorizontalBox();

        JPanel fieldsPanel = new JPanel();
        JPanel rightRootPanel = new JPanel(new BorderLayout());

        Collection<JCheckBox> formObjectsGroup = Lists.newArrayList();

        fieldsPanel.setPreferredSize(new Dimension(300, 900));

        ActionListener actionListener = event -> {
            Object sourceObject = event.getSource();
            if (sourceObject instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) sourceObject;
                String checkBoxName = checkBox.getName();
                table.tryAddFields(fieldMap.get(checkBoxName));
            } else if (sourceObject instanceof JButton) {
                saveFile(rootPath);
                return;
            }
            calcResult();
        };

        ketType.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                table.setMultiEntity(ketType.getSelectedIndex() == 0);
                refreshData(tableName.getText());
                calcResult();
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
                refreshData(text);
            }
        });


        drawContent(fieldsPanel, formObjectsGroup, fieldMap.values(), actionListener);


        execute.addActionListener(actionListener);
        JPanel executeDimension = new JPanel(new BorderLayout());
        executeDimension.setSize(new Dimension(300, 50));
        executeDimension.add(execute);

        titlePanel.add(tableName);
        titlePanel.add(ketType);

        JPanel leftRootPanel = FormBuilder.createFormBuilder()
                .addComponent(titlePanel)
                .addComponentFillVertically(fieldsPanel, 1)
                .addComponent(executeDimension)
                .getPanel();

        rightRootPanel.add(editor);

        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(300);
        rootPane.setLeftComponent(leftRootPanel);
        rootPane.setRightComponent(rightRootPanel);

    }

    private void refreshData(String text) {
        if (text.isEmpty()) {
            return;
        }
        table.removePrimaryField(table.getName() + "Id");
        if (table.isMultiEntity()) {
            Field object = new Field(text + "Id");
            object.setAlias("long");
            object.setAnnotation("@Column(fk = true)");
            object.setDescription(object.getName());
            table.addFields(object);
        }
        table.setName(text);
        calcResult();
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

    private void drawContent(JComponent panel, Collection<JCheckBox> checkBoxList, Collection<Field> params, ActionListener listener) {
        for (Param param : params) {
            JCheckBox checkBox = new JCheckBox(getParamInfo(param));
            checkBoxList.add(checkBox);
            checkBox.setName(param.getName());
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


    private void calcResult() {
        String result;
        StringBuilder stringBuilder = new StringBuilder("(");
        List<Field> fields = table.getFields();
        for (int i = 0; i < fields.size(); i++) {
            stringBuilder.append(fields.get(i).getAlias())
                    .append(" ")
                    .append(fields.get(i).getName());
            if (i != fields.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        table.setValueOf(stringBuilder.toString());
        if (table.isMultiEntity()) {
            result = velocityService.execute(ProjectKeys.MULTI_TEMPLATE, table);
        } else {
            result = velocityService.execute(ProjectKeys.SINGLE_TEMPLATE, table);
        }
        editor.setText(result);
    }

    private void saveFile(String rootBath) {
        ConsoleService consoleService = ConsoleService.getInstance();
        File tableFile = new File(StringUtil.buildPath(rootBath, ProjectKeys.CORE, ProjectKeys.DATABASE, ProjectKeys.TABLE,StringUtil.toUpper( table.getName(), ProjectKeys.JAVA)));
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


    public JComponent preferableFocusComponent() {
        return tableName;
    }


    private void initData(String workSpace,String content) {
        table = new Table("tableName", "");
        table.setMultiEntity(true);
        table.setWorkSpace(workSpace);
        table.setFields(new ArrayList<Field>());
        Field configId = new Field("configId");
        Field level = new Field("level");
        Field advanceLevel = new Field("advanceLevel");
        Field startLevel = new Field("startLevel");
        Field aptitude = new Field("aptitude");
        Field advanceExp = new Field("advanceExp");
        Field exp = new Field("exp");
        Field floor = new Field("floor");
        Field times = new Field("times");
        Field totalTimes = new Field("totalTimes");
        Field lastResetTime = new Field("lastResetTime");
        Field starUpHeroCost = new Field("starUpHeroCost");
        Field receiveList = new Field("receiveList");
        Field attributeAptitudeMap = new Field("attributeAptitudeMap");
        exp.setAlias("int");
        level.setAlias("int");
        floor.setAlias("int");
        configId.setAlias("int");
        aptitude.setAlias("int");
        startLevel.setAlias("int");
        advanceExp.setAlias("int");
        times.setAlias("int");
        totalTimes.setAlias("int");
        advanceLevel.setAlias("int");
        lastResetTime.setAlias("long");
        receiveList.setAlias("Collection<Long>");
        starUpHeroCost.setAlias("Collection<RewardObject>");
        attributeAptitudeMap.setAlias("Map<Integer, Integer>");

        exp.setDescription("经验");
        times.setDescription("次数");
        floor.setDescription("层级");
        totalTimes.setDescription("总次数");
        level.setDescription("等级");
        configId.setDescription("配置ID");
        aptitude.setDescription("资质");
        startLevel.setDescription("升星等级");
        advanceExp.setDescription("进阶经验");
        advanceLevel.setDescription("进阶等级");
        lastResetTime.setDescription("最后一次重置时间");
        starUpHeroCost.setDescription("养成消耗列表");
        receiveList.setDescription("奖励列表");
        receiveList.setDescription("奖励列表");
        attributeAptitudeMap.setDescription("资质属性");

        receiveList.setDefaultValue(" = Lists.newArrayList()");
        starUpHeroCost.setDefaultValue(" = Lists.newArrayList()");
        attributeAptitudeMap.setDefaultValue(" = Maps.newHashMap()");

        fieldMap.put(configId.getName(), configId);
        fieldMap.put(exp.getName(), exp);
        fieldMap.put(advanceExp.getName(), advanceExp);
        fieldMap.put(floor.getName(), floor);
        fieldMap.put(aptitude.getName(), aptitude);
        fieldMap.put(times.getName(), times);
        fieldMap.put(totalTimes.getName(), totalTimes);
        fieldMap.put(lastResetTime.getName(), lastResetTime);
        fieldMap.put(level.getName(), level);
        fieldMap.put(startLevel.getName(), startLevel);
        fieldMap.put(advanceLevel.getName(), advanceLevel);
        fieldMap.put(receiveList.getName(), receiveList);
        fieldMap.put(starUpHeroCost.getName(), starUpHeroCost);
        fieldMap.put(attributeAptitudeMap.getName(), attributeAptitudeMap);


        Field actorId = new Field("actorId");
        actorId.setAlias("long");
        actorId.setDescription("角色ID");
        actorId.setAnnotation("@Column(pk = true)");
        table.addFields(actorId);


        String text = "";
        if(content == null){
            try {
                InputStream resourceAsStream = VelocityService.class.getResourceAsStream(ProjectKeys.MULTI_TEMPLATE);
                if (resourceAsStream == null) {
                    return;
                }
                text = FileUtil.loadTextAndClose(resourceAsStream);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else{
            text = content;
        }

        editor = new EditorTextField(text, Application.project, JavaClassFileType.INSTANCE) {
            @Override
            @NotNull
            protected EditorEx createEditor() {
                EditorEx editor = super.createEditor();
                editor.setVerticalScrollbarVisible(true);
                editor.setHorizontalScrollbarVisible(true);
                editor.setOneLineMode(false);
                return editor;
            }
        };

    }

    public String getContent() {
        return editor.getText();
    }
}
