package com.yatoufang.action;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.find.findInProject.FindInProjectManager;
import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.popup.*;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBList;
import com.yatoufang.entity.Param;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.component.TextCellRender;
import com.yatoufang.ui.dialog.EntityTemplateDialog;
import com.yatoufang.ui.dialog.TextChooseDialog;
import com.yatoufang.utils.BuildUtil;
import com.yatoufang.utils.PSIUtil;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

public class PaintTest extends AnAction {


    private int age = 10;

    private static final String APPLICATION_TITLE = "Graph Editor Demo"; //$NON-NLS-1$
    private static final String DEMO_STYLESHEET = "demo.css"; //$NON-NLS-1$
    private static final String TREE_SKIN_STYLESHEET = "treeskins.css"; //$NON-NLS-1$
    private static final String TITLED_SKIN_STYLESHEET = "titledskins.css"; //$NON-NLS-1$
    private static final String FONT_AWESOME = "fontawesome.ttf"; //$NON-NLS-1$

    @Override
    public void actionPerformed(AnActionEvent e) {


        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        Application.project = file.getProject();
        PsiClass[] classes = file.getClasses();
        if (classes.length == 0)
            return;
        PsiClass aClass = classes[0];


        final String[] DATA = { "    /**\n" + "     *  获取对象信息\n" + "     */\n" + "    public LordGodEntity getEntity(int configId) {\n"
                + "        return entityMap.computeIfAbsent(configId, k -> LordGodEntity.valueOf(configId));\n" + "    }", "    /**\n" + "     *  添加记录\n" + "     */\n"
                + "    public void addActivateRecord(int id) {\n" + "        activateList.add(id);\n" + "    }", "    public void addExp(int addExp){\n"
                + "        this.exp += addExp;\n" + "    }" };
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JBList<String> list = new JBList<>(listModel);
        list.setCellRenderer(new TextCellRender());
        for (String datum : DATA) {
            listModel.addElement(datum);
        }


        ArrayList<String> arrayList = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            TextChooseDialog dialog = new TextChooseDialog(list, null, listModel,"");
            dialog.show();

        }
        System.out.println(arrayList);
    }

    private void test() {
        PsiExpressionStatement psiExpressionStatement;
        FindInProjectManager instance = FindInProjectManager.getInstance(Application.project);
        instance.findInProject(SimpleDataContext.getProjectContext(Application.project), null);
    }


    public void  testA(){
        final String[] DATA = { "    /**\n" + "     *  获取对象信息\n" + "     */\n" + "    public LordGodEntity getEntity(int configId) {\n"
            + "        return entityMap.computeIfAbsent(configId, k -> LordGodEntity.valueOf(configId));\n" + "    }", "    /**\n" + "     *  添加记录\n" + "     */\n"
            + "    public void addActivateRecord(int id) {\n" + "        activateList.add(id);\n" + "    }", "    public void addExp(int addExp){\n"
            + "        this.exp += addExp;\n" + "    }" };
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JBList<String> list = new JBList<>(listModel);
        list.setCellRenderer(new TextCellRender());
        for (String datum : DATA) {
            listModel.addElement(datum);
        }

        list.addListSelectionListener(event -> {
            int[] indices = list.getSelectedIndices();
            for (int index : indices) {
                System.out.println("选中: " + index + " = " + listModel.getElementAt(index));
            }
        });

        JBPopupFactory instance = JBPopupFactory.getInstance();
        instance.createComponentPopupBuilder(list,null)
            .setTitle("My Table")
            .setMovable(true)
            .setResizable(true)
            .setCancelOnClickOutside(false)
            .setOkHandler(() -> {System.out.println("hello");})
            .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
            .setRequestFocus(true)
            .setMinSize(new Dimension(300, 400))
            .createPopup()
            .showInFocusCenter();
    }


    private void createMethod(AnActionEvent e){
        PsiElement data = e.getData(LangDataKeys.PSI_ELEMENT);
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        Application.project = file.getProject();
        PsiClass[] classes = file.getClasses();
        if (classes.length == 0)
            return;
        PsiClass aClass = classes[0];
        for (PsiField field : aClass.getAllFields()) {
            System.out.println(PSIUtil.getDescription(field.getDocComment()));
        }
        PsiMethod activity2028GradeConfig = BuildUtil.createMethod(aClass, "public static Activity2028GradeConfig getGradeConfig (int data,int level){\n"
            + " Map<Integer,Activity2028GradeConfig> Activity2028GradeConfig = Activity2028GradeConfig.get(data);\n" + "return levelKeyMap.get(level);\n" + "}");
        System.out.println("activity2028GradeConfig = " + activity2028GradeConfig.getText());
    }

    private void psiFile(AnActionEvent e) {
        PsiElement data = e.getData(LangDataKeys.PSI_ELEMENT);
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        Application.project = file.getProject();
        PsiClass[] classes = file.getClasses();
        if (classes.length == 0)
            return;
        PsiClass aClass = classes[0];
        PsiClassType[] allTypes = aClass.getImplementsListTypes();
        PsiClassType type = allTypes[0];
        System.out.println(type.getPresentableText());
        for (PsiClassType listType : allTypes) {
            System.out.println(listType.getPresentableText());
        }
        PsiMethod methodByName = PSIUtil.getMethodByName(aClass, ProjectKeys.METHOD_FIND_KEY);
        if (methodByName == null) {
            return;
        }
        PsiVariable childOfType = PsiTreeUtil.getChildOfType(methodByName, PsiLocalVariable.class);
        System.out.println("childOfType = " + childOfType);
        PsiMethodCallExpression expression = PsiTreeUtil.findChildOfType(methodByName, PsiMethodCallExpression.class);
        if (expression != null) {
            PsiReferenceExpression methodExpression = expression.getMethodExpression();
            PsiExpression qualifier = methodExpression.getQualifierExpression();
            System.out.println(qualifier.getType().getPresentableText());
        }

    }

    private static void CMDtest() {

        GeneralCommandLine commandLine = new GeneralCommandLine("cmd", "/c", "git log origin/release/1.0.43 -10  --author= --pretty=format:\"%s\"");
        commandLine.setWorkDirectory("D:\\LYLTD");
        try {
            Process process = commandLine.createProcess();
            InputStream inputStream = process.getInputStream();
            String s = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println(s);
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dds(AnActionEvent e) {
        System.out.println(--age);
        new EntityTemplateDialog("", "").show();
        if (age > 0) {
            return;
        }
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }
        PsiClass[] classes = file.getClasses();
        PsiClass aClass = classes[0];
        PsiField field = PSIUtil.createField(aClass);
        PsiMethod method = PSIUtil.createMethod(aClass);
        PsiStatement statement = BuildUtil.createStatement(method, "File file = new File()");
        PsiImportStatement importStatement = BuildUtil.createImportStatement(aClass, "File");
        PsiField[] fields = aClass.getFields();
        PsiElement prepareElement;
        PsiEnumConstant enumConstant = null;
        if (aClass.isEnum()) {
            for (PsiField psiField : fields) {
                if (psiField instanceof PsiEnumConstant) {
                    System.out.println("psiField = " + psiField);
                    enumConstant = (PsiEnumConstant) psiField;
                }
            }
        }
        assert enumConstant != null;
        BuildUtil.addAnnotation(method, "Cmd(Id = FriendCmd.ADD_FRIEND, dispatchType = DispatchType.ACTOR)");
        PsiEnumConstant finalEnumConstant = (PsiEnumConstant) enumConstant.copy();
        WriteCommandAction.runWriteCommandAction(Application.project, () -> {
            PsiCodeBlock body = method.getBody();
            if (body != null) {
                body.add(statement);
            }
            aClass.add(method);
            finalEnumConstant.setName("TEST");
            aClass.add(finalEnumConstant);
            PsiImportList importList = file.getImportList();
            importList.add(importStatement);
            JavaCodeStyleManager.getInstance(Application.project).shortenClassReferences(file);
            CodeStyleManager.getInstance(Application.project).reformat(aClass);
        });
    }

    private void dd() {
        PackageChooserDialog selector = new PackageChooserDialog("Select a Package", Application.project);
        selector.show();
        PsiPackage selectedPackage = selector.getSelectedPackage();
        HashSet<Param> result = Sets.newHashSet();
        if (selectedPackage != null) {
            PsiPackage[] subPackages = selectedPackage.getSubPackages();
            for (PsiPackage psiPackage : subPackages) {
                PsiPackage[] psiPackages = psiPackage.getSubPackages();
                for (PsiPackage aPackage : psiPackages) {
                    if (aPackage.getName().contains(ProjectKeys.FACADE)) {
                        PsiClass[] allClass = aPackage.getClasses();
                        for (PsiClass aClass : allClass) {
                            if (aClass.isInterface()) {
                                PsiMethod[] allMethods = aClass.getAllMethods();
                                for (PsiMethod method : allMethods) {
                                    PsiParameterList parameterList = method.getParameterList();
                                    PsiParameter[] parameters = parameterList.getParameters();
                                    for (PsiParameter parameter : parameters) {
                                        String typeAlias = parameter.getType().getPresentableText();
                                        if (Application.isBasicType(typeAlias)) {
                                            Param param = new Param(parameter.getName());
                                            param.setTypeAlias(typeAlias);
                                            result.add(param);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        CMDtest();
    }
}
