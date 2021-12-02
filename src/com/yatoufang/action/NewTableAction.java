package com.yatoufang.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.yatoufang.templet.NotifyService;
import com.yatoufang.templet.ProjectKey;
import com.yatoufang.ui.TableTemplaterDialog;

import java.awt.*;
import java.util.Objects;

public class NewTableAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile file = (PsiFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning("No File Selected");
            return;
        }
        JBPopupFactory instance = JBPopupFactory.getInstance();
        TableTemplaterDialog dialog = new TableTemplaterDialog(file.getFileType());
        instance.createComponentPopupBuilder(dialog.getRootPanel(), null)
                .setTitle("My Table")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(1600, 1600))
                .createPopup()
                .showInFocusCenter();
    }

    private String getRootPath(PsiFile file) {
        VirtualFile virtualFile = file.getVirtualFile();

        return "rootPath";
    }
}
