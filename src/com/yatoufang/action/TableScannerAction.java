package com.yatoufang.action;

import com.google.common.collect.Maps;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.utils.ExceptionUtil;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Annotations;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author hse
 * @Date: 2021/3/15 0015
 */
public class TableScannerAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_FILE_SELECTED);
            return;
        }

        String rootPath = getRootPath(file);
        PsiClass[] classes = file.getClasses();
        for (PsiClass aClass : classes) {
            Table table = getTable(aClass);
            if (table == null) {
                continue;
            }
            printResult(table);
            if (rootPath == null || rootPath.isEmpty()) {
                continue;
            }
           // generateCode(rootPath, table);
        }
    }

    private void generateCode(String rootPath, Table table) {
        table.getFields().removeIf(k -> k.getName().equals(ProjectKeys.UPDATE_TIME));
        String moduleName = table.getName();
        HashMap<String, File> fileMap = Maps.newHashMap();
        String targetPath = StringUtil.buildPath(rootPath, ProjectKeys.MODULE + "_", moduleName.toUpperCase(Locale.ROOT) );
        ConsoleService consoleService = ConsoleService.getInstance();
        VelocityService velocityService = VelocityService.getInstance();
        File entityCmdFile = new File(StringUtil.buildPath(targetPath, StringUtil.toUpper(table.getName(), ProjectKeys.CMD, ProjectKeys.JAVA)));
        File entityHandlerFile = new File(StringUtil.buildPath(targetPath, StringUtil.toUpper(table.getName(), ProjectKeys.HANDLER, ProjectKeys.JAVA)));
        File daoFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.DAO, StringUtil.toUpper(table.getName(), ProjectKeys.DAO, ProjectKeys.JAVA)));
        File voFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.MODEL, StringUtil.toUpper(table.getName(), ProjectKeys.VO, ProjectKeys.JAVA)));
        File entityFacadeFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.FACADE, StringUtil.toUpper(table.getName(), ProjectKeys.FACADE, ProjectKeys.JAVA)));
        File entityResponseFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.RESPONSE, StringUtil.toUpper(table.getName(), ProjectKeys.RESPONSE, ProjectKeys.JAVA)));
        File helperFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.HELPER, StringUtil.toUpper(table.getName(), ProjectKeys.PUSH, ProjectKeys.HELPER, ProjectKeys.JAVA)));
        File entityFacadeImplFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.FACADE, StringUtil.toUpper(table.getName(), ProjectKeys.FACADE, ProjectKeys.IMPL, ProjectKeys.JAVA)));
        File daoImplFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.DAO, ProjectKeys.IMPL, StringUtil.toUpper(table.getName(), ProjectKeys.DAO, ProjectKeys.IMPL, ProjectKeys.JAVA)));
        File entityDeleteResponseFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.RESPONSE, StringUtil.toUpper(table.getName(), ProjectKeys.DELETE, ProjectKeys.RESPONSE, ProjectKeys.JAVA)));
        File entityRewardResponseFile = new File(StringUtil.buildPath(targetPath, ProjectKeys.RESPONSE, StringUtil.toUpper(table.getName(), ProjectKeys.REWARD, ProjectKeys.RESULT, ProjectKeys.RESPONSE, ProjectKeys.JAVA)));
        if (table.isMultiEntity()) {
            fileMap.put(ProjectKeys.MULTI_ENTITY_TEMPLATE, daoFile);
            fileMap.put(ProjectKeys.MULTI_ENTITY_IMPL_TEMPLATE, daoImplFile);
        } else {
            fileMap.put(ProjectKeys.SINGLE_ENTITY_TEMPLATE, daoFile);
            fileMap.put(ProjectKeys.SINGLE_ENTITY_IMPL_TEMPLATE, daoImplFile);
        }
        fileMap.put(ProjectKeys.ENTITY_VO_TEMPLATE, voFile);
        fileMap.put(ProjectKeys.PUSH_HELP_TEMPLATE, helperFile);
        fileMap.put(ProjectKeys.ENTITY_CMD_TEMPLATE, entityCmdFile);
        fileMap.put(ProjectKeys.ENTITY_FACADE_TEMPLATE, entityFacadeFile);
        fileMap.put(ProjectKeys.ENTITY_HANDLER_TEMPLATE, entityHandlerFile);
        fileMap.put(ProjectKeys.ENTITY_RESPONSE_TEMPLATE, entityResponseFile);
        fileMap.put(ProjectKeys.ENTITY_FACADE_IMPL_TEMPLATE, entityFacadeImplFile);
        fileMap.put(ProjectKeys.ENTITY_REWARD_RESPONSE_TEMPLATE, entityRewardResponseFile);
        fileMap.put(ProjectKeys.ENTITY_DELETE_RESPONSE_TEMPLATE, entityDeleteResponseFile);
        fileMap.forEach((fileName, file) -> {
            try {
                FileUtil.writeToFile(file, velocityService.execute(fileName, table));
                consoleService.print(file.getCanonicalPath() + " created successfully\n");
            } catch (IOException e) {
                consoleService.printError(ExceptionUtil.getExceptionInfo(e));
            }
        });
    }


    @Nullable
    private String getRootPath(PsiJavaFile file) {
        VirtualFile virtualFile = file.getVirtualFile();
        VirtualFile parent = virtualFile.getParent().getParent().getParent();
        if (parent.getCanonicalPath() == null || !parent.getCanonicalPath().endsWith(ProjectKeys.CORE)) {
            return null;
        }
        VirtualFile root = parent.getParent();
        String rootPath = root.getCanonicalPath();
        if (rootPath == null || rootPath.isEmpty()) {
            return null;
        }
        return rootPath;
    }

    @Nullable
    private Table getTable(PsiClass aClass) {
        PsiAnnotation annotation = aClass.getAnnotation(Annotations.TABLE);
        if (annotation == null) {
            NotifyService.notifyWarning(NotifyKeys.NO_TABLE_SELECTED);
            return null;
        }
        PsiAnnotationMemberValue name = annotation.findAttributeValue("name");
        PsiAnnotationMemberValue type = annotation.findAttributeValue("type");
        if (name == null || type == null) {
            return null;
        }
        Table table = new Table(name.getText(), type.getText());
        PsiField[] allFields = aClass.getAllFields();
        List<Field> tableField = Lists.newArrayList();
        PsiClassType[] extendsListTypes = aClass.getExtendsListTypes();
        for (PsiClassType extendsListType : extendsListTypes) {
            table.setMultiEntity(ProjectKeys.MULTI_ENTITY.equals(extendsListType.getClassName()));
        }
        PsiMethod[] methods = aClass.getMethods();
        for (PsiMethod method : methods) {
            if (ProjectKeys.VALUE_OF.equals(method.getName())) {
                table.setValueOf(method.getParameterList().getText());
            }
        }
        for (PsiField classField : allFields) {
            PsiAnnotation classFieldAnnotation = classField.getAnnotation(Annotations.COLUMN);
            if (classFieldAnnotation == null) {
                continue;
            }
            Field field = new Field(classField.getName());
            PsiAnnotationMemberValue primaryKey = classFieldAnnotation.findAttributeValue("pk");
            PsiAnnotationMemberValue foreignKey = classFieldAnnotation.findAttributeValue("fk");
            PsiAnnotationMemberValue typeAlias = classFieldAnnotation.findAttributeValue("alias");
            if (primaryKey != null) {
                field.setPrimaryKey(primaryKey.getText());
                table.setPrimaryKey(field);
            } else if (foreignKey != null) {
                field.setForeignKey(foreignKey.getText());
            }
            if (typeAlias != null && !"\"\"".equals(typeAlias.getText())) {
                field.setName(typeAlias.getText());
                field.setDescription(StringUtil.getChineseCharacter(PSIUtil.getDescription(classField.getDocComment())));
                field.setTypeAlias("TEXT");
                field.setAlias(classField.getType().getPresentableText());
                tableField.add(field);
                continue;
            }
            switch (classField.getType().getPresentableText()) {
                case "long":
                case "Long":
                    field.setTypeAlias("BIGINT(20)");
                    break;
                case "int":
                case "Integer":
                    field.setTypeAlias("INT(8)");
                    break;
                case "String":
                    field.setTypeAlias("VARCHAR(1000)");
                    break;
                case "Boolean":
                case "boolean":
                    field.setTypeAlias("BIT");
                    break;
                default:
                    field.setTypeAlias("TEXT");
                    break;
            }
            field.setAlias(classField.getType().getPresentableText());
            field.setDescription(PSIUtil.getDescription(classField.getDocComment()));
            tableField.add(field);
        }
        Field field = new Field("updateTime", "timestamp");
        field.setConstraint("NOT　NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
        tableField.add(field);
        table.setFields(tableField);
        table.setComment(PSIUtil.getDescription(aClass.getDocComment()));
        return table;
    }

    private void printResult(Table table) {
        ConsoleService instance = ConsoleService.getInstance();
        instance.printHead("File scanning process completed successfully");
        if (table.getComment().isEmpty()) {
            instance.printError("No comments fund in table : " + table.getName());
        }
        for (Field field : table.getFields()) {
            if (field.getDescription().isEmpty()) {
                instance.printError("No comments fund for field: " + field.getName());
            }
        }
        instance.printInfo(table.toString());
    }


}

//        JBPopupFactory instance = JBPopupFactory.getInstance();
//        ExecuteDialog executeDialog = new ExecuteDialog(methods);
//        instance.createComponentPopupBuilder(executeDialog.getContent(), null)
//                .setTitle("My POST")
//                .setMovable(true)
//                .setResizable(true)
//                .setCancelOnClickOutside(false)
//                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
//                .setRequestFocus(true)
//                .setMinSize(new Dimension(900, 450))
//                .setDimensionServiceKey(null, "com.yatoufang.http.popup", true)
//                .createPopup()
//                .showInFocusCenter();
//https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000649839-Generete-annotation-and-methods-code-in-method-in-already-existing-java-file-

