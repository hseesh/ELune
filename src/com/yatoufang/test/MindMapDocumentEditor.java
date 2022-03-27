package com.yatoufang.test;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.DocumentsEditor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.test.component.MindMapEditor;
import com.yatoufang.ui.customer.controller.MindmapController;
import com.yatoufang.ui.customer.model.Mindmap;
import com.yatoufang.ui.customer.model.Node;
import com.yatoufang.ui.customer.model.Option;
import com.yatoufang.ui.customer.view.MindmapView;
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
        mindMapEditor = new MindMapEditor();
    }

    @Override
    public @NotNull Document[] getDocuments() {
        return this.documents;
    }

    @Override
    public @NotNull JComponent getComponent() {
        return mindMapEditor.getComponent();
    }

    private JComponent test() {
        Option option=new Option();
        option.setMapArea(0, 0, 800, 600);
        Mindmap mindmap=new Mindmap(option);
        //init nodes
        Node root=mindmap.addRootNode("Test");
        Node model_=mindmap.addNode(root, "model");
        Node view_=mindmap.addNode(root, "view");
        Node controller_=mindmap.addNode(root, "controller");
        Node listener_=mindmap.addNode(root, "listener");
        mindmap.addNode(model_, "Mindmap");
        mindmap.addNode(model_, "Node");
        mindmap.addNode(model_, "Line");
        mindmap.addNode(model_, "Option");
        mindmap.addNode(model_, "Paintable");
        mindmap.addNode(view_, "MindmapView");
        mindmap.addNode(controller_, "MindmapController");
        mindmap.addNode(listener_, "PaintListener");
        //create view
        MindmapView view=new MindmapView();
        //create controller
        MindmapController controller=new MindmapController(view, mindmap);
        mindmap.addListener(controller);
        view.addMouseListener(controller);
        view.addMouseMotionListener(controller);
        return view;
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
