package com.yatoufang.test.model;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public enum ElementType {
    NONE(0),
    AREA(1),
    VISUAL_ATTRIBUTES(2),
    TEXT(3),
    ICONS(4),
    COLLAPSATOR(5);

    private final int id;

    ElementType(int id) {
        this.id = id;
    }

    public ElementType getType() {
        for (ElementType type : ElementType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return NONE;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
