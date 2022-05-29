package com.yatoufang.template

import com.intellij.codeInsight.template.*
import com.intellij.codeInsight.template.macro.MacroBase
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import com.yatoufang.templet.Application
import com.yatoufang.templet.ProjectKeys
import com.yatoufang.utils.PSIUtil
import com.yatoufang.utils.StringUtil
import com.yatoufang.utils.TemplateUtil

/**
 * @author GongHuang（hse）
 * @since 2022/5/28
 */
class ValueTMacro : MacroBase("valueT", "valueT(String)") {

    override fun calculateResult(params: Array<out Expression>, context: ExpressionContext?, quick: Boolean): Result? {
        val editor = context?.editor as EditorEx
        val file = PsiManager.getInstance(context.project).findFile(editor.virtualFile) as PsiJavaFile
        val psiClass = file.classes[0] ?: return null
        if (psiClass.name?.endsWith(ProjectKeys.VO_ALIAS) == true) {
            Application.project = context.project;
            val targetName = psiClass.name?.replace(ProjectKeys.VO_ALIAS, StringUtil.EMPTY)
            val targetClass = PSIUtil.findClassWithShortName(targetName)
            val text = TemplateUtil.valueOf(file.classes, targetClass)
            return text?.let { TextResult(it) }
        }
        return null
    }

    override fun isAcceptableInContext(context: TemplateContextType?): Boolean {
        return context is JavaCodeContextType
    }
}