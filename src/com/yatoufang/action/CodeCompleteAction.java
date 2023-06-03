package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.yatoufang.complete.EluneCompletionContributor;
import com.yatoufang.entity.Param;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class CodeCompleteAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) return;

        Editor editor = FileEditorManager.getInstance(event.getProject()).getSelectedTextEditor();
        if (editor == null) return;

        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(document);
        if (file == null) {
            return;
        }
        PsiElement element = file.findElementAt(offset);
        var psiMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        if (psiMethod == null) {
            return;
        }
        PsiMethodCallExpression callExpression = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression.class);
        if (callExpression == null) {
            return;
        }
        PsiMethod callMethod = callExpression.resolveMethod();
        if (callMethod == null) {
            return;
        }
        Collection<Param> origin = PSIUtil.getParams(callMethod.getParameterList());
        final Set<Param> elements = Sets.newHashSet();
        psiMethod.accept(new JavaRecursiveElementWalkingVisitor() {
            @Override
            public void visitLocalVariable(PsiLocalVariable variable) {
                Param param = new Param(variable.getName());
                param.setType(variable.getType());
                elements.add(param);
                // todo actorId, idolConfig.getCostList(), OperationType.IDOL_LEVEL_UP
            }
        });
        elements.addAll(PSIUtil.getParams(psiMethod.getParameterList()));
        Set<Param> superParam = PSIUtil.loadSuperFields(elements);
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Param param : origin) {
            if (index != 0) {
                builder.append(StringUtil.COMMA).append(StringUtil.SPACE);
            }
            if (elements.contains(param)) {
                builder.append(param.getName());
            }else{
                Optional<Param> optional = superParam.stream().filter(e -> e.getTypeAlias().equals(param.getTypeAlias())).findFirst();
                if (optional.isPresent()) {
                    builder.append(param.getName());
                }
            }
            ++ index;
        }
        if (builder.length() == 0) {
            return;
        }
        EluneCompletionContributor.suggestCompletion(project, editor, document, builder.toString(), offset, null);
    }
}
