package com.yatoufang.template.postfix

import com.intellij.codeInsight.template.postfix.templates.JavaPostfixTemplateProvider
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils
import com.intellij.psi.PsiElement

class PostEventPostfixTemplate(provider: JavaPostfixTemplateProvider) : StringBasedPostfixTemplate(
        "post",
        "() -> postEvent <T>",
        JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset(POST_EVENT), provider) {


    override fun getTemplateString(element: PsiElement): String {
        return "DispatchHelper.postEvent(new \$expr\$(actorId,  \$END\$)"
    }

}