package com.yatoufang.utils;


public class StringUtil {

    public static String toUpper(String... args){
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(getUpperCaseVariable(arg));
        }
        return builder.toString();
    }

    public static String getUpperCaseVariable(String variable){
        StringBuilder builder = new StringBuilder();
        char[] chars = variable.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i == 0 && chars[i] > 96 && chars[i] < 123){
                builder.append(chars[i] -= 32);
                continue;
            }
            builder.append(chars[i]);
        }
        return builder.toString();
    }
}
