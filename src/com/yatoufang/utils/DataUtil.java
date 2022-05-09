package com.yatoufang.utils;

import com.yatoufang.test.model.Element;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;
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

    public static String getRootPath(String canonicalPath, String key) {
        String[] split = canonicalPath.split(key);
        if (split.length == 1 || split.length == 2) {
            return split[0] + key + "/src/main/java/cn/daxiang/lyltd/" + key;
        }else if (split.length == 3) {
            return split[0] + key + split[1] + key;
        }
        return null;
    }

    public static Timer createTimer(@NotNull final String name, int delay, @NotNull ActionListener listener) {
        return new Timer(delay, listener) {
            public String toString() {
                return name;
            }
        };
    }

    public static Timer createTimer(int delay, @NotNull ActionListener listener) {
        return new Timer(delay, listener);
    }

    public static void initialise(Element element) {
        for (Element child : element.children) {
            child.parent = element;
            initialise(child);
        }
    }
}
