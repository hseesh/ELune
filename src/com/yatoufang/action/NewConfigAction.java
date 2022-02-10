package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.ConfigTemplateDialog;
import org.jetbrains.annotations.NotNull;

/**
 * @author GongHuang（hse）
 * @since 2022/1/13
 */
public class NewConfigAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        VirtualFile file = event.getData(LangDataKeys.VIRTUAL_FILE);
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
        new ConfigTemplateDialog(rootPath,workSpace).show();
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
