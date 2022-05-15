package com.yatoufang.test;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.test.filetype.MindMapFileType;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2021/12/20
 */
public class MindMapDocumentEditorProvider implements FileEditorProvider, DumbAware {

    private final Map<VirtualFile, FileEditor> fileFileEditorMap = new HashMap<>();

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        return virtualFile.getFileType() instanceof MindMapFileType;
    }

    @Override
    public @NotNull
    FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        if (!fileFileEditorMap.containsKey(virtualFile)) {
            synchronized (MindMapDocumentEditorProvider.class) {
                if (!fileFileEditorMap.containsKey(virtualFile)) {
                    fileFileEditorMap.put(virtualFile, new MindMapDocumentEditor(virtualFile));
                }
            }
        }
        return fileFileEditorMap.get(virtualFile);
    }

    @Override
    public void disposeEditor(@NotNull FileEditor editor) {
        FileEditorProvider.super.disposeEditor(editor);
    }


    @Override
    public @NotNull
    @NonNls
    String getEditorTypeId() {
        return "com.yatoufang.editor.MindMapDocumentEditor";
    }

    @Override
    public @NotNull
    FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }


}
