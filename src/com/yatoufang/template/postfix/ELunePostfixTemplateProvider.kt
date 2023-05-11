package com.yatoufang.template.postfix

import com.intellij.codeInsight.template.postfix.templates.*
import com.intellij.util.containers.ContainerUtil
import com.yatoufang.templet.Application

class ELunePostfixTemplateProvider : JavaPostfixTemplateProvider() {

    private val template: Set<PostfixTemplate> = ContainerUtil.newHashSet(LoadConfigPostfixTemplate(this), LoadGlobalConfigPostfixTemplate(this),
            LoadGlobalObjectConfigPostfixTemplate(this), SendRewardPostfixTemplate(this), RewardDecreasePostfixTemplate(this),
            PostEventPostfixTemplate(this))

    override fun getId(): String {
        return Application.PROJECT_NAME
    }

    override fun getTemplates(): Set<PostfixTemplate?> {
        return template
    }
}