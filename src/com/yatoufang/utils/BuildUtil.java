package com.yatoufang.utils;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import org.jetbrains.annotations.NotNull;

/**
 * @author GongHuang（hse）
 * @since 2022/4/1
 */
public class BuildUtil {

    @NotNull
    public static PsiField createField(PsiElement element, String type, String content) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        PsiType psiType = factory.createTypeFromText(type, element);
        PsiField field = factory.createField(content, psiType);
        PsiUtil.setModifierProperty(field, PsiModifier.PRIVATE, true);
        return field;
    }

    @NotNull
    public static PsiField createField(PsiElement element, String content) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        return factory.createFieldFromText(content,element);
    }

    @NotNull
    public static PsiMethod createMethod(PsiElement element, String type, String content) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        PsiType psiType = factory.createTypeFromText(type, element);
        return factory.createMethod(content, psiType);
    }

    @NotNull
    public static PsiMethod createMethod(PsiElement element, String content) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        return factory.createMethodFromText(content, element);
    }

    public static void addComment(@NotNull PsiElement element, String comment) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        PsiComment psiComment = factory.createCommentFromText(comment, null);
        element.addBefore(psiComment, element.getFirstChild());
    }

    public static void addAnnotation(@NotNull PsiField element, String content) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        PsiModifierList modifierList = element.getModifierList();
        if (modifierList == null) {
            return;
        }
        modifierList.addAnnotation(content);
    }

    public static void addAnnotation(@NotNull PsiMethod element, String content) {
        PsiModifierList modifierList = element.getModifierList();
        modifierList.addAnnotation(content);
    }

    public static void addAnnotation(@NotNull PsiClass element, String content) {
        PsiModifierList modifierList = element.getModifierList();
        if (modifierList == null) {
            return;
        }
        modifierList.addAnnotation(content);
    }

    public static PsiStatement createStatement(@NotNull PsiElement context, String statement) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        return factory.createStatementFromText(statement, context);
    }

    public static PsiCodeBlock createCodeBlock() {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        return null;
    }

    public static PsiImportStatement createImportStatement(@NotNull PsiElement context, String type) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        PsiClass[] classWithShortName = PSIUtil.findClassWithShortName(type);
        if (classWithShortName.length == 0) {
            return null;
        }
        return factory.createImportStatement(classWithShortName[0]);
    }

    public static PsiClass createClass(String text) {
        if (text == null) {
            return null;
        }
        PsiJavaFile fileFromText = (PsiJavaFile) PsiFileFactory.getInstance(Application.project).createFileFromText("tts.java", JavaFileType.INSTANCE, text);
        PsiClass[] classes = fileFromText.getClasses();
        if (classes.length == 0) {
            NotifyService.notifyWarning(NotifyKeys.INCORRECT);
            return null;
        }
        return classes[0];
    }

}
