package com.yatoufang.template

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiJavaFile

/**
 * @author GongHuang（hse）
 * @since 2022/5/28
 */
class MyTemplateContext : TemplateContextType("ELUNE", "Elune"){
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
       return templateActionContext.file is PsiJavaFile;
    }
}