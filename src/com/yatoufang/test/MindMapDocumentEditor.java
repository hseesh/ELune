package com.yatoufang.test;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.DocumentsEditor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.test.component.MindMapEditor;
import com.yatoufang.test.event.EditorContext;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.StringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * @author GongHuang（hse）
 * @since 2021/12/20
 */
public class MindMapDocumentEditor implements DocumentsEditor {

    private final VirtualFile file;
    private Document[] documents;
    private MindMapEditor mindMapEditor;

    public MindMapDocumentEditor(VirtualFile virtualFile) {
        this.file = virtualFile;
        initConfig();
    }

    private void initConfig() {
        final Document document = FileDocumentManager.getInstance().getDocument(this.file);
        this.documents = new Document[] {document};
        mindMapEditor = new  MindMapEditor();
        String canonicalPath = this.file.getCanonicalPath();
        if(canonicalPath == null){
            return;
        }
        String rootPath = StringUtil.EMPTY;
        if (canonicalPath.contains(ProjectKeys.GAME_SERVER)) {
            rootPath = DataUtil.getRootPath(canonicalPath, ProjectKeys.GAME_SERVER);
        } else if (canonicalPath.contains(ProjectKeys.WORLD_SERVER)) {
            rootPath = DataUtil.getRootPath(canonicalPath, ProjectKeys.WORLD_SERVER);
        } else if (canonicalPath.contains(ProjectKeys.BATTLE_SERVER)) {
            rootPath = DataUtil.getRootPath(canonicalPath, ProjectKeys.BATTLE_SERVER);
        }
        EditorContext.setFilePath(rootPath);
        EditorContext.setDocument(documents[0]);
    }

    @Override
    public @NotNull Document[] getDocuments() {
        return this.documents;
    }

    @Override
    public @NotNull JComponent getComponent() {
        return mindMapEditor.getComponent();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return mindMapEditor.getPreferredFocusedComponent();
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title)
    @NotNull String getName() {
         return "Mm-Editor";
    }

    @Override
    public void setState(@NotNull FileEditorState fileEditorState) {

    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public @Nullable FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override
    public void dispose() {
        mindMapEditor = null;
    }

    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {

    }

    @Override
    public @Nullable VirtualFile getFile() {
        return this.file;
    }


}
