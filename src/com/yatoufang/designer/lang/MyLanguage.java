package com.yatoufang.designer.lang;

import com.intellij.lang.Language;

/**
 * @author GongHuang（hse）
 * @since 2021/12/20
 */
public class MyLanguage extends Language {
    public static final MyLanguage INSTANCE = new MyLanguage();

    public MyLanguage() {
        super("MindMap");
    }
}
