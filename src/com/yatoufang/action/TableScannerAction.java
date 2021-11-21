package com.yatoufang.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.yatoufang.core.ConsoleCenter;
import com.yatoufang.core.Psi;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import com.yatoufang.entity.TemplateMethod;
import com.yatoufang.templet.Annotations;
import com.yatoufang.templet.NotifyService;
import org.apache.commons.compress.utils.Lists;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author hse
 * @Date: 2021/3/15 0015
 */
public class TableScannerAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            NotifyService.notifyWarning("No File Selected");
            return;
        }
        String resultStr = getResult();
        if (resultStr.length() > 0) {
            System.out.println(resultStr);
            return;
        }
        PsiClass[] classes = file.getClasses();
        if (classes.length == 0) return;
        for (PsiClass aClass : classes) {

            PsiAnnotation annotation = aClass.getAnnotation(Annotations.TABLE);
            if (annotation == null) {
                NotifyService.notifyWarning("No Table Selected");
                return;
            }
            PsiAnnotationMemberValue name = annotation.findAttributeValue("name");
            if (name == null) {
                return;
            }
            Table table = new Table(name.getText());
            PsiField[] allFields = aClass.getAllFields();
            List<Field> tableField = Lists.newArrayList();

            PsiMethod[] methods = aClass.getMethods();

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
                    field.setDescription(getChineseCharacter(Psi.getDescription(classField.getDocComment())));
                    field.setTypeAlias("TEXT");
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
                    default:
                        field.setTypeAlias("TEXT");
                        break;
                }
                field.setDescription(Psi.getDescription(classField.getDocComment()));
                tableField.add(field);
            }
            tableField.add(new Field("updateTime", "timestamp"));
            table.setFields(tableField);
            table.setComment(Psi.getDescription(aClass.getDocComment()));
            ConsoleCenter instance = ConsoleCenter.getInstance();
            instance.printHead("\nThe file scan  completed successfully\n");
            instance.printInfo(table.toString());

            //instance.print(test());
        }

    }

    private String getChineseCharacter(String value) {
        if (value.length() < 6) {
            return value;
        }
        StringBuilder builder = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c > 97 + 26) {
                builder.append(c);
            } else {
                break;
            }
        }
        return builder.toString();
    }


    private String getResult() {

        VelocityEngine velocityEngine = new VelocityEngine();

        Thread currentThread = Thread.currentThread();
        ClassLoader temp = Thread.currentThread().getContextClassLoader();
        try {
            currentThread.setContextClassLoader(getClass().getClassLoader());
            velocityEngine.setProperty("runtime.log.logsystem.class", NullLogChute.class.getName());
            velocityEngine.init();
        } finally {
            currentThread.setContextClassLoader(temp);
        }
        /* lets make a Context and put data into it */

        VelocityContext context = new VelocityContext();


        Table ido = new Table("ido");
        ido.setComment("神像");
        context.put("table", ido);


        StringWriter stringWriter = new StringWriter();

        String text = "";
        try {
            text = FileUtil.loadTextAndClose(Objects.requireNonNull(TableScannerAction.class.getResourceAsStream("/templates/DaoTemplate.vm")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Velocity.evaluate(context, stringWriter, "myLog", text);
        return stringWriter.toString();

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

