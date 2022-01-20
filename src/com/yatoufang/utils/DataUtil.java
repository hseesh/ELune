package com.yatoufang.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataUtil {

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

}
