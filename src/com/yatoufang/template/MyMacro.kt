package com.yatoufang.template

import com.intellij.codeInsight.template.*
import com.intellij.codeInsight.template.macro.MacroBase
/**
 * @author GongHuang（hse）
 * @since 2022/5/28
 */
class MyMacro : MacroBase("OTHER", "dds") {

    override fun calculateResult(params: Array<out Expression>, context: ExpressionContext?, quick: Boolean): Result? {
        val text = getTextResult(params, context, false);
        if (text == null || text.isEmpty()) {
            return null;
        }
        println(text);
        return TextResult(text);
    }

    override fun isAcceptableInContext(context: TemplateContextType?): Boolean {
        return true;
    }
}