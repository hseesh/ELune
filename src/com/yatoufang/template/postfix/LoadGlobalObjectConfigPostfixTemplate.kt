package com.yatoufang.template.postfix

import com.intellij.codeInsight.template.postfix.templates.JavaPostfixTemplateProvider
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils
import com.intellij.psi.PsiElement

class LoadGlobalObjectConfigPostfixTemplate(provider: JavaPostfixTemplateProvider) : StringBasedPostfixTemplate(
        "loadGlobalObject",
        "() -> findGlobalObject <T>",
        JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset(FIND_GLOBAL_CONFIG), provider) {


    override fun getTemplateString(element: PsiElement): String {
        return "globalConfigService.findGlobalObject(\$expr\$ \$END\$)"
    }

}
