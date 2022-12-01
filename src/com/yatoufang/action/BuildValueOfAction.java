package com.yatoufang.action;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.utils.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/10/28
 */
public class BuildValueOfAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        PsiClass aClass = DataUtil.getClass(anActionEvent);
        if (aClass == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        PsiFieldMember[] members = PSIUtil.buildFieldMember(aClass);
        List<PsiFieldMember> fieldMembers = SwingUtils.showFieldsDialog(members, false, true);
        if (fieldMembers.isEmpty()) {
            return;
        }
        String valueOf = TemplateUtil.valueOf(aClass, fieldMembers);
        PsiMethod method = BuildUtil.createMethod(aClass, valueOf);
        WriteCommandAction.runWriteCommandAction(Application.project, () -> {
            aClass.add(method);
        });
    }
}
