package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.yatoufang.complete.EluneCompletionContributor;
import com.yatoufang.complete.handler.CodeCompleteHandler;
import com.yatoufang.complete.listeners.ELuneFileEditorMangerListener;
import com.yatoufang.complete.model.context.CodeCompleteTrigger;
import com.yatoufang.complete.services.CodeCompleteService;
import com.yatoufang.entity.Method;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.PSIUtil;
import org.jetbrains.annotations.NotNull;

public class CodeCompleteAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null)
            return;
        Editor editor = FileEditorManager.getInstance(event.getProject()).getSelectedTextEditor();
        if (editor == null)
            return;
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        CodeCompleteTrigger trigger = new CodeCompleteTrigger();
        Method dataBase = CodeCompleteHandler.CONTEXT.getDataBase();
        CodeCompleteService service = CodeCompleteService.getInstance();
        if (dataBase == null) {
            PsiClass aClass = DataUtil.getClass(event);
            if (aClass == null) {
                NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
                return;
            }
            new ELuneFileEditorMangerListener().parser(aClass);
        }
        PsiFile psiFile = PsiDocumentManager.getInstance(Application.project).getPsiFile(document);
        if (psiFile != null) {
            PsiElement element = psiFile.findElementAt(offset);
            if (element == null) {
                return;
            }
            trigger.setElement(element);
            PsiElement scope = element.getParent();
            while (scope != null) {
                if (scope instanceof PsiMethod) {
                    break;
                }
                scope = scope.getParent();
            }
            if (scope == null) {
                return;
            }
            scope.accept(new JavaRecursiveElementWalkingVisitor() {
                @Override
                public void visitParameterList(PsiParameterList list) {
                    PsiParameter[] parameters = list.getParameters();
                    trigger.setArguments(parameters);
                }

                @Override
                public void visitVariable(PsiVariable variable) {
                    if (variable.getTextOffset() >= offset) {
                        stopWalking();
                        return;
                    }
                    trigger.addVariables(variable);

                  PSIUtil.searchClassFields(variable.getName(), variable.getType(), trigger.getReferenceVariables(variable.getType().getPresentableText()));
                }

                @Override
                public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                    if (expression.getTextOffset() >= offset) {
                        stopWalking();
                        return;
                    }
                    PsiExpressionList argumentList = expression.getArgumentList();
                    PsiExpression[] expressions = argumentList.getExpressions();
                    for (PsiExpression psiExpression : expressions) {
                        trigger.addExpressions(psiExpression);
                    }
                }
            });
            trigger.setMethod(PSIUtil.getMethod(scope));
        }
        String result = service.action(trigger);
        trigger.clearCache();
        if (result == null || result.isEmpty()) {
            return;
        }
        EluneCompletionContributor.suggestCompletion(project, editor, document, result, offset, null);
    }
}
