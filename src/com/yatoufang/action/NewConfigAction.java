package com.yatoufang.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/13
 */
public class NewConfigAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        JBPopupFactory instance = JBPopupFactory.getInstance();

        instance.createComponentPopupBuilder(null,null)
                .setTitle("My Config")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(1100, 800))
                .setDimensionServiceKey(null, Application.PROJECT_ID + ProjectKeys.CONFIG, true)
                .createPopup()
                .showInFocusCenter();
    }
}
