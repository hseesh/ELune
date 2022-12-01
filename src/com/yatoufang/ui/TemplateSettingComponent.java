package com.yatoufang.ui;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.psi.PsiPackage;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.config.service.AppSettingService;
import com.yatoufang.templet.Application;

import javax.swing.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/23
 */
public class TemplateSettingComponent {

    private final JTextArea author = new JTextArea();
    private final JTextArea dataConfigPath = new JTextArea();

    public TemplateSettingComponent() {

    }

    public JPanel init() {
        JButton select = new JButton("Select");
        select.addActionListener(e -> {
            PackageChooserDialog selector = new PackageChooserDialog("Select a Package", Application.project);
            selector.show();
            PsiPackage selectedPackage = selector.getSelectedPackage();
            if (selectedPackage != null) {
                System.out.println("selectedPackage = " + selectedPackage.getQualifiedName());
                dataConfigPath.setText(selectedPackage.getQualifiedName());
            }
        });
        AppSettingService service = AppSettingService.getInstance();
        author.setText(service.author);
        dataConfigPath.setText(service.dataConfigPath);
        return FormBuilder.createFormBuilder()
                .addLabeledComponent("Author:", author)
                .addLabeledComponent("Config path:",dataConfigPath)
                .addLabeledComponentFillVertically("Change path:",select)
                .getPanel();
    }

    public String getAuthor() {
        return author.getText();
    }

    public String getDataConfigPath() {
        return dataConfigPath.getText();
    }
}
