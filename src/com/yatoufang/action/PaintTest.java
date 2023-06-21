package com.yatoufang.action;

import com.google.common.collect.Maps;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.find.findInProject.FindInProjectManager;
import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBList;
import com.yatoufang.complete.handler.CodeCompleteHandler;
import com.yatoufang.complete.model.context.CodeCompleteTrigger;
import com.yatoufang.entity.Method;
import com.yatoufang.entity.Param;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.component.TextCellRender;
import com.yatoufang.ui.dialog.EntityTemplateDialog;
import com.yatoufang.ui.dialog.EnumTemplateDialog;
import com.yatoufang.ui.dialog.TextChooseDialog;
import com.yatoufang.utils.BuildUtil;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class PaintTest extends AnAction {

    private int age = 10;

    private static final String APPLICATION_TITLE = "Graph Editor Demo"; //$NON-NLS-1$
    private static final String DEMO_STYLESHEET = "demo.css"; //$NON-NLS-1$
    private static final String TREE_SKIN_STYLESHEET = "treeskins.css"; //$NON-NLS-1$
    private static final String TITLED_SKIN_STYLESHEET = "titledskins.css"; //$NON-NLS-1$
    private static final String FONT_AWESOME = "fontawesome.ttf"; //$NON-NLS-1$

    @Override
    public void actionPerformed(AnActionEvent e) {
        ConsoleService instance = ConsoleService.getInstance();
        PsiClass aClass = DataUtil.getClass(e);
        if (aClass == null) {
            return;
        }
        Editor editor = e.getData(LangDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        Document document = editor.getDocument();
        int offset = editor.getCaretModel().getOffset();
        PsiFile psiFile = PsiDocumentManager.getInstance(Application.project).getPsiFile(document);
        if (psiFile != null) {
            PsiElement element = psiFile.findElementAt(offset);
            if (element == null) {
                return;
            }
        }

    }


    private void test(PsiElement element){
        ConsoleService instance = ConsoleService.getInstance();
        PsiElement scope = element.getParent();
        while (scope != null) {
            if (scope instanceof PsiMethod) {
                break;
            }
            scope = scope.getParent();
        }
        if (scope == null) {
            return;
        }
        CodeCompleteTrigger trigger = new CodeCompleteTrigger();
        scope.accept(new JavaRecursiveElementWalkingVisitor() {
            @Override
            public void visitVariable(PsiVariable psiVariable) {
                instance.printInfo(psiVariable.getName() + " " + psiVariable.getType());
                trigger.addVariables(psiVariable);
            }

            @Override
            public void visitParameterList(PsiParameterList list) {
                PsiParameter[] parameters = list.getParameters();
                trigger.setArguments(parameters);
            }

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                PsiExpressionList argumentList = expression.getArgumentList();
                PsiExpression[] expressions = argumentList.getExpressions();
                for (PsiExpression psiExpression : expressions) {
                    trigger.addExpressions(psiExpression);
                }
            }
        });
        trigger.setMethod(PSIUtil.getMethod(scope));

        scope = scope.getParent();
        while (scope != null) {
            if (scope instanceof PsiClass) {
                break;
            }
            scope = scope.getParent();
        }
        PsiClass psiClass = (PsiClass) scope;
        if (psiClass == null) {
            return;
        }
        for (PsiField field : psiClass.getFields()) {
            String fieldName = field.getName().toLowerCase(Locale.ROOT);
            PsiClass fieldClass = PSIUtil.findClass(field.getType().getCanonicalText());
            if (fieldClass == null) {
                continue;
            }
            String className = fieldClass.getName();
            if (className == null) {
                continue;
            }
            if (fieldName.endsWith(ProjectKeys.DAO)) {
                PsiMethod[] methods = fieldClass.getMethods();
                for (PsiMethod method : methods) {
                    PsiType returnType = method.getReturnType();
                    if (returnType == null) {
                        continue;
                    }
                    String metaType = StringUtil.getMetaType(returnType.getPresentableText());
                    if (className.contains(metaType) && method.getName().contains(ProjectKeys.GET)) {
                        Method dataBase = PSIUtil.parser(method);
                        if (returnType.getCanonicalText().endsWith(String.valueOf(StringUtil.GRATE_THEN))) {
                            CodeCompleteHandler.CONTEXT.setDataBaseList(dataBase);
                        }else{
                            CodeCompleteHandler.CONTEXT.setDataBase(dataBase);
                        }
                    }
                }
                continue;
            }
            if (fieldName.endsWith(ProjectKeys.FACADE)) {
                continue;
            }
            List<Param> classAllFields = PSIUtil.getClassAllFields(fieldClass);
            for (Param param : classAllFields) {
                param.setRequired(false);
                String getString = param.getGetString();
                param.setGetString(field.getName() + StringUtil.POINT + getString);
                CodeCompleteHandler.CONTEXT.update(param.getName(), param);
            }
        }

        instance.printInfo(trigger.toString());
        instance.printError(CodeCompleteHandler.CONTEXT.toString());
    }

    //        for (PsiMethod allMethod : aClass.getAllMethods()) {
    //            if (allMethod.getName().contains("getRewardList")) {
    //                Collection<PsiReference> references = ReferencesSearch.search(allMethod).findAll();
    //                for (PsiReference reference : references) {
    //                    PsiElement element = reference.getElement();
    //                    if (element instanceof PsiJavaCodeReferenceElement) {
    //                        PsiJavaCodeReferenceElement ref = (PsiJavaCodeReferenceElement) element;
    //                        instance.print(String.format("Reference found in %s at line %s",
    //                                ref.getContainingFile().getName(), ref.getText()));
    //                    }
    //                }
    //            }
    //        }


    private PsiElement getPsiElement(Editor editor) {
        Document document = editor.getDocument();
        int offset = editor.getCaretModel().getOffset();
        PsiFile psiFile = PsiDocumentManager.getInstance(Application.project).getPsiFile(document);
        if (psiFile != null) {
            PsiElement element = psiFile.findElementAt(offset);
            while (element != null && !(element instanceof PsiVariable)) {
                element = element.getParent();
            }
            return element;
        }
        return null;

    }

    private void counterConfig() {
        PackageChooserDialog selector = new PackageChooserDialog("Select a Package", Application.project);
        selector.show();
        Map<Param, Integer> counter = Maps.newHashMap();
        PsiPackage selectedPackage = selector.getSelectedPackage();
        if (selectedPackage != null) {
            PsiClass[] classes = selectedPackage.getClasses();
            for (PsiClass aClass : classes) {
                List<Param> allFields = PSIUtil.getClassAllFields(aClass);
                for (Param field : allFields) {
                    counter.put(field, counter.getOrDefault(field, 0) + 1);
                }
            }
        }
        List<Param> top100 =
            counter.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(100).map(Map.Entry::getKey).collect(Collectors.toList());
        ConsoleService instance = ConsoleService.getInstance();
        instance.printInfo("Key\tValue \n");
        for (Param param : top100) {
            Integer value = counter.get(param);
            instance.printInfo(param.getName() + "\t" + value + " \n");
        }
    }

    private void counter(Project project) {
        Map<String, Integer> methodCalls = new HashMap<>();
        Map<String, Integer> variables = new HashMap<>();
        // 遍历工程中的每个Java文件
        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.projectScope(project));
        for (VirtualFile file : files) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile instanceof PsiJavaFile) {
                PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
                for (PsiClass aClass : psiJavaFile.getClasses()) {
                    aClass.accept(new JavaRecursiveElementWalkingVisitor() {
                        @Override
                        public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                            String methodName = expression.getMethodExpression().getReferenceName();
                            if (methodName != null) {

                                methodCalls.put(methodName, methodCalls.getOrDefault(methodName, 0) + 1);
                            }
                        }

                        @Override
                        public void visitVariable(PsiVariable variable) {
                            String name = variable.getName();
                            if (name != null) {
                                variables.put(name, variables.getOrDefault(name, 0) + 1);
                            }
                        }
                    });
                }
            }
        }
        Map<String, Integer> collect = methodCalls.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(100)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // 打印Map对象，使用制表符分割
        ConsoleService instance = ConsoleService.getInstance();
        instance.printInfo("Key\tValue \n");
        for (Map.Entry<String, Integer> entry : collect.entrySet()) {
            instance.printInfo(entry.getKey() + "\t" + entry.getValue() + " \n");
        }

        collect = variables.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(100)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // 打印Map对象，使用制表符分割
        instance.printInfo("Key\tValue \n");
        for (Map.Entry<String, Integer> entry : collect.entrySet()) {
            instance.printInfo(entry.getKey() + "\t" + entry.getValue() + " \n");
        }
    }

    private void test() {
        PsiExpressionStatement psiExpressionStatement;
        FindInProjectManager instance = FindInProjectManager.getInstance(Application.project);
        instance.findInProject(SimpleDataContext.getProjectContext(Application.project), null);

    }

    public void testA() {
        final String[] DATA = {"    /**\n" + "     *  获取对象信息\n" + "     */\n" + "    public LordGodEntity getEntity(int configId) {\n"
            + "        return entityMap.computeIfAbsent(configId, k -> LordGodEntity.valueOf(configId));\n" + "    }",
            "    /**\n" + "     *  添加记录\n" + "     */\n" + "    public void addActivateRecord(int id) {\n" + "        activateList.add(id);\n" + "    }",
            "    public void addExp(int addExp){\n" + "        this.exp += addExp;\n" + "    }"};
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
        instance.createComponentPopupBuilder(list, null).setTitle("My Table").setMovable(true).setResizable(true).setCancelOnClickOutside(false).setOkHandler(() -> {
            System.out.println("hello");
        }).setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel)).setRequestFocus(true).setMinSize(new Dimension(300, 400)).createPopup().showInFocusCenter();
    }

    private void createMethod(AnActionEvent e) {

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
        String Sencho = "Lingcao";
        new EnumTemplateDialog("", "", "").show();
    }

    public void test_1() {

        final String[] DATA = {"    /**\n" + "     *  获取对象信息\n" + "     */\n" + "    public LordGodEntity getEntity(int configId) {\n"
            + "        return entityMap.computeIfAbsent(configId, k -> LordGodEntity.valueOf(configId));\n" + "    }",
            "    /**\n" + "     *  添加记录\n" + "     */\n" + "    public void addActivateRecord(int id) {\n" + "        activateList.add(id);\n" + "    }",
            "    public void addExp(int addExp){\n" + "        this.exp += addExp;\n" + "    }"};
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JBList<String> list = new JBList<>(listModel);
        list.setCellRenderer(new TextCellRender());
        for (String datum : DATA) {
            listModel.addElement(datum);
        }

        ArrayList<String> arrayList = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            TextChooseDialog dialog = new TextChooseDialog(list, null, listModel, "");
            dialog.show();

        }
        System.out.println(arrayList);

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
