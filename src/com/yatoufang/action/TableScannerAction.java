package com.yatoufang.action;

import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttributeValue;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;
import com.yatoufang.core.Psi;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Annotations;
import com.yatoufang.templet.NotifyService;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * @author hse
 * @Date: 2021/3/15 0015
 */
public class TableScannerAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if(file == null){
            NotifyService.notifyWarning("No File Selected");
            return;
        }
        PsiClass[] classes = file.getClasses();
        if(classes.length == 0) return;
        for (PsiClass aClass : classes) {

            PsiAnnotation annotation = aClass.getAnnotation(Annotations.TABLE);
            if(annotation == null){
                NotifyService.notifyWarning("No Table Selected");
                return;
            }
            PsiAnnotationMemberValue name = annotation.findAttributeValue("name");
            if (name == null) {
                return;
            }
            Table table = new Table(name.getText());
            PsiField[] allFields = aClass.getAllFields();
            List<Field> tableField  = Lists.newArrayList();

            for (PsiField classField : allFields) {
                PsiAnnotation classFieldAnnotation = classField.getAnnotation(Annotations.COLUMN);
                if (classFieldAnnotation == null) {
                    continue;
                }
                Field field = new Field(classField.getName());
                PsiAnnotationMemberValue primaryKey = classFieldAnnotation.findAttributeValue("pk");
                PsiAnnotationMemberValue foreignKey = classFieldAnnotation.findAttributeValue("fk");
                PsiAnnotationMemberValue typeAlias = classFieldAnnotation.findAttributeValue("alias");
                if(primaryKey != null ) {
                    field.setPrimaryKey(primaryKey.getText());
                }else if(foreignKey != null){
                    field.setForeignKey(foreignKey.getText());
                }
                if(typeAlias != null && !"\"\"".equals(typeAlias.getText())){
                    field.setName(typeAlias.getText());
                    field.setDescription(getChineseCharacter(Psi.getDescription(classField.getDocComment())));
                    field.setTypeAlias("TEXT");
                    tableField.add(field);
                    continue;
                }

                switch (classField.getType().getPresentableText()){
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
            tableField.add(new Field("updateTime","timestamp"));
            table.setFields(tableField);
            table.setComment(Psi.getDescription(aClass.getDocComment()));
            System.out.println(table.toString());

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
    }

    private String getChineseCharacter(String value) {
        if(value.length() < 6){
            return value;
        }
        StringBuilder builder = new StringBuilder("");
        for (char c : value.toCharArray()) {
            if(c > 97 + 26){
                builder.append(c);
            }else{
                break;
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        String s = "临时属性资质key:SpriteAttributeType,value:属性资质";
        StringBuilder builder = new StringBuilder("");
        for (char c : s.toCharArray()) {
            if(c > 97 + 26){
                builder.append(c);
            }else{
                break;
            }
        }
        System.out.println(builder.toString());
    }

}
