package com.yatoufang.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static String now(){
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }
}
