package com.yatoufang.template

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiWhiteSpace

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
        val flag = element is PsiWhiteSpace
        if (flag.not()){
            if (element != null) {
                if(element.text.endsWith("zzz")){
                    return true
                }
            }
            return false
        }
        return flag
    }
}