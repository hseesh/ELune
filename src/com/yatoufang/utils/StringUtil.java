package com.yatoufang.utils;

import com.yatoufang.templet.ProjectKeys;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static final String EMPTY = "";
    public static final char COMMA = ',';
    public static final char COLON = ':';
    public static final char SPACE = ' ';
    public static final char EQUAL = '=';
    public static final char LEFT_BRACKET = '[';
    public static final char RIGHT_BRACKET = ']';
    public static final char LEFT_BRACE = '{';
    public static final char RIGHT_BRACE = '}';
    public static final String DOUBLE_QUOTATION = " \"\" ";
    public static final char NEW_LINE = '\n' ;

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
        boolean needUpper = true;
        for (int i = 0; i < chars.length; i++) {
            if (needUpper) {
                if (chars[i] > 96 && chars[i] < 123) {
                    builder.append(chars[i] -= 32);
                } else {
                    builder.append(chars[i]);
                }
                needUpper = false;
                continue;
            } else if (chars[i] == '_') {
                needUpper = true;
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
            if (chars[i] >= 65 && chars[i] < 97) {
                if (i != 0) {
                    builder.append("_");
                }
                builder.append(chars[i]);
                continue;
            }
            if (chars[i] > 96 && chars[i] < 123) {
                builder.append(chars[i] -= 32);
            } else {
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }

    public static String toLowerCaseWithUnderLine(String variable) {
        StringBuilder builder = new StringBuilder();
        char[] chars = variable.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 65 && chars[i] <= 97) {
                if (i != 0) {
                    builder.append("_");
                }
                builder.append(chars[i] += 32);
                continue;
            }
            builder.append(chars[i]);
        }
        return builder.toString();
    }

    public static String getChineseCharacter(String value) {
        if (value.length() < 6) {
            return value;
        }
        StringBuilder builder = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c > 122) {
                builder.append(c);
            } else {
                break;
            }
        }
        return builder.toString();
    }

    public static String getCustomerJson(String value) {
        if (value.isEmpty()) {
            return value;
        }
        StringBuilder builder = new StringBuilder();
        char[] chars = value.toCharArray();
        boolean needAdd = false;
        int startIndex = chars.length;
        ArrayList<Integer> indexes = Lists.newArrayList();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == LEFT_BRACE) {
                if (needAdd) {
                    builder.append(chars[i]);
                    needAdd = false;
                } else {
                    startIndex = nextIndex(startIndex, chars);
                    indexes.add(startIndex);
                }
            } else {
                if (indexes.contains(i)) {
                    continue;
                }
                if (chars[i] == COLON) {
                    needAdd = true;
                }
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }

    private static int nextIndex(int start, char[] chars) {
        for (int i = start - 1; i > 0; i--) {
            if (chars[i] == RIGHT_BRACE) {
                return i;
            }
        }
        return chars.length;
    }

    public static List<String> getParam(String s) {
        StringBuilder builder = new StringBuilder();
        List<String> params = Lists.newArrayList();
        boolean jumpFlag = true;
        for (char c : s.toCharArray()) {
            if (c == 9) {
                if (jumpFlag) {
                    params.add(builder.toString());
                    builder.delete(0, builder.length());
                    jumpFlag = false;
                }
            } else {
                builder.append(c);
                jumpFlag = true;
            }
        }
        return params;
    }

    public static void main(String[] args) {
        System.out.println(getUpperCaseVariable("Rune"));
        System.out.println(toUpper("Rune", ProjectKeys.PUSH, ProjectKeys.HELPER, ProjectKeys.JAVA));
    }

}
