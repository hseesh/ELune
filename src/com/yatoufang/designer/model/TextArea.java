package com.yatoufang.designer.model;

import com.yatoufang.config.MindMapConfig;

/**
 * @author GongHuang（hse）
 * @since 2022/1/12
 */
public class TextArea {
    public int width;
    public int height;

    public TextArea(int width, int height) {
        this.width = width;
        this.height = height;

        this.width = Math.max(this.width, MindMapConfig.element_width);
        this.height = Math.max(this.height, MindMapConfig.element_height);
    }
}
