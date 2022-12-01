package com.yatoufang.ui;

import com.google.gson.Gson;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.config.service.PackagingSettingsService;
import com.yatoufang.core.HttpUtils;
import com.yatoufang.entity.RequestBodyVO;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.groovy.util.Maps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Gary
 * @Date 2022-04-27 10:12
 * @Description:
 */
public class PackagingFormFactory {
    private JTextArea jTextArea;
    private List<String> serverList = List.of("game", "world", "web", "dbclean", "battle", "match");
    private List<String> branchesList = new ArrayList<>();
    private Font font = new Font(null, Font.ITALIC, 12);
    private List<JCheckBox> selectBoxList = new ArrayList<>();
    private ComboBox<Object> combobox;
    private JButton sendButton;
    private JButton grabButton;
    private JButton refreshButton;
    private ConsoleService instance;

    public JComponent createCenterPanel(Project project, ToolWindow toolWindow) {
        ConsoleService consoleService = new ConsoleService();
        instance = consoleService.getInstance(project);
        var branchesBox = Box.createHorizontalBox();
        combobox = new ComboBox<>();
        for (var s : branchesList) {
            combobox.addItem(s);
        }
        combobox.setSize(0, 30);
        branchesBox.add(new JBLabel("远程分支:"));
        branchesBox.add(combobox);

        var serverBox = Box.createHorizontalBox();
        for (var s : serverList) {
            var jCheckBox = creatCheckBox(s);
            if ("game".equals(s) || "world".equals(s)) {
                jCheckBox.setSelected(true);
            }
            selectBoxList.add(jCheckBox);
            serverBox.add(jCheckBox);
            serverBox.add(Box.createHorizontalGlue());
        }

        var textAreaBox = Box.createHorizontalBox();
        jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        jTextArea.setText("");
        jTextArea.setMinimumSize(new Dimension(350, 150));
        jTextArea.setMaximumSize(new Dimension(2000, 300));
        jTextArea.setPreferredSize(new Dimension(300, 300));
        textAreaBox.add(jTextArea);

        var buttonBox = Box.createHorizontalBox();
        refreshButton = new JButton("刷新");
        sendButton = new JButton("发送");
        grabButton = new JButton("获取");
        refreshButton.addActionListener(buttonListener(refreshButton, project));
        sendButton.addActionListener(buttonListener(sendButton, project));
        grabButton.addActionListener(buttonListener(grabButton, project));
        buttonBox.add(refreshButton);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(grabButton);
        buttonBox.add(sendButton);

        return FormBuilder.createFormBuilder().addComponent(branchesBox).addComponent(serverBox).addComponent(new JBLabel("提交内容:")).addComponent(textAreaBox)
            .addComponent(buttonBox).getPanel();
    }

    public void initBranchesList(Project project) {
        branchesList.clear();
        var processOutput = executeCommand("git branch -r", project);
        instance.print(processOutput + "\n");
        var list = processOutput.replace("  origin/", "").split("\n");
        for (var s : list) {
            if (s.contains("release/")) {
                branchesList.add(0, s);
                continue;
            }
            branchesList.add(s);
        }
        jTextArea.setText("");
    }

    private JBCheckBox creatCheckBox(String s) {
        var checkbox = new JBCheckBox();
        checkbox.setText(s);
        checkbox.setFont(font);
        return checkbox;
    }

    private ActionListener buttonListener(JButton jButton, Project project) {
        return e -> {
            switch (jButton.getText()) {
                case "发送":
                    new Thread(() -> this.doSendButton(project)).start();
                    break;
                case "获取":
                    jTextArea.setText("执行中,请稍后...\n");
                    new Thread(() -> jTextArea.setText(this.doGrabButton(project))).start();
                    break;
                case "刷新":
                    jTextArea.setText("执行中,请稍后...\n");
                    new Thread(() -> this.doRefreshButton(project)).start();
                    jTextArea.setText("");
                    break;
                default:
                    break;
            }
        };
    }

    private void doRefreshButton(Project project) {
        initBranchesList(project);
        combobox.removeAllItems();
        for (var s : branchesList) {
            combobox.addItem(s);
        }
        for (var jCheckBox : selectBoxList) {
            if ("game".equals(jCheckBox.getText()) || "world".equals(jCheckBox.getText())) {
                jCheckBox.setSelected(true);
                continue;
            }
            jCheckBox.setSelected(false);
        }
        jTextArea.setText("");
    }

    /**
     * git log --author="gary" --pretty=format:"%s" -5
     */
    private String doGrabButton(Project project) {
        changeTextArea("更新本地标签...");
        StringBuilder sb = new StringBuilder();
        var updateTags = List.of("git tag -d packaged", "git fetch origin packaged", "git fetch");
        for (var tag : updateTags) {
            var run = executeCommand(tag, project);
            changeTextArea(run);
        }
        changeTextArea("获取提交信息...");
        var processOutput = executeCommand("git log origin/" + combobox.getSelectedItem() + " --pretty=format:\"#%an-%s#%d\" -20 --decorate --topo-order", project);
        instance.print(processOutput + "\n");
        var collect = Arrays.stream(processOutput.split("\n")).collect(Collectors.toList());
        for (var x : collect) {
            if (x.contains("packaged")) {
                break;
            }
            if (!x.contains("Merge")) {
                sb.append(StringUtil.subString(x, "#", "#")).append("\n");
            }
        }
        return "".equals(sb.toString()) ? "无新提交" : sb.toString();
    }

    /**
     * "git tag packaged",
     * "git push origin packaged",
     * "git tag -d packaged",
     * "git push origin :refs/tags/packaged"
     */
    private void doSendButton(Project project) {
        var service = PackagingSettingsService.getInstance();
        if ("".equals(service.url) || "".equals(service.token)) {
            JOptionPane.showMessageDialog(new JFrame(), "配置不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String areaText = jTextArea.getText();
        if (areaText.length() <= 5) {
            JOptionPane.showMessageDialog(new JFrame(), "提交内容不能小于5个字", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if ("".equals(String.valueOf(combobox.getSelectedItem()))) {
            JOptionPane.showMessageDialog(new JFrame(), "需要选择分支", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        var message = new StringBuilder();
        message.append("所选分支:");
        message.append(combobox.getSelectedItem());
        message.append("\n");

        var content = new StringBuilder();
        content.append("发布版本:");
        List<String> collect = selectBoxList.stream().filter(AbstractButton::isSelected).map(JCheckBox::getText).collect(Collectors.toList());
        for (String text : collect) {
            content.append(text);
            if (collect.indexOf(text) != collect.size() - 1) {
                content.append("|");
            }
        }
        content.append("\n");
        content.append(areaText);
        message.append(content);
        // 消息对话框无返回, 仅做通知作用
        if (JOptionPane.showConfirmDialog(new JFrame(), message, "提交", JOptionPane.YES_NO_OPTION) == 0) {
            jTextArea.setText("");
            call(content.toString(), service.url, service.token, project);
            jTextArea.setText("");
        }
    }

    /**
     * git tag packaged
     * git push origin packaged
     * git tag -d packaged
     * git push origin :refs/tags/packaged
     */
    public String executeCommand(String s, Project project) {
        changeTextArea(s);
        var commandLine = new GeneralCommandLine("cmd", "/c", s);
        commandLine.setWorkDirectory(project.getBasePath());
        //        commandLine.setWorkDirectory("E:\\git\\LYLTD");
        var processOutput = "";
        try {
            var process = commandLine.createProcess();
            processOutput = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            if ("".equals(processOutput)) {
                processOutput = IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8);
            }
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processOutput;
    }

    public void changeTextArea(String s) {
        new Thread(() -> {
            jTextArea.append(s + "\n");
            instance.print(s + "\n");
        }).start();

    }

    /**
     * body:
     * {
     * "token": "67d3c9c14215b83a0d25f20e94bac9",
     * "ref": "release/1.0.43",
     * "variables": {
     * "CI_COMMIT_MESSAGE": "发布版本:game\n123"
     * }
     * }
     *
     * @param content
     */
    private void call(String content, String url, String token, Project project) {
        var map = Maps.of("CI_COMMIT_MESSAGE", content);
        var requestBodyVO = new RequestBodyVO(token, String.valueOf(combobox.getSelectedItem()), map);
        var json = new Gson().toJson(requestBodyVO);
        changeTextArea("请求:" + "url:" + url + "token:" + token);
        HttpUtils.sendPost(url, json);
        changeTextArea("更新远程标签...");
        var updateTags = List.of("git tag -d packaged", "git push origin :refs/tags/packaged", "git tag packaged", "git push origin packaged");
        for (var tag : updateTags) {
            var run = executeCommand(tag, project);
        }
    }
}
