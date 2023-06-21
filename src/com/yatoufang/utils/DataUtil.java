package com.yatoufang.utils;

import com.google.common.collect.Maps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.yatoufang.designer.model.Element;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.Expression;
import com.yatoufang.templet.MethodCallExpression;
import com.yatoufang.templet.ProjectKeys;
import org.jetbrains.annotations.NotNull;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataUtil implements MethodCallExpression {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final Map<String, String> WRAPPER_TYPE_MAP = Maps.newHashMap();
    public static final Map<String, String> BASIC_TYPE_MAP = Maps.newHashMap();

    static {
        WRAPPER_TYPE_MAP.put(Integer.class.getSimpleName(), int.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Long.class.getSimpleName(), long.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Boolean.class.getSimpleName(), boolean.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(List.class.getSimpleName(), Collection.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Set.class.getSimpleName(), Collection.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(HashSet.class.getSimpleName(), Collection.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(ArrayList.class.getSimpleName(), Collection.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(LinkedList.class.getSimpleName(), Collection.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Collection.class.getSimpleName(), Collection.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Map.class.getSimpleName(), Map.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(HashMap.class.getSimpleName(), Map.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(TreeMap.class.getSimpleName(), Map.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(LinkedHashMap.class.getSimpleName(), Map.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(ConcurrentHashMap.class.getSimpleName(), Map.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Double.class.getSimpleName(), double.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Float.class.getSimpleName(), float.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Byte.class.getSimpleName(), byte.class.getSimpleName());
        WRAPPER_TYPE_MAP.put(Character.class.getSimpleName(), char.class.getSimpleName());

        BASIC_TYPE_MAP.put(int.class.getSimpleName(), Integer.class.getSimpleName());
        BASIC_TYPE_MAP.put(long.class.getSimpleName(), Long.class.getSimpleName());
        BASIC_TYPE_MAP.put(boolean.class.getSimpleName(), Boolean.class.getSimpleName());
        BASIC_TYPE_MAP.put(double.class.getSimpleName(), Double.class.getSimpleName());
        BASIC_TYPE_MAP.put(float.class.getSimpleName(), Float.class.getSimpleName());
        BASIC_TYPE_MAP.put(byte.class.getSimpleName(), Byte.class.getSimpleName());
        BASIC_TYPE_MAP.put(char.class.getSimpleName(), Character.class.getSimpleName());
    }

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
        if (split.length == 1 || split.length == 2) {
            return split[0] + Expression.FORMAT_FLAG + "/src/main/java/cn/daxiang/lyltd/" + Expression.FORMAT_FLAG;
        } else if (split.length == 3) {
            return split[0] + Expression.FORMAT_FLAG + split[1] + Expression.FORMAT_FLAG;
        }
        return canonicalPath;
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
                return ProjectKeys.NEW + type + StringUtil.LEFT_ROUND_BRACKET + StringUtil.RIGHT_ROUND_BRACKET;
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

    public static boolean isWrapperType(String type) {
        return WRAPPER_TYPE_MAP.containsKey(type);
    }

    public static String getBasicType(String type) {
        return WRAPPER_TYPE_MAP.get(type);
    }

    public static boolean isSameBasicType(String type1, String type2) {
        String basicType1 = WRAPPER_TYPE_MAP.get(type1);
        String basicType2 = WRAPPER_TYPE_MAP.get(type2);
        if (basicType1 == null) {
            if (BASIC_TYPE_MAP.containsKey(type1)) {
                if (basicType2 == null) {
                    return type1.equals(type2);
                }
                return type1.equals(basicType2);
            }
            return isSameType(type1, type2);
        }
        if (basicType2 == null) {
            if (BASIC_TYPE_MAP.containsKey(type2)) {
                return type2.equals(basicType1);
            }
        }
        return basicType1.equals(basicType2);
    }

    public static boolean isSameType(String type1, String type2) {
        if (type1.equals(type2)) {
            return true;
        }
        String[] parserType1 = StringUtil.parser(type1);
        String[] parserType2 = StringUtil.parser(type2);
        if (parserType1.length == parserType2.length) {
            String superType1 = WRAPPER_TYPE_MAP.get(parserType1[0]);
            String superType2 = WRAPPER_TYPE_MAP.get(parserType2[0]);
            if (superType1 == null || superType2 == null) {
                return false;
            }
            if (superType1.equals(superType2)) {
                return parserType1[1].equals(parserType2[1]);
            }
        }
        return false;
    }

    public static double calculateLevenshtein(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return 1.0 - (double) dp[len1][len2] / Math.max(len1, len2);
    }

    public static void main(String[] args) {
        long star = System.currentTimeMillis();
        System.out.println(isSameBasicType("int", "String"));
        System.out.println(isSameBasicType("int", "Integer"));
        System.out.println(isSameBasicType("int", "int"));
        System.out.println(isSameBasicType("int", "long"));
        System.out.println(isSameBasicType("Date", "Date"));
        System.out.println(isSameBasicType("Date", "Date"));
        System.out.println(isSameBasicType("Collection<String>", "Date"));
        System.out.println(isSameBasicType("Collection<String>", "Collection<Date>"));
        System.out.println(isSameBasicType("Collection<String>", "List<Date>"));
        System.out.println(isSameBasicType("Collection<String>", "List<String>"));
        System.out.println(isSameBasicType("Collection<String>", "Collection<String>"));
        System.out.println(System.currentTimeMillis() - star);
    }

}
