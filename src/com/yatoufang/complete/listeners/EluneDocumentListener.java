package com.yatoufang.complete.listeners;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.complete.EluneCompletionContributor;
import org.jetbrains.annotations.NotNull;

/**
 * @author hse
 * @since 2023/1/23 0023
 */
public class EluneDocumentListener implements DocumentListener {

    private final Project project;

    public EluneDocumentListener(Project project) {
        this.project = project;
    }


    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        Document doc = event.getDocument();

        VirtualFile file = FileDocumentManager.getInstance().getFile(doc);
        if (file == null || !file.isInLocalFileSystem()) return;

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) return;

        String text = doc.getText();
        if (text.isBlank()) return;

        int offset = editor.getCaretModel().getOffset();
        if (offset >= text.length()) return;

        int offsetPlusOne = offset + 1;

        System.out.println( text.charAt(offset));
        System.out.println();

        if (offset > 0) {
            return;
        }
        EluneCompletionContributor.suggestCompletion(project, editor, doc, text, offsetPlusOne, null);
    }
}
