package com.yatoufang.action;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.intellij.execution.util.ExecUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.yatoufang.templet.Application;
import com.yatoufang.test.lang.MyLanguage;
import com.yatoufang.ui.customer.view.PaintDialog;
import org.apache.commons.compress.utils.Lists;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PaintTest extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //new PaintDialog(Application.project).show();
//        PsiFileFactory factory = PsiFileFactory.getInstance(Application.project);
//        PsiFile file = factory.createFileFromText(MyLanguage.INSTANCE, "1211");
//        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(Application.project, file.getVirtualFile(), 0);
//        FileEditorManager.getInstance(Application.project).openEditor(openFileDescriptor, true);
        ArrayList<String> commands = Lists.newArrayList();
        commands.add("C:\\WINDOWS\\system32\\cmd.exe");
        GeneralCommandLine commandLine = new GeneralCommandLine("cmd", "/c","svn status D:\\main\\doc\\程序调用表");
        try {
            String processOutput = ScriptRunnerUtil.getProcessOutput(commandLine);
            System.out.println(processOutput);
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        }
    }
}
