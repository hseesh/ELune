package com.yatoufang.ui;


import com.google.common.collect.Maps;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.config.CodeTemplateState;
import com.yatoufang.entity.Config;
import com.yatoufang.entity.ConfigParam;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.ExceptionUtil;
import com.yatoufang.utils.StringUtil;
import com.yatoufang.utils.SwingUtils;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author hse
 * @date 2021/6/5 0005
 */
public class ConfigTemplateDialog extends DialogWrapper {

    private JSplitPane rootPane;

    private EditorTextField editor;

    private String workSpace;

    private final ArrayList<String> files = Lists.newArrayList();
    private final Map<String, String> fileMap = Maps.newHashMap();


    private final VelocityService velocityService;


    public ConfigTemplateDialog(String rootPath, String workSpace) {
        super(Application.project,true);
        this.workSpace = workSpace;
        velocityService = VelocityService.getInstance();
        initData();
        init();
    }


    private void saveFile() {

    }



    public static void main(String[] args) {
        String text = "data\ttype\trank\trewards\t\t\t\t\tnationalRewards\t\t\t\t\tshowrewards\n";
        String text2 = "incarnation_reinforce_config\tIncarnationReinforceConfig\t\t\t\n" +
                "cs\tcs\tcs\tc\tsc\n" +
                "quality\tlevel\tcosts\tnextLevel\tattribute\n";
        Config parser = parser(text2);
        System.out.println(parser);
    }

    public static Config parser(String text) {
        if (text.isEmpty()) {
            return null;
        }
        Config config = new Config();
        String[] split = text.split("\n");
        switch (split.length) {
            case 3:
                config.setFileName(StringUtil.getParam(split[0]));
            case 2:
                config.setIndexLists(StringUtil.getParam(split[1]));
            case 1:
                config.setParams(StringUtil.getParam(split[split.length - 1]));
                break;
            default:
                break;
        }
        return config;
    }


    private void initData() {
        String text = velocityService.execute(ProjectKeys.MULTI_TEMPLATE, new Config());
        editor = SwingUtils.createEditor(text);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        rootPane = new JSplitPane();
        rootPane.setMinimumSize(new Dimension(1100, 800));
        editor.setFont(new Font(null, Font.PLAIN, 14));

        JTextField configData = new JTextField();

        JButton execute = new JButton("Execute");

        JPanel rightRootPanel = new JPanel(new BorderLayout());

        JTextArea editor = new JTextArea();

        CollectionListModel<String> listModel = new CollectionListModel<>(files);
        JBList<String> fileList = new JBList<>(listModel);


        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fileList);

        //component controller
        decorator.setAddAction(anActionButton -> {
//            String fileName = getFileName();
//            listModel.add(fileName);
//            fileMap.put(fileName, "");
//            fileList.setSelectedIndex(listModel.getSize() - 1);
//            System.out.println("listModel = " + listModel.getSize());

        });

        decorator.setRemoveAction(anActionButton -> {
            String selectedValue = fileList.getSelectedValue();
            fileMap.remove(selectedValue);
        });

        decorator.setEditAction(anActionButton -> {
            String name = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
            if (name != null && name.length() != 0) {
                System.out.println("str = " + name);
                fileMap.replace(fileList.getSelectedValue(), name);
            }
        });

        fileList.addListSelectionListener(e -> {
            String selectedValue = fileList.getSelectedValue();
            System.out.println("selectedValue = " + selectedValue);
            String temp = fileMap.get(selectedValue);
            editor.setText(temp);
        });

        configData.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                Config config = parser(configData.getText());
                if (config == null) {
                    return;
                }
                if (config.getFileName() == null) {
                    String name = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
                    if (name == null || name.isEmpty()) {
                        return;
                    }
                    config.setFileName(name);
                    if (config.hasParams()) {
                        String result = velocityService.execute(ProjectKeys.CONFIG_TEMPLATE, config);
                        editor.setText(result);
                        fileMap.put(fileList.getSelectedValue(), result);
                    }
                }
            }
        });
        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                fileMap.replace(fileList.getSelectedValue(), editor.getText());
            }
        });


        execute.addActionListener(e -> {
            saveFile();
        });
        JPanel panel = decorator.createPanel();
        panel.setMinimumSize(new Dimension(300,600));
        JPanel executeDimension = new JPanel(new BorderLayout());
        executeDimension.setSize(new Dimension(300, 50));
        executeDimension.add(execute);

        JPanel leftRootPanel = FormBuilder.createFormBuilder()
                .addComponentFillVertically(configData, 1)
                .addComponent(panel)
                .addComponent(executeDimension)
                .getPanel();

        rightRootPanel.add(editor);

        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(300);
        rootPane.setLeftComponent(leftRootPanel);
        rootPane.setRightComponent(rightRootPanel);

        return rootPane;
    }
}
