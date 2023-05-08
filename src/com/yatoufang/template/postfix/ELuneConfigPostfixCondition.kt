package com.yatoufang.template.postfix

import com.intellij.openapi.util.Condition
import com.intellij.psi.*
import com.yatoufang.templet.ProjectKeys


val FIND_CONFIG = Condition { expression: PsiElement? ->
    if (expression !is PsiReferenceExpression && expression !is PsiMethodCallExpression) {
        return@Condition false
    } else {
        if (!expression.text.contains(ProjectKeys.CONFIG_ALIAS)) {
            return@Condition false
        }
        val ref = if (expression is PsiMethodCallExpression) expression.methodExpression else (expression as PsiReferenceExpression?)!!
        val qualifierExpression = ref.qualifierExpression
        return@Condition if (qualifierExpression != null) {
            false
        } else {
            val result = ref.advancedResolve(true)
            val element = result.element
            element == null || element is PsiClass
        }
    }
}

val FIND_GLOBAL_CONFIG = Condition { expression: PsiElement? ->
    if (expression == null) {
        return@Condition false
    }
    if (expression is PsiReferenceExpression) {
        if (expression.text.contains(ProjectKeys.CONFIG_ALIAS + ProjectKeys.KEY)) {
            return@Condition true
        }
    }
    return@Condition false;
}
