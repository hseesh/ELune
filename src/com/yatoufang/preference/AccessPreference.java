package com.yatoufang.preference;

import com.intellij.psi.PsiField;
import com.yatoufang.preference.model.AccessContentType;

import java.util.Collection;

/**
 * @author GongHuang（hse）
 * @since 2022/10/17
 */
public interface  AccessPreference {

    boolean check(PsiField field);

    AccessContentType getType();

    Collection<String> getExpression();

    String[] getKey();
}
