package com.yatoufang.ui;

import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.yatoufang.config.CodeTemplateState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author hse
 * @date 2021/6/25 0025
 */
public class CodeTemplateSetting {

    private JSplitPane rootPane;

    public CodeTemplateSetting() {
        CodeTemplateState instance = CodeTemplateState.getInstance();
        initPanel(instance.codeMap);
    }


    private void initPanel(HashMap<String, String> codeMap) {

        ArrayList<String> arrayList = new ArrayList<>();
        final HashMap<String,String> templateMap = codeMap;
        templateMap.forEach((s, s2) -> arrayList.add(s));
        //init ui component
        rootPane = new JSplitPane();
        JPanel buttonDimension = new JPanel();

        JTextArea editor = new JTextArea();

        CollectionListModel<String> listModel = new CollectionListModel<>(arrayList);
        JBList<String> jbList = new JBList<>(listModel);


        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(jbList);

        //component controller
        decorator.setAddAction(anActionButton -> {
            String name = Messages.showInputDialog("Please input your template name", "Customer Code Template", null);
            System.out.println("str = " + name);
            if(name != null && name.length() != 0){
                listModel.add(name);
                templateMap.put(name,"");
                jbList.setSelectedIndex(listModel.getSize() - 1);
                System.out.println("listModel = " + listModel.getSize());
            }
        });

        decorator.setRemoveAction(anActionButton -> {
            String selectedValue = jbList.getSelectedValue();
            templateMap.remove(selectedValue);
        });

        decorator.setEditAction(anActionButton -> {
            String name = Messages.showInputDialog("Please input your template name", "Customer Code Template", null);
            System.out.println("str = " + name);
            if(name != null && name.length() != 0){
                templateMap.replace(jbList.getSelectedValue(),name);
            }

        });

        jbList.addListSelectionListener(e -> {
            String selectedValue = jbList.getSelectedValue();
            System.out.println("selectedValue = " + selectedValue);
            String temp = templateMap.get(selectedValue);
            editor.setText(temp);
        });


        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                templateMap.replace(jbList.getSelectedValue(), editor.getText());
                CodeTemplateState instance = CodeTemplateState.getInstance();
                instance.codeMap = templateMap;
                instance.loadState(instance);
            }
        });

        // layout component
        buttonDimension.setSize(40, 80);
        editor.setFont(new Font(null, Font.PLAIN, 15));


        rootPane.setLeftComponent(decorator.createPanel());
        rootPane.setRightComponent(editor);

        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(200);
    }


    public JSplitPane getContent() {
        return rootPane;
    }

}
