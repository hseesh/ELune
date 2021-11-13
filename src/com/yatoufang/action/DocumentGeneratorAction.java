package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;
import com.yatoufang.config.AppSettingsState;
import com.yatoufang.core.PersistentController;
import com.yatoufang.entity.Method;
import com.yatoufang.entity.Param;
import com.yatoufang.core.Parser;
import com.yatoufang.core.Psi;
import com.yatoufang.templet.*;
import com.yatoufang.ui.ExportDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hse
 * @Date: 2021/1/10
 */
public class DocumentGeneratorAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        PsiElement data = e.getData(LangDataKeys.PSI_ELEMENT);

        if (file == null) {
            NotifyService.notifyWarning("No File Selected");
            return;
        }
        Application.project = e.getProject();
        Application.setBasePackage(file.getPackageName());
        AppSettingsState settingsState = AppSettingsState.getInstance().getState();
        String baseUrl = PersistentController.readYaml(null, false);

        MarkdownGenerator markdownGenerator = new MarkdownGenerator();
        String classUrl = "";
        if (data instanceof PsiMethod) {
            for (PsiClass psiClass : file.getClasses()) {
                if (psiClass.hasAnnotation(Annotations.RESTCONTROLLER) || psiClass.hasAnnotation(Annotations.CONTROLLER)) {
                    PsiAnnotation classAnnotation = psiClass.getAnnotation(Annotations.REQUESTMAPPING);
                    classAnnotation = classAnnotation == null ? psiClass.getAnnotation(Annotations.CONTROLLER) : classAnnotation;
                    classUrl = Psi.getClassUrl(classAnnotation);
                }
                PsiMethod psiMethod = (PsiMethod) data;
                Method method = new Parser().action(psiMethod, baseUrl, classUrl);
                MarkdownGenerator generator = new MarkdownGenerator();
                List<Param> params = method.getParams();
                if (method.getDescription() == null) {
                    generator.addTitle(method.getName(), 3);
                } else {
                    generator.addTitle(method.getDescription(), 3);
                }
                if (method.getRequestType() != null) {
                    generator.addContent(method.getRequestMethod());
                }
                if (method.getUrl() != null) {
                    generator.addURL(method.getReviseUrl());
                }
                if (params.size() > 0) {
                    generator.addContent("Request-Parameters:");
                    generator.addTableHead();
                    for (Param param : params) {
                        generator.addTableRow(param.getALL());
                    }
                } else {
                    generator.addContent("Request-Parameters: null");
                }
                if (!settingsState.requestExample) {
                    if (method.getRequestExample() != null) {
                        generator.addRequestExample(method.getRequestExample());
                    }
                }
                if (!settingsState.responseFields) {
                    if ("void".equals(method.getReturnType())) {
                        generator.addContent("Response-Fields: void");
                    } else {
                        generator.addContent("Response-Fields: ");
                        generator.addResponseTable();
                        generator.addTableRow(method.getReturnList());
                        if (!settingsState.responseExample) {
                            generator.addResponseExample(method.getResponseExample());
                        }
                    }
                }
                generator.newLine();
                new ExportDialog(e.getProject(), generator.getContent(), method.getName()).show();
                break;

            }

        } else {
            for (PsiClass psiClass : file.getClasses()) {
                // remove following ifStatement for parsing all methods
                if (psiClass.hasAnnotation(Annotations.RESTCONTROLLER) || psiClass.hasAnnotation(Annotations.CONTROLLER)) {
                    PsiAnnotation classAnnotation = psiClass.getAnnotation(Annotations.REQUESTMAPPING);
                    classAnnotation = classAnnotation == null ? psiClass.getAnnotation(Annotations.CONTROLLER) : classAnnotation;
                    classUrl = Psi.getClassUrl(classAnnotation);
                    ArrayList<Method> methods = action(psiClass, baseUrl, classUrl);
                    int methodIndex = 1;
                    markdownGenerator.addTitle(psiClass.getQualifiedName(), 2);
                    for (Method method : methods) {
                        markdownGenerator.addTitle(methodIndex++ + ". " + method.getDescription(), 3);
                        markdownGenerator.addContent(method.getRequestMethod());
                        markdownGenerator.addURL(method.getReviseUrl());
                        List<Param> params = method.getParams();
                        if (params.size() > 0) {
                            markdownGenerator.addContent("Request-Parameters:");
                            markdownGenerator.addTableHead();
                            for (Param param : params) {
                                markdownGenerator.addTableRow(param.getALL());
                            }
                        } else {
                            markdownGenerator.addContent("Request-Parameters: null");
                        }
                        if (!settingsState.requestExample) {
                            markdownGenerator.addRequestExample(method.getRequestExample());
                        }

                        if (!settingsState.responseFields) {
                            if ("void".equals(method.getReturnType())) {
                                markdownGenerator.addContent("Response-Fields: void");
                            } else {
                                markdownGenerator.addContent("Response-Fields: ");
                                markdownGenerator.addResponseTable();
                                markdownGenerator.addTableRow(method.getReturnList());
                            }
                        }
                        if (!settingsState.responseExample) {
                            if (method.getReturnType() != null && !"void".equals(method.getReturnType())) {
                                markdownGenerator.addResponseExample(method.getResponseExample());
                            }
                        }
                        markdownGenerator.newLine();
                    }
                    new ExportDialog(e.getProject(), markdownGenerator.getContent(), psiClass.getQualifiedName()).show();
                }else{
                    NotifyService.notifyWarning("No Spring Annotation Scanned");
                }
            }
        }

    }


    private ArrayList<Method> action(PsiClass psiClass, String baseUrl, String classUrl) {
        PsiMethod[] allMethods = psiClass.getMethods();
        ArrayList<Method> methodArrayList = new ArrayList<>();
        for (PsiMethod psiMethod : allMethods) {
            PsiAnnotation[] annotations = psiMethod.getAnnotations();
            for (PsiAnnotation annotation : annotations) {
                if (annotation.hasQualifiedName(Annotations.REQUESTMAPPING)
                        || annotation.hasQualifiedName(Annotations.POSTMAPPING)
                        || annotation.hasQualifiedName(Annotations.GETMAPPING)
                        || annotation.hasQualifiedName(Annotations.PUTMAPPING)
                        || annotation.hasQualifiedName(Annotations.DELETEMAPPING)) {
                    methodArrayList.add(new Parser().action(psiMethod, baseUrl, classUrl));
                    break;
                }
            }
        }
        return methodArrayList;
    }
}
