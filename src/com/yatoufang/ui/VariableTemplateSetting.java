package com.yatoufang.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.yatoufang.config.VariableTemplateState;
import com.yatoufang.entity.Param;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class VariableTemplateSetting {

    private  JPanel panel;


    public VariableTemplateSetting() {
        VariableTemplateState instance = VariableTemplateState.getInstance();
        initPanel(instance.params);
    }

    private void initPanel(List<Param> params) {
        VariableTemplateController tableController = new VariableTemplateController(params);
        JBTable table = new JBTable(tableController);
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table);
        decorator.addExtraAction(new AnActionButton() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                VariableTemplateState instance = VariableTemplateState.getInstance();
                instance.params = tableController.getParams();
                instance.loadState(instance);
            }
        });
        panel = decorator.createPanel();
    }



    public JComponent getRootPane() {
        return panel;
    }

}
