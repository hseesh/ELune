package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yatoufang.ui.ConfigTemplateDialog;
import org.jetbrains.annotations.NotNull;

/**
 * @author GongHuang（hse）
 * @since 2022/1/13
 */
public class NewConfigAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        new ConfigTemplateDialog("","").show();
    }
}
