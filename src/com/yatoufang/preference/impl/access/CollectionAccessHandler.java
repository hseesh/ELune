package com.yatoufang.preference.impl.access;

import com.intellij.psi.PsiField;
import com.yatoufang.preference.AccessPreferenceHandler;
import com.yatoufang.preference.model.AccessContentType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author GongHuang（hse）
 * @since 2022/10/17
 */
public class CollectionAccessHandler extends AccessPreferenceHandler {
    @Override
    public boolean check(PsiField field) {
        for (String key : getKey()) {
            if (field.getType().getPresentableText().startsWith(key)) {
                return true;
            }

        }
        return false;
    }

    @Override
    public AccessContentType getType() {
        return AccessContentType.COLLECTION;
    }

    @Override
    public Collection<String> getExpression() {
        return getConfig(getType());
    }

    @Override
    public String[] getKey() {
        return new String[]{Collection.class.getSimpleName(), ArrayList.class.getSimpleName(), List.class.getSimpleName(), Set.class.getSimpleName()};
    }
}
