package com.yatoufang.designer;

import com.google.common.collect.Maps;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.designer.component.MindMapEditor;
import com.yatoufang.editor.component.RootPanel;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.StringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2021/12/20
 */
public class MindMapDocumentEditor implements DocumentsEditor {

    private final VirtualFile file;
    private Document[] documents;
    private MindMapEditor mindMapEditor;
    private final RootPanel rootPanel = new RootPanel();
    private FileEditorState fileEditorState;
    private final Map userdata = Maps.newHashMap();

    public MindMapDocumentEditor(VirtualFile virtualFile) {
        this.file = virtualFile;
        initConfig();
    }

    private void initConfig() {
        final Document document = FileDocumentManager.getInstance().getDocument(this.file);
        this.documents = new Document[]{document};
        String canonicalPath = this.file.getCanonicalPath();
        if (canonicalPath == null) {
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
        RootPanel.getDrawingSurface().getModel().setBasePath(rootPath);
    }

    @Override
    public @NotNull
    Document[] getDocuments() {
        return this.documents;
    }

    @Override
    public @NotNull
    JComponent getComponent() {
        return rootPanel;
    }

    @Override
    public @Nullable
    JComponent getPreferredFocusedComponent() {
        return rootPanel;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title)
    @NotNull
    String getName() {
        return "Mm-Editor";
    }

    @Override
    public void setState(@NotNull FileEditorState fileEditorState) {
        this.fileEditorState = fileEditorState;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public @Nullable
    FileEditorLocation getCurrentLocation() {
        return new FileEditorLocation() {
            @Override
            public @NotNull
            FileEditor getEditor() {
                return MindMapDocumentEditor.this;
            }

            @Override
            public int compareTo(@NotNull FileEditorLocation o) {
                return o.compareTo(this);
            }
        };
    }

    @Override
    public void dispose() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getUserData(@NotNull Key<T> key) {
        return (T) userdata.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {
        userdata.put(key, t);
    }

    @Override
    public @Nullable
    VirtualFile getFile() {
        return this.file;
    }


}
