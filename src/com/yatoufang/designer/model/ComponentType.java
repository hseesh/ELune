package com.yatoufang.designer.model;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public enum ComponentType {
    NONE(0),
    AREA(1),
    VISUAL_ATTRIBUTES(2),
    TEXT(3),
    ICONS(4),
    COLLAPSATOR(5);

    private final int ID;

    ComponentType(int id) {
        this.ID = id;
    }

    public ComponentType getType() {
        for (ComponentType type : ComponentType.values()) {
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
