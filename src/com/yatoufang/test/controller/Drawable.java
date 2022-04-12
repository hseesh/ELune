package com.yatoufang.test.controller;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/31 0031
 */
public interface Drawable {

    /**
     *  calc element bounds
     */
    void init();
    /**
     *  draw custom component
     * @param g Graphics object
     */
    void draw(Graphics2D g);
}
