package com.yatoufang.ui;


import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.ui.EditorTextField;
import com.yatoufang.config.CodeTemplateState;
import com.yatoufang.config.VariableTemplateState;
import com.yatoufang.core.Psi;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.TemplateMethod;
import com.yatoufang.templet.Application;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hse
 * @date 2021/6/5 0005
 */
public class CodeGeneratorDialog {

    private JSplitPane rootPane;

    private final TemplateMethod method;

    private EditorTextField editor;
    private ArrayList<JCheckBox> formFieldList;
    private ArrayList<JCheckBox> otherFieldList;
    private ArrayList<JCheckBox> superFormFieldList;

    private final List<Param> result;
    private final List<Param> superResult;

    private  List<String> codeTemplates;

    private HashMap<String, String> codeMap;

    private final PsiFile psiFile;

    public CodeGeneratorDialog(TemplateMethod method, List<String> formObjects, List<Param> result, List<Param> superResult, PsiFile file) {
        this.psiFile = file;
        this.method = method;
        this.result = result;
        this.superResult = superResult;
        drawPanel(formObjects, result, superResult, loadCustomVariable(),loadCustomTemplate());
    }



    private void drawPanel(List<String> formObjects, List<Param> result, List<Param> superResult,List<Param> customVariable,ArrayList<String> customTemplate) {

        rootPane = new JSplitPane();

        editor = new EditorTextField();
        editor.setFont(new Font(null, Font.PLAIN, 15));

        ArrayList<String> requestTypeList = new ArrayList<>();
        ArrayList<String> accessAuthority = new ArrayList<>();
        ArrayList<String> requestMethod = new ArrayList<>();
        requestTypeList.add("requestParam");
        requestTypeList.add("requestBody");
        requestTypeList.add("pathVariable");
        requestTypeList.add("null");
        accessAuthority.add("public");
        accessAuthority.add("protected");
        accessAuthority.add("no modifier");
        accessAuthority.add("private");
        requestMethod.add("NULL");
        requestMethod.add("GET");
        requestMethod.add("POST");
        requestMethod.add("PUT");
        requestMethod.add("DELETE");

        JLabel requestType = new JLabel("Request Type");
        JLabel requestMethodTitle = new JLabel("Request Method");
        JLabel entityTitle = new JLabel("Current File Objects");
        JLabel formFieldsTitle = new JLabel("Current Object Fields");
        JLabel otherFieldsTitle = new JLabel("Other");
        JLabel customTemplateTitle = new JLabel("custom Template");
        JLabel superFormFieldsTitle = new JLabel("Super Class Fields");
        JLabel accessAuthorityTitle = new JLabel("Access Authority");

        JButton insert = new JButton("Insert");
        JButton execute = new JButton("Execute");
        JButton select = new JButton("Select File");
        JButton codeTemplate = new JButton("CodeTemplate");

        Box content = Box.createVerticalBox();

        Box entityTitlePanel = Box.createHorizontalBox();

        JPanel entityPanel = new JPanel();
        JPanel leftRootPanel = new JPanel(new BorderLayout());
        JPanel rightRootPanel = new JPanel(new BorderLayout());
        JPanel requestTypePanel = new JPanel();
        JPanel accessAuthorityPanel = new JPanel();
        JPanel requestMethodPanel = new JPanel();
        JPanel fieldsPanel = new JPanel();
        JPanel otherFieldsPanel = new JPanel();
        JPanel superFieldsPanel = new JPanel();
        JPanel codeTemplatePanel = new JPanel();

        entityPanel.setPreferredSize(new Dimension(600, ((formObjects.size() / 4) + 1) * 50));
        fieldsPanel.setPreferredSize(new Dimension(600, ((result.size() / 4) + 1) * 50));
        //otherFieldsPanel.setPreferredSize(new Dimension(600, ((result.size() / 4) + 1) * 50));
        superFieldsPanel.setPreferredSize(new Dimension(600, ((superResult.size() / 4) + 1) * 50));

        ButtonGroup formObjectsGroup = new ButtonGroup();
        ButtonGroup requestTypeGroup = new ButtonGroup();
        ButtonGroup accessAuthorityGroup = new ButtonGroup();
        ButtonGroup requestMethodGroup = new ButtonGroup();
        ButtonGroup codeTemplateGroup = new ButtonGroup();

        formFieldList = new ArrayList<>();
        otherFieldList = new ArrayList<>();
        superFormFieldList = new ArrayList<>();


        ActionListener actionListener = event -> {
            Object sourceObject = event.getSource();
            if (sourceObject instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) sourceObject;
                String text = radioButton.getText();
                radioButtonController(text);
            } else if (sourceObject instanceof JButton) {
                JButton button = (JButton) sourceObject;
                buttonController(button.getText());
            }
        };

        drawContent(fieldsPanel, formFieldList, result,actionListener);

        drawContent(otherFieldsPanel, otherFieldList, customVariable,actionListener);

        drawContent(superFieldsPanel, superFormFieldList, superResult,actionListener);

        drawContent(entityPanel, formObjectsGroup, formObjects, actionListener);

        drawContent(requestTypePanel, requestTypeGroup, requestTypeList, actionListener);

        drawContent(codeTemplatePanel, codeTemplateGroup, customTemplate, actionListener);

        drawContent(requestMethodPanel, requestMethodGroup, requestMethod, actionListener);

        drawContent(accessAuthorityPanel, accessAuthorityGroup, accessAuthority, actionListener);

        insert.addActionListener(actionListener);
        select.addActionListener(actionListener);
        execute.addActionListener(actionListener);
        // codeTemplate.addActionListener(new MyListener());

        entityTitlePanel.add(entityTitle, BorderLayout.CENTER);
        entityTitlePanel.add(select, BorderLayout.CENTER);
        entityTitlePanel.add(codeTemplate, BorderLayout.EAST);

        content.add(entityTitlePanel);
        content.add(entityPanel);
        content.add(formFieldsTitle);
        content.add(fieldsPanel);
        content.add(superFormFieldsTitle);
        content.add(superFieldsPanel);
        content.add(otherFieldsTitle);
        content.add(otherFieldsPanel);
        content.add(customTemplateTitle);
        content.add(codeTemplatePanel);
        content.add(requestMethodTitle);
        content.add(requestMethodPanel);
        content.add(requestType);
        content.add(requestTypePanel);
        content.add(accessAuthorityTitle);
        content.add(accessAuthorityPanel);

        leftRootPanel.add(execute, BorderLayout.SOUTH);
        leftRootPanel.add(content);

        rightRootPanel.add(editor);
        rightRootPanel.add(insert, BorderLayout.SOUTH);


        rootPane.setDividerSize(2);
        rootPane.setDividerLocation(600);

        rootPane.setLeftComponent(leftRootPanel);
        rootPane.setRightComponent(rightRootPanel);

    }


    private void drawContent(JComponent panel, ButtonGroup buttonGroup, List<String> tags, ActionListener listener) {
        for (int i = 0; i < tags.size(); i++) {
            JRadioButton radioButton = new JRadioButton(tags.get(i));
            if (i == 0) {
                radioButton.setSelected(true);
            }
            radioButton.addActionListener(listener);
            buttonGroup.add(radioButton);
            panel.add(radioButton);

        }
    }

    private void drawContent(JComponent panel, ArrayList<JCheckBox> checkBoxList, List<Param> params, ActionListener listener) {
        for (Param param : params) {
            JCheckBox checkBox = new JCheckBox(getParamInfo(param));
            checkBoxList.add(checkBox);
            checkBox.addActionListener(listener);
            panel.add(checkBox);
        }
    }

    private String getParamInfo(Param param) {
        if (param.getDescription() != null && param.getDescription().length() != 0) {
            return param.getName() + "(" + param.getDescription() + ")";
        } else {
            return param.getName();
        }
    }

    private void buttonController(String text) {
        switch (text) {
            case "Execute":
                execute();
                break;
            case "Insert":
                if (editor.getText().length() != 0) {
                    PsiElement originElement = method.getOriginElement();
                    PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
                    PsiMethod psiMethod = factory.createMethodFromText(editor.getText(), null);
                    CodeStyleManager instance = CodeStyleManager.getInstance(Application.project);
                    PsiElement reformat = instance.reformat(psiMethod);
                    Psi.replace(originElement, reformat);
                    //PsiTestUtil.checkFileStructure(psiFile);
                    //Psi.delete(method.getDeleteElement());
                }
                break;
            default:
                break;
        }
    }

    private void execute() {
        ArrayList<String> targetList = new ArrayList<>();
        for (JCheckBox checkBox : formFieldList) {
            if (checkBox.isSelected()) {
                targetList.add(checkBox.getText());
            }
        }
        for (JCheckBox checkBox : superFormFieldList) {
            if (checkBox.isSelected()) {
                targetList.add(checkBox.getText());
            }
        }
        for (JCheckBox checkBox : otherFieldList) {
            if (checkBox.isSelected()) {
                targetList.add(checkBox.getText());
            }
        }

        ArrayList<Param> params = new ArrayList<>();
        ArrayList<Param> source = new ArrayList<>();
        source.addAll(result);
        source.addAll(superResult);

        for (Param param : source) {
            String name = param.getName();
            for (String target : targetList) {
                if (target.equals(name)) {
                    params.add(param);
                }
            }
        }
        System.out.println("params = " + params);
        method.setParams(params);

        editor.setText(getResult(method));
    }

    private void radioButtonController(String text) {
        if ("requestParam,requestBody,pathVariable,null".contains(text)) {
            if ("null".equals(text)) {
                method.setContentType("");
            } else {
                method.setContentType(text);
            }
        } else if ("public,protected,no modifier,private".contains(text)) {
            if ("no modifier".equals(text)) {
                method.setAccessAuthority(text);
            } else {
                method.setAccessAuthority("");
            }
        } else if ("GET,POST,PUT,DELETE,NULL".contains(text)) {
            if ("NULL".equals(text)) {
                method.setRequestMethod("");
            } else {
                switch (text){
                    case "GET":
                        method.setRequestMethod("PostMapping");
                        break;
                    case "POST":
                        method.setRequestMethod("GetMapping");
                        break;
                    case "PUT":
                        method.setRequestMethod("PutMapping");
                        break;
                    case "DELETE":
                        method.setRequestMethod("DeleteMapping");
                        break;
                    default:break;
                }

            }
        } else if (method.getEntity().contains(text)) {
            method.setSelectedEntity(text);
        }else if(method.getService().contains(text)){
            method.setSelectedEntity(text);
        }else if(method.getDao().contains(text)){
            method.setSelectedDao(text);
        }else if(codeTemplates.contains(text)){
            method.setCodeTemplate(codeMap.get(text));
        }
        execute();
    }


    public JComponent getRootPanel() {
        return rootPane;
    }


    private String getResult(TemplateMethod method) {

        VelocityEngine velocityEngine = new VelocityEngine();

        velocityEngine.init();

        Velocity.init();

        /* lets make a Context and put data into it */

        VelocityContext context = new VelocityContext();

        context.put("requestMethod", method.getRequestMethod());
        context.put("methodName", method.getName());
        context.put("accessAuthority", method.getAccessAuthority());
        context.put("returnType", method.getReturnType());
        context.put("contentType", method.getContentType());
        context.put("params", method.getParams());
        context.put("entity", method.getEntity());
        context.put("service", method.getService());
        context.put("dao", method.getDao());
        context.put("selectedEntity",method.getSelectedEntity());
        context.put("selectedService",method.getSelectedService());
        context.put("selectedDao",method.getSelectedDao());



        /* lets make our own string to render */

        StringWriter stringWriter = new StringWriter();
        if(method.getCodeTemplate() != null && method.getCodeTemplate().length() > 0){
            Velocity.evaluate(context, stringWriter, "myLog", method.getCodeTemplate());
            return stringWriter.toString();
        }
        return "";
    }


    private ArrayList<String> loadCustomTemplate() {
        ArrayList<String> strings = new ArrayList<>();
        CodeTemplateState instance = CodeTemplateState.getInstance();
        instance.codeMap.forEach((s, s2) -> {
            strings.add(s);
        });
        codeTemplates = strings;
        codeMap = instance.codeMap;
        return strings;
    }

    private List<Param> loadCustomVariable() {
        VariableTemplateState instance = VariableTemplateState.getInstance();
        return instance.params;
    }

}
