package com.yatoufang.ui.customer.view;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.ui.customer.controller.MindmapController;
import com.yatoufang.ui.customer.model.Mindmap;
import com.yatoufang.ui.customer.model.Node;
import com.yatoufang.ui.customer.model.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author hse
 * @Date: 2021/1/25
 */
public class PaintDialog extends DialogWrapper {

    public PaintDialog(@Nullable Project project) {
        super(project);
        init();
        setTitle("Export File");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
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



}