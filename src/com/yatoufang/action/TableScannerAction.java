package com.yatoufang.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.yatoufang.core.Parser;
import com.yatoufang.core.Psi;
import com.yatoufang.entity.HttpState;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyService;
import com.yatoufang.templet.SpringAnnotation;
import com.yatoufang.ui.ExecuteDialog;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author hse
 * @Date: 2021/3/15 0015
 */
public class TableScannerAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if(file == null){
            NotifyService.notifyWarning("No File Selected");
            return;
        }
        PsiClass[] classes = file.getClasses();
        if(classes.length == 0) return;
        for (PsiClass aClass : classes) {

        }


//        JBPopupFactory instance = JBPopupFactory.getInstance();
//        ExecuteDialog executeDialog = new ExecuteDialog(methods);
//        instance.createComponentPopupBuilder(executeDialog.getContent(), null)
//                .setTitle("My POST")
//                .setMovable(true)
//                .setResizable(true)
//                .setCancelOnClickOutside(false)
//                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
//                .setRequestFocus(true)
//                .setMinSize(new Dimension(900, 450))
//                .setDimensionServiceKey(null, "com.yatoufang.http.popup", true)
//                .createPopup()
//                .showInFocusCenter();
    }
}
