package com.yatoufang.utils;


public class StringUtil {

    public static final String EMPTY = "";
    public static final char COMMA = ',';
    public static final char COLON = ':';
    public static final char SPACE = ' ';
    public static final char LEFT_BRACKET = '[';
    public static final char RIGHT_BRACKET = ']';

    public static String buildPath(String rootPath, String... args) {
        StringBuilder builder = new StringBuilder(rootPath);
        for (String arg : args) {
            builder.append("\\").append(arg);
        }
        return builder.toString();
    }

    public static String toUpper(String... args) {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(getUpperCaseVariable(arg));
        }
        return builder.toString();
    }

    public static String getUpperCaseVariable(String variable) {
        StringBuilder builder = new StringBuilder();
        char[] chars = variable.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0 && chars[i] > 96 && chars[i] < 123) {
                builder.append(chars[i] -= 32);
                continue;
            }
            builder.append(chars[i]);
        }
        return builder.toString();
    }

    public static String toUpperCaseWithUnderLine(String variable) {
        StringBuilder builder = new StringBuilder();
        char[] chars = variable.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 65 && chars[i] <= 97) {
                if (i != 0) {
                    builder.append("_");
                }
                builder.append(chars[i]);
                continue;
            }
            if (chars[i] > 96 && chars[i] < 123) {
                builder.append(chars[i] -= 32);
            }else{
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }

    public static String getChineseCharacter(String value) {
        if (value.length() < 6) {
            return value;
        }
        StringBuilder builder = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c > 123) {
                builder.append(c);
            } else {
                break;
            }
        }
        return builder.toString();
    }

}
