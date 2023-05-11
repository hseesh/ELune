package com.yatoufang.template.postfix

import com.intellij.codeInsight.template.postfix.templates.JavaPostfixTemplateProvider
import com.intellij.codeInsight.template.postfix.templates.editable.JavaEditablePostfixTemplate
import com.intellij.codeInsight.template.postfix.templates.editable.JavaPostfixTemplateExpressionCondition
import com.intellij.pom.java.LanguageLevel

class SendRewardPostfixTemplate(provider: JavaPostfixTemplateProvider) : JavaEditablePostfixTemplate(
        "send",
        " RewardHelper.sendRewardList(actorId, \$EXPR\$, \$END\$);",
        " RewardHelper.sendRewardList();",
        setOf<JavaPostfixTemplateExpressionCondition>(RewardArrayCondition())
        , LanguageLevel.JDK_1_8, true, provider


) {
    override fun isBuiltin(): Boolean {
        return true
    }

}