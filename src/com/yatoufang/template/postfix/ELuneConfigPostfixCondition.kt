package com.yatoufang.template.postfix

import com.intellij.codeInsight.template.postfix.templates.editable.JavaPostfixTemplateExpressionCondition
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils
import com.intellij.openapi.util.Condition
import com.intellij.psi.*
import com.intellij.psi.util.InheritanceUtil
import com.yatoufang.templet.ProjectKeys
import com.yatoufang.utils.StringUtil
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.w3c.dom.events.Event


val FIND_CONFIG = Condition { expression: PsiElement? ->
    if (expression !is PsiReferenceExpression && expression !is PsiMethodCallExpression) {
        return@Condition false
    } else {
        if (!expression.text.contains(ProjectKeys.CONFIG_ALIAS)) {
            return@Condition false
        }
        return@Condition inContext(expression)
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


val POST_EVENT = Condition { expression: PsiElement? ->
    if (expression !is PsiReferenceExpression && expression !is PsiMethodCallExpression) {
        return@Condition false
    } else {
        if (!expression.text.contains(Event::class.java.simpleName)) {
            return@Condition false
        }
        return@Condition inContext(expression)
    }
}

private fun inContext(expression: PsiElement?): Boolean {
    val ref = if (expression is PsiMethodCallExpression) expression.methodExpression else (expression as PsiReferenceExpression?)!!
    val qualifierExpression = ref.qualifierExpression
    return if (qualifierExpression != null) {
        false
    } else {
        val result = ref.advancedResolve(true)
        val element = result.element
        element == null || element is PsiClass
    }
}


class RewardArrayCondition : JavaPostfixTemplateExpressionCondition {
    override fun value(element: PsiExpression): Boolean {
        return element.type?.canonicalText?.contains(ProjectKeys.REWARD_OBJECT + StringUtil.GRATE_THEN) ?: false
    }

    override fun getId(): String {
        return ID
    }

    @Nls
    override fun getPresentableName(): String {
        return ID
    }

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else {
            other != null && this.javaClass == other.javaClass
        }
    }

    override fun hashCode(): Int {
        return this.javaClass.hashCode()
    }

    companion object {
        @NonNls
        val ID = "RewardArray"
    }
}