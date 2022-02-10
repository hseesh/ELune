package com.yatoufang.action;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.intellij.execution.util.ExecUtil;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.psi.*;
import com.yatoufang.entity.ConfigParam;
import com.yatoufang.entity.MyCookie;
import com.yatoufang.entity.Param;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Annotations;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.test.lang.MyLanguage;
import com.yatoufang.ui.ModuleGeneratorDialog;
import com.yatoufang.ui.customer.view.PaintDialog;
import com.yatoufang.utils.PSIUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PaintTest extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        new ModuleGeneratorDialog().show();
    }


    private void test() {
        PsiExpressionStatement psiExpressionStatement;
    }

    private void CMDtest() {
        ArrayList<String> commands = Lists.newArrayList();
        commands.add("C:\\WINDOWS\\system32\\cmd.exe");
        GeneralCommandLine commandLine = new GeneralCommandLine("cmd", "/c", "svn status D:\\main\\doc\\程序调用表");
        try {
            String processOutput = ScriptRunnerUtil.getProcessOutput(commandLine);
            System.out.println(processOutput);
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        }
    }


}
