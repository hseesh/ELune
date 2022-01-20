package com.yatoufang.utils;

import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.ui.EditorTextField;
import com.yatoufang.templet.Application;
import org.jetbrains.annotations.NotNull;

/**
 * @author GongHuang（hse）
 * @since 2022/1/20
 */
public class SwingUtils {

    public static EditorTextField createEditor(String text){
        return new EditorTextField(text, Application.project, JavaClassFileType.INSTANCE) {
            @Override
            @NotNull
            protected EditorEx createEditor() {
                EditorEx editor = super.createEditor();
                editor.setVerticalScrollbarVisible(true);
                editor.setHorizontalScrollbarVisible(true);
                editor.setOneLineMode(false);
                return editor;
            }
        };
    }
}
