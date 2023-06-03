package com.yatoufang.complete.listeners;

import com.intellij.ide.DataManager;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.yatoufang.complete.services.EluneDocumentListenerService;
import org.jetbrains.annotations.NotNull;


/**
 * @author hse
 * @since 2023/1/23 0023
 */
public class EluneDynamicPluginListener implements DynamicPluginListener {

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        DataManager.getInstance().getDataContextFromFocusAsync().then(dataContext -> {
            Project project = dataContext.getData(CommonDataKeys.PROJECT);
            if (project != null) {
                project.getService(EluneDocumentListenerService.class).addDocumentListenerForProject(project);
            }
            return dataContext;
        });
    }
}
