package com.yatoufang.designer;

import com.google.common.collect.Maps;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.editor.Model;
import com.yatoufang.editor.component.RootPanel;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.StringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author GongHuang（hse）
 * @since 2021/12/20
 */
public class MindMapDocumentEditor implements DocumentsEditor {

    private final VirtualFile file;
    private Document[] documents;
    private RootPanel rootPanel;
    private Map<Object, Object> userdata = Maps.newHashMap();

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
            rootPath = DataUtil.getWorkSpace(canonicalPath, ProjectKeys.GAME_SERVER);
        } else if (canonicalPath.contains(ProjectKeys.WORLD_SERVER)) {
            rootPath = DataUtil.getWorkSpace(canonicalPath, ProjectKeys.WORLD_SERVER);
        } else if (canonicalPath.contains(ProjectKeys.BATTLE_SERVER)) {
            rootPath = DataUtil.getWorkSpace(canonicalPath, ProjectKeys.BATTLE_SERVER);
        }
        if (this.documents[0].getText().length() == 0) {
            Model model = new Model();
            rootPanel = new RootPanel(model);
            model.setFilePath(file.getPath());
            model.setBasePath(rootPath);
            return;
        }
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file.getPath()));
            Model model = (Model) (inputStream.readObject());
            inputStream.close();
            if (model == null) {
                model = new Model();
            }
            Model finalModel = model;
            String finalRootPath = rootPath;
            rootPanel = new RootPanel(model);
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (Application.project == null) {
                        return;
                    }
                    ApplicationManager.getApplication().runReadAction(() ->{
                        finalModel.setFilePath(file.getPath());
                        finalModel.setBasePath(finalRootPath);
                        finalModel.addListeners();
                        finalModel.addComponents();
                        finalModel.updateAfterOpened();
                    });
                    timer.cancel();
                }
            };
            timer.scheduleAtFixedRate(timerTask, TimeUnit.SECONDS.toMillis(Calendar.APRIL), TimeUnit.SECONDS.toMillis(Calendar.APRIL));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {
        userdata.put(key, t);
    }

    @Override
    public @Nullable
    VirtualFile getFile() {
        return this.file;
    }


}
