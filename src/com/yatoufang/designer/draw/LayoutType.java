package com.yatoufang.designer.draw;

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

    public int getID() {
        return ID;
    }

    public static LayoutType getType(int id) {
        for (LayoutType type : LayoutType.values()) {
            if (type.getID() == id) {
                return type;
            }
        }
        return NONE;
    }

    LayoutType(int id) {
        this.ID = id;
    }
}
