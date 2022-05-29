package com.yatoufang.template

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
/**
 * @author GongHuang（hse）
 * @since 2022/5/28
 */
class MyTemplateContext : TemplateContextType("OTHER", "Elune"){
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean = true
}