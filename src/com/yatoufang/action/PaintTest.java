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
        selectPackage();
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

    private void selectPackage() {
        PackageChooserDialog selector = new PackageChooserDialog("Select a Package", Application.project);
        selector.show();
        PsiPackage selectedPackage = selector.getSelectedPackage();
        if (selectedPackage == null) {
            return;
        }
        PsiClass[] classes = selectedPackage.getClasses();
        HashSet<ConfigParam> params = Sets.newHashSet();
        ArrayList<ConfigParam> paramLists = Lists.newArrayList();
        ArrayList<ConfigParam> referenceList = Lists.newArrayList();
        ArrayList<String> expressionList = Lists.newArrayList();
        for (PsiClass aClass : classes) {
            if (!aClass.hasAnnotation(Annotations.DATA_FILE)) {
                NotifyService.notifyWarning(NotifyKeys.NO_PACKAGE_SELECTED);
                continue;
            }
            PsiField[] fields = aClass.getFields();
            getExpressions(expressionList, aClass);
            for (PsiField field : fields) {
                PsiType type = field.getType();
                ConfigParam param = new ConfigParam(field.getName());
                param.setTypeAlias(type.getPresentableText());
                param.setDescription(PSIUtil.getDescription(field.getDocComment()));
                if (field.hasAnnotation(Annotations.FILED_IGNORE)) {
                    param.setDefaultValue(PSIUtil.getFiledValue((PsiElement) field));
                    referenceList.add(param);
                } else {
                    paramLists.add(param);
                }
            }
            for (ConfigParam configParam : referenceList) {
                FLAG:
                for (int i = 0; i < expressionList.size(); i++) {
                    if (expressionList.get(i).contains(configParam.getName())) {
                        for (ConfigParam param : paramLists) {
                            if (expressionList.get(i).contains(param.getName())) {
                                param.setReferenceExpression(expressionList.get(i));
                            }
                            if (i > 0) {
                                int index = i;
                                if (expressionList.get(--index).contains(param.getName())) {
                                    param.setReferenceExpression(expressionList.get(index) + expressionList.get(i));
                                    param.setAliaParam(configParam);
                                    break FLAG;
                                }
                            }
                        }
                    }
                }
            }
            params.addAll(paramLists);
            paramLists.clear();
            referenceList.clear();
            expressionList.clear();
        }
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String str = gson.toJson(params);
        System.out.println(str);
        HashSet<ConfigParam> configParamHashSet = gson.fromJson(str, TypeToken.getParameterized(HashSet.class, ConfigParam.class).getType());
        System.out.println(configParamHashSet.size());
    }

    private void getExpressions(ArrayList<String> expressionList, PsiClass aClass) {
        PsiMethod method = PSIUtil.getMethodByName(aClass, ProjectKeys.INITIALIZE_METHOD);
        if (method != null) {
            PsiCodeBlock body = method.getBody();
            if (body != null) {
                PsiStatement[] statements = body.getStatements();
                for (PsiStatement statement : statements) {
                    expressionList.add(statement.getText());
                }
            }
        }
    }
}
