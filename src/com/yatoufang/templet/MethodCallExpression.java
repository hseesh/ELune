package com.yatoufang.templet;

/**
 * @author GongHuang（hse）
 * @since 2022/8/22
 */
public interface MethodCallExpression {
    // PsiModifier

    String CONFIG_IN_ABSENT = "%s.computeIfAbsent(%s, k -> %s);";

    String IF_ABSENT = "%s.computeIfAbsent(%s, k -> %s)";
    String CLEAR = "%s.clear();";

    String VALUES = "%s.values();";

    String PUT = "put";

    String ADD = "add";

    String GET = "get";

    String SET = "set";

    String NEW_CON_MAP = "Maps.newConcurrentMap()";

    String GET_CACHE = "this.get(%s.class, %s);";

    String UPDATE_CACHE  = "this.update(%s, %s);";

    String PUT_HASH_OBJECT = "putHashObject(cacheKey, String.valueOf(%s), %s);";

    String CLEAR_SORT_SET = "clearSortSet(cacheKey);";

    String ADD_LIST_OBJECT = "addListObject(cacheKey, list);";


}
