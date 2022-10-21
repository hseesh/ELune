package com.yatoufang.utils;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.EditorTextField;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/1/20
 */
public class SwingUtils {

    public static EditorTextField createEditor(String text) {
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

    public static void createPreviewWindow(String text) {
        JBPopupFactory instance = JBPopupFactory.getInstance();
        EditorTextField editor = createEditor(text);
        editor.setFont(new Font(null, Font.PLAIN, 15));
        instance.createComponentPopupBuilder(editor, null)
                .setTitle("Preview Code")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(true)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(300, 400))
                .createPopup()
                .showInFocusCenter();
    }

    public static List<PsiFieldMember> showFieldsDialog(PsiFieldMember[] elements, boolean allowEmptySelection, boolean allowMultiSelection) {
        MemberChooser<PsiFieldMember> dialog = new MemberChooser<>(elements, allowEmptySelection, allowMultiSelection, Application.project);
        dialog.setCopyJavadocVisible(false);
        dialog.setTitle("NotifyKeys.SELECT_FIELDS");
        dialog.show();
        if (MemberChooser.CANCEL_EXIT_CODE == dialog.getExitCode()) {
            return Collections.emptyList();
        }
        return dialog.getSelectedElements();

    }
}
