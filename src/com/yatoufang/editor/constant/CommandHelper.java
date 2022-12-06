package com.yatoufang.editor.constant;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hse
 * @since 2022/9/23 0023
 */
public final class CommandHelper {
    private CommandHelper() {
    }

    private static final Set<Character> COMMANDS = new HashSet<>();

    public static void setCommand(KeyEvent e) {
        COMMANDS.add(e.getKeyChar());
    }

    public static void removeCommand(KeyEvent e) {
        COMMANDS.remove(e.getKeyChar());
    }

    public static boolean getCommand(int keyCode) {
        return getCommand((char) keyCode);
    }

    public static boolean getCommand(char c) {
        return COMMANDS.contains(c);
    }
}