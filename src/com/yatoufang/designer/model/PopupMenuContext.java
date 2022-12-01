package com.yatoufang.designer.model;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/3/25
 */
public class PopupMenuContext {
    private Component invoke;
    private int x;
    private int y;

    public Component getInvoke() {
        return invoke;
    }

    public void setInvoke(Component invoke) {
        this.invoke = invoke;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
