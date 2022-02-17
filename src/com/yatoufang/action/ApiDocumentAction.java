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
/**
 * @author hse
 */
public class ApiDocumentAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiElement data = e.getData(LangDataKeys.PSI_ELEMENT);
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        PsiClass[] classes = file.getClasses();
        if (classes.length == 0) return;
        PsiClass aClass = classes[0];
        String fileName = aClass.getQualifiedName();
        PsiClass superClass = aClass.getSuperClass();
        ArrayList<TcpMethod> methods = Lists.newArrayList();
        if (superClass == null || !Objects.equals(superClass.getQualifiedName(), ProjectKeys.GATE_WAY)) {
            return;
        }
        PsiMethod methodName = PSIUtil.getMethodByName(classes[0], ProjectKeys.GET_MODULE);
        if (methodName == null) {
            return;
        }
        String moduleCode = getModuleCode(methodName);
        if (data instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) data;
            PsiAnnotation cmdAnnotation = psiMethod.getAnnotation(Annotations.CMD);
            if (cmdAnnotation == null) {
                return;
            }
            parser(psiMethod, methods, moduleCode);
        } else {
            for (PsiMethod method : aClass.getMethods()) {
                PsiAnnotation cmdAnnotation = method.getAnnotation(Annotations.CMD);
                if (cmdAnnotation == null) {
                    continue;
                }
                parser(method, methods, moduleCode);
            }
        }
        MarkdownGenerator markdownGenerator = new MarkdownGenerator();
        markdownGenerator.build(methods, fileName);
        new ExportDialog(Application.project, markdownGenerator.getContent(), fileName).show();
    }

    /**
     * get module code for current handler
     *
     * @param method getModule() method
     * @return module code
     */
    private String getModuleCode(PsiMethod method) {
        PsiReturnStatement[] returnStatements = PsiUtil.findReturnStatements(method);
        for (PsiReturnStatement returnStatement : returnStatements) {
            PsiExpression returnValue = returnStatement.getReturnValue();
            if (returnValue != null) {
                PsiReference reference = returnValue.getReference();
                if (reference != null) {
                    PsiElement resolve = reference.resolve();
                    if (resolve instanceof PsiField) {
                        PsiField field = (PsiField) resolve;
                        return PSIUtil.getFiledValue(field);
                    }
                }
            }
        }
        return null;
    }

    /**
     *  parsing info for handler file
     * @param method selected method
     * @param methods  tcp protocol view object collections
     * @param moduleCode handler module code
     */
    private void parser(PsiMethod method, List<TcpMethod> methods, String moduleCode) {
        PsiAnnotation cmdAnnotation = method.getAnnotation(Annotations.CMD);
        if (cmdAnnotation == null) {
            return;
        }
        TcpMethod tcpMethod = new TcpMethod(moduleCode);
        PsiAnnotationParameterList parameterList = cmdAnnotation.getParameterList();
        PsiNameValuePair[] attributes = parameterList.getAttributes();
        for (PsiNameValuePair attribute : attributes) {
            for (PsiElement child : attribute.getChildren()) {
                if (child instanceof PsiReferenceExpression) {
                    PsiReferenceExpression expression = (PsiReferenceExpression) child;
                    PsiElement resolve = expression.resolve();
                    if (resolve instanceof PsiField) {
                        PsiField field = (PsiField) resolve;
                        tcpMethod.setCmdCode(PSIUtil.getFiledValue(field));
                        tcpMethod.setMethodName(PSIUtil.getDescription(field.getDocComment()));
                    }
                }
            }
            break;
        }
        PsiCodeBlock body = method.getBody();
        if (body == null) {
            return;
        }
        PsiStatement[] statements = body.getStatements();
        if (statements.length == 0) {
            return;
        }
        PsiStatement firstStatement = statements[0];
        if (firstStatement.getText().contains(ProjectKeys.GET_VALUE_METHOD)) {
            PsiElement firstChild = firstStatement.getFirstChild();
            if (firstChild instanceof PsiLocalVariable) {
                PsiLocalVariable localVariable = (PsiLocalVariable) firstChild;
                String jsonStr = new Parser().getResponseExample(localVariable.getType(), null);
                PsiClass aClass = PSIUtil.findClass(localVariable.getType().getCanonicalText());
                PSIUtil.getClassFields(aClass, tcpMethod.getParams(), localVariable.getType());
                tcpMethod.setContent(jsonStr);
            }
        } else {
            tcpMethod.setContent(StringUtil.EMPTY);
        }
        methods.add(tcpMethod);
    }
}
