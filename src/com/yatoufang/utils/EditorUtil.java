package com.yatoufang.utils;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class EditorUtil {


    public static String[] breakToLines(String text) {
        int lineNum = countLines(text);
        String[] result = new String[lineNum];
        StringBuilder line = new StringBuilder();

        int index = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                result[index++] = line.toString();
                line.setLength(0);
            } else {
                line.append(text.charAt(i));
            }
        }
        result[index] = line.toString();
        return result;
    }


    public static int countLines( String text) {
        int result = 1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                result++;
            }
        }
        return result;
    }

}
