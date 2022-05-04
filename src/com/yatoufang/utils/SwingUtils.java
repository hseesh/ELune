package com.yatoufang.utils;

import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.EditorTextField;
import com.yatoufang.templet.Application;
import com.yatoufang.ui.TableTemplateDialog;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

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

    public static void previewCode(String text){
        JBPopupFactory instance = JBPopupFactory.getInstance();
        instance.createComponentPopupBuilder(createEditor(text),null)
                .setTitle("Preview Code")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(700, 500))
                .createPopup()
                .showInFocusCenter();
    }
}
