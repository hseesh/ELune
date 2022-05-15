package com.yatoufang.ui.dialog;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.yatoufang.templet.Application;
import com.yatoufang.service.NotifyService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author hse
 * @Date: 2021/1/25
 */
public class DocumentExportDialog extends DialogWrapper {

    private final String content;
    private final String fileName;

    public DocumentExportDialog(@Nullable Project project, String content, String fileName) {
        super(project);
        this.content = content;
        this.fileName = fileName;
        init();
        setTitle("Export File");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JBScrollPane scrollPane = new JBScrollPane();
        JTextArea textArea = new JTextArea();
        textArea.setText(this.content);
        scrollPane.setViewportView(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 700));
        return scrollPane;
    }

//    @Override
//    protected @Nullable JComponent createNorthPanel() {
//        JPanel jPanel = new JPanel();
//        JButton markdown = new JButton("MarkDown");
//        JButton word = new JButton("Word");
//
//        markdown.addActionListener(e -> {
//
//        });
//        word.addActionListener(e -> {
//
//        });
//
//        jPanel.add(markdown);
//        jPanel.add(word);
//        return jPanel;
//    }

    @NotNull
    @Override
    protected JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        JPanel jPanel = new JPanel();
        JButton copyButton = new JButton("Copy");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Close");
        jPanel.add(copyButton);
        jPanel.add(saveButton);
        jPanel.add(cancelButton);

        copyButton.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(content);
            clipboard.setContents(selection, null);
            dispose();
        });

        saveButton.addActionListener(e -> saveFile());
        cancelButton.addActionListener(e -> dispose());
        return jPanel;
    }

    private void saveFile(){
        String selectPath = getSelectedPath();
        if(selectPath != null){
            File file = new File(selectPath + "\\" +fileName + ".md");
            try {
                FileWriter fileWriter = new FileWriter(file,false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(content);
                bufferedWriter.flush();
                bufferedWriter.close();
                fileWriter.close();
                NotifyService.notify("File Successfully Saved");
            } catch (IOException error) {
                error.printStackTrace();
            }
        }

        dispose();
    }

    private String getSelectedPath() {
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(fileChooserDescriptor, Application.project, null);
        for (VirtualFile virtualFile : virtualFiles) {
            return virtualFile.getPath();
        }
        return null;
    }

}