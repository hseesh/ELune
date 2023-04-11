package com.yatoufang.template

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.*

/**
 * @author GongHuang（hse）
 * @since 2022/5/28
 */
class MyTemplateContext : TemplateContextType("ELUNE", "Elune") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        val isInContext = templateActionContext.file is PsiJavaFile
        if (isInContext.not()) {
            return false;
        }
        val file = templateActionContext.file
        val startOffset = templateActionContext.startOffset
        val element: PsiElement? = file.findElementAt(startOffset)
        val parent = element?.parent
        return parent is PsiJavaCodeReferenceElement || parent is PsiParameterList
    }
}