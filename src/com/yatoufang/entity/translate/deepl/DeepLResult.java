package com.yatoufang.entity.translate.deepl;

import java.util.List;

/**
 * @author hse
 * @since 2023/3/3
 */
public class DeepLResult {

    private List<Translations> translations;

    public void setTranslations(List<Translations> translations) {
        this.translations = translations;
    }

    public List<Translations> getTranslations() {
        return translations;
    }

    public String getTranslateResult(){
        if(translations != null && translations.size() > 0){
            return translations.get(0).getText();
        }
        return null;
    }



}
