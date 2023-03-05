package com.yatoufang.entity.translate.deepl;

/**
 * @author hse
 * @since 2023/3/3
 */
public class Translations {

    private String detected_source_language;
    private String text;

    public void setDetected_source_language(String detected_source_language) {
        this.detected_source_language = detected_source_language;
    }

    public String getDetected_source_language() {
        return detected_source_language;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
