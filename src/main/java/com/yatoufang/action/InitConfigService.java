package com.yatoufang.action;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.yatoufang.entity.Struct;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.MethodCallExpression;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/8/19
 */
public class InitConfigService extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            return;
        }
        Application.project = file.getProject();
        PsiClass[] classes = file.getClasses();
        if (classes.length == 0)
            return;
        PsiClass aClass = classes[0];
        PsiReferenceList extendsList = aClass.getExtendsList();
        if (extendsList == null) {
            return;
        }
        if (!extendsList.getText().contains(ProjectKeys.CONFIG_ADAPTER)) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        PsiElement cleanElement = null, initElement = null;
        for (PsiMethod method : aClass.getMethods()) {
            if (ProjectKeys.CLEAN_METHOD.equals(method.getName())) {
                cleanElement = method.getBody();
                continue;
            }
            if (ProjectKeys.INITIALIZE_METHOD.equals(method.getName())) {
                initElement = method.getBody();
            }
        }
        if (initElement != null) {
            System.out.println(initElement.getTextRange());
        }
        if (cleanElement != null) {
            System.out.println(cleanElement.getTextRange());
        }

        List<Struct> list = Lists.newArrayList();
        for (PsiField field : aClass.getAllFields()) {
            Struct struct = new Struct(field.getName());
            StringUtil.getStructInfo(field.getType().getPresentableText(), struct);
            if (struct.getFields().size() == 0) {
                continue;
            }
            StringUtil.getInitOrder(PSIUtil.getDescription(field.getDocComment()), struct);
            if (struct.getOrder().isEmpty() || struct.getFileName().isEmpty()) {
                continue;
            }
            list.add(struct);
        }

        ConsoleService consoleService = ConsoleService.getInstance();
        StringBuilder init = new StringBuilder();
        StringBuilder clear = new StringBuilder();
        List<PsiMethod> methods = Lists.newArrayList();
        for (Struct struct : list) {
            try {
                init.append(TemplateUtil.buildInitMethod(struct)).append(StringUtil.NEW_LINE);
            } catch (Exception exception) {
                String exceptionInfo = ExceptionUtil.getExceptionInfo(exception);
                consoleService.printError(exceptionInfo);
            }
            try {
                PsiMethod method = BuildUtil.createMethod(aClass, TemplateUtil.buildAccessMethod(struct));
                methods.add(method);
            } catch (Exception exception) {
                String exceptionInfo = ExceptionUtil.getExceptionInfo(exception);
                consoleService.printError(exceptionInfo);
            }
            clear.append(String.format(MethodCallExpression.CLEAR, struct.getName())).append(StringUtil.NEW_LINE);
        }

        Document document = PsiDocumentManager.getInstance(Application.project).getDocument(file.getContainingFile());

        PsiElement finalCleanElement = cleanElement;
        PsiElement finalInitElement = initElement;
        WriteCommandAction.runWriteCommandAction(Application.project, () -> {
            for (PsiMethod method : methods) {
                aClass.add(method);
            }
            if (document == null) {
                return;
            }
            if (finalCleanElement != null) {
                document.insertString(finalCleanElement.getTextRange().getStartOffset() + 1, clear.toString());
            }
            if (finalInitElement != null) {
                document.insertString(finalInitElement.getTextRange().getStartOffset() + 1, init.toString());
            }
            PsiDocumentManager.getInstance(Application.project).doPostponedOperationsAndUnblockDocument(document);
        });

    }
}
