package com.yatoufang.action;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.utils.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/8/19
 */
public class FieldsCopyAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiClass aClass = DataUtil.getClass(e);
        if (aClass == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        TreeClassChooserFactory instance = TreeClassChooserFactory.getInstance(Application.project);
        TreeClassChooser selector = instance.createAllProjectScopeChooser(NotifyKeys.SELECT_FILE);
        selector.showDialog();
        PsiClass selectedClass = selector.getSelected();
        if (selectedClass == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        PsiFieldMember[] members = PSIUtil.buildFieldMember(selectedClass);
        List<PsiFieldMember> fieldMembers = SwingUtils.showFieldsDialog(members, false, true);
        if (fieldMembers.isEmpty()) {
            return;
        }
        PsiFieldMember[] fieldsMember = fieldMembers.toArray(new PsiFieldMember[0]);
        List<PsiFieldMember> initMember = SwingUtils.showFieldsDialog(fieldsMember, false, true);
        if (initMember == null) {
            return;
        }
        String valueOf = TemplateUtil.valueOf(aClass, selectedClass, initMember);
        PsiMethod method = BuildUtil.createMethod(aClass, valueOf);
        WriteCommandAction.runWriteCommandAction(Application.project, () -> {
            for (PsiFieldMember member : fieldMembers) {
                PsiField field = (PsiField) member.getElement().copy();
                PsiAnnotation[] annotations = field.getAnnotations();
                for (PsiAnnotation annotation : annotations) {
                    annotation.delete();
                }
                aClass.add(field);
            }
            aClass.add(method);
        });

    }

}
