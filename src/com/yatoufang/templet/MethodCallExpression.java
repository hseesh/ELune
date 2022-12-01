package com.yatoufang.templet;

/**
 * @author GongHuang（hse）
 * @since 2022/8/22
 */
public interface MethodCallExpression {
    // PsiModifier

    String CONFIG_IN_ABSENT = "%s.computeIfAbsent(%s, k -> %s);";

    String CLEAR = "%s.clear();";

    String PUT = "put";

    String ADD = "add";

    String GET = "get";
    String SET = "set";



}
