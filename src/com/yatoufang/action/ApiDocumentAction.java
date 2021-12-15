package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.yatoufang.core.MarkdownGenerator;
import com.yatoufang.core.Parser;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.TcpMethod;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Annotations;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.templet.SystemKeys;
import com.yatoufang.ui.ExportDialog;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiDocumentAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(SystemKeys.NO_FILE_SELECTED);
            return;
        }
        ArrayList<TcpMethod> methods = Lists.newArrayList();
        PsiClass cacheClass = null;
        PsiClass[] classes = file.getClasses();
        String fileName = StringUtil.EMPTY;
        for (PsiClass aClass : classes) {
            PsiClass superClass = aClass.getSuperClass();
            if (superClass == null || !Objects.equals(superClass.getQualifiedName(), ProjectKeys.GATE_WAY)) {
                return;
            }
            String methodModule = null;
            for (PsiMethod method : aClass.getMethods()) {
                PsiAnnotation cmdAnnotation = method.getAnnotation(Annotations.CMD);
                if (cmdAnnotation == null) {
                    if (method.getName().equals(ProjectKeys.GET_MODULE)) {
                        methodModule = getMethodModule(method);
                    }
                }
                cacheClass = parser(method, methods, methodModule, cacheClass);
            }
            fileName = aClass.getQualifiedName();
            break;
        }
        MarkdownGenerator markdownGenerator = new MarkdownGenerator();
        markdownGenerator.build(methods, fileName);
        new ExportDialog(Application.project, markdownGenerator.getContent(), fileName).show();
    }

    @NotNull
    private PsiClass searchInfo(PsiClass cacheClass, PsiAnnotationMemberValue idAttribute, TcpMethod tcpMethod) {
        String text = idAttribute.getText();
        String[] split = text.split("\\.");
        if (cacheClass == null || !Objects.equals(cacheClass.getName(), split[0])) {
            PsiClass[] classWithShortName = PSIUtil.findClassWithShortName(split[0]);
            for (PsiClass psiClass : classWithShortName) {
                if (psiClass != null) {
                    cacheClass = psiClass;
                }
            }
        }
        assert cacheClass != null;
        PsiField[] allFields = cacheClass.getAllFields();
        for (PsiField psiField : allFields) {
            String name = psiField.getName();
            if (!name.equals(split[1])) {
                continue;
            }
            PsiElement[] elements = psiField.getChildren();
            for (PsiElement element : elements) {
                if (element instanceof PsiLiteralExpression) {
                    String value = element.getText();
                    tcpMethod.setCmdCode(value);
                    tcpMethod.setMethodName(PSIUtil.getDescription(psiField.getDocComment()));
                }
            }
        }
        return cacheClass;
    }

    private String getMethodModule(PsiMethod method) {
        PsiReturnStatement[] returnStatements = PsiUtil.findReturnStatements(method);
        for (PsiReturnStatement returnStatement : returnStatements) {
            PsiExpression returnValue = returnStatement.getReturnValue();
            if (returnValue == null) {
                continue;
            }
            String text = returnValue.getText();
            String[] split = text.split("\\.");
            if (split.length != 2) {
                continue;
            }
            return PSIUtil.getFieldsValue(ProjectKeys.MODULE_NAME, split[1]);
        }
        return null;
    }

    private PsiClass parser(PsiMethod method, List<TcpMethod> methods, String methodModule, PsiClass cacheClass) {
        PsiAnnotation cmdAnnotation = method.getAnnotation(Annotations.CMD);
        if (cmdAnnotation == null) {
            return null;
        }
        PsiAnnotationMemberValue idAttribute = cmdAnnotation.findAttributeValue("Id");
        if (idAttribute == null) {
            return null;
        }
        TcpMethod tcpMethod = new TcpMethod(methodModule);
        cacheClass = searchInfo(cacheClass, idAttribute, tcpMethod);
        PsiCodeBlock body = method.getBody();
        if (body == null) {
            return null;
        }
        PsiStatement[] statements = body.getStatements();
        if (statements.length == 0) {
            return null;
        }
        PsiStatement firstStatement = statements[0];
        if (firstStatement.getText().contains(ProjectKeys.GET_VALUE)) {
            PsiElement firstChild = firstStatement.getFirstChild();
            if (firstChild instanceof PsiLocalVariable) {
                PsiLocalVariable localVariable = (PsiLocalVariable) firstChild;
                String jsonStr = new Parser().getResponseExample(localVariable.getType(), null);
                tcpMethod.setContent(jsonStr);
            }
        } else {
            tcpMethod.setContent(StringUtil.EMPTY);
        }
        methods.add(tcpMethod);
        return cacheClass;
    }
}
