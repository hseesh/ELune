package com.yatoufang.utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.yatoufang.designer.model.Element;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.MethodCallExpression;
import com.yatoufang.templet.ProjectKeys;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DataUtil implements MethodCallExpression {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String now() {
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static boolean isOdd(int i) {
        return (i & 1) == 0;
    }

    public static String getUID() {
        return String.valueOf(getRandomCode(5));
    }

    public static Integer getRandomCode(int size) {
        double n = Math.pow(10, (size - 1));
        int num;

        if (size > 1) {
            num = (int) (Math.random() * (9 * n) + n);
        } else {
            num = (int) (Math.random() * 10);
        }
        return num;
    }

    public static String getRootPath(String canonicalPath, String key) {
        String[] split = canonicalPath.split(key);
        if (split.length == 1 || split.length == 2) {
            return split[0] + key + "/src/main/java/cn/daxiang/lyltd/" + key;
        } else if (split.length == 3) {
            return split[0] + key + split[1] + key;
        }
        return null;
    }


    public static String getWorkSpace(String canonicalPath, String key) {
        String[] split = canonicalPath.split(key);
        return split[0] + key + "/src/main/java/cn/daxiang/lyltd/";
    }


    public static PsiClass getClass(@NotNull AnActionEvent e) {
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null) {
            return null;
        }
        PsiClass[] classes = file.getClasses();
        if (classes.length == 0) {
            return null;
        }
        Application.project = file.getProject();
        return classes[0];
    }

    public static Timer createTimer(@NotNull final String name, int delay, @NotNull ActionListener listener) {
        return new Timer(delay, listener) {
            public String toString() {
                return name;
            }
        };
    }

    public static void valueOf(Table table) {
        StringBuilder stringBuilder = new StringBuilder("(");
        List<Field> fields = table.getFields();
        for (int i = 0; i < fields.size(); i++) {
            stringBuilder.append(fields.get(i).getAlias()).append(" ").append(fields.get(i).getName());
            if (i != fields.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        table.setValueOf(stringBuilder.toString());
    }

    public static String valueOf(List<Param> fields) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            stringBuilder.append(fields.get(i).getTypeAlias()).append(" ").append(fields.get(i).getName());
            if (i != fields.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static Timer createTimer(int delay, @NotNull ActionListener listener) {
        return new Timer(delay, listener);
    }

    public static void initialise(Element element) {
        if (element == null) {
            return;
        }
        for (Element child : element.children) {
            child.parent = element;
            initialise(child);
        }
    }

    public static String getInitValue(String type) {
        return StringUtil.SPACE + StringUtil.SPACE + getDefaultValue(type);
    }

    public static String getDefaultValue(String type) {
        switch (type.trim()) {
            case "Map":
                return "Maps.newHashMap()";
            case "TreeMap":
                return "Maps.newTreeMap()";
            case "List":
            case "Collection":
                return "Lists.newArrayList()";
            case "Set":
                return "Sets.newSet()";
            default:
                return ProjectKeys.NEW  + type + StringUtil.LEFT_ROUND_BRACKET + StringUtil.RIGHT_ROUND_BRACKET;
        }
    }

    public static String getBaseType(String type) {
        switch (type.trim()) {
            case "Integer":
                return "int";
            case "Long":
                return "long";
            case "Double":
                return "double";
            case "Float":
                return "float";
            case "Boolean":
                return "boolean";
            case "Byte":
                return "byte";
            case "Char":
                return "char";
            default:
                return type;
        }
    }

    public static String getCreateExpression(String type) {
        switch (type.trim()) {
            case "Map":
            case "TreeMap":
                return PUT;
            case "List":
            case "Collection":
            case "Set":
                return ADD;
            default:
                return type;
        }
    }
}
