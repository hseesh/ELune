package com.yatoufang.ui;


import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.ui.EditorTextField;
import com.yatoufang.config.CodeTemplateState;
import com.yatoufang.config.VariableTemplateState;
import com.yatoufang.core.VelocityService;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.TemplateMethod;
import com.yatoufang.templet.Application;
import com.yatoufang.utils.PSIUtil;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hse
 * @date 2021/6/5 0005
 */
public class TableTemplaterDialog {

    private JSplitPane rootPane;

    private EditorTextField editor;

    private final List<String> result;

    private VelocityService velocityService;


    public TableTemplaterDialog(FileType fileType) {
        result = Lists.newArrayList();
        ArrayList<String> fields = Lists.newArrayList();
        fields.add("actorId");
        fields.add("advanceLevel");
        fields.add("startLevel");
        fields.add("level");
        drawPanel(fields, fileType);
        velocityService = VelocityService.getInstance();
    }

    private void drawPanel(List<String> formObjects,FileType fileType) {

        rootPane = new JSplitPane();

        editor = new EditorTextField(Application.project, fileType);
        editor.setFont(new Font(null, Font.PLAIN, 15));


        JLabel entityTitle = new JLabel("Current File Objects");
        JButton execute = new JButton("Execute");

        Box content = Box.createVerticalBox();

        Box entityTitlePanel = Box.createHorizontalBox();

        JPanel entityPanel = new JPanel();
        JPanel leftRootPanel = new JPanel(new BorderLayout());
        JPanel rightRootPanel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel();


        entityPanel.setPreferredSize(new Dimension(300, ((formObjects.size() / 4) + 1) * 50));

        ButtonGroup formObjectsGroup = new ButtonGroup();

        ActionListener actionListener = event -> {
            Object sourceObject = event.getSource();
            if (sourceObject instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) sourceObject;
                String text = radioButton.getText();
                if(!result.remove(text)){
                    result.add(text);
                }
            } else if (sourceObject instanceof JButton) {
                JButton button = (JButton) sourceObject;
                buttonController(button.getText());
            }
        };


        drawContent(entityPanel, formObjectsGroup, formObjects, actionListener);

        execute.addActionListener(actionListener);

        entityTitlePanel.add(entityTitle, BorderLayout.CENTER);


        content.add(entityTitlePanel);
        content.add(entityPanel);
        content.add(fieldsPanel);

        leftRootPanel.add(execute, BorderLayout.SOUTH);
        leftRootPanel.add(content);

        rightRootPanel.add(editor);

        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(600);

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

    private void drawContent(JComponent panel, ArrayList<JCheckBox> checkBoxList, List<Param> params, ActionListener listener) {
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

    private void buttonController(String text) {
        if ("Execute".equals(text)) {
            execute();
        }
    }

    private void execute() {
        System.out.println("params = " + result);
        editor.setText(result.toString());
    }


    public JComponent getRootPanel() {
        return rootPane;
    }


}
