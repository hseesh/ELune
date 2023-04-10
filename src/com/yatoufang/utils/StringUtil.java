package com.yatoufang.utils;

import com.yatoufang.entity.Config;
import com.yatoufang.entity.Struct;
import com.yatoufang.service.TranslateService;
import com.yatoufang.templet.ProjectKeys;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static final String EMPTY = "";
    public static final char COMMA = ',';
    public static final char COLON = ':';
    public static final char SPACE = ' ';
    public static final char EQUAL = '=';
    public static final char POINT = '.';
    public static final char SEMICOLON = ';';
    public static final String TABLE_FLAG = "\t";
    public static final String SPACE_FLAG = " ";
    public static final String SPLIT_FLAG = "#";
    public static final char EMAIL = '@';
    public static final char CHINESE_FULL_STOP = '。';
    public static final String ENGLISH_FULL_STOP = "\\.";
    public static final char LESS_THEN = '<';
    public static final char GRATE_THEN = '>';
    public static final char LEFT_BRACKET = '[';
    public static final char RIGHT_BRACKET = ']';
    public static final char LEFT_ROUND_BRACKET = '(';
    public static final char RIGHT_ROUND_BRACKET = ')';
    public static final char LEFT_BRACE = '{';
    public static final char RIGHT_BRACE = '}';
    public static final String DOUBLE_QUOTATION = " \"\" ";
    public static final char NEW_LINE = '\n';
    public static final String UNDER_LINE = "_";

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

    public static String toLowerCaseForFirstChar(String variable) {
        if (variable == null || variable.isEmpty()) {
            return variable;
        }
        char[] chars = variable.toCharArray();
        StringBuilder builder = new StringBuilder();
        if (chars[0] >= 65 && chars[0] < 97) {
            builder.append(chars[0] += 32);
        } else {
            builder.append(chars[0]);
        }
        for (int i = 1; i < chars.length; i++) {
            builder.append(chars[i]);
        }
        return builder.toString();
    }

    public static String toUpperCaseForFirstCharacter(String variable) {
        if (variable == null || variable.isEmpty()) {
            return variable;
        }
        char[] chars = variable.toCharArray();
        StringBuilder builder = new StringBuilder();
        if (chars[0] >= 97 && chars[0] < 123) {
            builder.append(chars[0] -= 32);
        } else {
            builder.append(chars[0]);
        }
        for (int i = 1; i < chars.length; i++) {
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

    public static String toCameCaseForSmallCameCase(String variable) {
        char[] chars = variable.toCharArray();
        StringBuilder builder = new StringBuilder();
        boolean flag = false;
        for (char aChar : chars) {
            if (aChar == '_') {
                flag = true;
                continue;
            }
            if (flag) {
                if (aChar > 96 && aChar < 123) {
                    aChar -= 32;
                }
                builder.append(aChar);
                flag = false;
                continue;
            }
            builder.append(aChar);
        }
        return builder.toString();
    }

    //SPACE KEY ASCII 32
    public static String toCameCaseFormTranslate(String variable) {
        char[] chars = variable.toCharArray();
        StringBuilder builder = new StringBuilder();
        if (chars[0] >= 65 && chars[0] < 97) {
            builder.append(chars[0] += 32);
        } else {
            builder.append(chars[0]);
        }
        boolean upperCase = false;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == 32) {
                upperCase = true;
                continue;
            }
            if (upperCase) {
                if (chars[i] > 96 && chars[i] < 123) {
                    builder.append(chars[i] -= 32);
                } else {
                    builder.append(chars[i]);
                }
                upperCase = false;
                continue;
            }
            builder.append(chars[i]);
        }
        return builder.toString();
    }

    public static String toLowerCaseWithUnderLine(String variable) {
        StringBuilder builder = new StringBuilder();
        char[] chars = variable.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 65 && chars[i] < 97) {
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

    // _ ASIC 95
    public static String toCamelCaseFromUpperSnake(String variable) {
        int len = variable.length();
        StringBuilder builder = new StringBuilder(len);
        char[] charArray = variable.toCharArray();
        boolean capitalizeNextLetter = false;
        for (char c : charArray) {
            if (c == '_') {
                capitalizeNextLetter = true;
            } else {
                if (capitalizeNextLetter) {
                    builder.append(Character.toUpperCase(c));
                    capitalizeNextLetter = false;
                } else {
                    builder.append(Character.toLowerCase(c));
                }
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
            if (c > 122) {
                builder.append(c);
            } else {
                break;
            }
        }
        return builder.toString();
    }

    public static String collectChineseCharacter(String value) {
        StringBuilder builder = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c > 122) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static boolean containChineseCharacter(String chars) {
        for (char c : chars.toCharArray()) {
            if (c > 122) {
                return true;
            }
        }
        return false;
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
        List<String> params = new ArrayList<>();
        String[] split = s.split(TABLE_FLAG);
        Collections.addAll(params, split);
        return params;
    }

    public static String removeFirstAndLastString(String variable) {
        if (variable == null || variable.length() < 2) {
            return variable;
        }
        StringBuilder builder = new StringBuilder();
        char[] chars = variable.toCharArray();
        for (int i = 1; i < chars.length - 1; i++) {
            builder.append(chars[i]);
        }
        return builder.toString();
    }

    public static String getPrefix(String variable, String prefix) {
        int index = variable.indexOf(prefix);
        if (index > 0) {
            return variable.substring(0, index);
        }
        return variable;
    }

    public static String getSuffix(String variable, String suffix) {
        int index = variable.indexOf(suffix);
        if (index > 0) {
            return variable.substring(0, index);
        }
        return variable;
    }

    /**
     * 截取俩个字符串之间的字符串
     *
     * @param str
     * @param strStart
     * @param strEnd
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
        // 如果俩个字符相同，结束字符为第二个
        if (strStart.equalsIgnoreCase(strEnd)) {
            int fromIndex = getFromIndex(str, strEnd, 2);
            strEndIndex = fromIndex;
        }

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    //子字符串modelStr在字符串str中第count次出现时的下标
    private static int getFromIndex(String str, String modelStr, Integer count) {
        //对子字符串进行匹配
        Matcher slashMatcher = Pattern.compile(modelStr).matcher(str);
        int index = 0;
        //matcher.find();尝试查找与该模式匹配的输入序列的下一个子序列
        while (slashMatcher.find()) {
            index++;
            //当modelStr字符第count次出现的位置
            if (index == count) {
                break;
            }
        }
        //matcher.start();返回以前匹配的初始索引。
        return slashMatcher.start();
    }

    public static void getStructInfo(String variable, Struct struct) {
        char[] chars = variable.toCharArray();
        String key = EMPTY;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == GRATE_THEN) {
                struct.setValue(builder.toString());
                break;
            }
            switch (chars[i]) {
                case LESS_THEN:
                    key = builder.toString();
                    builder.setLength(0);
                    continue;
                case COMMA:
                    struct.addFields(key, builder.toString());
                    builder.setLength(0);
                    key = EMPTY;
                    continue;
                default:
                    builder.append(chars[i]);
                    break;
            }
        }
        if (key.length() == 0) {
            return;
        }
        String value = builder.toString().replaceAll(String.valueOf(GRATE_THEN), EMPTY);
        struct.addFields(key.trim(), value);
    }

    public static void getInitOrder(String variable, Struct struct) {
        boolean flag = false;
        char[] chars = variable.toCharArray();
        StringBuilder builder = new StringBuilder();
        FLAG:
        for (int i = 0; i < chars.length - 1; i++) {
            switch (chars[i]) {
                case LESS_THEN:
                    flag = true;
                    continue;
                case COMMA:
                    struct.addOrder(builder.toString());
                    builder.setLength(0);
                    continue;
                case GRATE_THEN:
                    struct.addOrder(builder.toString());
                    builder.setLength(0);
                    break FLAG;
                default:
                    if (flag) {
                        builder.append(chars[i]);
                    }
                    break;
            }
        }
        int index = variable.indexOf(ProjectKeys.LINK);
        if (index > 0) {
            variable = variable.substring(index + ProjectKeys.LINK.length());
            if (variable.endsWith(String.valueOf(RIGHT_BRACE))) {
                struct.setFileName(variable.substring(0, variable.length() - 1));
            }
            return;
        }
        struct.setFileName(EMPTY);
    }

    //0~9 ASCII 48~58 ！
    public static String filterNumber(String variable) {
        StringBuilder builder = new StringBuilder();
        for (char c : variable.toCharArray()) {
            if (c >= 48 && c <= 58) {
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
    }


    public static void translateIfNecessary(Config config, List<String> param) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < param.size(); i++) {
            if (i != param.size() - 1) {
                builder.append(StringUtil.SPLIT_FLAG);
            }
            builder.append(param.get(i));
        }
        String character = StringUtil.collectChineseCharacter(builder.toString());
        if (character.length() > 0) {
            String action = TranslateService.translate(character);
            String[] strings = action.split(StringUtil.SPLIT_FLAG);
            if (strings.length < 1) {
                return;
            }
            param.clear();
            Collections.addAll(param, strings);
            config.setParams(param);
        }
    }

    public static String getMetaType(String type) {
        StringBuilder builder = new StringBuilder();
        boolean flag = false;
        for (char c : type.toCharArray()) {
            if (c == LESS_THEN) {
                builder.setLength(0);
                flag = true;
                continue;
            }
            if (c == GRATE_THEN) {
                flag = true;
                continue;
            }
            if (flag) {
                builder.append(c);
            }
        }
        if (builder.length() == 0) {
            return type;
        }
        return builder.toString();
    }


    public static boolean getAllCharacter(String content, List<String> result) {
        char[] chars = content.toCharArray();
        int i = 0, j = 0, k = 0, l = 0;
        while (i < chars.length) {
            if (chars[i] == LESS_THEN) {
                k++;
            }
            if (k == 0) {
                i++;
                continue;
            }
            if (Character.isLetterOrDigit(chars[i])) {
                StringBuilder builder = new StringBuilder();
                while (i < chars.length && Character.isLetterOrDigit(chars[i])) {
                    builder.append(chars[i]);
                    i++;
                }
                result.add(builder.toString());
                ++ j;
                if (j > 1) {
                    l = 0;
                    j = 0;
                }
            } else {
                if (chars[i] == LESS_THEN) {
                    l++;
                }
                i++;
            }
        }
        return l > 0;
    }

    public static String splitCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append(SPACE);
            }
            result.append(c);
        }
        return result.toString().trim();
    }


    public static void main(String[] args) {
        System.out.println(Override.class.getSimpleName());
        System.out.println(toCamelCaseFromUpperSnake("ENDLESS_MAGIC_TOWER_RANK_MAP"));
    }
}
