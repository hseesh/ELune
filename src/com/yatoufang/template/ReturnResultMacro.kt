package com.yatoufang.template

import com.intellij.codeInsight.template.*
import com.intellij.codeInsight.template.macro.MacroBase
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.yatoufang.templet.Application
import com.yatoufang.templet.ProjectKeys
import com.yatoufang.utils.StringUtil
import com.yatoufang.utils.TemplateUtil

/**
 * @author GongHuang（hse）
 * @since 2022/7/28
 */
class ReturnResultMacro : MacroBase("invalidParam", "invalidParam()") {

    override fun calculateResult(params: Array<Expression>, context: ExpressionContext, quick: Boolean): Result? {
        val project = context.project
        val offset = context.startOffset
        val editor = context.editor ?: return null
        Application.project = context.project;
        val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
        val place = file!!.findElementAt(offset)
        val psiMethod = PsiTreeUtil.getParentOfType(place, PsiMethod::class.java)
        val returnType = psiMethod?.returnType ?: return TextResult(TemplateUtil.RETURN_NULL)
        if (returnType.presentableText.contains(StringUtil.toUpper(ProjectKeys.RESULT))) {
            return TextResult(TemplateUtil.returnInvalidParam(returnType.presentableText))
        }
        return TextResult(TemplateUtil.RETURN_NULL)
    }

    override fun isAcceptableInContext(context: TemplateContextType?): Boolean {
        return context is JavaCodeContextType
    }

}