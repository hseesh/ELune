package com.yatoufang.ui.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.editor.Model;
import com.yatoufang.editor.component.impl.EnumNode;
import com.yatoufang.entity.EnumClass;
import com.yatoufang.entity.Param;
import com.yatoufang.service.TranslateService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.thread.ELuneScheduledThreadPoolExecutor;
import com.yatoufang.utils.FileWrite;
import com.yatoufang.utils.StringUtil;
import com.yatoufang.utils.SwingUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    private Model model;

    private Point clickedPoint;

    public EnumTemplateDialog(String rootPath, String workSpace, String content) {
        super(Application.project, true);
        this.rootPath = rootPath;
        this.workSpace = workSpace;
        editor = SwingUtils.createEditor(StringUtil.EMPTY);
        editor.setFont(new Font(null, Font.PLAIN, 14));
        velocityService = VelocityService.getInstance();
        init();
        showInputDialog();
    }

    public EnumTemplateDialog(Model model, Point clickedPoint) {
        super(Application.project, true);
        this.rootPath = model.getBasePath();
        this.workSpace = model.getModuleName();
        editor = SwingUtils.createEditor(StringUtil.EMPTY);
        editor.setFont(new Font(null, Font.PLAIN, 14));
        velocityService = VelocityService.getInstance();
        this.clickedPoint = clickedPoint;
        this.model = model;
        init();
        showInputDialog();
    }

    public void showInputDialog() {
        String fileName = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
        ELuneScheduledThreadPoolExecutor instance = ELuneScheduledThreadPoolExecutor.getInstance();
        if (fileName == null || fileName.isEmpty()) {
            instance.schedule(() -> {
                SwingUtilities.invokeLater(this::dispose);
            }, Calendar.FEBRUARY, TimeUnit.SECONDS);
            return;
        }
        instance.execute(() -> {
            String translate = TranslateService.translate(fileName, true, true);
            enumClass.setDescription(fileName);
            enumClass.setAlias(translate.replaceAll(String.valueOf(StringUtil.SPACE), StringUtil.EMPTY));
            enumClass.setName(enumClass.getAlias());
            String execute = velocityService.execute(ProjectKeys.ENUM_TEMPLATE, enumClass);
            SwingUtilities.invokeLater(() -> {
                editor.setText(execute);
            });
        });
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        JSplitPane rootPane = new JSplitPane();
        rootPane.setMinimumSize(new Dimension(900, 700));
        JTextArea configData = new JTextArea();
        configData.setRows(15);
        configData.setFont(new Font(null, Font.PLAIN, 14));
        configData.setMinimumSize(new Dimension(300, 100));

        configData.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = configData.getText().replace(StringUtil.NEW_LINE, StringUtil.CHINESE_FULL_STOP);
                if (text.isEmpty()) {
                    return;
                }
                String result = TranslateService.translate(text);
                if (result == null) {
                    return;
                }
                result = result.toUpperCase(Locale.ROOT).replaceAll("THE", StringUtil.EMPTY);
                String[] meta = text.split(String.valueOf(StringUtil.CHINESE_FULL_STOP));
                String[] split = result.split(StringUtil.ENGLISH_FULL_STOP);
                for (int i = 0; i < split.length; i++) {
                    String origin = meta[i];
                    if (origin.isEmpty()) {
                        continue;
                    }
                    Param param = new Param(split[i].trim().replaceAll(String.valueOf(StringUtil.SPACE), StringUtil.UNDER_LINE));
                    param.setDescription(origin);
                    enumClass.tryAddFields(param);
                }
                String execute = velocityService.execute(ProjectKeys.ENUM_TEMPLATE, enumClass);
                editor.setText(execute);
                // pp node attr assignment
            }
        });
        JButton execute = new JButton("Execute");
        execute.addActionListener(e -> {
            String filePath = StringUtil.buildPath(rootPath, ProjectKeys.MODULE, enumClass.getName(), ProjectKeys.MODEL, fileName + ProjectKeys.JAVA);
            FileWrite.write(editor.getText(), filePath, true, false);
        });
        JPanel empty = new JPanel();
        JPanel rightRootPanel = new JPanel(new BorderLayout());
        JPanel executeDimension = new JPanel(new BorderLayout());
        executeDimension.add(execute);
        JPanel leftRootPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(NotifyKeys.INPUT_VALUE, empty)
                .addComponent(configData)
                .addLabeledComponent(NotifyKeys.SELECT_VALUE, empty)
                .addComponentFillVertically(empty, 1)
                .addComponent(executeDimension).getPanel();
        rightRootPanel.add(editor);
        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(300);
        rootPane.setLeftComponent(leftRootPanel);
        rootPane.setRightComponent(rightRootPanel);
        return rootPane;

    }


    @Override
    public void doCancelAction() {
        super.doCancelAction();
        if (model != null && clickedPoint != null) {
            EnumNode configNode = new EnumNode(model, clickedPoint);
            configNode.refresh(editor.getText());
            model.add(configNode);
        }
    }
}
