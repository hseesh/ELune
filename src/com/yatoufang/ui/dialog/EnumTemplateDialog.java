package com.yatoufang.ui.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBSlider;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.entity.EnumClass;
import com.yatoufang.entity.Param;
import com.yatoufang.service.TranslateService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;
import com.yatoufang.utils.SwingUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

/**
 * @author GongHuang（hse）
 * @since 2022/12/8
 */
public class EnumTemplateDialog extends DialogWrapper {

    private final String rootPath;

    private final String workSpace;

    private EditorTextField editor;

    private final VelocityService velocityService;


    public String fileName;

    private final EnumClass enumClass = new EnumClass();

    public EnumTemplateDialog(String rootPath, String workSpace, String content) {
        super(Application.project, true);
        this.rootPath = rootPath;
        this.workSpace = workSpace;
        editor = SwingUtils.createEditor(content);
        editor.setFont(new Font(null, Font.PLAIN, 14));
        velocityService = VelocityService.getInstance();
        init();
        String fileName = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
        if (fileName == null || fileName.isEmpty()) {
            dispose();
            return;
        }
        enumClass.setName("Test");
        this.fileName = fileName;
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        JSplitPane rootPane = new JSplitPane();
        rootPane.setMinimumSize(new Dimension(900, 700));
        JTextArea configData = new JTextArea();
        configData.setRows(15);
        configData.setFont(new Font(null, Font.PLAIN, 14));
        configData.setMinimumSize(new Dimension(300, 100));
        TranslateService instance = TranslateService.getInstance();

        configData.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = configData.getText().replace(StringUtil.NEW_LINE, StringUtil.EQUAL);
                if (text.isEmpty()) {
                    return;
                }
                String result = instance.action(text);
                if (result == null) {
                    return;
                }
                result = result.toUpperCase(Locale.ROOT);
                String[] meta = text.replaceAll(String.valueOf(StringUtil.SPACE), StringUtil.UNDER_LINE).split(String.valueOf(StringUtil.EQUAL));
                String[] split = result.split(String.valueOf(StringUtil.EQUAL));
                for (int i = 0; i < split.length; i++) {
                    String origin = meta[i];
                    if (origin.isEmpty()) {
                        continue;
                    }
                    Param param = new Param(split[i]);
                    param.setDescription(origin);
                    enumClass.tryAddFields(param);
                }
                String execute = velocityService.execute(ProjectKeys.ENUM_TEMPLATE, enumClass);
                editor.setText(execute);
                // pp node attr assignment
            }
        });
        JButton execute = new JButton("Execute");
        JBSlider slider = new JBSlider(0, 15, 0);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.addChangeListener(event -> {
            if (event.getSource() instanceof JSlider) {
                JSlider source = (JSlider) event.getSource();
                int value = source.getValue();
            }
        });
        JPanel empty = new JPanel();
        JPanel rightRootPanel = new JPanel(new BorderLayout());
        JPanel executeDimension = new JPanel(new BorderLayout());
        executeDimension.add(execute);
        JPanel leftRootPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(NotifyKeys.INPUT_VALUE, empty)
                .addComponent(configData)
                .addLabeledComponent(NotifyKeys.SELECT_VALUE, empty)
                .addComponent(slider)
                .addComponentFillVertically(empty, 1)
                .addComponent(executeDimension).getPanel();
        rightRootPanel.add(editor);
        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(300);
        rootPane.setLeftComponent(leftRootPanel);
        rootPane.setRightComponent(rightRootPanel);
        return rootPane;

    }
}