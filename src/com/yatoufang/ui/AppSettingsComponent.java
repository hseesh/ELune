package com.yatoufang.ui;

import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

/**
 * @author hse
 * @Date: 2021/2/3
 */
public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JCheckBox requestExample = new JCheckBox("Remove Request-Example");
    private final JCheckBox responseFields = new JCheckBox("Remove Response-Fields");
    private final JCheckBox responseExample = new JCheckBox("Remove Response-Example");


    // private final JBTextField hostInfo = new JBTextField("http host info");
    //.addComponent(new JLabel("Http Request"), 1)


    public AppSettingsComponent() {
        CodeTemplateSetting codeTemplateDialog = new CodeTemplateSetting();
        VariableTemplateSetting variableTemplateSetting = new VariableTemplateSetting();
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(new JLabel("Custom Your Document Template"))
                .addComponent(requestExample)
                .addComponent(responseFields)
                .addComponent(responseExample)
                .addComponent(new JLabel("My Variable Template"))
                .addComponent(variableTemplateSetting.getRootPane())
                .addComponent(new JLabel("My Code Template"))
                .addComponentFillVertically(codeTemplateDialog.getContent(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }


    public boolean getRequestExampleSate() {
        return requestExample.isSelected();
    }

    public boolean getResponseFieldsState() {
        return responseFields.isSelected();
    }

    public boolean getResponseExampleState() {
        return responseExample.isSelected();
    }


    public void setRequestExampleState(boolean flag) {
        requestExample.setSelected(flag);
    }

    public void setResponseFieldsState(boolean flag) {
        responseFields.setSelected(flag);
    }

    public void setResponseExample(boolean flag) {
        responseExample.setSelected(flag);
    }

}
