package com.yatoufang.action;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.find.findInProject.FindInProjectManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.ui.dialog.EntityTemplateDialog;
import com.yatoufang.utils.BuildUtil;
import com.yatoufang.utils.PSIUtil;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PaintTest extends AnAction {

    private int age = 10;

    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println(--age);
        new EntityTemplateDialog("","").show();
        if(age > 0){
            return;
        }
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        PsiClass[] classes = file.getClasses();
        PsiClass aClass = classes[0];
        PsiField field = PSIUtil.createField(aClass);
        PsiMethod method = PSIUtil.createMethod(aClass);
        PsiStatement statement = BuildUtil.createStatement(method, "File file = new File()");
        PsiImportStatement importStatement = BuildUtil.createImportStatement(aClass, "File");
        PsiField[] fields = aClass.getFields();
        PsiElement prepareElement;
        PsiEnumConstant enumConstant = null;
        if (aClass.isEnum()) {
            for (PsiField psiField : fields) {
                if (psiField instanceof PsiEnumConstant) {
                    System.out.println("psiField = " + psiField);
                    enumConstant = (PsiEnumConstant) psiField;
                }
            }
        }
        assert enumConstant != null;
        BuildUtil.addAnnotation(method, "Cmd(Id = FriendCmd.ADD_FRIEND, dispatchType = DispatchType.ACTOR)");
        PsiEnumConstant finalEnumConstant = (PsiEnumConstant) enumConstant.copy();
        WriteCommandAction.runWriteCommandAction(Application.project, () -> {
            PsiCodeBlock body = method.getBody();
            if (body != null) {
                body.add(statement);
            }
            aClass.add(method);
            finalEnumConstant.setName("TEST");
            aClass.add(finalEnumConstant);
            PsiImportList importList = file.getImportList();
            importList.add(importStatement);
            JavaCodeStyleManager.getInstance(Application.project).shortenClassReferences(file);
            CodeStyleManager.getInstance(Application.project).reformat(aClass);
        });
    }

    private void test() {
        PsiExpressionStatement psiExpressionStatement;
        FindInProjectManager instance = FindInProjectManager.getInstance(Application.project);
        instance.findInProject(SimpleDataContext.getProjectContext(Application.project), null);
    }

    private static  void CMDtest() {


        GeneralCommandLine commandLine = new GeneralCommandLine("cmd", "/c", "git log origin/release/1.0.43 -10  --author= --pretty=format:\"%s\"");
        commandLine.setWorkDirectory("D:\\LYLTD");
        try {
            Process process = commandLine.createProcess();
            InputStream inputStream = process.getInputStream();
            String s = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println(s);
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        CMDtest();
    }
}
