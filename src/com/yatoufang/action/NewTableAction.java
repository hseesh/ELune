package com.yatoufang.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.ui.TableTemplaterDialog;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class NewTableAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = e.getData(LangDataKeys.VIRTUAL_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        String rootPath;
        String workSpace;
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_MODULE_SELECTED);
            return;
        }
        if (canonicalPath.contains(ProjectKeys.GAME_SERVER)) {
            workSpace = ProjectKeys.GAME_SERVER;
            rootPath = getRootPath(canonicalPath, ProjectKeys.GAME_SERVER);
        } else if (canonicalPath.contains(ProjectKeys.WORLD_SERVER)) {
            workSpace = ProjectKeys.WORLD_SERVER;
            rootPath = getRootPath(canonicalPath, ProjectKeys.WORLD_SERVER);
        } else if (canonicalPath.contains(ProjectKeys.BATTLE_SERVER)) {
            workSpace = ProjectKeys.BATTLE_SERVER;
            rootPath = getRootPath(canonicalPath, ProjectKeys.BATTLE_SERVER);
        }else{
            NotifyService.notifyWarning(NotifyKeys.NO_MODULE_SELECTED);
            return;
        }
        JBPopupFactory instance = JBPopupFactory.getInstance();
        TableTemplaterDialog dialog = new TableTemplaterDialog(rootPath,workSpace);
        instance.createComponentPopupBuilder(dialog.getRootPanel(), dialog.preferableFocusComponent())
                .setTitle("My Table")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(1100, 800))
                .setDimensionServiceKey(null, Application.PROJECT_ID + ProjectKeys.TABLE, true)
                .createPopup()
                .showInFocusCenter();
    }



    @NotNull
    private String getRootPath(String canonicalPath, String key) {
        String[] split = canonicalPath.split(key);
        if (split.length == 1 || split.length == 2) {
            return split[0] + key + "/src/main/java/cn/daxiang/lyltd/" + key;
        }else if (split.length == 3) {
            return split[0] + key + split[1] + key;
        }
        return null;
    }
}
