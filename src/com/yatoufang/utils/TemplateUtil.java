package com.yatoufang.utils;

import com.google.common.collect.Maps;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.yatoufang.entity.Struct;
import com.yatoufang.entity.complete.SerializeLayer;
import com.yatoufang.templet.Expression;
import com.yatoufang.templet.MethodCallExpression;
import com.yatoufang.templet.ProjectKeys;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author GongHuang（hse）
 * @since 2022/5/29 0029
 */
public class TemplateUtil extends StringUtil implements Expression, MethodCallExpression {

    public static String valueOf(String className, List<PsiField> fields) {
        StringBuilder builder =  new StringBuilder(PsiModifier.PUBLIC + SPACE + PsiModifier.STATIC + SPACE);
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
            if (flag++ != 0) {
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
        define = define.replaceAll(String.valueOf(COMMA), LOG_FLAG) + LEFT_BRACE + RIGHT_BRACE;
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

    public static String buildInitMethod(Struct struct) {
        String lastType = struct.getLastKey();
        String recommendName = struct.getName();
        String fileName = getFileName(struct.getFileName());
        StringBuilder builder = new StringBuilder();
        String listAll = String.format(CONFIG_LIST_ALL, struct.getFileName(), fileName, struct.getFileName());
        String forLoop = String.format(CONFIG_FOR_LOOP, struct.getFileName(), fileName);
        builder.append(listAll).append(NEW_LINE).append(forLoop).append(LEFT_BRACE).append(NEW_LINE);
        for (int i = 1; i < struct.getFields().values().size(); i++) {
            String key = struct.getKeyByIndex(i);
            String name = struct.getRecommendName(i);
            builder.append(struct.getInfo(i)).append(SPACE).append(name).append(SPACE).append(EQUAL).append(SPACE);
            String getFields = ProjectKeys.CONFIG + POINT + GET + toUpperCaseForFirstCharacter(struct.getOrder(i - 1)) + LEFT_ROUND_BRACKET + RIGHT_ROUND_BRACKET;
            String ifAbsent = String.format(CONFIG_IN_ABSENT, recommendName, getFields, DataUtil.getDefaultValue(key));
            builder.append(ifAbsent).append(NEW_LINE);
            recommendName = name;
        }
        String getFields;
        String lastValue = struct.getLastValue();
        builder.append(recommendName).append(POINT);
        if (ProjectKeys.CONFIG.equals(lastValue)) {
            getFields = ProjectKeys.CONFIG;
        } else {
            getFields = ProjectKeys.CONFIG + POINT + GET + toUpperCaseForFirstCharacter(lastValue) + LEFT_ROUND_BRACKET + RIGHT_ROUND_BRACKET;
        }
        String secondWord = ProjectKeys.CONFIG + POINT + GET + toUpperCaseForFirstCharacter(struct.getPenultima()) + LEFT_ROUND_BRACKET + RIGHT_ROUND_BRACKET;
        if (lastType.contains(Map.class.getSimpleName())) {
            if (ProjectKeys.CONFIG.equals(struct.getLastKey().trim())) {
                builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(secondWord).append(COMMA).append(ProjectKeys.CONFIG);
            } else {
                builder.append(DataUtil.getCreateExpression(lastType)).append(LEFT_ROUND_BRACKET).append(secondWord).append(COMMA).append(getFields);
            }
        } else {
            if (ProjectKeys.CONFIG.equals(struct.getLastKey().trim())) {
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
        return toLowerCaseForFirstChar(filterNumber(replace));
    }

    public static String buildAccessMethod(Struct struct) {
        StringBuilder builder = new StringBuilder();
        String getName = buildGetName(struct.getValue());
        builder.append(String.format(COMMENT, struct.getComment())).append(NEW_LINE);
        builder.append(PsiModifier.PUBLIC).append(SPACE).append(PsiModifier.STATIC).append(SPACE).append(struct.getReturnExpression()).append(SPACE).append(getName)
                .append(LEFT_ROUND_BRACKET);
        List<String> fieldsValues = struct.getFieldsValues();
        int maxIndex = fieldsValues.size();
        if (Collection.class.getSimpleName().equals(struct.getLastKey())) {
            maxIndex--;
        }
        for (int i = 0; i < maxIndex; i++) {
            if (Collection.class.getSimpleName().equals(struct.getKeyByIndex(i))) {
                maxIndex--;
                builder.append(struct.getInfo(i));
            } else {
                builder.append(DataUtil.getBaseType(fieldsValues.get(i)));
            }
            builder.append(SPACE).append(struct.getOrder(i));
            if (i != maxIndex - 1) {
                builder.append(COMMA);
            }
        }
        builder.append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE);
        String lastVariable = struct.getName();
        if (fieldsValues.size() > 1) {
            List<String> order = struct.getOrder();
            for (int i = 0, index = 1; i < maxIndex - 1; i++) {
                String recommendName = struct.getRecommendName(index);
                builder.append(struct.getInfo(index)).append(SPACE).append(recommendName).append(SPACE).append(EQUAL).append(SPACE);
                builder.append(String.format(GET_SOME, lastVariable, order.get(i)));
                lastVariable = recommendName;
            }
        }
        builder.append(NEW_LINE).append(ProjectKeys.RETURN).append(SPACE).append(String.format(GET_SOME, lastVariable, struct.getPenultima())).append(NEW_LINE).append(RIGHT_BRACE);
        return builder.toString();
    }

    public static String buildMap(SerializeLayer serializeLayer) {
        if (serializeLayer.getNames().size() <= 1) {
            return EMPTY;
        }
        List<String> keys = serializeLayer.getNames();
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(COMMENT, serializeLayer.getDescription())).append(NEW_LINE);
        builder.append(PsiModifier.PUBLIC).append(SPACE).append(PsiModifier.STATIC).append(SPACE).append(PsiModifier.FINAL);
        for (int i = 0; i < keys.size() - 1; i++) {
            builder.append(SPACE).append(Map.class.getSimpleName()).append(LESS_THEN);
            String mappingKey = serializeLayer.getTypes().get(i);
            if (long.class.getName().equals(mappingKey)) {
                mappingKey = Long.class.getSimpleName();
            } else if (int.class.getName().equals(mappingKey)) {
                mappingKey = Integer.class.getSimpleName();
            }
            builder.append(mappingKey).append(COMMA);
        }
        String mappingKey = keys.get(keys.size() - 1);
        if (mappingKey.toLowerCase().contains(ProjectKeys.RANK_KEY.toLowerCase())) {
            builder.append(String.format(RANK, mappingKey));
        } else {
            builder.append(SPACE).append(mappingKey);
        }
        builder.append(String.valueOf(GRATE_THEN).repeat(keys.size() - 1));
        builder.append(SPACE).append(serializeLayer.getName()).append(UNDER_LINE).append(Map.class.getSimpleName().toUpperCase()).
                append(SPACE).append(EQUAL).append(SPACE).append(NEW_CON_MAP).append(SEMICOLON);
        return builder.toString();
    }

    public static void buildOperatorMethod(SerializeLayer serializeLayer, Map<String, String> methodMap) {
        if (serializeLayer.isCollection()) {
            return;
        }
        String name, finaValue, returnType;
        StringBuilder builder = new StringBuilder();
        String map = Map.class.getSimpleName().toUpperCase();
        boolean rankCacheFlag = serializeLayer.getLastElement().toLowerCase().contains(ProjectKeys.RANK_KEY.toLowerCase());
        if (rankCacheFlag) {
            finaValue = NEW_RANK;
            returnType = String.format(RANK, serializeLayer.getLastElement()).replace(SEMICOLON, SPACE);
        } else {
            returnType = serializeLayer.getLastElement();
            finaValue = DataUtil.getDefaultValue(serializeLayer.getLastElement());
        }
        builder.append(EMAIL).append(Override.class.getSimpleName()).append(NEW_LINE);
        builder.append(PsiModifier.PUBLIC).append(SPACE).append(returnType).append(SPACE);
        if (rankCacheFlag) {
            name = ProjectKeys.GET + toUpperCaseForFirstCharacter(toCamelCaseFromUpperSnake(serializeLayer.getName().replace(map, EMPTY)));
            builder.append(name);
        } else {
            name = buildGetName(serializeLayer.getLastElement());
            builder.append(name);
        }
        builder.append(SPACE).append(LEFT_ROUND_BRACKET);
        for (int i = 0; i < serializeLayer.getNames().size() - 1; i++) {
            String key = serializeLayer.getNames().get(i);
            String type = serializeLayer.getTypes().get(i);
            if (i > 0) {
                builder.append(COMMA);
            }
            builder.append(type).append(SPACE).append(key);
        }
        builder.append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE).append(ProjectKeys.RETURN).append(SPACE);
        if (serializeLayer.getNames().size() > 1) {
            builder.append(String.format(IF_ABSENT, serializeLayer.getName() + UNDER_LINE + map, serializeLayer.getNames().get(0), NEW_CON_MAP)).append(NEW_LINE);
        }
        for (int i = 1; i < serializeLayer.getNames().size() - 2; i++) {
            builder.append(String.format(IF_ABSENT, EMPTY, serializeLayer.getNames().get(i), NEW_CON_MAP)).append(NEW_LINE);
        }
        if (serializeLayer.getNames().size() == 1) {
            builder.append(String.format(GET_CACHE, serializeLayer.getLastElement(), serializeLayer.getName()));
        } else {
            builder.append(String.format(IF_ABSENT, EMPTY, serializeLayer.getPenultimateElement(), finaValue)).append(SEMICOLON);
        }
        builder.append(NEW_LINE).append(RIGHT_BRACE);
        methodMap.put(name, builder.toString());
        builder.setLength(0);
        builder.append(EMAIL).append(Override.class.getSimpleName()).append(NEW_LINE);
        builder.append(PsiModifier.PUBLIC).append(SPACE).append(void.class.getSimpleName()).append(SPACE);
        if (rankCacheFlag) {
            name = ProjectKeys.UPDATE + toUpperCaseForFirstCharacter(toCamelCaseFromUpperSnake(serializeLayer.getName().replace(map, EMPTY)));
            builder.append(name);
        } else {
            name = ProjectKeys.UPDATE + filterNumber(serializeLayer.getLastElement().replaceAll(toUpperCaseForFirstCharacter(ProjectKeys.ACTIVITY), EMPTY));
            builder.append(name);
        }
        builder.append(LEFT_ROUND_BRACKET);
        StringBuilder formatBuilder = new StringBuilder();
        for (int i = 0; i < serializeLayer.getNames().size() - 1; i++) {
            String key = serializeLayer.getNames().get(i);
            String type = serializeLayer.getTypes().get(i);
            if (i > 0) {
                builder.append(COMMA);
                formatBuilder.append(COMMA);
            }
            builder.append(type).append(SPACE).append(key);
            formatBuilder.append(key).append(SPACE);
        }
        if (rankCacheFlag) {
            builder.append(COMMA).append(SPACE).append(List.class.getSimpleName()).append(LESS_THEN).append(serializeLayer.getLastElement()).append(GRATE_THEN)
                    .append(SPACE).append(List.class.getSimpleName().toLowerCase());
        } else {
            if (serializeLayer.getNames().size() > 1) {
                builder.append(COMMA);
            }
            builder.append(SPACE).append(serializeLayer.getLastElement()).append(SPACE).append(toLowerCaseForFirstChar(serializeLayer.getLastElement()));
        }
        builder.append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE).append(SPACE);
        if (serializeLayer.getNames().size() == 1) {
            builder.append(String.format(UPDATE_CACHE, toLowerCaseForFirstChar(serializeLayer.getLastElement()), serializeLayer.getName()));
        } else {
            builder.append(String.class.getSimpleName()).append(SPACE).append(ProjectKeys.CACHE_KEY).append(SPACE).append(EQUAL).append(String.format(FORMAT, serializeLayer.getName(), formatBuilder)).append(SEMICOLON);
            if (rankCacheFlag) {
                builder.append(NEW_LINE).append(SPACE).append(CLEAR_SORT_SET).append(NEW_LINE).append(ADD_LIST_OBJECT);
            } else {
                String variable = toLowerCaseForFirstChar(serializeLayer.getLastElement());
                builder.append(NEW_LINE).append(SPACE).append(String.format(PUT_HASH_OBJECT, variable + POINT + serializeLayer.getRecommendName(), variable));
            }
        }
        builder.append(NEW_LINE).append(RIGHT_BRACE);
        methodMap.put(name, builder.toString());
        if (rankCacheFlag || serializeLayer.getNames().size() <= 1) {
            return;
        }
        builder.setLength(0);
        builder.append(EMAIL).append(Override.class.getSimpleName()).append(NEW_LINE);
        builder.append(PsiModifier.PUBLIC).append(SPACE).append(String.format(COLLECTION_TYPE, serializeLayer.getLastElement())).append(SPACE);
        name = buildGetName(serializeLayer.getLastElement() + List.class.getSimpleName());
        builder.append(name).append(SPACE).append(LEFT_ROUND_BRACKET);
        for (int i = 0; i < serializeLayer.getNames().size() - 2; i++) {
            String key = serializeLayer.getNames().get(i);
            String type = serializeLayer.getTypes().get(i);
            if (i > 0) {
                builder.append(COMMA);
            }
            builder.append(type).append(SPACE).append(key);
        }
        builder.append(RIGHT_ROUND_BRACKET).append(LEFT_BRACE).append(NEW_LINE).append(ProjectKeys.RETURN).append(SPACE);
        builder.append(String.format(IF_ABSENT, serializeLayer.getName() + UNDER_LINE + map, serializeLayer.getNames().get(0), NEW_CON_MAP)).append(NEW_LINE);
        for (int i = 1; i < serializeLayer.getNames().size() - 2; i++) {
            builder.append(String.format(IF_ABSENT, EMPTY, serializeLayer.getNames().get(i), NEW_CON_MAP)).append(NEW_LINE);
        }
        builder.append(String.format(VALUES, EMPTY)).append(NEW_LINE).append(RIGHT_BRACE);
        methodMap.put(name, builder.toString());
    }


    public static void main(String[] args) {

    }
}
