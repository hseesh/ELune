package com.yatoufang.config;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.yatoufang.ui.PackagingFormFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author User
 */
public class PackagingToolWindow implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //        ContentFactory factory = ContentFactory.SERVICE.getInstance();
        //        PackagingFormFactory packagingFormFactory = new PackagingFormFactory();
        //        packagingFormFactory.initPanel(toolWindow, project);
        //        Content content = factory.createContent(centerPanel, "", false);
        //        toolWindow.getContentManager().addContent(content);
        initPanel(toolWindow, project);
    }

    public void initPanel(ToolWindow toolWindow, Project project) {
        PackagingFormFactory packagingFormFactory = new PackagingFormFactory();
        JComponent centerPanel = packagingFormFactory.createCenterPanel(project, toolWindow);
        ContentFactory factory = ContentFactory.SERVICE.getInstance();
        Content content = factory.createContent(centerPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
