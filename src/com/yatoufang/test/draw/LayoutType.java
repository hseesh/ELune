package com.yatoufang.test.draw;

/**
 * @author GongHuang（hse）
 * @since 2022/1/10
 */
public enum LayoutType {



    RIGHT_TIME(1),
    RIGHT_TREE(2),
    LEFT_TREE(3),

    NONE(0);


    private final int ID;

    LayoutType(int id) {
        this.ID = id;
    }
}
