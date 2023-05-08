package com.yatoufang.template.postfix

import com.intellij.codeInsight.template.postfix.templates.JavaPostfixTemplateProvider
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils
import com.intellij.psi.PsiElement

class LoadGlobalConfigPostfixTemplate(provider: JavaPostfixTemplateProvider) : StringBasedPostfixTemplate(
        "loadGlobal",
        "() -> findGlobalConfig <T>",
        JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset(FIND_GLOBAL_CONFIG), provider) {

    override fun getTemplateString(element: PsiElement): String {
        return "globalConfigService.findGlobalConfig(\$expr\$ \$END\$)"
    }
}
