package com.yatoufang.utils;

import com.google.common.collect.Maps;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.yatoufang.templet.ProjectKeys;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author GongHuang（hse）
 * @since 2022/5/29 0029
 */
public class TemplateUtil {

    public static String valueOf(String className, List<PsiField> fields) {
        StringBuilder builder = new StringBuilder("public static ");
        builder.append(className)
                .append(StringUtil.SPACE)
                .append(ProjectKeys.VALUE_OF)
                .append(StringUtil.SPACE)
                .append(StringUtil.LEFT_ROUND_BRACKET);
        for (int i = 0; i < fields.size(); i++) {
            if (i != 0) {
                builder.append(StringUtil.COMMA);
            }
            builder.append(fields.get(i).getType().getPresentableText())
                    .append(StringUtil.SPACE)
                    .append(fields.get(i).getName())
                    .append(StringUtil.SPACE);
        }
        builder.append(StringUtil.RIGHT_ROUND_BRACKET).append(StringUtil.LEFT_BRACE).append(StringUtil.NEW_LINE);
        builder.append(className).append(StringUtil.SPACE)
                .append(ProjectKeys.MODEL).append(StringUtil.SPACE)
                .append(StringUtil.EQUAL).append(StringUtil.SPACE)
                .append(ProjectKeys.NEW).append(className)
                .append(StringUtil.SPACE).append(StringUtil.LEFT_ROUND_BRACKET)
                .append(StringUtil.RIGHT_ROUND_BRACKET).append(StringUtil.SEMICOLON);
        for (PsiField field : fields) {
            builder.append(ProjectKeys.MODEL).append(StringUtil.POINT).append(field.getName())
                    .append(StringUtil.SPACE).append(StringUtil.EQUAL).append(StringUtil.SPACE)
                    .append(field.getName()).append(StringUtil.SEMICOLON).append(StringUtil.NEW_LINE);
        }
        builder.append(ProjectKeys.RETURN).append(StringUtil.SPACE)
                .append(ProjectKeys.MODEL).append(StringUtil.SEMICOLON).append(StringUtil.RIGHT_BRACE);
        return builder.toString();
    }

    @Nullable
    public static String valueOf(@Nullable PsiClass[] classes, @Nullable PsiClass[] targetClass) {
        if (classes == null) return null;
        PsiClass aClass = classes[0];
        PsiClass originClass = targetClass == null ? null : targetClass[0];
        PsiField[] fields = aClass.getAllFields();
        String name = originClass == null ? Object.class.getName() : originClass.getName();
        String module = ProjectKeys.VO_ALIAS.toLowerCase(Locale.ROOT);
        String nameAlias = StringUtil.toLowerCaseForFirstChar(name);
        StringBuilder builder = new StringBuilder("public static ");
        builder.append(aClass.getName())
                .append(StringUtil.SPACE)
                .append(ProjectKeys.VALUE_OF)
                .append(StringUtil.SPACE)
                .append(StringUtil.LEFT_ROUND_BRACKET)
                .append(name).append(StringUtil.SPACE)
                .append(nameAlias)
                .append(StringUtil.RIGHT_ROUND_BRACKET)
                .append(StringUtil.LEFT_BRACE)
                .append(StringUtil.NEW_LINE)
                .append(aClass.getName()).append(StringUtil.SPACE)
                .append(module).append(StringUtil.SPACE)
                .append(StringUtil.EQUAL).append(StringUtil.SPACE)
                .append(ProjectKeys.NEW).append(aClass.getName())
                .append(StringUtil.SPACE).append(StringUtil.LEFT_ROUND_BRACKET)
                .append(StringUtil.RIGHT_ROUND_BRACKET).append(StringUtil.SEMICOLON);
        HashMap<String, String> map = Maps.newHashMap();
        if (originClass != null) {
            for (PsiField field : originClass.getAllFields()) {
                if (field.getType().getPresentableText().equals(boolean.class.getName())) {
                    map.put(field.getName(), ProjectKeys.IS + StringUtil.getUpperCaseVariable(field.getName()));
                    continue;
                }
                map.put(field.getName(), ProjectKeys.GET + StringUtil.getUpperCaseVariable(field.getName()));
            }
        }
        for (PsiField field : fields) {
            builder.append(module).append(StringUtil.POINT).append(field.getName())
                    .append(StringUtil.SPACE).append(StringUtil.EQUAL).append(StringUtil.SPACE);
            if (originClass == null) {
                builder.append(ProjectKeys.NULL).append(StringUtil.SEMICOLON).append(StringUtil.NEW_LINE);
            } else {
                String method = map.get(field.getName());
                if (method == null) {
                    builder.append(ProjectKeys.NULL).append(StringUtil.SEMICOLON).append(StringUtil.NEW_LINE);
                    continue;
                }
                builder.append(nameAlias).append(StringUtil.POINT).append(map.get(field.getName()))
                        .append(StringUtil.LEFT_ROUND_BRACKET).append(StringUtil.RIGHT_ROUND_BRACKET)
                        .append(StringUtil.SEMICOLON).append(StringUtil.NEW_LINE);
            }
        }
        builder.append(ProjectKeys.RETURN).append(StringUtil.SPACE)
                .append(module).append(StringUtil.SEMICOLON).append(StringUtil.RIGHT_BRACE);
        return builder.toString();
    }
}
