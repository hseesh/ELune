package com.yatoufang.ui.dialog;

import com.google.common.collect.Maps;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.FormBuilder;
import com.yatoufang.config.service.AppSettingService;
import com.yatoufang.entity.Config;
import com.yatoufang.entity.ConfigParam;
import com.yatoufang.service.NotifyService;
import com.yatoufang.service.TranslateService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Annotations;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.designer.event.EditorContext;
import com.yatoufang.utils.FileWrite;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.StringUtil;
import com.yatoufang.utils.SwingUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author hse
 * @date 2021/6/5 0005
 */
public class ConfigTemplateDialog extends DialogWrapper {

    private EditorTextField editor;

    private String rootPath;
    private String workSpace;

    private boolean designerModel;

    private final ArrayList<String> files = Lists.newArrayList();

    private Map<String, String> fileMap = Maps.newHashMap();
    private HashSet<ConfigParam> params = new HashSet<>();

    private final VelocityService velocityService;

    public ConfigTemplateDialog(String rootPath, String workSpace) {
        super(Application.project, true);
        this.rootPath = rootPath;
        this.workSpace = workSpace;
        velocityService = VelocityService.getInstance();
        initData();
        init();
        ApplicationManager.getApplication().runReadAction(() -> {
            try {
                params = new BankGroundTask().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Collection<ConfigParam> configParams = FileWrite.loadConfig(this.getClass(), ConfigParam.class, ProjectKeys.PATH_CONFIG_FIELDS);
            params.addAll(configParams);
        });
    }

    public ConfigTemplateDialog(String rootPath, String workSpace, Map<String, String> fileMap) {
        super(Application.project, true);
        try {
            params = new BankGroundTask().call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.fileMap = fileMap;
        this.rootPath = rootPath;
        this.designerModel = true;
        this.workSpace = workSpace;
        velocityService = VelocityService.getInstance();
        String defaultContent = fileMap.values().stream().findFirst().orElse(StringUtil.EMPTY);
        editor = SwingUtils.createEditor(defaultContent);
        editor.setFont(new Font(null, Font.PLAIN, 14));
        files.addAll(fileMap.keySet());
        init();
    }

    private void saveFile() {
        if (fileMap.size() > 0) {
            fileMap.forEach((fileName, fileContent) -> {
                String filePath = StringUtil.buildPath(rootPath, ProjectKeys.CONFIG_PATH, fileName + ProjectKeys.JAVA);
                FileWrite.write(fileContent, filePath, true, false);
            });
        }
    }

    public static Config parser(String text) {
        if (text.isEmpty()) {
            return null;
        }
        Config config = new Config();
        String[] split = text.split("\n");
        switch (split.length) {
            case 4:
                config.setFileDescription(StringUtil.getParam(split[0]));
            case 3:
                config.setFileName(StringUtil.getParam(split[1]));
            case 2:
                config.setIndexLists(StringUtil.getParam(split[2]));
            case 1:
                List<String> param = StringUtil.getParam(split[split.length - 1]);
                config.setParams(param);
                break;
            default:
                break;
        }
        return config;
    }
    private void initData() {
        String text = velocityService.execute(ProjectKeys.CONFIG_TEMPLATE, new Config());
        editor = SwingUtils.createEditor(text);
        editor.setFont(new Font(null, Font.PLAIN, 14));
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JSplitPane rootPane = new JSplitPane();

        rootPane.setMinimumSize(new Dimension(1100, 800));

        JTextArea configData = new JTextArea();

        JButton execute = new JButton("Execute");

        JPanel rightRootPanel = new JPanel(new BorderLayout());

        CollectionListModel<String> listModel = new CollectionListModel<>(files);
        JBList<String> fileList = new JBList<>(listModel);

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fileList);

        decorator.setRemoveAction(anActionButton -> {
            String selectedValue = fileList.getSelectedValue();
            fileMap.remove(selectedValue);
        });

        decorator.setEditAction(anActionButton -> {
            String name = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
            if (name != null && name.length() != 0) {
                fileMap.replace(fileList.getSelectedValue(), name);
                listModel.setElementAt(name, fileList.getSelectedIndex());
            }
        });

        fileList.addListSelectionListener(e -> {
            String selectedValue = fileList.getSelectedValue();
            String temp = fileMap.get(selectedValue);
            editor.setText(temp);
        });

        configData.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                configData.setText(StringUtil.EMPTY);
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
                }
                if (config.hasParams()) {
                    config.build(params);
                    String result = velocityService.execute(ProjectKeys.CONFIG_TEMPLATE, config);
                    int elementIndex = listModel.getElementIndex(config.getFileName());
                    if (elementIndex > -1) {
                        fileList.setSelectedIndex(elementIndex);
                    } else {
                        listModel.add(config.getFileName());
                        fileList.setSelectedIndex(listModel.getSize() - 1);
                    }
                    fileMap.put(fileList.getSelectedValue(), result);
                    editor.setText(result);
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
        configData.setMinimumSize(new Dimension(300, 400));
        panel.setMinimumSize(new Dimension(300, 400));
        JPanel executeDimension = new JPanel(new BorderLayout());
        executeDimension.setSize(new Dimension(300, 50));
        executeDimension.add(execute);

        JPanel leftRootPanel = FormBuilder.createFormBuilder().addComponentFillVertically(configData, 1).addComponent(panel).addComponent(executeDimension).getPanel();

        rightRootPanel.add(editor);

        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(300);
        rootPane.setLeftComponent(leftRootPanel);
        rootPane.setRightComponent(rightRootPanel);

        return rootPane;
    }

    @Override
    protected @NotNull JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        return new JPanel();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
        if (designerModel) {
            EditorContext.setDesigner(fileMap);
        }
    }

    public Map<String, String> getFileMap() {
        return fileMap;
    }

    static class BankGroundTask implements Callable<HashSet<ConfigParam>> {

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public HashSet<ConfigParam> call() throws Exception {
            return selectPackage();
        }

        private HashSet<ConfigParam> selectPackage() {
            AppSettingService service = AppSettingService.getInstance();
            if (service.dataConfigPath == null || service.dataConfigPath.isEmpty()) {
                return Sets.newHashSet();
            }
            PsiPackage selectedPackage = JavaPsiFacade.getInstance(Application.project).findPackage(service.dataConfigPath);
            if (selectedPackage == null) {
                NotifyService.notifyError("No package Match: " + service.dataConfigPath);
                return Sets.newHashSet();
            }
            PsiClass[] classes = selectedPackage.getClasses();
            HashSet<ConfigParam> params = Sets.newHashSet();
            ArrayList<ConfigParam> paramLists = Lists.newArrayList();
            ArrayList<ConfigParam> referenceList = Lists.newArrayList();
            ArrayList<String> expressionList = Lists.newArrayList();
            for (PsiClass aClass : classes) {
                if (!aClass.hasAnnotation(Annotations.DATA_FILE)) {
                    continue;
                }
                PsiField[] fields = aClass.getFields();
                getExpressions(expressionList, aClass);
                for (PsiField field : fields) {
                    PsiType type = field.getType();
                    ConfigParam param = new ConfigParam(field.getName());
                    param.setTypeAlias(type.getPresentableText());
                    param.setDescription(PSIUtil.getDescription(field.getDocComment()));
                    if (field.hasAnnotation(Annotations.FILED_IGNORE)) {
                        String filedValue = PSIUtil.getFiledValue((PsiElement) field);
                        filedValue = filedValue == null ? StringUtil.EMPTY : filedValue;
                        param.setDefaultValue(filedValue);
                        referenceList.add(param);
                    } else {
                        param.setDefaultValue(StringUtil.EMPTY);
                        paramLists.add(param);
                    }
                }
                for (ConfigParam configParam : referenceList) {
                    FLAG:
                    for (int i = 0; i < expressionList.size(); i++) {
                        if (expressionList.get(i).contains(configParam.getName())) {
                            for (ConfigParam param : paramLists) {
                                if (expressionList.get(i).contains(param.getName())) {
                                    param.setReferenceExpression(expressionList.get(i));
                                }
                                if (i > 0) {
                                    int index = i;
                                    if (expressionList.get(--index).contains(param.getName())) {
                                        param.setReferenceExpression(expressionList.get(index) + expressionList.get(i));
                                        param.setAliaParam(configParam);
                                        break FLAG;
                                    }
                                }
                            }
                        }
                    }
                }
                params.addAll(paramLists);
                paramLists.clear();
                referenceList.clear();
                expressionList.clear();
            }
            return params;
        }

        private void getExpressions(ArrayList<String> expressionList, PsiClass aClass) {
            PsiMethod method = PSIUtil.getMethodByName(aClass, ProjectKeys.INITIALIZE_METHOD);
            if (method != null) {
                PsiCodeBlock body = method.getBody();
                if (body != null) {
                    PsiStatement[] statements = body.getStatements();
                    for (PsiStatement statement : statements) {
                        expressionList.add(statement.getText());
                    }
                }
            }
        }
    }

}
