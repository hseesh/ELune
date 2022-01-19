package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.yatoufang.core.MarkdownGenerator;
import com.yatoufang.core.Parser;
import com.yatoufang.entity.TcpMethod;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Annotations;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.ui.ExportDialog;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiDocumentAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiElement data = e.getData(LangDataKeys.PSI_ELEMENT);
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        PsiClass cacheClass = null;
        PsiClass[] classes = file.getClasses();
        if (classes.length == 0) return;
        PsiClass aClass = classes[0];
        String fileName = aClass.getQualifiedName();
        PsiClass superClass = aClass.getSuperClass();
        ArrayList<TcpMethod> methods = Lists.newArrayList();
        if (superClass == null || !Objects.equals(superClass.getQualifiedName(), ProjectKeys.GATE_WAY)) {
            return;
        }
        PsiMethod getModuleMethod = PSIUtil.getMethodByName(classes[0], ProjectKeys.GET_MODULE);
        if (getModuleMethod == null) {
            return;
        }
        String methodModule = getMethodModule(getModuleMethod);
        if (data instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) data;
            PsiAnnotation cmdAnnotation = psiMethod.getAnnotation(Annotations.CMD);
            if (cmdAnnotation == null) {
                return;
            }
            parser(psiMethod, methods, methodModule, cacheClass);
        } else {
            for (PsiMethod method : aClass.getMethods()) {
                PsiAnnotation cmdAnnotation = method.getAnnotation(Annotations.CMD);
                if (cmdAnnotation == null) {
                    continue;
                }
                cacheClass = parser(method, methods, methodModule, cacheClass);
            }
        }
        MarkdownGenerator markdownGenerator = new MarkdownGenerator();
        markdownGenerator.build(methods, fileName);
        new ExportDialog(Application.project, markdownGenerator.getContent(), fileName).show();
    }


    private PsiClass searchInfo(PsiClass cacheClass, PsiAnnotationMemberValue idAttribute, TcpMethod tcpMethod) {
        String text = idAttribute.getText();
        String[] attribute = text.split("\\.");
        if (cacheClass == null || !Objects.equals(cacheClass.getName(), attribute[0])) {
            PsiClass[] classWithShortName = PSIUtil.findClassWithShortName(attribute[0]);
            for (PsiClass psiClass : classWithShortName) {
                if (psiClass != null) {
                    cacheClass = psiClass;
                }
            }
        }
        if (cacheClass == null) {
            return cacheClass;
        }
        PsiField field = PSIUtil.getField(cacheClass, attribute[1]);
        if (field == null) {
            return cacheClass;
        }
        String value = PSIUtil.getFiledValue(field);
        tcpMethod.setCmdCode(value);
        tcpMethod.setMethodName(PSIUtil.getDescription(field.getDocComment()));
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
            return cacheClass;
        }
        PsiAnnotationMemberValue idAttribute = cmdAnnotation.findAttributeValue("Id");
        if (idAttribute == null) {
            return cacheClass;
        }
        TcpMethod tcpMethod = new TcpMethod(methodModule);
        cacheClass = searchInfo(cacheClass, idAttribute, tcpMethod);
        PsiCodeBlock body = method.getBody();
        if (body == null) {
            return cacheClass;
        }
        PsiStatement[] statements = body.getStatements();
        if (statements.length == 0) {
            return cacheClass;
        }
        PsiStatement firstStatement = statements[0];
        if (firstStatement.getText().contains(ProjectKeys.GET_VALUE_METHOD)) {
            PsiElement firstChild = firstStatement.getFirstChild();
            if (firstChild instanceof PsiLocalVariable) {
                PsiLocalVariable localVariable = (PsiLocalVariable) firstChild;
                String jsonStr = new Parser().getResponseExample(localVariable.getType(), null);
                PsiClass aClass = PSIUtil.findClass(localVariable.getType().getCanonicalText());
                PSIUtil.getClassFields(aClass,tcpMethod.getParams(),localVariable.getType());
                tcpMethod.setContent(jsonStr);
            }
        } else {
            tcpMethod.setContent(StringUtil.EMPTY);
        }
        methods.add(tcpMethod);
        return cacheClass;
    }
}
