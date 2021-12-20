package com.yatoufang.test.lang;

import com.intellij.lang.Language;

/**
 * @author GongHuang（hse）
 * @since 2021/12/20
 */
public class MyLanguage extends Language {
    public static final MyLanguage INSTANCE = new MyLanguage();

    public MyLanguage() {
        super("NBMindMap", "text/x-nbmmd+plain");
    }
}
