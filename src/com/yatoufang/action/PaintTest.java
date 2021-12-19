package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yatoufang.templet.Application;
import com.yatoufang.ui.customer.view.PaintDialog;

public class PaintTest extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        new PaintDialog(Application.project).show();
    }
}
