package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.yatoufang.service.TranslateService;
import com.yatoufang.templet.Annotations;
import com.yatoufang.templet.Application;
import com.yatoufang.thread.ELuneScheduledThreadPoolExecutor;
import com.yatoufang.utils.*;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author GongHuang（hse）
 * @since 2023/3/30
 */
public class AutoTranslateAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        PsiClass aClass = DataUtil.getClass(anActionEvent);
        if (aClass == null) {
            return;
        }
        int count = 0;
        ELuneScheduledThreadPoolExecutor executor = ELuneScheduledThreadPoolExecutor.getInstance();
        for (PsiMethod method : aClass.getMethods()) {
            if (method.getAnnotations().length > 0) {
                continue;
            }
            String description = PSIUtil.getDescription(method.getDocComment());
            if (description == null || description.isEmpty()) {
                String camelCase = StringUtil.splitCamelCase(method.getName());
                executor.schedule(() -> {
                    String result = TranslateService.translate(camelCase, false, false);
                    if (result == null || result.isEmpty()) {
                        return;
                    }
                    String comment = String.format(TemplateUtil.COMMENT, result);
                    WriteCommandAction.runWriteCommandAction(Application.project, () -> {
                        BuildUtil.addComment(method, comment);
                    });
                }, (long) KeyEvent.VK_DELETE * Calendar.HOUR *  count++, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
