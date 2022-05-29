package com.yatoufang.template

import com.intellij.codeInsight.template.*
import com.intellij.codeInsight.template.macro.MacroBase
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import com.yatoufang.utils.PSIUtil

/**
 * @author GongHuang（hse）
 * @since 2022/5/28
 */
class ValueOfMacro : MacroBase("valueOf", "valueOf(String)") {

    override fun calculateResult(params: Array<out Expression>, context: ExpressionContext?, quick: Boolean): Result? {
        val editor = context?.editor as EditorEx
        val file = PsiManager.getInstance(context.project).findFile(editor.virtualFile) as PsiJavaFile
        val text = PSIUtil.getClassValueOf(file.classes)
        return text?.let { TextResult(it) }
    }

    override fun isAcceptableInContext(context: TemplateContextType?): Boolean {
        return context is JavaCodeContextType
    }
}