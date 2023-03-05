package com.yatoufang.preference.impl.access;

import com.intellij.psi.PsiField;
import com.yatoufang.preference.AccessPreferenceHandler;
import com.yatoufang.preference.model.AccessContentType;

import java.util.Collection;

/**
 * @author GongHuang（hse）
 * @since 2022/10/17
 */
public class IntegerAccessHandler extends AccessPreferenceHandler {
    @Override
    public boolean check(PsiField field) {
        for (String key : getKey()) {
            if (key.equals(field.getType().getPresentableText())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AccessContentType getType() {
        return AccessContentType.INTEGER;
    }

    @Override
    public Collection<String> getExpression() {
        return getConfig(getType());
    }

    @Override
    public String[] getKey() {
        return new String[]{int.class.getName(), long.class.getName(), Integer.class.getSimpleName(), Long.class.getSimpleName(), float.class.getName(), double.class.getName()};
    }
}
