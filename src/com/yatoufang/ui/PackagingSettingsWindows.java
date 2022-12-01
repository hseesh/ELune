package com.yatoufang.ui;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.config.service.PackagingSettingsService;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: Gary
 * @Date 2022-04-26 14:14
 * @Description:
 */
public class PackagingSettingsWindows {
    private JBTextField urlText;
    private JBTextField tokenText;

    public JPanel init() {
        var jPanel = new JPanel();
        JBLabel jbLabel = new JBLabel("插件设置");
        jbLabel.setFont(new Font(null, Font.LAYOUT_LEFT_TO_RIGHT, 30));
        jPanel.add(jbLabel);
        urlText = new JBTextField("");
        tokenText = new JBTextField("");
        PackagingSettingsService service = PackagingSettingsService.getInstance();
        urlText.setText(service.url);
        tokenText.setText(service.token);
        return FormBuilder.createFormBuilder().addComponent(jPanel).addLabeledComponent("URL:", urlText).addLabeledComponent("Token:", tokenText).getPanel();
    }

    public String getUrlText() {
        return urlText.getText();
    }

    public String getTokenText() {
        return tokenText.getText();
    }
}
