package com.yatoufang.service;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Application;
import com.yatoufang.ui.dialog.ModuleGeneratorDialog;
import icons.Icon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/11/19 19:45
 */
public class ConsoleService implements Console {

    private ConsoleView consoleView;

    public Table tableObject;

    private static ConsoleService instance;

    private ConsoleService packagedInstance;

    private ConsoleService(){

    }

    public static ConsoleService getInstance(Table table, String rootPath) {
        if (instance == null) {
            instance = new ConsoleService();
            instance.init(table, rootPath);
        }
        return instance;
    }

    public static ConsoleService getInstance() {
        if (instance == null) {
            instance = new ConsoleService();
            instance.init();
        }
        return instance;
    }

    public ConsoleService getInstance(Project project) {
        if (packagedInstance == null) {
            packagedInstance = new ConsoleService();
            packagedInstance.init(project);
        }
        return packagedInstance;
    }

    public void init(Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("ELune");
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        if (toolWindow == null) {
            return;
        }
        Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "PackagedConsole", true);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
        toolWindow.activate(() -> {
        });
    }

    @Override
    public void init() {
        ToolWindow toolWindow = ToolWindowManager.getInstance(Application.project).getToolWindow("ELune");
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(Application.project).getConsole();
        if (toolWindow == null) {
            return;
        }
        Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Table Scanner", true);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
        toolWindow.activate(() -> {
        });
    }

    @Override
    public void init(Table table, String rootPath) {
        tableObject = table;
        ToolWindow toolWindow = ToolWindowManager.getInstance(Application.project).getToolWindow("ELune");
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(Application.project).getConsole();
        if (toolWindow == null) {
            return;
        }
        JPanel panel = new JPanel(new BorderLayout());
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.addAction(new AnAction("CLear All", "CLear", Icon.CLEAR) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                consoleView.clear();
            }
        });
        actionGroup.addAction(new AnAction("Run Module Generator", "Run", Icon.RUN) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                new ModuleGeneratorDialog(tableObject, rootPath).show();
            }
        });
        panel.add(consoleView.getComponent(), BorderLayout.CENTER);
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("Table Scanner", actionGroup, false);
        panel.add(toolbar.getComponent(), BorderLayout.WEST);
        Content content = toolWindow.getContentManager().getFactory().createContent(panel, "Table Scanner", true);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
        toolWindow.activate(() -> {
        });
    }

    @Override
    public void printHead(String info) {
        String result = "\n" + info + "\n";
        consoleView.print(result, ConsoleViewContentType.SYSTEM_OUTPUT);
    }

    @Override
    public void print(String info) {
        consoleView.print(info, ConsoleViewContentType.NORMAL_OUTPUT);
    }

    @Override
    public void printInfo(String info) {
        consoleView.print(info, ConsoleViewContentType.LOG_DEBUG_OUTPUT);
    }

    @Override
    public void printError(String info) {
        String result = "\n" + info + "\n";
        consoleView.print(result, ConsoleViewContentType.ERROR_OUTPUT);
    }

}
