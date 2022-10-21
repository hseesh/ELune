package com.yatoufang.preference.model;

/**
 * @author GongHuang（hse）
 * @since 2022/10/18
 */
public enum AccessContentType {

    INTEGER(1),

    COLLECTION(2),

    MAP(3),

    NONE(0);

    private final int id;

    AccessContentType(int id) {
        this.id = id;
    }

    public static AccessContentType getType(int id) {
        for (AccessContentType type : AccessContentType.values()) {
            if (type.id == id) {
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
