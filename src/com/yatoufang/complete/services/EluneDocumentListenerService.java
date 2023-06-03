package com.yatoufang.complete.services;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.yatoufang.complete.listeners.EluneDocumentListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hse
 * @since 2023/1/23 0023
 */
public class EluneDocumentListenerService {

    private final Map<Project, EluneDocumentListener> documentListenerMap = new HashMap<>();

    public void addDocumentListenerForProject(Project project) {
        if (!documentListenerMap.containsKey(project)) {
            EluneDocumentListener documentListener = new EluneDocumentListener(project);
            documentListenerMap.put(project, documentListener);
            EditorFactory.getInstance().getEventMulticaster().addDocumentListener(documentListener, () -> {});
        }
    }

    public void removeDocumentListenerForProject(Project project) {
        EluneDocumentListener documentListener = documentListenerMap.remove(project);
        if (documentListener != null) {
            EditorFactory.getInstance().getEventMulticaster().removeDocumentListener(documentListener);
        }
    }
}
