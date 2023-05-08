package com.yatoufang.template.postfix

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.macro.SuggestVariableNameMacro
import com.intellij.codeInsight.template.postfix.templates.JavaPostfixTemplateProvider
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils
import com.intellij.psi.PsiElement


class LoadConfigPostfixTemplate(provider: JavaPostfixTemplateProvider) : StringBasedPostfixTemplate(
        "load",
        "() -> findConfig <T>",
        JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset(FIND_CONFIG), provider) {


    override fun getTemplateString(element: PsiElement): String {
        return "\$expr\$ \$varName\$ = globalConfigService.findConfig(IdentiyKey.build(\$END\$), \$expr\$.class);"
    }

    override fun setVariables(template: Template, element: PsiElement) {
        super.setVariables(template, element)
        val nameMacro = MacroCallNode(SuggestVariableNameMacro())
        template.addVariable("varName", nameMacro, nameMacro, true)
    }
}
