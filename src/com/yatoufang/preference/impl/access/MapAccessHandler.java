package com.yatoufang.preference.impl.access;

import com.intellij.psi.PsiField;
import com.yatoufang.preference.AccessPreferenceHandler;
import com.yatoufang.preference.model.AccessContentType;

import java.util.Collection;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/10/17
 */
public class MapAccessHandler extends AccessPreferenceHandler {
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
        return AccessContentType.MAP;
    }

    @Override
    public Collection<String> getExpression() {
        return getConfig(getType());
    }


    @Override
    public String[] getKey() {
        return new String[]{Map.class.getSimpleName()};
    }
}
