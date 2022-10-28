package com.yatoufang.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.yatoufang.entity.Struct;
import com.yatoufang.templet.Expression;
import com.yatoufang.templet.MethodCallExpression;
import com.yatoufang.templet.ProjectKeys;
import kotlinx.serialization.StringFormat;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author GongHuang（hse）
 * @since 2022/5/29 0029
 */
public class TemplateUtil extends StringUtil implements Expression, MethodCallExpression {

    public static String valueOf(String className, List<PsiField> fields) {
        StringBuilder builder = new StringBuilder("public static ");
        builder.append(className).append(SPACE).append(ProjectKeys.VALUE_OF).append(SPACE).append(LEFT_ROUND_BRACKET);
        for (int i = 0; i < fields.size(); i++) {
            if (i != 0) {
                builder.append(COMMA);
            }
            builder.append(fields.get(i).getType().getPresentableText()).append(SPACE).append(fields.get(i).getName()).append(SPACE);
        }
        builder.append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE);
        builder.append(className).append(SPACE).append(ProjectKeys.MODEL).append(SPACE).append(EQUAL).append(SPACE).append(ProjectKeys.NEW).append(className).append(SPACE)
                .append(LEFT_ROUND_BRACKET).append(RIGHT_ROUND_BRACKET).append(SEMICOLON);
        for (PsiField field : fields) {
            builder.append(ProjectKeys.MODEL).append(POINT).append(field.getName()).append(SPACE).append(EQUAL).append(SPACE).append(field.getName()).append(SEMICOLON)
                    .append(NEW_LINE);
        }
        builder.append(ProjectKeys.RETURN).append(SPACE).append(ProjectKeys.MODEL).append(SEMICOLON).append(RIGHT_BRACE);
        return builder.toString();
    }

    @Nullable
    public static String valueOf(@Nullable PsiClass[] classes, @Nullable PsiClass[] targetClass) {
        if (classes == null)
            return null;
        PsiClass aClass = classes[0];
        PsiClass originClass = targetClass == null ? null : targetClass[0];
        if (aClass == null) {
            return null;
        }
        PsiField[] fields = aClass.getAllFields();
        String name = originClass == null ? Object.class.getName() : originClass.getName();
        String module = ProjectKeys.VO_ALIAS.toLowerCase(Locale.ROOT);
        String nameAlias = toLowerCaseForFirstChar(name);
        StringBuilder builder = new StringBuilder("public static ");
        builder.append(aClass.getName()).append(SPACE).append(ProjectKeys.VALUE_OF).append(SPACE).append(LEFT_ROUND_BRACKET).append(name).append(SPACE).append(nameAlias)
                .append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE).append(aClass.getName()).append(SPACE).append(module).append(SPACE).append(EQUAL).append(SPACE)
                .append(ProjectKeys.NEW).append(aClass.getName()).append(SPACE).append(LEFT_ROUND_BRACKET).append(RIGHT_ROUND_BRACKET).append(SEMICOLON);
        HashMap<String, String> map = Maps.newHashMap();
        if (originClass != null) {
            for (PsiField field : originClass.getAllFields()) {
                if (field.getType().getPresentableText().equals(boolean.class.getName())) {
                    map.put(field.getName(), ProjectKeys.IS + getUpperCaseVariable(field.getName()));
                    continue;
                }
                map.put(field.getName(), ProjectKeys.GET + getUpperCaseVariable(field.getName()));
            }
        }
        for (PsiField field : fields) {
            builder.append(module).append(POINT).append(field.getName()).append(SPACE).append(EQUAL).append(SPACE);
            if (originClass == null) {
                builder.append(ProjectKeys.NULL).append(SEMICOLON).append(NEW_LINE);
            } else {
                String method = map.get(field.getName());
                if (method == null) {
                    builder.append(ProjectKeys.NULL).append(SEMICOLON).append(NEW_LINE);
                    continue;
                }
                builder.append(nameAlias).append(POINT).append(map.get(field.getName())).append(LEFT_ROUND_BRACKET).append(RIGHT_ROUND_BRACKET).append(SEMICOLON).append(NEW_LINE);
            }
        }
        builder.append(ProjectKeys.RETURN).append(SPACE).append(module).append(SEMICOLON).append(RIGHT_BRACE);
        return builder.toString();
    }

    public static String valueOf(PsiClass aClass, List<PsiFieldMember> initMember) {
        String module = ProjectKeys.VO_ALIAS.toLowerCase(Locale.ROOT);
        StringBuilder paramList = new StringBuilder();
        int flag = 0;
        for (PsiFieldMember member : initMember) {
            PsiField field = member.getElement();
            if (flag ++ != 0) {
                paramList.append(COMMA).append(SPACE);
            }
            paramList.append(field.getType().getPresentableText()).append(SPACE).append(field.getName());
        }
        StringBuilder builder = new StringBuilder(PsiModifier.PUBLIC + SPACE + PsiModifier.STATIC + SPACE);
        builder.append(aClass.getName()).append(SPACE).append(ProjectKeys.VALUE_OF).append(SPACE).append(LEFT_ROUND_BRACKET).append(paramList)
                .append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE).append(aClass.getName()).append(SPACE).append(module).append(SPACE).append(EQUAL).append(SPACE)
                .append(ProjectKeys.NEW).append(aClass.getName()).append(SPACE).append(LEFT_ROUND_BRACKET).append(RIGHT_ROUND_BRACKET).append(SEMICOLON).append(NEW_LINE);
        for (PsiFieldMember member : initMember) {
            PsiField field = member.getElement();
            String fieldName = field.getName();
            if (field.getType().getPresentableText().equals(boolean.class.getName())) {
                builder.append(module).append(POINT).append(fieldName).append(SPACE).append(EQUAL).append(SPACE).append(fieldName);
            } else {
                builder.append(module).append(POINT).append(fieldName).append(SPACE).append(EQUAL).append(SPACE).append(fieldName);
            }
            builder.append(SEMICOLON).append(NEW_LINE);
        }
        builder.append(ProjectKeys.RETURN).append(SPACE).append(module).append(SEMICOLON).append(RIGHT_BRACE);
        return builder.toString();
    }


    public static String valueOf(PsiClass aClass, PsiClass originClass, List<PsiFieldMember> initMember) {
        String name = originClass == null ? Object.class.getName() : originClass.getName();
        String module = ProjectKeys.VO_ALIAS.toLowerCase(Locale.ROOT);
        String nameAlias = toLowerCaseForFirstChar(name);
        StringBuilder builder = new StringBuilder(PsiModifier.PUBLIC + SPACE + PsiModifier.STATIC + SPACE);
        builder.append(aClass.getName()).append(SPACE).append(ProjectKeys.VALUE_OF).append(SPACE).append(LEFT_ROUND_BRACKET).append(name).append(SPACE).append(nameAlias)
                .append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE).append(aClass.getName()).append(SPACE).append(module).append(SPACE).append(EQUAL).append(SPACE)
                .append(ProjectKeys.NEW).append(aClass.getName()).append(SPACE).append(LEFT_ROUND_BRACKET).append(RIGHT_ROUND_BRACKET).append(SEMICOLON).append(NEW_LINE);
        for (PsiFieldMember member : initMember) {
            PsiField field = member.getElement();
            String fieldName = field.getName();
            if (field.getType().getPresentableText().equals(boolean.class.getName())) {
                builder.append(module).append(POINT).append(fieldName).append(SPACE).append(EQUAL).append(SPACE).append(nameAlias).append(POINT).append(ProjectKeys.IS).append(getUpperCaseVariable(fieldName));
            } else {
                builder.append(module).append(POINT).append(fieldName).append(SPACE).append(EQUAL).append(SPACE).append(nameAlias).append(POINT).append(ProjectKeys.GET).append(getUpperCaseVariable(fieldName));
            }
            builder.append(LEFT_ROUND_BRACKET).append(RIGHT_ROUND_BRACKET).append(SEMICOLON).append(NEW_LINE);
        }
        builder.append(ProjectKeys.RETURN).append(SPACE).append(module).append(SEMICOLON).append(RIGHT_BRACE);
        return builder.toString();
    }

    public static String trim(String variable) {
        int index = variable.indexOf(LESS_THEN);
        if (index > 0) {
            variable = variable.substring(0, index);
        }
        return variable;
    }

    public static String ifNull(String variable) {
        return String.format(IF_NULL, variable) + LEFT_BRACE + RIGHT_BRACE;
    }

    public static String ifNullThenReturn(String variable) {
        variable = trim(variable);
        return String.format(IF_NULL, variable) + LEFT_BRACE + RETURN_NULL + RIGHT_BRACE;
    }

    public static String ifNullThenReturn(String variable, String returnType) {
        returnType = trim(returnType);
        return String.format(IF_NULL, variable) + LEFT_BRACE + String.format(RETURN_INVALID_PARAM, returnType) + RIGHT_BRACE;
    }

    public static String returnInvalidParam(String returnType) {
        returnType = trim(returnType);
        return String.format(RETURN_INVALID_PARAM, returnType);
    }

    public static String ifNullThenReturn(String variable, String variableType, String returnType, String define, String paramList) {
        returnType = trim(returnType);
        String replace = String.valueOf(SPACE + COLON + LEFT_BRACE + RIGHT_BRACE + COMMA);
        define = define.replaceAll(String.valueOf(COMMA), replace) + LEFT_BRACE + RIGHT_BRACE;
        String content = String.format(LOG_CONFIG_NOT_FOUND, variableType, define, paramList) + String.format(RETURN_CONFIG_NOT_FOUND, returnType);
        return String.format(IF_NULL, variable) + LEFT_BRACE + content + RIGHT_BRACE;
    }

    public static String isFailThenReturn(String variable, String returnType, String variableType) {
        returnType = trim(returnType);
        String ifExpression = String.format(IF_FAIL, variable);
        if (variableType.equals(returnType)) {
            return ifExpression + LEFT_BRACE + String.format(RETURN_RESULT, variable) + RIGHT_BRACE;
        } else {
            return ifExpression + LEFT_BRACE + String.format(RETURN_STATUS_CODE, returnType, variable) + RIGHT_BRACE;
        }
    }

    public static String initialize(Struct struct) {
        String recommendName = struct.getName();
        String lastType = EMPTY;
        String fileName = getFileName(struct.getValue());
        StringBuilder builder = new StringBuilder();
        String listAll = String.format(CONFIG_LIST_ALL, struct.getValue(), fileName, struct.getValue());
        String forLoop = String.format(CONFIG_FOR_LOOP, struct.getValue(), fileName);
        builder.append(listAll).append(NEW_LINE).append(forLoop).append(LEFT_BRACE).append(NEW_LINE);
        if (struct.getFields().size() == 1) {
            for (Map.Entry<String, String> entry : struct.getFields().entrySet()) {
                String getFields = ProjectKeys.CONFIG + POINT + GET + toUpperCaseForFirstCharacter(struct.getLast()) + LEFT_ROUND_BRACKET + RIGHT_ROUND_BRACKET;
                String secondWord = ProjectKeys.CONFIG + POINT + GET + toUpperCaseForFirstCharacter(struct.getPenultima()) + LEFT_ROUND_BRACKET + RIGHT_ROUND_BRACKET;
                if (entry.getKey().contains("Map")) {
                    if (ProjectKeys.CONFIG.equals(struct.getLast().trim())) {
                        builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(secondWord).append(COMMA).append(ProjectKeys.CONFIG);
                    } else {
                        builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(secondWord).append(COMMA).append(getFields);
                    }
                } else {
                    if (ProjectKeys.CONFIG.equals(struct.getLast().trim())) {
                        builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(ProjectKeys.CONFIG);
                    } else {
                        builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(getFields);
                    }
                }
                builder.append(RIGHT_ROUND_BRACKET).append(SEMICOLON);
            }
        } else {
            int index = 1;
            boolean flag = true;
            List<String> order = struct.getOrder();
            for (Map.Entry<String, String> entry : struct.getFields().entrySet()) {
                if (flag) {
                    flag = false;
                    continue;
                }
                String name = struct.getRecommendName(index);
                builder.append(struct.getInfo(index)).append(SPACE).append(name).append(SPACE).append(EQUAL).append(SPACE);
                if (entry.getKey().contains("Map")) {
                    String getFields = ProjectKeys.CONFIG + POINT + GET + toUpperCaseForFirstCharacter(order.get(index)) + LEFT_ROUND_BRACKET + RIGHT_ROUND_BRACKET;
                    String ifAbsent = String.format(CONFIG_IN_ABSENT, recommendName, getFields, DataUtil.getDefaultValue(entry.getKey()));
                    builder.append(ifAbsent).append(NEW_LINE);
                } else {
                    builder.append(DataUtil.getDefaultValue(entry.getKey())).append(COLON).append(NEW_LINE);
                }
                ++index;
                recommendName = name;
                lastType = entry.getKey();
            }
        }
        builder.append(recommendName).append(POINT);
        String getFields = ProjectKeys.CONFIG + POINT + GET + toUpperCaseForFirstCharacter(struct.getLast()) + LEFT_ROUND_BRACKET + RIGHT_ROUND_BRACKET;
        String secondWord = ProjectKeys.CONFIG + POINT + GET + toUpperCaseForFirstCharacter(struct.getPenultima()) + LEFT_ROUND_BRACKET + RIGHT_ROUND_BRACKET;
        if (lastType.contains("Map")) {
            if (ProjectKeys.CONFIG.equals(struct.getLast().trim())) {
                builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(secondWord).append(COMMA).append(ProjectKeys.CONFIG);
            } else {
                builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(secondWord).append(COMMA).append(getFields);
            }
        } else {
            if (ProjectKeys.CONFIG.equals(struct.getLast().trim())) {
                builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(ProjectKeys.CONFIG);
            } else {
                builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(getFields);
            }
        }
        builder.append(RIGHT_ROUND_BRACKET).append(SEMICOLON).append(NEW_LINE).append(SPACE).append(RIGHT_BRACE);
        return builder.toString();
    }

    public static String getFileName(String variable) {
        String replace = variable.replace(toUpper(ProjectKeys.ACTIVITY), EMPTY).trim();
        return toLowerCaseForFirstChar(filterNumber(replace) + 's');
    }

    public static String buildGetName(String variable) {
        String replace = variable.replace(toUpper(ProjectKeys.ACTIVITY), GET).trim();
        return toLowerCaseForFirstChar(filterNumber(replace)) + SPACE;
    }

    public static String getSomeThing(Struct struct) {
        StringBuilder builder = new StringBuilder();
        String getName = buildGetName(struct.getValue());
        builder.append(String.format(COMMENT, struct.getComment())).append(NEW_LINE);
        builder.append(PsiModifier.PUBLIC).append(SPACE).append(PsiModifier.STATIC).append(SPACE).append(struct.getValue()).append(SPACE).append(getName)
                .append(LEFT_ROUND_BRACKET);
        List<String> fieldsValues = struct.getFieldsValues();
        for (int i = 0; i < fieldsValues.size(); i++) {
            builder.append(DataUtil.getBaseType(fieldsValues.get(i))).append(SPACE).append(struct.getOrder(i));
            if (i != fieldsValues.size() - 1) {
                builder.append(COMMA);
            }
        }
        builder.append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE);
        String lastVariable = struct.getName();
        if (fieldsValues.size() > 1) {
            List<String> order = struct.getOrder();
            for (int i = 0, index = 1; i < order.size() - 1; i++) {
                String recommendName = struct.getRecommendName(index);
                builder.append(struct.getInfo(index)).append(SPACE).append(recommendName).append(SPACE).append(EQUAL).append(SPACE);
                builder.append(String.format(GET_SOME, lastVariable, order.get(i)));
                lastVariable = recommendName;
            }
        }
        builder.append(NEW_LINE).append(ProjectKeys.RETURN).append(SPACE).append(String.format(GET_SOME, lastVariable, struct.getLast())).append(NEW_LINE).append(RIGHT_BRACE);
        return builder.toString();
    }

    public static void main(String[] args) {
        Map<String, String> maps = Maps.newTreeMap();
        List<Object> lists = Lists.newArrayList();
        System.out.println(getFileName("Activity2028RankConfig"));
    }
}
