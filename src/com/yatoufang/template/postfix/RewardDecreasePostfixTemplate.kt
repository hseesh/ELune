package com.yatoufang.template.postfix

import com.intellij.codeInsight.template.postfix.templates.JavaPostfixTemplateProvider
import com.intellij.codeInsight.template.postfix.templates.editable.JavaEditablePostfixTemplate
import com.intellij.codeInsight.template.postfix.templates.editable.JavaPostfixTemplateExpressionCondition
import com.intellij.pom.java.LanguageLevel

class RewardDecreasePostfixTemplate(provider: JavaPostfixTemplateProvider) : JavaEditablePostfixTemplate(
        "decrease",
        " RewardHelper.decrease(actorId, \$EXPR\$, \$END\$);",
        "() -> decrease();",
        setOf<JavaPostfixTemplateExpressionCondition>(RewardArrayCondition())
        , LanguageLevel.JDK_1_8, true, provider
) {
    override fun isBuiltin(): Boolean {
        return true
    }

}