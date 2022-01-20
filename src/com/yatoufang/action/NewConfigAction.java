package com.yatoufang.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.ConfigTemplateDialog;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

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
