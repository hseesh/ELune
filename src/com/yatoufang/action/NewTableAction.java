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
import com.yatoufang.templet.ProjectKey;
import com.yatoufang.ui.TableTemplaterDialog;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class NewTableAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = e.getData(LangDataKeys.VIRTUAL_FILE);
        if (file == null) {
            NotifyService.notifyWarning("No File Selected");
            return;
        }
        String rootPath;
        String workSpace;
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null) {
            return;
        }
        if (canonicalPath.contains(ProjectKey.GAME_SERVER)) {
            workSpace = ProjectKey.GAME_SERVER;
            rootPath = getRootPath(canonicalPath, ProjectKey.GAME_SERVER);
        } else if (canonicalPath.contains(ProjectKey.WORLD_SERVER)) {
            workSpace = ProjectKey.WORLD_SERVER;
            rootPath = getRootPath(canonicalPath, ProjectKey.WORLD_SERVER);
        } else if (canonicalPath.contains(ProjectKey.BATTLE_SERVER)) {
            workSpace = ProjectKey.BATTLE_SERVER;
            rootPath = getRootPath(canonicalPath, ProjectKey.BATTLE_SERVER);
        }else{
            NotifyService.notifyWarning("No Module Selected");
            return;
        }
        JBPopupFactory instance = JBPopupFactory.getInstance();
        TableTemplaterDialog dialog = new TableTemplaterDialog(rootPath,workSpace);
        instance.createComponentPopupBuilder(dialog.getRootPanel(), null)
                .setTitle("My Table")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(1100, 800))
                .setDimensionServiceKey(null, Application.PROJECT_ID + ProjectKey.TABLE, true)
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
