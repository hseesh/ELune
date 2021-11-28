package com.yatoufang.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtil {

    public static String getExceptionInfo(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String msg = out.toString();
        pout.close();
        try {
            out.close();
        } catch (Exception d) {
            d.printStackTrace();
        }
        return msg;
    }
}
