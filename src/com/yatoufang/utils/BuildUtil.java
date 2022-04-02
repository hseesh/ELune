package com.yatoufang.utils;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.yatoufang.templet.Application;
import org.jetbrains.annotations.NotNull;

/**
 * @author GongHuang（hse）
 * @since 2022/4/1
 */
public class BuildUtil {

    private static final PsiElementFactory FACTORY = JavaPsiFacade.getInstance(Application.project).getElementFactory();

    @NotNull
    public static PsiField createField(PsiElement element, String type, String content) {
        PsiType psiType = FACTORY.createTypeFromText(type, element);
        PsiField field = FACTORY.createField(content, psiType);
        PsiUtil.setModifierProperty(field, PsiModifier.PRIVATE, true);
        return field;
    }

    @NotNull
    public static PsiMethod createMethod(PsiElement element, String type, String content) {
        PsiType psiType = FACTORY.createTypeFromText(type, element);
        return FACTORY.createMethod(content, psiType);
    }

    public static void addComment(@NotNull PsiElement element, String comment) {
        PsiComment psiComment = FACTORY.createCommentFromText(comment, null);
        element.addBefore(psiComment, element.getFirstChild());
    }


    public static void addAnnotation(@NotNull PsiField element, String content) {
        PsiModifierList modifierList = element.getModifierList();
        if(modifierList == null){
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
        if(modifierList == null){
            return;
        }
        modifierList.addAnnotation(content);
    }

    public static PsiStatement createStatement(@NotNull PsiElement context, String statement){
        return  FACTORY.createStatementFromText(statement,context);
    }

    public static PsiImportStatement createImportStatement(@NotNull PsiElement context, String type){
//        PsiType psiType = FACTORY.createTypeFromText(type, context);
//        PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
//        if(psiClass == null){
//            return null;
//        }
        PsiClass[] classWithShortName = PSIUtil.findClassWithShortName(type);
        if(classWithShortName.length == 0){
            return null;
        }
        return  FACTORY.createImportStatement(classWithShortName[0]);
    }
}
