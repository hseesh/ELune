package com.yatoufang.template

import com.intellij.codeInsight.template.*
import com.intellij.codeInsight.template.macro.MacroBase
import com.intellij.codeInsight.template.macro.MacroUtil
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import com.intellij.util.containers.ContainerUtil
import com.yatoufang.templet.Application
import com.yatoufang.templet.ProjectKeys
import com.yatoufang.utils.PSIUtil
import com.yatoufang.utils.StringUtil
import com.yatoufang.utils.TemplateUtil

/**
 * @author GongHuang（hse）
 * @since 2022/7/28
 */
class CheckMeMacro : MacroBase("checkMe", "checkMe(String)") {

    override fun calculateResult(params: Array<Expression>, context: ExpressionContext, quick: Boolean): Result? {
        val elements = getVariables(params, context)
        if (elements == null || elements.isEmpty()) {
            return null
        }
        val psiElement = elements[0];
        Application.project = context.project;
        val variableElement = psiElement as PsiVariable
        val variable = variableElement.identifyingElement?.text
        val psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod::class.java)
        val returnType = psiMethod?.returnType ?: return TextResult(TemplateUtil.ifNull(variable))
        if (variable != null) {
            if (variableElement.type.presentableText.contains(StringUtil.toUpper(ProjectKeys.RESULT))) {
                return TextResult(TemplateUtil.isFailThenReturn(variable, returnType.presentableText, variableElement.type.presentableText))
            } else {
                val psiClass = PSIUtil.findClass(variableElement.type.canonicalText) ?: return TextResult(TemplateUtil.ifNull(variable))
                val allTypes: Array<PsiClassType> = psiClass.implementsListTypes
                if (allTypes.isEmpty()) {
                    return TextResult(TemplateUtil.ifNull(variable))
                }
                val type = allTypes[0]
                if (!ProjectKeys.MODEL_ADAPTER.contains(type.name)) {
                    return TextResult(TemplateUtil.ifNull(variableElement.type.presentableText))
                }
                val method = PSIUtil.getMethodByName(psiClass, ProjectKeys.METHOD_FIND_KEY) ?: return TextResult(TemplateUtil.ifNull(variable))
                val expression = PsiTreeUtil.findChildOfType(method, PsiMethodCallExpression::class.java)
                if (expression != null) {
                    var targetStatement: PsiDeclarationStatement? = null
                    val argumentList = StringUtil.removeFirstAndLastString(expression.argumentList.text);
                    val statements = PsiTreeUtil.findChildrenOfAnyType(psiMethod, PsiDeclarationStatement::class.java)
                    for (statement in statements) {
                        if (statement.text.contains(ProjectKeys.METHOD_BUILD_KEY)) {
                            if (statement.textOffset < context.startOffset) {
                                targetStatement = statement;
                            }
                        }
                    }
                    if (targetStatement == null) {
                        return TextResult(TemplateUtil.ifNull(variable))
                    }
                    var buildText: String = StringUtil.EMPTY;
                    var callExpression = PsiTreeUtil.findChildOfType(targetStatement, PsiMethodCallExpression::class.java)
                    if (callExpression != null && callExpression.text.contains(ProjectKeys.METHOD_FIND_CONFIG)) {
                        callExpression = PsiTreeUtil.findChildOfType(callExpression, PsiMethodCallExpression::class.java)
                    }
                    if (callExpression != null) {
                        buildText = callExpression.argumentList.text
                    }
                    if (callExpression != null && buildText.startsWith(StringUtil.LEFT_ROUND_BRACKET)) {
                        buildText = StringUtil.removeFirstAndLastString(buildText)
                    }
                    return TextResult(TemplateUtil.ifNullThenReturn(variable, variableElement.type.presentableText, returnType.presentableText, argumentList, buildText))
                }
                return TextResult(TemplateUtil.ifNull(variable))
            }
        }
        return null
    }

    override fun isAcceptableInContext(context: TemplateContextType?): Boolean {
        return context is JavaCodeContextType
    }

    private fun getVariables(params: Array<Expression>, context: ExpressionContext): Array<PsiElement?>? {
        if (params.size != 1) return null
        val result = params[0].calculateResult(context) ?: return null
        val project = context.project
        val offset = context.startOffset
        val editor = context.editor ?: return null
        val array = ArrayList<PsiElement>()
        val type = MacroUtil.resultToPsiType(result, context)
        val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
        val place = file!!.findElementAt(offset)
        val variables = MacroUtil.getVariablesVisibleAt(place, "")
        val manager = PsiManager.getInstance(project)
        for (`var` in variables) {
            if (`var` is PsiField && `var`.hasModifierProperty(PsiModifier.STATIC)) {
                val varClass = `var`.containingClass!!
                val placeClass = PsiTreeUtil.getParentOfType(place, PsiClass::class.java)
                if (!manager.areElementsEquivalent(varClass, placeClass)) continue
            } else if (`var` is PsiLocalVariable) {
                val range = `var`.getNameIdentifier()!!.textRange
                if (range != null && range.contains(offset)) {
                    continue
                }
            }
            array.add(`var`)
        }
        val expressions = MacroUtil.getStandardExpressionsOfType(place, type)
        ContainerUtil.addAll(array, *expressions)
        val calcResult: MutableList<PsiElement> = ArrayList()
        val skip = listOf("true", "false", "this", "super")
        for (variable in array) {
            if (!skip.contains(variable.text)) {
                calcResult.add(variable)
            }
        }
        return PsiUtilCore.toPsiElementArray(calcResult)
    }
}