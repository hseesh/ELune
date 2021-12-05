package com.yatoufang.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.yatoufang.entity.HttpState;
import com.yatoufang.core.Parser;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.templet.Application;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Annotations;
import com.yatoufang.ui.ExecuteDialog;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author hse
 * @Date: 2021/3/15 0015
 */
public class HttpRequestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (e.getProject() == null) return;
        Application.project = e.getProject();
        PsiElement data = e.getData(LangDataKeys.PSI_ELEMENT);
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if(file == null){
            NotifyService.notifyWarning("No File Selected");
            return;
        }

        ArrayList<HttpState> methods = new ArrayList<>();
        Application.setBasePackage(file.getPackageName());

        if (data instanceof PsiMethod) {
            String classUrl = "";
            for (PsiClass psiClass : file.getClasses()) {
                if (psiClass.hasAnnotation(Annotations.RESTCONTROLLER) || psiClass.hasAnnotation(Annotations.CONTROLLER)) {
                    PsiAnnotation classAnnotation = psiClass.getAnnotation(Annotations.REQUESTMAPPING);
                    classAnnotation = classAnnotation == null ? psiClass.getAnnotation(Annotations.CONTROLLER) : classAnnotation;
                    classUrl = PSIUtil.getClassUrl(classAnnotation);
                }
                PsiMethod psiMethod = (PsiMethod) data;
                HttpState httpState = new Parser().action(psiMethod, classUrl);
                methods.add(httpState);
            }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               

        }

        if(methods.size() == 0){
            methods.add(new HttpState());
        }

        JBPopupFactory instance = JBPopupFactory.getInstance();
        ExecuteDialog executeDialog = new ExecuteDialog(methods);
        instance.createComponentPopupBuilder(executeDialog.getContent(), null)
                .setTitle("My POST")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(900, 450))
                .setDimensionServiceKey(null, "com.yatoufang.http.popup", true)
                .createPopup()
                .showInFocusCenter();
    }
}
