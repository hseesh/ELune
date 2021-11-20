package com.yatoufang.core;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.yatoufang.templet.Application;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/11/19 19:45
 */
public class ConsoleCenter implements ConsoleService{
    private ConsoleView consoleView;

    private static  ConsoleCenter instance;

    public static ConsoleCenter getInstance(){
        if(instance == null){
            instance = new ConsoleCenter();
            instance.init();
        }
        return  instance;
    }

    @Override
    public void init() {
        ToolWindow toolWindow = ToolWindowManager.getInstance(Application.project).getToolWindow("ELune");
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(Application.project).getConsole();
        assert toolWindow != null;
        Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Table Scanner", true);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
        toolWindow.activate(() ->{});
    }

    @Override
    public void printHead(String info) {
        consoleView.print(info, ConsoleViewContentType.SYSTEM_OUTPUT);
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
        consoleView.print(info, ConsoleViewContentType.ERROR_OUTPUT);
    }


}
