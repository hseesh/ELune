package com.yatoufang.preference;

import com.intellij.psi.PsiField;
import com.yatoufang.preference.impl.access.CollectionAccessHandler;
import com.yatoufang.preference.impl.access.IntegerAccessHandler;
import com.yatoufang.preference.impl.access.MapAccessHandler;

import java.util.Collection;

/**
 * @author GongHuang（hse）
 * @since 2022/10/17
 */
public class AccessPreferenceService {

    public static AccessPreferenceService instance;

    public static AccessPreferenceService getInstance() {
        if (instance == null) {
            instance = new AccessPreferenceService();
        }
        return instance;
    }

     AccessPreferenceService() {
        AccessPreferenceHandler.clear();
         new IntegerAccessHandler();
         new CollectionAccessHandler();
         new MapAccessHandler();
    }

    public Collection<String> action(PsiField psiField){
        for (AccessPreferenceHandler handler : AccessPreferenceHandler.getHandlers()) {
            if (handler.check(psiField)) {
                return handler.getExpression();
            }
        }
        return null;
    }

}
