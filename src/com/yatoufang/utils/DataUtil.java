package com.yatoufang.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataUtil {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String now(){
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static boolean isOdd(int i){
        return (i & 1) == 0 ;
    }

}
