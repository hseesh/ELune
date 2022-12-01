package com.yatoufang.designer.model;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public enum ElementType {
    NONE(0),
    ROOT(1),
    TOP_ROOT(2),
    NORMAL(3),
    ICONS(4);

    private final int ID;

    ElementType(int id) {
        this.ID = id;
    }

    public ElementType getType() {
        for (ElementType type : ElementType.values()) {
            if (type.getID() == ID) {
                return type;
            }
        }
        return NONE;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return String.valueOf(ID);
    }
}
