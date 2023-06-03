package com.yatoufang.complete.listeners;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.yatoufang.complete.services.EluneDocumentListenerService;
import org.jetbrains.annotations.NotNull;


/**
 * @author hse
 * @since 2023/1/23 0023
 */
public class EluneProjectManagerListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        EluneDocumentListenerService service = project.getService(EluneDocumentListenerService.class);
        service.addDocumentListenerForProject(project);
    }

    @Override
    public void projectClosed(@NotNull Project project) {
        EluneDocumentListenerService service = project.getService(EluneDocumentListenerService.class);
        service.removeDocumentListenerForProject(project);
    }
}
